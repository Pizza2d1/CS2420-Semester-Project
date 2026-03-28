package CS2420_Semester_Project;

import static CS2420_Semester_Project.PlaneGui.*;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;

/*
* A record is the same as a class, it just automatically has the getters
* and setters for personID and personSprite, clean code
*/
public class Person {

    // Testing variables
    private static final boolean REPORT_MOVEMENT = false;
    private static final boolean REPORT_COLLISIONS = false;

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
    public JLabel personSprite;
    public List<Location> moveToLocationQueue;
    public State state;

    public Person(int personID, boolean isMoving, JLabel personSprite, List<Location> moveToLocationQueue, State state) {
        this.personID = personID;
        this.isMoving = isMoving;
        this.personSprite = personSprite;
        this.moveToLocationQueue = moveToLocationQueue;
        this.state = state;
    }
    
    public int getSeatX() {
        int seatingID = personID-1;
        return (seatingID % 3) * PERSON_STEP_X + ((seatingID % 6 < 3) ? PLANE_GRID_1_X : PLANE_GRID_2_X);
    }
    public int getSeatY() {
        int seatingID = personID-1;
        return (seatingID / 6) * PERSON_STEP_Y + PLANE_GRID_1_Y;
    }

    public int getX() {
        return this.personSprite.getX();
    }
    public void setX(int new_x_pos) {
        personSprite.setLocation(new_x_pos, getY());
    }

    public int getY() {
        return this.personSprite.getY();
    }
    public void setY(int new_y_pos) {
        personSprite.setLocation(getX(), new_y_pos);
    }

    public void moveX(int offset) {
        setX(this.personSprite.getX() + offset);
        if (REPORT_MOVEMENT) System.out.println(String.format("New X pos: %d", getX()));
    }

    public void moveY(int offset) {
        setY(this.personSprite.getY() + offset);
        if (REPORT_MOVEMENT) System.out.println(String.format("New Y pos: %d", getY()));
    }

    // Collision checking movements
    public void moveX(int offset, List<Person> peopleArr, boolean peopleCollision) {
        if (peopleCollision && checkForCollision(peopleArr, getX() + offset, getY())) {
            return;
            // lockMovementForCollision(peopleArr, getX() + offset, getY());
        }
        setX(this.personSprite.getX() + offset);
        setColor(Color.GREEN);
        state = State.MOVING;
        if (REPORT_MOVEMENT) System.out.println(String.format("New X pos: %d", getX()));
    }

    public void moveY(int offset, List<Person> peopleArr, boolean peopleCollision) {
        checkForCollision(peopleArr, getX(), getY() + offset);
        if (peopleCollision && state == State.BLOCKED) {
            return;
        }
        setY(this.personSprite.getY() + offset);
        setColor(Color.GREEN);
        state = State.MOVING;
        if (REPORT_MOVEMENT) System.out.println(String.format("New Y pos: %d", getY()));
    }

    private boolean checkForCollision(List<Person> peopleArr, int target_x, int target_y) {
        for (Person other_person : peopleArr) {
            if (this.equals(other_person)) break;
            if (target_x == other_person.getX() && target_y == other_person.getY()) {
                if (REPORT_COLLISIONS) {
                    System.out.println(String.format("COLLISION:\nPerson %d: %d %d\nPerson %d: %d %d\n\n",
                    this.personID, getX(), getY(), other_person.personID, other_person.getX(),other_person.getY()));
                }
                setColor(Color.YELLOW);
                if (state == State.BLOCKED) return false; // Force movement if blocked for more than a tick
                state = State.BLOCKED;
                return true;
            }
        }
        state = State.MOVING;
        return false;
    }


    public void setColor(Color newColor) {
        this.personSprite.setBackground(newColor);
        this.personSprite.repaint();;
    }
}


record Location(int x, int y) {
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
};

enum State {
    BLOCKED,
    MOVING,
    SEATED
}



// // Couldn't use java record because they are immutable, big sad
// class Location {
//    	public int x;
//    	public int y;
// 	public Location(int x, int y) {
// 		this.x = x;
// 		this.y = y;
// 	}
    
//     @Override
//     public String toString() {
//       return "X: " + x + " Y: " + y;
//     }

//     @Override
//     public boolean equals(Object obj) {
//         if (this == obj)
//             return true;

//         if (obj == null
//             || this.getClass() != obj.getClass())
//             return false;

//         Location p1 = (Location)obj;

//         return this.x == p1.x
//             && this.y == p1.y;
//     }
// }