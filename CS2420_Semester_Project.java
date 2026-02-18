package CS2420_Semester_Project;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import CS2420_Semester_Project.PlaneGui.Person;
//import CS2420_Semester_Project.PlaneGui.peopleArr; // Nvm this is stupid

public class CS2420_Semester_Project {
	
	public static List<Person> peopleArr;
	static int CLOCK_SPEED = 1000;


	// NOTE: To start the clock you need to press space
	public static void main(String[] args) {
		
		// Starts up the gui and keybinds
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// TODO Clean up my dirty code
				try {
					PlaneGui frame = new PlaneGui();
					frame.setVisible(true);
					frame.addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent e) {
							int keyCode = e.getKeyCode();
							try {
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
		
		// Coding in main should go here
		// TODO Backend stuff
		
		
		
		
	}
	
	
	
	// Interprets keyboard clicks (For starting and pausing actions)
	static void action(int keyCode) throws IOException {
		switch (keyCode) {
			case KeyEvent.VK_SPACE:
				Person testPerson = PlaneGui.peopleArr.get(0);
				Timer timer = new Timer();
				timer.schedule( new Clock(testPerson), 0, CLOCK_SPEED);
				break;
			default:
				break;
		}
	}
}

// Needed to run sim people actions at a constant interval, works as a timer
class Clock extends TimerTask {
	private Person person;
	Clock(Person person) {
		this.person = person;
	}
    public void run() {
        this.person.moveX(-15);
    }
}

