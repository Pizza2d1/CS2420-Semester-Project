package CS2420_Semester_Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Cory
 */
public class PassengerQueue<T> {

    private static int people_amount = CS2420_Semester_Project.people_amount;

    public static List<Person> backToFront(List<Person> queue) {
        int[] queueOrder = new int[people_amount];
        int count=0;
        for (int i = 29; i >= 0; i--) {
            for (int j = 1; j <= 3; j++) {
                queueOrder[count]=(i * 6 + j);
                count++;
            }
            for (int k = 6; k >= 4; k--) {
                queueOrder[count]=(i * 6 + k);
                count++;
            }
        }
        return sortPeopleArr(queue, queueOrder);
    }

    public static List<Person> random(List<Person> queue) {
        Collections.shuffle(queue);
        return queue;
    }

    public static List<Person> frontToBack(List<Person> queue) {
        int[] queueOrder = new int[people_amount];
        for (int i = 0; i < people_amount; i++) {
            queueOrder[i] = i;
        }
        // for (int i : queueOrder) System.out.println(i);
        // for (Person person : queue) System.out.println(person.personID);
        return sortPeopleArr(queue, queueOrder);
    }

    /*
    *   GROUPS WILL BE BOARDING BACK TO FRONT
    */
    public static List<Person> groupsOf6(List<Person> queue) {
        List<Person> tempGroup = new ArrayList<>();
        List<Person> new_arr = new ArrayList<>();
        queue = frontToBack(queue); // resets the order of the peopleArr back to 1,2,3...
        for (int i = (people_amount/6)-1; i >= 0; i--) {
            for (int j = 1; j <= 3; j++) {
                tempGroup.add(queue.get((i * 6 + j)-1));
            }
            for (int k = 6; k >= 4; k--) {
                tempGroup.add(queue.get((i * 6 + k)-1));
            }
            Collections.shuffle(tempGroup); // Simulate the individual groups being random
            for (Person j : tempGroup) new_arr.add(j);
            tempGroup.clear();
        }
        return new_arr;
    }

    public static List<Person> groupsOf12(List<Person> queue) {
        List<Person> tempGroup = new ArrayList<>();
        List<Person> new_arr = new ArrayList<>();
        queue = frontToBack(queue); // resets the order of the peopleArr back to 1,2,3...
        int counter = 0;
        for (int i = (people_amount/6)-1; i >= 0; i--) {
            for (int j = 1; j <= 3; j++) {
                tempGroup.add(queue.get((i * 6 + j)-1));
            }
            for (int k = 6; k >= 4; k--) {
                tempGroup.add(queue.get((i * 6 + k)-1));
            }
            if (counter == 1) { // diff of below
                Collections.shuffle(tempGroup); // Simulate the individual groups being random
                for (Person j : tempGroup) new_arr.add(j);
                System.out.println();
                tempGroup.clear();
                counter=0;
            } else {
                counter++;
            }
        }
        return new_arr;
    }

    public static List<Person> groupsOf18(List<Person> queue) {
        List<Person> tempGroup = new ArrayList<>();
        List<Person> new_arr = new ArrayList<>();
        queue = frontToBack(queue); // resets the order of the peopleArr back to 1,2,3...
        int counter = 0;
        for (int i = (people_amount/6)-1; i >= 0; i--) {
            for (int j = 1; j <= 3; j++) {
                tempGroup.add(queue.get((i * 6 + j)-1));
            }
            for (int k = 6; k >= 4; k--) {
                tempGroup.add(queue.get((i * 6 + k)-1));
            }
            if (counter == 2) { // diff of above
                Collections.shuffle(tempGroup); // Simulate the individual groups being random
                for (Person j : tempGroup) new_arr.add(j);
                tempGroup.clear();
                counter=0;
            } else {
                counter++;
            }
        }
        return new_arr;
    }

    static void shuffleArray(int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    private static List<Person> sortPeopleArr(List<Person> queue, int[] queueOrder) {
        int[] logx = new int[queue.size()];
        List<Person> temp_person_arr = new ArrayList<>();
        for (int i = 0; i < queue.size(); i++) {
            logx[i] = queue.get(i).personID;
        }
        int counter = 0;
        mainloop:
        while (true) {
            if (temp_person_arr.size() == queue.size()) break;
            for (int i = 0; i < logx.length; i++) {
                int personValue = logx[i];
                if (personValue == counter){
                    counter++;
                    temp_person_arr.add(queue.get(i));
                    continue mainloop;
                }
            }
        }
		// for (Person person : queue) {
		// 	System.out.println("PERSON ID: " + person.personID);
		// }
		// for (Person person : temp_person_arr) {
		// 	System.out.println("TEMP ID: " + person.personID);
		// }
        return temp_person_arr;
    }

    // private static void syncPeopleArr(List<Person> queue, int[] queueOrder) {
        // List<Person> new_array = new ArrayList<>();
        // int[] logx = new int[CS2420_Semester_Project.people_amount];
        // int[] logy = new int[CS2420_Semester_Project.people_amount];
        // for (int i : queueOrder) {
            // new_array.add()
        // }
    // }
}
