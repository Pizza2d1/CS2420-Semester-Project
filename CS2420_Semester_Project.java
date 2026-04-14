package CS2420_Semester_Project;

// For player constants
import static CS2420_Semester_Project.Person.*;
import static CS2420_Semester_Project.PlaneGui.*;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class CS2420_Semester_Project {

	// Testing variables
	static boolean peopleCollision;
	static Sort_Type queueType;
	public static int people_amount;
	static int clockSpeed;

	// REPLACED BY CONFIG FILE
	// static boolean peopleCollision = true;
	// static Sort_Type queueType = Sort_Type.FRONT_TO_BACK;
	// public static int people_amount = 180;
	// static int clockSpeed = 300;

	static Color red = Color.getHSBColor(0, 100, 50);

	static List<Person> peopleArr = new ArrayList<>();
	static JPanel contentPane = new JPanel();
	static JLabel planeDisplay = new JLabel();
	static JLabel tickClock = new JLabel("");
	static JLabel statsDisplay = new JLabel("");
	static boolean unloading = false;
	static boolean paused = false;
	static int ticks = 0;
	static Display_Clock display_clock = Display_Clock.STOPPED;
	static List<Integer> queue = new ArrayList<>();

	public static void main(String[] args) {

		getSimSettings(); // Required (unless we decide to remove it)

		// Starts up the gui and keybinds
		EventQueue.invokeLater(() -> {
			try {
				PlaneGui frame = new PlaneGui(peopleArr, planeDisplay, contentPane); // Can prob make this section shorter, but whatever
				frame.setVisible(true);
				frame.addKeyListener(new KeyAdapter() {
          			@Override
					public void keyPressed(KeyEvent e) {
						int keyCode = e.getKeyCode();
						try { // Two try/catch statements? Ewwww
							action(keyCode);
						} catch (IOException err) {}
					}
				});
			} catch (Exception err2) {}
		});

		// Create people array so that they can be sorted
		for (int i = 0; i < people_amount; i++) {
			addPerson(i);
		}

		// Decide what queue to use
		PassengerQueue.random(peopleArr);
		switch (queueType) {
			case RANDOM:
				peopleArr = PassengerQueue.random(peopleArr); break;
			case BACK_TO_FRONT:
				peopleArr = PassengerQueue.backToFront(peopleArr); break;
			case FRONT_TO_BACK:
				System.out.println("before");
				peopleArr = PassengerQueue.frontToBack(peopleArr);
				System.out.println("after");
				break;
			case GROUPS_6:
				peopleArr = PassengerQueue.groupsOf6(peopleArr); break;
			case GROUPS_12:
				peopleArr = PassengerQueue.groupsOf12(peopleArr); break;
			case GROUPS_18:
				peopleArr = PassengerQueue.groupsOf18(peopleArr); break;
			default:
				break;
		}

		// for (Person person : peopleArr) {
			// System.out.println("PERSON ID: " + person.personID);
			// System.out.println("SEAT X: " + person.getSeatX());
			// System.out.println("SEAT Y: " + person.getSeatY());
		// }

		// Heart of the program, will run at a certain clock speed that can be changed at runtime, all movement and gui elements rely on it
		mainClock();

		// Add display clock to show amount of ticks that the simulation takes
		displayClock();
		// Add a way to display the current stats so that they can be debugged in real time
		statsDisplay();
	}

	public static void addPerson(int seatingNumber) {
		JLabel personSprite = new JLabel(String.valueOf(seatingNumber));
		personSprite.setFont(new Font("Arial", Font.PLAIN, 8));
		personSprite.setOpaque(true);
		personSprite.setBackground(red);
		personSprite.setLocation(PERSON_SPAWN_X, PERSON_SPAWN_Y);
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		Person test = new Person(seatingNumber, false, personSprite, new Location(PERSON_SPAWN_X, PERSON_SPAWN_Y), new ArrayList<>(), State.MOVING);
		peopleArr.add(test);
		contentPane.add(test.personSprite);
	}

	private static void mainClock() {
		Runnable task = () -> {
			long startTime = System.currentTimeMillis();
			while (true) {
				long tempTime = System.currentTimeMillis();
				if (tempTime - startTime > clockSpeed) {
					if (paused) continue;
					startTime = System.currentTimeMillis();

					// Actions per tick happen here vvv

					if (checkIfAllSeated()) unloading = true;
					if (display_clock == Display_Clock.RUNNING) {
						if (unloading) {
							movePeopleHorizontalPriority();
						} else {
							movePeopleVerticalPriority();
						}
						ticks++;
					}
					System.out.println("tick");
					tickClock.setText(String.valueOf(ticks));
					updateStats();
				}
			}
		};
		startThread(task);
	}

	private static void displayClock() {
		tickClock.setFont(new Font("Arial", Font.PLAIN, 20));
		tickClock.setOpaque(true);
		tickClock.setLocation(PERSON_SPAWN_X-50, 30);
		tickClock.setSize(100, 100);
		contentPane.add(tickClock);
	}

	private static void statsDisplay() {
		statsDisplay.setFont(new Font("Arial", Font.PLAIN, 10));
		statsDisplay.setOpaque(true);
		statsDisplay.setLocation(PERSON_SPAWN_X-80, 200);
		statsDisplay.setSize(200, 100);
		contentPane.add(statsDisplay);
	}
	private static void updateStats() {
		statsDisplay.setText(
			"<html>People Collision: " + peopleCollision + "<br>" +
			"Queue type: " + queueType + "<br>" +
			"Amount of people: " + people_amount + "<br>" +
			"Clock speed: " + clockSpeed + "<br>" +
			"Paused: " + paused + "<br>" +
			"Unloading: " + unloading + "</html>"
		);
	}

	private static void moveAllToLocation(Location targetLocation) {
		allow_queuing();
		paused = false;
		Runnable task = () -> {
			long startTime = System.currentTimeMillis();
			int personCount = 0;
			while (true) {
				if (display_clock == Display_Clock.STOPPED) break;
				long tempTime = System.currentTimeMillis();
				if (tempTime - startTime > clockSpeed) {
					if (personCount == peopleArr.size()) break;
					queueMovementToLocation(peopleArr.get(personCount), targetLocation);
					startTime = System.currentTimeMillis();
					personCount++;
				}
			}
			Thread.currentThread().interrupt();
		};
		startThread(task);
	}

	private static void moveAllToSeats(List<Person> people) {
		allow_queuing();
		paused = false;
		Runnable task = () -> {
			long startTime = System.currentTimeMillis();
			int personCount = 0;
			while (true) {
				if (display_clock == Display_Clock.STOPPED) break;
				long tempTime = System.currentTimeMillis();
				if (tempTime - startTime > clockSpeed) {
					if (personCount == people.size())
						break;
					Location targetLocation = new Location(people.get(personCount).seatLocation.x, people.get(personCount).seatLocation.y);
					queueMovementToLocation(people.get(personCount), targetLocation);
					startTime = System.currentTimeMillis();
					personCount++;
				}
			}
			Thread.currentThread().interrupt();
		};
		startThread(task);
	}

	private static void queueMovementToLocation(Person person, Location targetLocation) {
		if (targetLocation.x % PERSON_STEP_X != 0 || targetLocation.y % PERSON_STEP_Y != 4) {
			System.out.println("BAD LOCATION INPUT:\nX: " + targetLocation.x + "\nY: " + targetLocation.y);
			return;
		} else if (person.location.x == targetLocation.x && person.location.y == targetLocation.y) {
			System.out.println("Location already reached");
			return;
		}
		System.out.println("Queued");
		person.moveToLocationQueue.add(targetLocation);
	}

	// For loading
	private static void movePeopleVerticalPriority() {
		for (Person person : peopleArr) {
			if (person.moveToLocationQueue.isEmpty()) continue;
			Location targetLocation = person.moveToLocationQueue.getFirst();
			System.out.println("Started moving to " + targetLocation);
			if (person.location.y < targetLocation.y) {
				person.moveY(PERSON_STEP_Y, peopleArr, peopleCollision);
			} else if (person.location.y > targetLocation.y) {
				person.moveY(-PERSON_STEP_Y, peopleArr, peopleCollision);
			} else if (person.location.x < targetLocation.x) {
				person.moveX(PERSON_STEP_X, peopleArr, peopleCollision);
			} else if (person.location.x > targetLocation.x) {
				person.moveX(-PERSON_STEP_X, peopleArr, peopleCollision);
			} else {
				person.isMoving = false;
				person.setColor(Color.RED);
				person.moveToLocationQueue.removeFirst();
				System.out.println("Finished moving");
				checkIfSeated(person);
			}
		}
	}
	// For unloading
	private static void movePeopleHorizontalPriority() {
		for (Person person : peopleArr) {
			if (person.moveToLocationQueue.isEmpty()) continue;
			Location targetLocation = person.moveToLocationQueue.getFirst();
			System.out.println("Started moving to " + targetLocation);
			if (person.location.x < targetLocation.x) {
				person.moveX(PERSON_STEP_X, peopleArr, peopleCollision);
			} else if (person.location.x > targetLocation.x) {
				person.moveX(-PERSON_STEP_X, peopleArr, peopleCollision);
			} else if (person.location.y < targetLocation.y) {
				person.moveY(PERSON_STEP_Y, peopleArr, peopleCollision);
			} else if (person.location.y > targetLocation.y) {
				person.moveY(-PERSON_STEP_Y, peopleArr, peopleCollision);
			} else {
				person.isMoving = false;
				person.setColor(Color.RED);
				person.moveToLocationQueue.removeFirst();
				System.out.println("Finished moving");
				checkIfSeated(person);
			}
		}
	}

	public static boolean checkIfSeated(Person person) {
		if (person.location.equals(person.seatLocation)) {
			person.state = State.SEATED;
			return true;
		}
		return false;
	}

	public static boolean checkIfAllSeated() {
		for (Person person : peopleArr) {
			if (person.state != State.SEATED) {
				return false;
			} else {
				continue;
			}
		}
		paused = true;
		return true;
	}

	// Interprets keyboard clicks (For starting and pausing actions)
	static void action(int keyCode) throws IOException {
		switch (keyCode) {
			// KeyEvent.VK_SPACE will check for when the spacebar is pressed and case an
			// event

			// START SIMULATION
			case KeyEvent.VK_SPACE:
				if (!unloading) {
					moveAllToLocation(new Location(PLANE_GRID_2_X-PERSON_STEP_X*2, PLANE_GRID_2_Y-PERSON_STEP_Y));
					pauseCurrentThread(10);
					moveAllToSeats(peopleArr);
				} else {
					allow_queuing();
					moveAllToLocation(new Location(PLANE_GRID_2_X-PERSON_STEP_X*2, PLANE_GRID_2_Y-PERSON_STEP_Y));
					moveAllToLocation(new Location(PERSON_SPAWN_X, PERSON_SPAWN_Y));
				}
				break;
			case KeyEvent.VK_W:
				for (int i = 0; i < peopleArr.size(); i++) {
					peopleArr.get(i).moveY(-PERSON_STEP_Y);
				}
				break;
			case KeyEvent.VK_A:
				for (int i = 0; i < peopleArr.size(); i++) {
					peopleArr.get(i).moveX(-PERSON_STEP_X);
				}
				break;
			case KeyEvent.VK_S:
				for (int i = 0; i < peopleArr.size(); i++) {
					peopleArr.get(i).moveY(PERSON_STEP_Y);
				}
				break;
			case KeyEvent.VK_D:
				for (int i = 0; i < peopleArr.size(); i++) {
					peopleArr.get(i).moveX(PERSON_STEP_X);
				}
				break;

			case KeyEvent.VK_COMMA:
				clockSpeed-=100;
				break;
			case KeyEvent.VK_PERIOD:
				clockSpeed+=100;
				break;

			case KeyEvent.VK_K:
				allow_queuing();
				queueMovementToLocation(peopleArr.get(0), new Location(PLANE_GRID_1_X, PLANE_GRID_1_Y-PERSON_STEP_Y));
				queueMovementToLocation(peopleArr.get(0), new Location(PLANE_GRID_2_X, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_J:
				allow_queuing();
				queueMovementToLocation(peopleArr.get(1), new Location(PLANE_GRID_1_X, PLANE_GRID_1_Y-PERSON_STEP_Y));
				queueMovementToLocation(peopleArr.get(1), new Location(PLANE_GRID_2_X, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;

			case KeyEvent.VK_U:
				moveAllToLocation(new Location(PLANE_GRID_1_X, PLANE_GRID_1_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_I:
				moveAllToLocation(new Location(PLANE_GRID_2_X-PERSON_STEP_X*2, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_O:
				moveAllToLocation(new Location(PLANE_GRID_2_X+PERSON_STEP_X*3, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_P:
				paused = (paused) ? false : true;
				break;
			case KeyEvent.VK_Y:
				moveAllToSeats(peopleArr);
				break;

			// Simulate loading/unloading
			case KeyEvent.VK_1:
				moveAllToLocation(new Location(PLANE_GRID_2_X-PERSON_STEP_X*2, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_2:
				moveAllToSeats(peopleArr);
				break;
			case KeyEvent.VK_3:
				unloading = (unloading) ? false : true;
				paused = false;
				break;
			case KeyEvent.VK_4:
				moveAllToLocation(new Location(PLANE_GRID_2_X-PERSON_STEP_X*2, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_5:
				moveAllToLocation(new Location(PERSON_SPAWN_X, PERSON_SPAWN_Y));
				break;
			case KeyEvent.VK_6:
				switch (queueType) {
					case RANDOM:
						peopleArr = PassengerQueue.backToFront(peopleArr); 
						queueType = Sort_Type.BACK_TO_FRONT;
						break;
					case BACK_TO_FRONT:
						peopleArr = PassengerQueue.frontToBack(peopleArr);
						queueType = Sort_Type.FRONT_TO_BACK;
						break;
					case FRONT_TO_BACK:
						peopleArr = PassengerQueue.groupsOf6(peopleArr);
						queueType = Sort_Type.GROUPS_6;
						break;
					case GROUPS_6:
						peopleArr = PassengerQueue.groupsOf12(peopleArr);
						queueType = Sort_Type.GROUPS_12;
						break;
					case GROUPS_12:
						peopleArr = PassengerQueue.groupsOf18(peopleArr);
						queueType = Sort_Type.GROUPS_18;
						break;
					case GROUPS_18:
						peopleArr = PassengerQueue.random(peopleArr);
						queueType = Sort_Type.RANDOM;
						break;
					default:
						break;
				}
				break;


			case KeyEvent.VK_Q:
				System.exit(0);
				break;
			
			// RESET
			case KeyEvent.VK_R:
				reset();
				break;
			// // Example keybind for letter 'e'
			// case KeyEvent.VK_E:
			// RUN ACTION
			// break;
			default:
				break;
		}
	}

	private static void allow_queuing() {
		display_clock = Display_Clock.RUNNING;
	}
	private static void reset() {
		unloading = false;
		display_clock = Display_Clock.STOPPED;
		tickClock.setText("0");
		ticks = 0;
		pauseCurrentThread(100);
		for (Person person : peopleArr) {
			person.moveToLocationQueue.clear();
			person.setX(PERSON_SPAWN_X);
			person.setY(PERSON_SPAWN_Y);
		}
	}

	// Stops the process for a certain amount of time, not recommended for timers
	public static void pauseCurrentThread(int milli) {
		try {
			Thread.sleep(milli);
		} catch (InterruptedException penis_hehe) {
			Thread.currentThread().interrupt();
		}
	}

	private static void startThread(Runnable task) {
		Thread thread = new Thread(task);
		thread.start();
	}

	private static void getSimSettings() { // I obviously ripped this from stackOverflow
		String[] result = new String[16]; // ERROR POINT
		InputStream input = CS2420_Semester_Project.class.getResourceAsStream("config.txt");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
			String line;
			boolean second_line_checker = false;
			while ((line = reader.readLine()) != null) {
				if (second_line_checker) addToArr(result, line);
				second_line_checker = (second_line_checker) ? false : true;
			}
		} catch (IOException e) {
			System.out.println("Config file not found in src/CS2420_Semester_Project directory");
			e.printStackTrace();
		}

		// Malleable part vvv
		peopleCollision = getBoolVal(result, Config_Names.PEOPLE_COLLISION.getValue());
		queueType = Sort_Type.fromvalue(getIntVal(result, Config_Names.QUEUE_TYPE.getValue()));
		people_amount = getIntVal(result, Config_Names.PEOPLE_AMOUNT.getValue());
		clockSpeed = getIntVal(result, Config_Names.CLOCK_SPEED.getValue());
	}
	private static boolean getBoolVal(String[] arr, int placement) {
		return Boolean.valueOf(arr[placement]);
	}
	private static int getIntVal(String[] arr, int placement) {
		return Integer.valueOf(arr[placement]);
	}
	private static void addToArr(String[] arr, String value) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == null)  { arr[i] = value;	break; }
		}
	}
}

enum Display_Clock {
	RUNNING,
	STOPPED
}

enum Sort_Type {
	RANDOM(0),
	BACK_TO_FRONT(1),
	FRONT_TO_BACK(2),
	GROUPS_6(3),
	GROUPS_12(4),
	GROUPS_18(5);

	private final int value; // Super compact code time!
    Sort_Type(int value) { this.value = value; }
    public int getvalue() { return value; }
	public static Sort_Type fromvalue(int value) {
		for (Sort_Type s : Sort_Type.values()) {if (s.value == value) return s;}
		throw new IllegalArgumentException("Invalid code");
	}
}
enum Config_Names {
	PEOPLE_COLLISION(0),
	QUEUE_TYPE(1),
	PEOPLE_AMOUNT(2),
	CLOCK_SPEED(3);

    private final int value;
    Config_Names(int value) { this.value = value; }
    public int getValue() { return value; }
}
