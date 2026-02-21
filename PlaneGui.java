package CS2420_Semester_Project;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PlaneGui extends JFrame {

	private static final long serialVersionUID = 1L;
	JPanel contentPane;

	final static int WINDOW_X = 100;
	final static int WINDOW_Y = 100;
	final static int WINDOW_HEIGHT = 1000;
	final static int WINDOW_WIDTH = 800;

	// Depends on the plane image dimensions
	final static int PLANE_HEIGHT = 1000;
	final static int PLANE_WIDTH = 650;
	  
	final static int PERSON_WIDTH = 10;
	final static int PERSON_HEIGHT = 10;
	// Spawning in the corner for now
	final static int PERSON_SPAWN_X = WINDOW_WIDTH-50;
	final static int PERSON_SPAWN_Y = 50;
	
	//static List<Person> peopleArr = new ArrayList<>();

	JLabel planeDisplay = new JLabel();
	static ImageIcon greenPerson = new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/green.png"));
	static ImageIcon yellowPerson = new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/yellow.png"));
	static ImageIcon redPerson = new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/red.png"));
	List<Person> peopleArr = new ArrayList<>();
	
	
	public record Person(int seatLocation, JLabel personColor) {
			public int getX() {
				return personColor.getX();
			} 
			public void setX(int new_x_pos) {
				personColor.setLocation(new_x_pos, getY());
				System.out.println(String.format("New X pos: %d", new_x_pos));
			} 
			public int getY() {
				return personColor.getY();
			} 
			public void setY(int new_y_pos) {
		    	personColor.setLocation(getX(), new_y_pos);
		    	System.out.println(String.format("New Y pos: %d", new_y_pos));
			}
		    public void moveX(int offset) {
		    	personColor.setLocation(personColor.getX()+offset, personColor.getY());
		    	System.out.println(String.format("New X pos: %d", personColor.getX()+offset));
			} 
		    public void moveY(int offset) {
		    	personColor.setLocation(personColor.getX(), personColor.getY()+offset);
		    	System.out.println(String.format("New Y pos: %d", personColor.getY()+offset));
			} 
		    public void setColor(ImageIcon newColor) {
			  this.personColor.setIcon(newColor);
		    } 
		    public JLabel getSprite() {
		      return this.personColor;
		    } 
		
		    // Seated and finished tasks
		    public void setColorGreen() {
			  this.personColor.setIcon(greenPerson);
		    } 
		    // In the middle of a timed task (such as when storing/retrieving luggage and MAYBE when scouching past someone seated)
		    public void setColorYellow() {
			  this.personColor.setIcon(yellowPerson);
		    }
		    // Currently waiting to be able to start task (default)
		    public void setColorRed() {
		      this.personColor.setIcon(redPerson);
		    } 
	}
	
//  	public class Person {
//  		int seatLocation;
//  		JLabel personColor;
//    
//  		Person(int seatLocation, JLabel personColor) {
//  			this.seatLocation = seatLocation;
//  			this.personColor = personColor;
//  		}
//    
//	    public int getX() {
//	      return personColor.getX();
//	    } 
//	    public void setX(int new_x_pos) {
//	    	personColor.setLocation(new_x_pos, getY());
//	    	System.out.println(String.format("New X pos: %d", new_x_pos));
//	    } 
//	    public int getY() {
//		  return personColor.getY();
//		} 
//		public void setY(int new_y_pos) {
//	    	personColor.setLocation(getX(), new_y_pos);
//	    	System.out.println(String.format("New Y pos: %d", new_y_pos));
//		}
//	    public void moveX(int offset) {
//	    	personColor.setLocation(personColor.getX()+offset, personColor.getY());
//	    	System.out.println(String.format("New X pos: %d", personColor.getX()+offset));
//		} 
//	    public void moveY(int offset) {
//	    	personColor.setLocation(personColor.getX(), personColor.getY()+offset);
//	    	System.out.println(String.format("New Y pos: %d", personColor.getY()+offset));
//		} 
//	    public void setColor(ImageIcon newColor) {
//		  this.personColor.setIcon(newColor);
//	    } 
//	    public JLabel getSprite() {
//	      return this.personColor;
//	    } 
//	    public void setSprite(JLabel newSprite) {
//	      this.personColor = newSprite;
//	    } 
//	
//	    // Seated and finished tasks
//	    public void setColorGreen() {
//		  this.personColor.setIcon(greenPerson);
//	    } 
//	    // In the middle of a timed task (such as when storing/retrieving luggage and MAYBE when scouching past someone seated)
//	    public void setColorYellow() {
//		  this.personColor.setIcon(yellowPerson);
//	    }
//	    // Currently waiting to be able to start task (default)
//	    public void setColorRed() {
//	      this.personColor.setIcon(redPerson);
//	    } 
//  	}

	public PlaneGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		planeDisplay.setSize(PLANE_WIDTH, PLANE_HEIGHT);
		planeDisplay.setIcon(new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/plane_outline-test2.png")));
		contentPane.add(planeDisplay);
		//Person test = createPerson(15);
		//contentPane.add(test.getSprite());
		//contentPane.add(createPerson(15).getSprite());
	}
	
	public void addPerson(int seatingNumber) {
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
}
