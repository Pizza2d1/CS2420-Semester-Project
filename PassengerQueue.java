package CS2420_Semester_Project;

import java.util.ArrayList;

/**
 *
 * @author Cory
 */
public class PassengerQueue {
    
    public static ArrayList<Integer> backToFront() {
        ArrayList<Integer> backToFrontQueue = new ArrayList<>();
        for (int i = 29; i >= 0; i--) {
            for (int j = 1; j <= 3; j++) {
                backToFrontQueue.add(i * 6 + j);
            }
            
            for (int k = 6; k >= 4; k--) {
                backToFrontQueue.add(i * 6 + k);
            }
        }
        return backToFrontQueue;
    }
    
    public static void main(String[] args) {
//        ArrayList<Integer> queue = backToFront();
        
        System.out.println(backToFront().toString());
    }
}
