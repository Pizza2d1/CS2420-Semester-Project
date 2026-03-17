package CS2420_Semester_Project;

// For player constants
import static CS2420_Semester_Project.Person.*;
import static CS2420_Semester_Project.PlaneGui.*;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CS2420_Semester_Project {
	
	static ImageIcon greenPerson = new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/green.png"));
	static ImageIcon yellowPerson = new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/yellow.png"));
	static ImageIcon redPerson = new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/red.png"));

	
	static int CLOCK_SPEED = 100;
	static List<Person> peopleArr = new ArrayList<>();
	static List<Pair<PersonChecker, Location>> peopleMovementQueue = new ArrayList<>();
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

		Timer timer = new Timer();
		timer.schedule(new MainClock(peopleMovementQueue, CLOCK_SPEED), 0, CLOCK_SPEED);

		// Example adding people with seating numbers
		addPerson(1); // Person given the value (seatnumber) of 1
		addPerson(2);
		
		// Coding in main should go here
		// TODO Backend stuff






		
		
	}
	

	// We can shorten this down later, since a lot of this is redundant
	public static void addPerson(int seatingNumber) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(PERSON_SPAWN_X, PERSON_SPAWN_Y);
		personSprite.setIcon(redPerson);
		Person test = new Person(seatingNumber, personSprite);
		peopleArr.add(test);
		contentPane.add(test.getSprite());
	}

	public static void addPerson(int seatingNumber, int x_pos, int y_pos) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(x_pos, y_pos);
		personSprite.setIcon(redPerson);
		Person test = new Person(seatingNumber, personSprite);
		peopleArr.add(test);
		contentPane.add(test.getSprite());
	}

	public static Person createPerson(int seatingNumber) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(PERSON_SPAWN_X, PERSON_SPAWN_Y);
		personSprite.setIcon(redPerson);
		Person test = new Person(seatingNumber, personSprite);
		peopleArr.add(test);
		return test;
	}

	// For spirtes that need an offset
	public static Person createPerson(int seatingNumber, int x_pos, int y_pos) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(x_pos, y_pos);
		personSprite.setIcon(redPerson);
		Person test = new Person(seatingNumber, personSprite);
		return test;
	}


	public static void moveToLocation(Person person, int x, int y) {
		 	peopleMovementQueue.add(new Pair<>(new PersonChecker(person, false, new Timer()), new Location(x, y)));
			// Timer timer = new Timer();
			// timer.schedule(new SnakeTimer(person, x, y), 0, CLOCK_SPEED);
	}
	public static void addMovementTimer(Person person, int x, int y) {
			Timer timer = new Timer();
			timer.schedule(new SnakeTimer(person, x, y), 0, CLOCK_SPEED);
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
					timer.schedule(new MovementTimer(testPerson), i * CLOCK_SPEED, CLOCK_SPEED);
				}
				break;
			case KeyEvent.VK_W:
				System.out.println(peopleArr.size());
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

			case KeyEvent.VK_P:
				Person person = peopleArr.get(0);
				moveToLocation(person, PLANE_GRID_1_X, PLANE_GRID_1_Y);
				break;
			case KeyEvent.VK_O:
				Person person1 = peopleArr.get(0);
				moveToLocation(person1, 0, 0);
				break;
			

			case KeyEvent.VK_Q:
				System.exit(0);
				break;
			// // Example keybind for letter 'e'
			// case KeyEvent.VK_E:
			// 	RUN ACTION
			// break;
			default:
				break;
		}
	}

	// Stops the process for a certain amount of time, not recommended for timers
	// Dont use this, it is a bad method
	public static void sleepy(int milli) {
		try {
			Thread.sleep(milli);
		} catch (InterruptedException penis_hehe) {
			// recommended because catching InterruptedException clears interrupt flag
			Thread.currentThread().interrupt();
			// you probably want to quit if the thread is interrupted
			return;
		}
	}
}


// Needed to run sim people actions at a constant interval, works as a timer
class MainClock extends TimerTask {
	private List<Pair<PersonChecker, Location>> queue;
	private int CLOCK_SPEED;
	MainClock(List<Pair<PersonChecker,Location>> peopleMovementQueue, int CLOCK_SPEED) {
		this.queue = peopleMovementQueue;
		this.CLOCK_SPEED = CLOCK_SPEED;
	}
	List<SnakeTimer> activeTimers = new ArrayList<>();
    public void run() {
		List<Person> movementCache = new ArrayList<>();
		for (Pair<PersonChecker, Location> item : queue) {
			Person person = item.p().person();
			Timer timer = item.p().timer();
			Location pointB = item.b();
			
			System.out.println(activeTimers.size());
			for (SnakeTimer activeTimer : activeTimers) {
				if (activeTimer.getPerson() == person) {
					// If the movement is done, stop moving and remove from queue
					if (activeTimer.getFlag() == true) {
						queue.remove(item);
						System.out.println("STOPPED TIMER");
						timer.cancel();
						timer = null;
						activeTimers.remove(activeTimer);
						//continue;
					}
				}
			}
			// If multiple move requests, wait until first is done
			if (movementCache.contains(person)) {
				System.out.println("PENIS");
				continue;
			} else {
				movementCache.add(person);
			}

			SnakeTimer timertype = new SnakeTimer(person, pointB.x(), pointB.y());
			addMovementTimer(person, timer, timertype);
			activeTimers.add(timertype);
			System.out.println(activeTimers.size());
		}
		movementCache = null;
		System.out.println("Tick");



	}
	public void addMovementTimer(Person person, Timer timer, SnakeTimer timertype) { // flag will be used to check if the movement is finished
			// SnakeTimer timerObject = new SnakeTimer(person, x, y);
			timer.schedule(timertype, 0, this.CLOCK_SPEED);
	}
}



class SnakeTimer extends TimerTask {
	public Person person;
	private int x;
	private int y;
	public boolean flag = false;
	SnakeTimer(Person person, int x, int y) {
		this.person = person;
		this.x = x;
		this.y = y;
	}
    public void run() {
		//Simple circle loop
		if (person.getY() < y) {
			this.person.moveY(PERSON_STEP_Y);
		} else if (person.getY() > y) {
			this.person.moveY(-PERSON_STEP_Y);
		} else if (person.getX() < x) {
			this.person.moveX(PERSON_STEP_X);
		} else if (person.getX() > x) {
			this.person.moveX(-PERSON_STEP_X);
		} else {
			this.flag = true;
			//cancel();
		}
    }
	public Person getPerson() {
		return this.person;
	}
	public boolean getFlag() {
		return this.flag;
	}
}

// class SnakeTimer extends TimerTask {
// 	private Person person;
// 	private int x;
// 	private int y;
// 	SnakeTimer(Person person, int x, int y) {
// 		this.person = person;
// 		this.x = x;
// 		this.y = y;
// 	}
//     int count = 1;
//     public void run() {
// 		//Simple circle loop
// 		if (person.getY() < y) {
// 			this.person.moveY(PERSON_STEP_Y);
// 		} else if (person.getY() > y) {
// 			this.person.moveY(-PERSON_STEP_Y);
// 		} else if (person.getX() < x) {
// 			this.person.moveX(PERSON_STEP_X);
// 		} else if (person.getX() > x) {
// 			this.person.moveX(-PERSON_STEP_X);
// 		} else {
// 			cancel();
// 		}
//     }
// }

class MovementTimer extends TimerTask {
	private Person person;
	MovementTimer(Person person) {
		this.person = person;
	}
    int count = 1;
    public void run() {
    //Simple circle loop
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
      System.out.println(count);
    }
}

// class Pair<P,B> {
//     private P p;
//     private B b;
//     public Pair(P p, B b){
//         this.p = p;
//         this.b = b;
//     }
//     public P getP(){ return p; }
//     public B getB(){ return b; }
//     public void setP(P p){ this.p = p; }
//     public void setB(B b){ this.b = b; }
// }


record Pair<P,B>(P p, B b) {} // Shortened version of above Pair class
record PersonChecker(Person person, boolean flag, Timer timer) {}
record Location(int x, int y) {}