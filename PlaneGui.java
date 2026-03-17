package CS2420_Semester_Project;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
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

	final static int PLANE_GRID_1_X = 270;
	final static int PLANE_GRID_1_Y = 142;

	final static int PLANE_GRID_2_X = 375;
	final static int PLANE_GRID_2_Y = 142;
	
	public PlaneGui(List<Person> peopleArr, JLabel planeDisplay, JPanel contentPane) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		planeDisplay.setSize(PLANE_WIDTH, PLANE_HEIGHT);
		planeDisplay.setIcon(new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/plane_outline-test2.png")));

		JPanel GridPanel1 = new JPanel();
		GridPanel1.setBounds(265, 130, 50, 700);
		GridPanel1.setLayout(new GridLayout(30, 3));
		for (int i = 0; i < 90; i++) {
			addGridSeat(GridPanel1);
		}
		JPanel GridPanel2 = new JPanel();
		GridPanel2.setBounds(340, 130, 50, 700);
		GridPanel2.setLayout(new GridLayout(30, 3));
		for (int i = 0; i < 90; i++) {
			addGridSeat(GridPanel2);
		}
		contentPane.add(GridPanel1);
		contentPane.add(GridPanel2);
		contentPane.add(planeDisplay);
	}
	
	public final void addGridSeat(JPanel panel) {
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		JLabel GridItem = new JLabel("");
		panel.add(GridItem);
		GridItem.setBorder(border);
	}
}
