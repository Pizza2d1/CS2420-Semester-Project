package CS2420_Semester_Project;

// For player constants
import static CS2420_Semester_Project.Person.*;
import static CS2420_Semester_Project.PlaneGui.*;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CS2420_Semester_Project {

	// Testing variables
	static boolean peopleCollision = true;

	static Color red = Color.getHSBColor(0, 100, 50);

	static int CLOCK_SPEED = 1000;
	static List<Person> peopleArr = new ArrayList<>();
	static JPanel contentPane = new JPanel();
	static JLabel planeDisplay = new JLabel();

	// NOTE: To start the clock you need to press space
	public static void main(String[] args) {
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
						} catch (IOException err) {
						}
					}
				});
			} catch (Exception err2) {
			}
		});

		// Coding in main should go here
		// TODO Backend stuff

		// Example adding people with seating numbers
		for (int i = 0; i < 20; i++) {
			addPerson(i);
			// Randomize list
			Collections.shuffle(peopleArr);
		}

	}

	// We can shorten this down later, since a lot of this is redundant
	private static void addPerson(int seatingNumber) {
		// JLabel personSprite = new JLabel("•");
		// personSprite.setFont(new Font("Arial", Font.PLAIN, 100)); // Large font for a visible ball
		// // personSprite.setForeground(Color.RED); // Set initial color
		// personSprite.setOpaque(true); // Enable background painting
		// personSprite.setBackground(Color.RED); // Optional: set background to contrast   
		// personSprite.setLocation(PERSON_SPAWN_X, PERSON_SPAWN_Y);
		// personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);

		JLabel personSprite = new JLabel(String.valueOf(seatingNumber));
		// System.out.println(personSprite.getFont().getSize());
		personSprite.setFont(new Font("Arial", Font.PLAIN, 8)); // Large font for a visible ball
		personSprite.setOpaque(true); // Enable background painting
		personSprite.setBackground(red); // Optional: set background to contrast   
		personSprite.setLocation(PERSON_SPAWN_X, PERSON_SPAWN_Y);
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		Person test = new Person(seatingNumber, false, personSprite, new ArrayList<>());
		peopleArr.add(test);
		contentPane.add(test.personSprite);
	}

	private static void moveAllToLocation(List<Person> people, Location targetLocation) {
		Runnable task = () -> {
			long startTime = System.currentTimeMillis();
			int personCount = 0;
			while (true) {
				long tempTime = System.currentTimeMillis();
				if (tempTime - startTime > CLOCK_SPEED) {
					if (personCount == people.size())
						break;
					moveToLocation_YPriority(people.get(personCount), targetLocation);
					startTime = System.currentTimeMillis();
					personCount++;
				}
			}
			Thread.currentThread().interrupt();
		};
		startThread(task);
	}

	
	private static void makeLoggerClock() {
		Runnable task = () -> {
			long startTime = System.currentTimeMillis();
			while (true) {
				long tempTime = System.currentTimeMillis();
				if (tempTime - startTime > CLOCK_SPEED) {
					startTime = System.currentTimeMillis();

				}
			}
		};
		startThread(task);
	}


	private static void moveToLocation_YPriority(Person person, Location targetLocation) {
		Runnable task = () -> {
			long startTime = System.currentTimeMillis();
			while (true) {
				long tempTime = System.currentTimeMillis();
				if (tempTime - startTime > CLOCK_SPEED) {
					startTime = System.currentTimeMillis();
					if (person.getY() < targetLocation.y()) {
						person.moveY(PERSON_STEP_Y, peopleArr, peopleCollision);
					} else if (person.getY() > targetLocation.y()) {
						person.moveY(-PERSON_STEP_Y, peopleArr, peopleCollision);
					} else if (person.getX() < targetLocation.x()) {
						person.moveX(PERSON_STEP_X, peopleArr, peopleCollision);
					} else if (person.getX() > targetLocation.x()) {
						person.moveX(-PERSON_STEP_X, peopleArr, peopleCollision);
					} else {
						person.isMoving = false;
						break;
					}
					
				}
			}
			Thread.currentThread().interrupt();
		};
		while (true) {
			if (!person.isMoving) {
				person.isMoving = true;
				startThread(task);
				person.setColor(Color.RED);
				break;
			}
		}
	}

	private static void queueMovementToLocation(Person person, Location targetLocation) {
		Runnable task = () -> {
			while (!person.moveToLocationQueue.isEmpty()) {
				moveToLocationQueuedVertical(person);
			}
			Thread.currentThread().interrupt();
		};

		person.moveToLocationQueue.add(targetLocation);
		if (!person.isMoving) {
			person.isMoving = true;
			startThread(task);
		}
	}

	// Prioritizes Vertical (up and down) movement over Horizontal
	private static void moveToLocationQueuedVertical(Person person) {
		Location targetLocation = person.moveToLocationQueue.getFirst();
		System.out.println("Started moving to " + targetLocation);
		long startTime = System.currentTimeMillis();
		while (true) {
			long tempTime = System.currentTimeMillis();
			if (tempTime - startTime > CLOCK_SPEED) {
				startTime = System.currentTimeMillis();
				if (person.getY() < targetLocation.y()) {
					person.moveY(PERSON_STEP_Y, peopleArr, peopleCollision);
				} else if (person.getY() > targetLocation.y()) {
					person.moveY(-PERSON_STEP_Y, peopleArr, peopleCollision);
				} else if (person.getX() < targetLocation.x()) {
					person.moveX(PERSON_STEP_X, peopleArr, peopleCollision);
				} else if (person.getX() > targetLocation.x()) {
					person.moveX(-PERSON_STEP_X, peopleArr, peopleCollision);
				} else {
					person.isMoving = false;
					break;
				}
				
			}
		}
		person.setColor(Color.RED);
		person.moveToLocationQueue.removeFirst();
		System.out.println("Finished moving");
	}

	// Interprets keyboard clicks (For starting and pausing actions)
	static void action(int keyCode) throws IOException {
		switch (keyCode) {
			// KeyEvent.VK_SPACE will check for when the spacebar is pressed and case an
			// event
			case KeyEvent.VK_SPACE:
				for (int i = 0; i < peopleArr.size(); i++) {
					Person testPerson = peopleArr.get(i);
					Timer timer = new Timer();
					timer.schedule(new MoveInCircleTimer(testPerson), i * CLOCK_SPEED, CLOCK_SPEED);
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
				CLOCK_SPEED-=100;
				break;
			case KeyEvent.VK_PERIOD:
				CLOCK_SPEED+=100;
				break;

			case KeyEvent.VK_K:
				queueMovementToLocation(peopleArr.get(0), new Location(PLANE_GRID_1_X, PLANE_GRID_1_Y-PERSON_STEP_Y));
				queueMovementToLocation(peopleArr.get(0), new Location(PLANE_GRID_2_X, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_J:
				queueMovementToLocation(peopleArr.get(1), new Location(PLANE_GRID_1_X, PLANE_GRID_1_Y-PERSON_STEP_Y));
				queueMovementToLocation(peopleArr.get(1), new Location(PLANE_GRID_2_X, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;

			case KeyEvent.VK_U:
				moveAllToLocation(peopleArr, new Location(PLANE_GRID_1_X, PLANE_GRID_1_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_I:
				moveAllToLocation(peopleArr, new Location(PLANE_GRID_2_X, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_O:
				moveAllToLocation(peopleArr, new Location(PLANE_GRID_2_X+PERSON_STEP_X*3, PLANE_GRID_2_Y-PERSON_STEP_Y));
				break;
			case KeyEvent.VK_P:
				moveAllToLocation(peopleArr, new Location(PLANE_GRID_1_X, PLANE_GRID_1_Y));
				break;

			case KeyEvent.VK_Q:
				System.exit(0);
				break;
			// // Example keybind for letter 'e'
			// case KeyEvent.VK_E:
			// RUN ACTION
			// break;
			default:
				break;
		}
	}

	// Stops the process for a certain amount of time, not recommended for timers
	// Dont use this, it is a bad method, I use it as a reminder
	public static void sleepy(int milli) {
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

	// private void immediateTimer(int clockSpeed, Runnnable methodToRun) {

	// 	long startTime = System.currentTimeMillis();
	// 	while (true) {
	// 		long tempTime = System.currentTimeMillis();
	// 		if (tempTime - startTime > CLOCK_SPEED) {
	// 			startTime = System.currentTimeMillis();
	// 			if (person.getY() < targetLocation.y()) {
	// 				person.moveY(PERSON_STEP_Y, peopleArr, peopleCollision);
	// 			} else if (person.getY() > targetLocation.y()) {
	// 				person.moveY(-PERSON_STEP_Y, peopleArr, peopleCollision);
	// 			} else if (person.getX() < targetLocation.x()) {
	// 				person.moveX(PERSON_STEP_X, peopleArr, peopleCollision);
	// 			} else if (person.getX() > targetLocation.x()) {
	// 				person.moveX(-PERSON_STEP_X, peopleArr, peopleCollision);
	// 			} else {
	// 				person.isMoving = false;
	// 				break;
	// 			}
				
	// 		}
	// 	}
	// }
}

class MoveInCircleTimer extends TimerTask {
	private final Person person;

	MoveInCircleTimer(Person person) {
		this.person = person;
	}

	int count = 1;

	public void run() {
		// Simple circle loop
		switch (count) {
			case 1:
				this.person.moveX(-15);
				count++;
				break;
			case 2:
				this.person.moveY(-15);
				count++;
				break;
			case 3:
				this.person.moveX(15);
				count++;
				break;
			case 4:
				this.person.moveY(15);
				count = 1;
				break;
		}
		// System.out.println(count);
	}
}

// Needed to run sim people actions at a constant interval, works as a timer
// class MainClock extends TimerTask {
// private final int CLOCK_SPEED;

// MainClock(int CLOCK_SPEED) {
// this.CLOCK_SPEED = CLOCK_SPEED;
// }

// public void run() {

// }
// }

// class MoveToPointTimer extends TimerTask {
// public Person person;
// private int x;
// private int y;
// MoveToPointTimer(Person person, int x, int y) {
// this.person = person;
// this.x = x;
// this.y = y;
// }
