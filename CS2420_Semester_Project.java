package CS2420_Semester_Project;

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

  	// Make sure it follows the person png size
	final static int PERSON_WIDTH = 10;
	final static int PERSON_HEIGHT = 10;
	// Spawning in the corner for now
	final static int PERSON_SPAWN_X = PlaneGui.WINDOW_WIDTH-50;
	final static int PERSON_SPAWN_Y = 50;
	
	static ImageIcon greenPerson = new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/green.png"));
	static ImageIcon yellowPerson = new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/yellow.png"));
	static ImageIcon redPerson = new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/red.png"));

	
	static int CLOCK_SPEED = 400;
	static List<Person> peopleArr = new ArrayList<>();
	static JPanel contentPane = new JPanel();
	static JLabel planeDisplay = new JLabel();

	// NOTE: To start the clock you need to press space
	public static void main(String[] args) {
		// Starts up the gui and keybinds
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlaneGui frame = new PlaneGui(peopleArr, planeDisplay, contentPane); // Can prob make this section shorter, but whatever
					frame.setVisible(true);
					frame.addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent e) {
							int keyCode = e.getKeyCode();
							try { // Two try/catch statements? Ewwww
								action(keyCode);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Example adding people with seating numbers
		addPerson(1); // Person given the value (seatnumber) of 1
		addPerson(2);
		
		// Coding in main should go here
		// TODO Backend stuff
		
		
	}
	

	public static void addPerson(int seatingNumber) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(PERSON_SPAWN_X, PERSON_SPAWN_Y);
		personSprite.setIcon(redPerson);
		Person test = new Person(seatingNumber, personSprite);
		peopleArr.add(test);
		contentPane.add(test.getSprite());
	}

	public void addPerson(int seatingNumber, int x_pos, int y_pos) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(x_pos, y_pos);
		personSprite.setIcon(redPerson);
		Person test = new Person(seatingNumber, personSprite);
		peopleArr.add(test);
		contentPane.add(test.getSprite());
	}

	public Person createPerson(int seatingNumber) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(PERSON_SPAWN_X, PERSON_SPAWN_Y);
		personSprite.setIcon(redPerson);
		Person test = new Person(seatingNumber, personSprite);
		peopleArr.add(test);
		return test;
	}

	// For spirtes that need an offset
	public Person createPerson(int seatingNumber, int x_pos, int y_pos) {
		JLabel personSprite = new JLabel();
		personSprite.setSize(PERSON_WIDTH, PERSON_HEIGHT);
		personSprite.setLocation(x_pos, y_pos);
		personSprite.setIcon(redPerson);
		Person test = new Person(seatingNumber, personSprite);
		return test;
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
					peopleArr.get(i).moveY(-15);
				}
				break;
			case KeyEvent.VK_A:
				for (int i = 0; i < peopleArr.size(); i++) {
					peopleArr.get(i).moveX(-15);
				}
				break;
			case KeyEvent.VK_S:
				for (int i = 0; i < peopleArr.size(); i++) {
					peopleArr.get(i).moveY(15);
				}
				break;
			case KeyEvent.VK_D:
				for (int i = 0; i < peopleArr.size(); i++) {
					peopleArr.get(i).moveX(15);
				}
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
