package CS2420_Semester_Project;

import java.awt.Color;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
* A record is the same as a class, it just automatically has the getters
* and setters for personID and personColor, clean code
*/
public class Person {

    // Testing variables
    private static boolean reportMovement = false;
    private static boolean reportCollisions = false;

    // Make sure it follows the person png size
    public static final int PERSON_WIDTH = 10;
    public static final int PERSON_HEIGHT = 10;
    public static final int PERSON_STEP_X = 15;
    public static final int PERSON_STEP_Y = 23;
    // Spawning in the corner for now
    public static final int PERSON_SPAWN_X = PlaneGui.WINDOW_WIDTH-50;
    public static final int PERSON_SPAWN_Y = 50;

    public int personID;
    public boolean isMoving;
    public Location location;
    public JLabel personColor;

    public Person(int personID, boolean isMoving, Location location, JLabel personColor) {
        this.personID = personID;
        this.isMoving = isMoving;
        this.location = location;
        this.personColor = personColor;
    }
    
    public int getX() {
        return this.location.x;
    }
    public void setX(int new_x_pos) {
        this.location.x = new_x_pos;
        // System.out.println(String.format("New X pos: %d", new_x_pos));
    }

    public int getY() {
        return this.location.y;
    }
    public void setY(int new_y_pos) {
        this.location.y = new_y_pos;
        // System.out.println(String.format("New Y pos: %d", new_y_pos));
    }

    public void moveX(int offset) {
        setX(this.location.x + offset);
        updateSprite();
        if (reportMovement) System.out.println(String.format("New X pos: %d", getX()));
    }

    public void moveY(int offset) {
        setY(this.location.y + offset);
        updateSprite();
        if (reportMovement) System.out.println(String.format("New Y pos: %d", getY()));
    }

    // Collision checking movements
    public void moveX(int offset, List<Person> peopleArr) {
        checkForCollision(peopleArr, new Location(getX() + offset, getY()));
        setX(this.location.x + offset);
        updateSprite();
        if (reportMovement) System.out.println(String.format("New X pos: %d", getX()));
    }

    public void moveY(int offset, List<Person> peopleArr) {
        checkForCollision(peopleArr, new Location(getX(), getY() + offset));
        setY(this.location.y + offset);
        updateSprite();
        if (reportMovement) System.out.println(String.format("New Y pos: %d", getY()));
    }

    // Will make the person wait for a open space to move
    private void checkForCollision(List<Person> peopleArr, Location targetLocation) {
        boolean flag = true;
        while (true) {
            flag = false;
            for (Person other_person : peopleArr) {
                if (this.equals(other_person)) break;
                if (targetLocation.equals(other_person.location)) {
                    if (reportCollisions)
                        System.out.println(String.format("COLLISION:\nPerson %d: %d %d\nPerson %d: %d %d\n\n",
                        this.personID, getX(), getY(), other_person.personID, other_person.getX(),other_person.getY()));
                    setColor(Color.YELLOW);
                    flag = true;
                    break;
                }
            }
            if (!flag) break;
        }
        setColor(Color.GREEN);
    }


    @Deprecated
    public void setColor(ImageIcon newColor) {
        this.personColor.setIcon(newColor);
    }

    public void setColor(Color newColor) {
        this.personColor.setBackground(newColor);
        this.personColor.repaint();;
    }

    public JLabel getSprite() {
        return this.personColor;
    }

    public void updateSprite() {
        personColor.setLocation(getX(), getY());
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

    public boolean hasSameLocation(Location other) {
        if (this.location == other)
            return true;
        if (other == null
            || this.location.getClass() != other.getClass())
            return false;
        Location p1 = (Location)other;
        return this.location.x == p1.x
            && this.location.y == p1.y;
    }
}

// Couldn't use java record because they are immutable, big sad
class Location {
   	public int x;
   	public int y;
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
    
    @Override
    public String toString() {
      return "X: " + x + " Y: " + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null
            || this.getClass() != obj.getClass())
            return false;

        Location p1 = (Location)obj;

        return this.x == p1.x
            && this.y == p1.y;
    }
}