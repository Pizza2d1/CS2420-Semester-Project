package CS2420_Semester_Project;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * @author Pizza2d1
 */

public class PlaneGui extends JFrame {

	// For Jframe shenanigans
	private static final long serialVersionUID = 1L;
	
	// Customizable
	final static int WINDOW_X = 100;
	final static int WINDOW_Y = 100;
	final static int WINDOW_HEIGHT = 1000;
	final static int WINDOW_WIDTH = 800;

	// static int WINDOW_X;
	// static int WINDOW_Y;
	// static int WINDOW_HEIGHT;
	// static int WINDOW_WIDTH;

	// Depends on the plane image dimensions
	final static int PLANE_HEIGHT = 1000;
	final static int PLANE_WIDTH = 650;

	// REVEVANT TO THE PEOPLE, NOT TO THE GRID DISPLAY
	final static int PLANE_GRID_1_X = 270;
	final static int PLANE_GRID_1_Y = 188;

	final static int PLANE_GRID_2_X = 345;
	final static int PLANE_GRID_2_Y = 188;


	public PlaneGui(List<Person> peopleArr, JLabel planeDisplay, JPanel contentPane) {
		// getSimSettings();
		// System.out.println(WINDOW_X);
		// System.out.println(WINDOW_Y);
		// System.out.println(WINDOW_HEIGHT);
		// System.out.println(WINDOW_WIDTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		planeDisplay.setSize(PLANE_WIDTH, PLANE_HEIGHT);
		planeDisplay.setIcon(new ImageIcon(CS2420_Semester_Project.class.getResource("/CS2420_Semester_Project/sprites/plane_outline-test2.png")));

		JPanel GridPanel1 = new JPanel();
		GridPanel1.setBounds(PLANE_GRID_1_X-5, PLANE_GRID_1_Y-12, 50, 700);
		GridPanel1.setLayout(new GridLayout(30, 3));
		for (int i = 0; i < 90; i++) {
			addGridSeat(GridPanel1);
		}
		JPanel GridPanel2 = new JPanel();
		GridPanel2.setBounds(PLANE_GRID_2_X-5, PLANE_GRID_1_Y-12, 50, 700);
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
	
	// private static void getSimSettings() {
	// 	String[] result = new String[16]; // ERROR POINT
	// 	InputStream input = PlaneGui.class.getResourceAsStream("config.txt");
	// 	try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
	// 		String line;
	// 		boolean second_line_checker = false;
	// 		while ((line = reader.readLine()) != null) {
	// 			if (second_line_checker) addToArr(result, line);
	// 			second_line_checker = (second_line_checker) ? false : true;
	// 		}
	// 	} catch (IOException e) {
	// 		System.out.println("Config file not found in src/CS2420_Semester_Project directory");
	// 		e.printStackTrace();
	// 	}
	// 	WINDOW_X = getIntVal(result, Config_Names.WINDOW_X.getValue());
	// 	WINDOW_Y = getIntVal(result, Config_Names.WINDOW_Y.getValue());
	// 	WINDOW_HEIGHT = getIntVal(result, Config_Names.WINDOW_HEIGHT.getValue());
	// 	WINDOW_WIDTH = getIntVal(result, Config_Names.WINDOW_WIDTH.getValue());
	// }
	// private static int getIntVal(String[] arr, int placement) {
	// 	return Integer.valueOf(arr[placement]);
	// }
	// private static void addToArr(String[] arr, String value) {
	// 	for (int i = 0; i < arr.length; i++) {
	// 		if (arr[i] == null)  { arr[i] = value;	break; }
	// 	}
	// }

	// enum Config_Names {
	// 	WINDOW_X(4),
	// 	WINDOW_Y(5),
	// 	WINDOW_HEIGHT(6),
	// 	WINDOW_WIDTH(7);
	//     private final int value;
	//     Config_Names(int value) { this.value = value; }
	//     public int getValue() { return value; }
	// }
}

