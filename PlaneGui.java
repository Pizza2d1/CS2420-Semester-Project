package CS2420_Semester_Project;

import java.awt.Color;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PlaneGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	final int WINDOW_X = 100;
	final int WINDOW_Y = 100;
	final int WINDOW_HEIGHT = 1000;
	final int WINDOW_WIDTH = 800;

	final int PLANE_HEIGHT = 1000;
	final int PLANE_WIDTH = 600;
	  
	final int PERSON_WIDTH = 200;
	final int PERSON_HEIGHT = 250;
	final int PERSON_SPAWN_X = WINDOW_X-50;
	final int PERSON_SPAWN_Y = 50;

	List<Integer> stack;
	JLabel planeDisplay = new JLabel();

  	public class Person {
  		Color personColor;
  		int x_pos;
  		int y_pos;
    
  		Person() {
  			this.personColor = Color.green;
  			this.x_pos = PERSON_SPAWN_X;
  			this.y_pos = PERSON_SPAWN_Y;
  		}
    
	    public Color getColor() {
	      return this.personColor;
	    } 
	    public void setColor(Color newColor) {
	      this.personColor = newColor;
	    } 
	    public int getX() {
	      return this.x_pos;
	    } 
	    public void setX(int new_x_pos) {
	      this.x_pos = new_x_pos;
	    } 
	    public int getY() {
	      return this.y_pos;
	    } 
	    public void setY(int new_y_pos) {
	      this.y_pos = new_y_pos;
	    } 
	
	    public void setColorGreen() {
	      this.personColor = Color.green;
	    } 
	    public void setColorYellow() {
	      this.personColor = Color.yellow;
	    } 
	    public void setColorRed() {
	      this.personColor = Color.red;
	    } 
  	}

	/**
	 * Create the frame.
	 */
	public PlaneGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		planeDisplay.setSize(PLANE_WIDTH, PLANE_HEIGHT);
		planeDisplay.setIcon(new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/plane_outline-test.png")));
		contentPane.add(planeDisplay);
	}
}
