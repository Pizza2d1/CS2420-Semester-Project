package CS2420_Semester_Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Cory
 */
public class PassengerQueue {

    private static final int people_amount = CS2420_Semester_Project.people_amount;

    public static List<Person> random(List<Person> queue) {
        Collections.shuffle(queue);
        return queue;
    }

    public static List<Person> backToFront(List<Person> queue) {
        List<Person> tempGroup = new ArrayList<>();
        List<Person> new_arr = new ArrayList<>();
        queue = frontToBack(queue); // resets the order of the peopleArr back to 1,2,3...
        for (int i = (people_amount/6)-1; i >= 0; i--) {
            addLeftToRightRows(queue, tempGroup, i);
            new_arr.addAll(tempGroup);
            tempGroup.clear();
        }
        return new_arr;
    }

    public static List<Person> frontToBack(List<Person> queue) {
        queue.sort(new CustomComparator());
//        int[] queueOrder = new int[people_amount];
//        for (int i = 0; i < people_amount; i++) {
//            queueOrder[i] = i;
//        }
        // for (int i : queueOrder) System.out.println(i);
        // for (Person person : queue) System.out.println(person.personID);
//        return sortPeopleArr(queue, queueOrder);
        return queue;
    }

    /*
    *   GROUPS WILL BE BOARDING BACK TO FRONT
    */
    public static List<Person> groupsOf6(List<Person> queue) {
        List<Person> tempGroup = new ArrayList<>();
        List<Person> new_arr = new ArrayList<>();
        queue = frontToBack(queue); // resets the order of the peopleArr back to 1,2,3...
        for (int i = (people_amount/6)-1; i >= 0; i--) {
            addLeftToRightRows(queue, tempGroup, i);
            Collections.shuffle(tempGroup); // Simulate the individual groups being random
            new_arr.addAll(tempGroup);
            tempGroup.clear();
        }
        return new_arr;
    }

    private static void addLeftToRightRows(List<Person> queue, List<Person> tempGroup, int i) {
        for (int j = 1; j <= 3; j++) {
            tempGroup.add(queue.get((i * 6 + j)-1));
        }
        for (int k = 6; k >= 4; k--) {
            tempGroup.add(queue.get((i * 6 + k)-1));
        }
    }

    public static List<Person> groupsOf12(List<Person> queue) {
        List<Person> tempGroup = new ArrayList<>();
        List<Person> new_arr = new ArrayList<>();
        queue = frontToBack(queue); // resets the order of the peopleArr back to 1,2,3...
        int counter = 0;
        for (int i = (people_amount/6)-1; i >= 0; i--) {
            addLeftToRightRows(queue, tempGroup, i);
            if (counter == 1) { // diff of below
                Collections.shuffle(tempGroup); // Simulate the individual groups being random
                new_arr.addAll(tempGroup);
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
            addLeftToRightRows(queue, tempGroup, i);
            if (counter == 2) { // diff of above
                Collections.shuffle(tempGroup); // Simulate the individual groups being random
                new_arr.addAll(tempGroup);
                tempGroup.clear();
                counter=0;
            } else {
                counter++;
            }
        }
        return new_arr;
    }

//    static void shuffleArray(int[] ar) {
//        Random rnd = new Random();
//        for (int i = ar.length - 1; i > 0; i--) {
//            int index = rnd.nextInt(i + 1);
//            int a = ar[index];
//            ar[index] = ar[i];
//            ar[i] = a;
//        }
//    }

//    private static List<Person> sortPeopleArr(List<Person> queue, int[] queueOrder) {
//        int[] logx = new int[queue.size()];
//        List<Person> temp_person_arr = new ArrayList<>();
//        for (int i = 0; i < queue.size(); i++) {
//            logx[i] = queue.get(i).personID;
//        }
//        int counter = 0;
//        mainloop:
//        while (temp_person_arr.size() != queue.size()) {
//            for (int i = 0; i < logx.length; i++) {
//                int personValue = logx[i];
//                if (personValue == counter) {
//                    counter++;
//                    temp_person_arr.add(queue.get(i));
//                    continue mainloop;
//                }
//            }
//        }
//        for (Person person : queue) {
//            System.out.println("PERSON ID: " + person.personID);
//        }
//        for (Person person : temp_person_arr) {
//            System.out.println("TEMP ID: " + person.personID);
//        }
//        return temp_person_arr;
//    }
//    private static List<Person> sortPeopleArr(List<Person> queue, int[] queueOrder) {
//        List<Person> newArr = new ArrayList<>();
//        queue = frontToBack(queue);
//        for (int i = 0; i < queueOrder.length; i++) {
//            newArr.add(queue.get(i));
//        }
//        int[] logx = new int[queue.size()];
//        List<Person> temp_person_arr = new ArrayList<>();
//        for (int i = 0; i < queue.size(); i++) {
//            logx[i] = queue.get(i).personID;
//        }
//        int counter = 0;
//        mainloop:
//        while (temp_person_arr.size() != queue.size()) {
//            for (int i = 0; i < logx.length; i++) {
//                int personValue = logx[i];
//                if (personValue == counter) {
//                    counter++;
//                    temp_person_arr.add(queue.get(i));
//                    continue mainloop;
//                }
//            }
//        }
//		 for (Person person : queue) {
//		 	System.out.println("PERSON ID: " + person.personID);
//		 }
//		 for (Person person : temp_person_arr) {
//		 	System.out.println("TEMP ID: " + person.personID);
//		 }
//        for (Person person : newArr) System.out.println(person.personID);
//        return newArr;
//    }
}

class CustomComparator implements Comparator<Person> {
    @Override
    public int compare(Person o1, Person o2) {
        return o1.compareTo(o2);
    }
}