package CS2420_Semester_Project;

// For player constants
import CS2420_Semester_Project.subclasses.*;

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

/**
 * @author Cory
 * @author Pizza2d1
 */
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
	static boolean[] peopleMovingTracker;
	static JPanel contentPane = new JPanel();
	static JLabel planeDisplay = new JLabel();
	static JLabel tickClock = new JLabel("");
	static JLabel statsDisplay = new JLabel("");
	static boolean unloading = false;
	static boolean load_complete = false;
	static boolean paused = false;
	static int ticks = 0;

	public static void main(String[] args) {

		getSimSettings(); // Required (unless we decide to remove it)
		peopleMovingTracker = new boolean[people_amount];

		// Starts up the gui and keybinds
		EventQueue.invokeLater(() -> {
			try {
				PlaneGui frame = new PlaneGui(planeDisplay, contentPane); // Can prob make this section shorter, but whatever
				frame.setVisible(true);
				frame.addKeyListener(new KeyAdapter() {
          			@Override
					public void keyPressed(KeyEvent e) {
						int keyCode = e.getKeyCode();
						try { // Two try/catch statements? Ewwww
							action(keyCode);
						} catch (IOException _) {}
					}
				});
			} catch (Exception _) {}
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

		// Heart of the program, will run at a certain clock speed that can be changed at runtime, all movement and gui elements rely on it
		mainClock();

		// Add display clock to show amount of ticks that the simulation takes
		clockDisplay();
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
		Person test = new Person(seatingNumber, false, personSprite, new Location(PERSON_SPAWN_X, PERSON_SPAWN_Y), new ArrayList<>(), PersonState.MOVING);
		peopleArr.add(test);
		contentPane.add(test.personSprite);
	}

	private static void mainClock() {
		Runnable task = () -> {
			long startTime = System.currentTimeMillis();
			while (true) {
				long tempTime = System.currentTimeMillis();
				if (tempTime - startTime > clockSpeed) {
					updateStats();
					if (paused) continue;
					startTime = System.currentTimeMillis();

					// Actions per tick happen here vvv

					if (unloading) {
						movePeopleHorizontalPriority();
					} else {
						movePeopleVerticalPriority();
						checkIfAllSeated();
					}
					if (peopleAreMoving()) ticks++;

					System.out.println("tick");
					tickClock.setText(String.valueOf(ticks));
				}
			}
		};
		startThread(task);
	}

	private static void clockDisplay() {
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
			"Load complete: " + load_complete + "<br>" +
			"Unloading: " + unloading + "</html>"
		);
	}

	private static void moveAllToLocation(Location targetLocation) {
//		allow_queuing();
		paused = false;
		Runnable task = () -> {
			long startTime = System.currentTimeMillis();
			int personCount = 0;
			while (true) {
//				if (display_clock == Display_Clock.STOPPED) break;
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

//	private static void moveAllToLocation(Location targetLocation) {
//		paused = false;
//		Runnable task = () -> {
//			for (Person person : peopleArr) {
//				queueMovementToLocation(person, targetLocation);
//				sleep(100);
//			}
//		};
//		startThread(task);
//	}

	private static void moveAllToSeats(List<Person> people) {
		paused = false;
		Runnable task = () -> {
			long startTime = System.currentTimeMillis();
			int personCount = 0;
			while (true) {
//				if (display_clock == Display_Clock.STOPPED) break;
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
				setPersonToNotMoving(person);
				person.setColor(Color.RED);
				person.moveToLocationQueue.removeFirst();
				System.out.println("Finished moving");
				checkIfSeated(person);
				break;
			}
			setPersonToMoving(person);
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
				setPersonToNotMoving(person);
				person.setColor(Color.RED);
				person.moveToLocationQueue.removeFirst();
				System.out.println("Finished moving");
				checkIfSeated(person);
				break;
			}
			setPersonToMoving(person);
		}
	}

	public static boolean checkIfSeated(Person person) {
		if (person.location.equals(person.seatLocation)) {
			person.state = PersonState.SEATED;
			return true;
		}
		return false;
	}

	public static boolean checkIfAllSeated() {
		for (Person person : peopleArr) {
			if (!checkIfSeated(person)) return false;
		}
		if (!load_complete) paused = true;
		load_complete = true;
		return true;
	}
	private static boolean peopleAreMoving() {
		for (boolean b : peopleMovingTracker) {
			if (b) return true;
		}
		return false;
	}

	// Interprets keyboard clicks (For starting and pausing actions)
	static void action(int keyCode) throws IOException {
		switch (keyCode) {
			case KeyEvent.VK_W:
				for (Person person : peopleArr) {
					person.moveY(-PERSON_STEP_Y);
				}
				break;
			case KeyEvent.VK_A:
				for (Person person : peopleArr) {
					person.moveX(-PERSON_STEP_X);
				}
				break;
			case KeyEvent.VK_S:
				for (Person person : peopleArr) {
					person.moveY(PERSON_STEP_Y);
				}
				break;
			case KeyEvent.VK_D:
				for (Person person : peopleArr) {
					person.moveX(PERSON_STEP_X);
				}
				break;

			case KeyEvent.VK_SPACE: // For starting the simulation automatically
				if (!unloading) {
					moveAllToLocation(new Location(PLANE_GRID_2_X-PERSON_STEP_X*2, PLANE_GRID_2_Y-PERSON_STEP_Y));
					pauseCurrentThread(10);
					moveAllToSeats(peopleArr);
				} else {
					moveAllToLocation(new Location(PLANE_GRID_2_X-PERSON_STEP_X*2, PLANE_GRID_2_Y-PERSON_STEP_Y));
					moveAllToLocation(new Location(PERSON_SPAWN_X, PERSON_SPAWN_Y));
				}
				break;

			case KeyEvent.VK_COMMA:
				clockSpeed = (clockSpeed <= 100) ? 50 : clockSpeed-100; // Speeds up the clock by 100 milli, up to 1 tick per 50 milli
				break;
			case KeyEvent.VK_PERIOD:
				clockSpeed = (clockSpeed == 50) ? 100 : clockSpeed+100; // Slows down the clock, if at the fastest speed just set to 100 milli
				break;

			case KeyEvent.VK_P:
				paused = !paused;
				break;

			// Simulate loading/unloading
			case KeyEvent.VK_1:
				moveAllToLocation(new Location(PLANE_GRID_2_X-PERSON_STEP_X*2, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_2:
				moveAllToSeats(peopleArr);
				break;
			case KeyEvent.VK_3:
				unloading = !unloading;
//				paused = false;
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
			
			case KeyEvent.VK_R:
				reset();
				break;
			default:
				break;
		}
	}

	private static void reset() {
		unloading = false;
		load_complete = false;
		tickClock.setText("0");
		ticks = 0;
		pauseCurrentThread(100);
		for (Person person : peopleArr) {
			person.moveToLocationQueue.clear();
			person.setX(PERSON_SPAWN_X);
			person.setY(PERSON_SPAWN_Y);
			setPersonToMoving(person);
		}
		peopleArr.forEach(person -> person.moveToLocationQueue.clear());
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
				second_line_checker = !second_line_checker;
			}
		} catch (IOException _) {
			System.out.println("Config file not found in src/CS2420_Semester_Project directory");
		}

		// Malleable part vvv
		peopleCollision = getBoolVal(result, Config_Names.PEOPLE_COLLISION.getValue());
		queueType = Sort_Type.fromvalue(getIntVal(result, Config_Names.QUEUE_TYPE.getValue()));
		people_amount = getIntVal(result, Config_Names.PEOPLE_AMOUNT.getValue());
		clockSpeed = getIntVal(result, Config_Names.CLOCK_SPEED.getValue());
	}
	private static boolean getBoolVal(String[] arr, int placement) {
		if (arr[placement] == null) return true;
		return arr[placement].equals("1");
	}
	private static int getIntVal(String[] arr, int placement) {
		return (arr[placement] != null) ? Integer.parseInt(arr[placement]) : 0;
	}
	private static void addToArr(String[] arr, String value) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == null)  { arr[i] = value;	break; }
		}
	}
	private static void setPersonToMoving(Person person) {
		peopleMovingTracker[person.personID] = true;
	}
	private static void setPersonToNotMoving(Person person) {
		peopleMovingTracker[person.personID] = false;
	}
	private static boolean getPersonIsMoving(Person person) {
		return peopleMovingTracker[person.personID];
	}
	private static void sleep(int milliseconds) {
		long startTime = System.currentTimeMillis();
		while (true) {
			long tempTime = System.currentTimeMillis();
			if (tempTime - startTime > milliseconds) {
				return;
			}
		}
	}
}
