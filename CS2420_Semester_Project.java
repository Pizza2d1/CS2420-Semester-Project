package CS2420_Semester_Project;

import java.awt.EventQueue;

public class CS2420_Semester_Project {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlaneGui frame = new PlaneGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
