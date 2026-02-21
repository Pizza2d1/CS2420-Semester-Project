package CS2420_Semester_Project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class Testing_Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Testing_Frame frame = new Testing_Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Testing_Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblNewLabel = new JLabel("New label");
		contentPane.add(lblNewLabel);

		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(204, 0, 0)));
		contentPane.add(panel);
		panel.setLayout(new GridLayout(30, 3, 0, 0));
		panel.setBounds(0, 0, 100, 100);

		addPanel(" ");
		addPanel(" ");
		addPanel(" ");
		addPanel(" ");
		addPanel(" ");
		addPanel(" ");

	}

	public void addPanel(String balls) {
		JLabel lblNewLabel_1 = new JLabel(balls);
		panel.add(lblNewLabel_1);
	}

}
