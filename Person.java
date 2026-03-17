package CS2420_Semester_Project;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
* A record is the same as a class, it just automatically has the getters
* and setters for seatLocation and personColor, clean code
*/
public record Person(int seatLocation, JLabel personColor) {

  	// Make sure it follows the person png size
	final static int PERSON_WIDTH = 10;
	final static int PERSON_HEIGHT = 10;
	final static int PERSON_STEP_X = 15;
	final static int PERSON_STEP_Y = 23;
	// Spawning in the corner for now
	final static int PERSON_SPAWN_X = PlaneGui.WINDOW_WIDTH-50;
	final static int PERSON_SPAWN_Y = 50;
    
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
        personColor.setLocation(personColor.getX() + offset, personColor.getY());
        System.out.println(String.format("New X pos: %d", personColor.getX() + offset));
    }

    public void moveY(int offset) {
        personColor.setLocation(personColor.getX(), personColor.getY() + offset);
        System.out.println(String.format("New Y pos: %d", personColor.getY() + offset));
    }

    public void setColor(ImageIcon newColor) {
        this.personColor.setIcon(newColor);
    }

    public JLabel getSprite() {
        return this.personColor;
    }

    // Seated and finished tasks
    public void setColorGreen() {
        this.personColor.setIcon(CS2420_Semester_Project.greenPerson);
    }

    // In the middle of a timed task (such as when storing/retrieving luggage and MAYBE when scouching past someone seated)
    public void setColorYellow() {
        this.personColor.setIcon(CS2420_Semester_Project.yellowPerson);
    }

    // Currently waiting to be able to start task (default)
    public void setColorRed() {
        this.personColor.setIcon(CS2420_Semester_Project.redPerson);
    }
}
