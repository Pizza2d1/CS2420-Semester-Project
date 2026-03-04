package CS2420_Semester_Project;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import CS2420_Semester_Project.PlaneGui.Person;

public class CS2420_Semester_Project {
	
	static int CLOCK_SPEED = 400;
	static List<Person> peopleArr;
	static PlaneGui frame;

	// NOTE: To start the clock you need to press space
	public static void main(String[] args) {
		
		// Starts up the gui and keybinds
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// TODO Clean up my dirty code
				try {
					PlaneGui frame = new PlaneGui();
					peopleArr = frame.peopleArr;
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
					// TODO Gonna try and make these person instances a method that can be called
					frame.addPerson(1); // Person given the value (seatnumber) of 1
					frame.addPerson(2);
					frame.addPerson(5);

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
      // KeyEvent.VK_SPACE will check for when the spacebar is pressed and case an event
			case KeyEvent.VK_SPACE:
				for (int i = 0; i < peopleArr.size(); i++) {
					Person testPerson = peopleArr.get(i);
					Timer timer = new Timer();
					timer.schedule(new MovementTimer(testPerson), i*CLOCK_SPEED, CLOCK_SPEED);
				}
				break;
      // // Example keybind for letter 'e'
			//case KeyEvent.VK_E:
      //  RUN ACTION
			//	break;
			default:
				break;
		}
	}
	
  // Stops the process for a certain amount of time, not recommended for timers
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


