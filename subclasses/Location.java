package CS2420_Semester_Project.subclasses;

/*
 * @author Pizza2d1
 */
public class Location { // Couldn't use java record because they are immutable, big sad
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
