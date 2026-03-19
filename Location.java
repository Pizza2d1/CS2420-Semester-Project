package CS2420_Semester_Project; 

public class Location {
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
}