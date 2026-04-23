package CS2420_Semester_Project;

import CS2420_Semester_Project.subclasses.Location;
import CS2420_Semester_Project.subclasses.PersonState;

import static CS2420_Semester_Project.PlaneGui.*;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;

/**
 * @author Pizza2d1
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
    public PersonState state;
    public Location location;
    public Location seatLocation = new Location(0, 0);

    public Person(int personID, boolean isMoving, JLabel personSprite, Location location, List<Location> moveToLocationQueue, PersonState state) {
        this.personID = personID;
        this.isMoving = isMoving;
        this.personSprite = personSprite;
        this.location = location;
        this.moveToLocationQueue = moveToLocationQueue;
        this.state = state;
        seatLocation.x = getSeatX(); 
        seatLocation.y = getSeatY(); 
    }
    
    public int getSeatX() {
        // int seatingID = personID-1;
        int seatingID = personID;
        return (seatingID % 3) * PERSON_STEP_X + ((seatingID % 6 < 3) ? PLANE_GRID_1_X : PLANE_GRID_2_X);
    }
    public int getSeatY() {
        // int seatingID = personID-1;
        int seatingID = personID;
        return (seatingID / 6) * PERSON_STEP_Y + PLANE_GRID_1_Y;
    }

    public int getX() {
        return location.x;
    }
    public void setX(int new_x_pos) {
        location.x = new_x_pos;
        updateSprite();
        // System.out.println("PERSON " + personID + " CHANGED POS TO " + new_x_pos);
        // personSprite.setLocation(new_x_pos, getY());
    }

    public int getY() {
        return location.y;
    }
    public void setY(int new_y_pos) {
        location.y = new_y_pos;
        updateSprite();
        // System.out.println("PERSON " + personID + " CHANGED POS TO " + new_y_pos);
        // personSprite.setLocation(getX(), new_y_pos);
    }

    public void moveX(int offset) {
        setX(location.x + offset);
        if (REPORT_MOVEMENT) System.out.printf("New X pos: %d%n", location.x);
    }

    public void moveY(int offset) {
        setY(location.y + offset);
        if (REPORT_MOVEMENT) System.out.printf("New Y pos: %d%n", location.y);
    }

    // Collision checking movements
    public void moveX(int offset, List<Person> peopleArr, boolean peopleCollision) {
        Location targetLocation = location;
        targetLocation.x+=offset;
        checkForCollision(peopleArr, targetLocation);
        if (peopleCollision && state == PersonState.BLOCKED) {
            return;
            // lockMovementForCollision(peopleArr, getX() + offset, getY());
        }
        setX(location.x);
        setColor(Color.GREEN);
        state = PersonState.MOVING;
        if (REPORT_MOVEMENT) System.out.printf("Person: %d\nNew X pos: %d\nMoved %d units%n", personID, location.x, offset);
    }

    public void moveY(int offset, List<Person> peopleArr, boolean peopleCollision) {
        Location targetLocation = location;
        targetLocation.y+=offset;
        checkForCollision(peopleArr, targetLocation);
        if (peopleCollision && state == PersonState.BLOCKED) {
            return;
        }
        setY(location.y);
        setColor(Color.GREEN);
        state = PersonState.MOVING;
        if (REPORT_MOVEMENT) System.out.printf("Person: %d\nNew Y pos: %d\nMoved %d units%n", personID, location.y, offset);
    }

    private boolean checkForCollision(List<Person> peopleArr, Location targetLocation) {
        for (Person other_person : peopleArr) {
            if (this.equals(other_person)) break;
            if (targetLocation.equals(other_person.location)) {
                if (REPORT_COLLISIONS) {
                    System.out.printf("COLLISION:\nPerson %d: %d %d\nPerson %d: %d %d\n\n%n",
                    personID, location.x, location.y, other_person.personID, other_person.location.x, other_person.location.y);
                }
                setColor(Color.YELLOW);
                if (state == PersonState.BLOCKED && other_person.state != PersonState.SEATED) {
                    return location.x == targetLocation.x; // Force movement if blocked for more than a tick when trying to get into seat
                }
                state = PersonState.BLOCKED;
                return true;
            }
        }
        state = PersonState.MOVING;
        return false;
    }

    private void updateSprite() {
        personSprite.setLocation(location.x, location.y);
    }
    public void setColor(Color newColor) {
        personSprite.setBackground(newColor);
        personSprite.repaint();;
    }
}


// record Location(int x, int y) {
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
// };
