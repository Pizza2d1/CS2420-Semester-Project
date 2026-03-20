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

		// Timer timer = new Timer();
		// timer.schedule(new MainClock(peopleMovementQueue, CLOCK_SPEED), 0, CLOCK_SPEED);

		// Coding in main should go here
		// TODO Backend stuff

		// Example adding people with seating numbers
		addPerson(1); // Person given the value (seatnumber) of 1
		addPerson(2);
		addPerson(3);
		addPerson(4);
		
		
	}
	
	// We can shorten this down later, since a lot of this is redundant
	public static void addPerson(int seatingNumber) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(PERSON_SPAWN_X, PERSON_SPAWN_Y);
		personSprite.setIcon(redPerson);
		Person test = new Person(seatingNumber, false, new Location(PERSON_SPAWN_X, PERSON_SPAWN_Y), personSprite);
		peopleArr.add(test);
		contentPane.add(test.getSprite());
	}
	
	public static void addPerson(int seatingNumber, ImageIcon color) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(PERSON_SPAWN_X, PERSON_SPAWN_Y);
		personSprite.setIcon(color);
		Person test = new Person(seatingNumber, false, new Location(PERSON_SPAWN_X, PERSON_SPAWN_Y), personSprite);
		peopleArr.add(test);
		contentPane.add(test.getSprite());
	}

	public static void addPerson(int seatingNumber, int x_pos, int y_pos) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(x_pos, y_pos);
		personSprite.setIcon(redPerson);
		Person test = new Person(seatingNumber, false, new Location(PERSON_SPAWN_X, PERSON_SPAWN_Y), personSprite);
		peopleArr.add(test);
		contentPane.add(test.getSprite());
	}

	private static void startThread(Runnable task) {
    Thread thread = new Thread(task);
    thread.start(); // This creates a new thread and calls run() in it
	}

	public static void moveAllToLocation(List<Person> people, int x, int y) {
			Runnable task = () -> {
				long startTime = System.currentTimeMillis();
				int personCount = 0;
				while (true) { 
					long tempTime = System.currentTimeMillis();
					if (tempTime - startTime > CLOCK_SPEED) {
						if (personCount == people.size()) {
							break;
						}
						moveToLocation(people.get(personCount), x, y);
						startTime = System.currentTimeMillis();
						personCount++;
					}
				}
				Thread.currentThread().interrupt();
  	  };
			startThread(task);
	}



	public static void moveToLocation(Person person, int x, int y) {
			Runnable task = () -> {
				long startTime = System.currentTimeMillis();
				Location targetLocation = new Location(x,y);
				// System.out.println("Person: "+ person.seatLocation + "\n" + targetLocation);
				mainloop:
				while (true) {
					if (person.equals(peopleArr.get(0))) break;
					long tempTime = System.currentTimeMillis();
					for (Person other_people : peopleArr) {
						if (person.equals(other_people)) continue;
						if (person.location == targetLocation) {
							continue mainloop;
						} else {
							break;
						}
					}
					if (tempTime - startTime > CLOCK_SPEED) {
						startTime = System.currentTimeMillis();
						if (person.getY() < y) {
							person.moveY(PERSON_STEP_Y);
						} else if (person.getY() > y) {
							person.moveY(-PERSON_STEP_Y);
						} else if (person.getX() < x) {
							person.moveX(PERSON_STEP_X);
						} else if (person.getX() > x) {
							person.moveX(-PERSON_STEP_X);
						} else {
							// System.out.println("Finished moving");
							person.isMoving = false;
							break;
						}
					}
				}
				Thread.currentThread().interrupt();
  	  };
			while (true) { 
				if (person.equals(peopleArr.get(0))) break;
				if (!person.isMoving) {
					person.isMoving = true;
					startThread(task);
					break;
				}
			}
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

			case KeyEvent.VK_U:
				moveAllToLocation(peopleArr, PLANE_GRID_1_X, PLANE_GRID_1_Y);
				break;
			case KeyEvent.VK_I:
				moveAllToLocation(peopleArr, PLANE_GRID_2_Y, PLANE_GRID_2_Y);
				break;
			case KeyEvent.VK_O:
				moveAllToLocation(peopleArr, PLANE_GRID_1_X, PLANE_GRID_1_Y);
				break;
			case KeyEvent.VK_P:
				moveAllToLocation(peopleArr, PLANE_GRID_1_X, PLANE_GRID_1_Y);
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

class MoveInCircleTimer extends TimerTask {
	private Person person;
	MoveInCircleTimer(Person person) {
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
      // System.out.println(count);
    }
}

// Needed to run sim people actions at a constant interval, works as a timer
// class MainClock extends TimerTask {
// 	private final int CLOCK_SPEED;
	
// 	MainClock(int CLOCK_SPEED) {
// 		this.CLOCK_SPEED = CLOCK_SPEED;
// 	}

//   public void run() {

// 	}
// }



// class MoveToPointTimer extends TimerTask {
// 	public Person person;
// 	private int x;
// 	private int y;
// 	MoveToPointTimer(Person person, int x, int y) {
// 		this.person = person;
// 		this.x = x;
// 		this.y = y;
// 	}

//   @Override
//   public void run() {
// 		if (person.getY() < this.y) {
// 			this.person.moveY(PERSON_STEP_Y);
// 		} else if (person.getY() > this.y) {
// 			this.person.moveY(-PERSON_STEP_Y);
// 		} else if (person.getX() < this.x) {
// 			this.person.moveX(PERSON_STEP_X);
// 		} else if (person.getX() > this.x) {
// 			this.person.moveX(-PERSON_STEP_X);
// 		} else {
// 			System.out.println("Big balls");
// 			Thread.currentThread().interrupt();
// 		}
//   }
// }

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
