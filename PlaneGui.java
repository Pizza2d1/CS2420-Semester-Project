package CS2420_Semester_Project;

import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PlaneGui extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Customizable
	final static int WINDOW_X = 100;
	final static int WINDOW_Y = 100;
	final static int WINDOW_HEIGHT = 1000;
	final static int WINDOW_WIDTH = 800;

	// Depends on the plane image dimensions
	final static int PLANE_HEIGHT = 1000;
	final static int PLANE_WIDTH = 650;
	
	public PlaneGui(List<Person> peopleArr, JLabel planeDisplay, JPanel contentPane) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		planeDisplay.setSize(PLANE_WIDTH, PLANE_HEIGHT);
		planeDisplay.setIcon(new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/plane_outline-test2.png")));
		contentPane.add(planeDisplay);
	}
}
