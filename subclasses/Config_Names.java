package CS2420_Semester_Project.subclasses;

public enum Config_Names {
	PEOPLE_COLLISION(0),
	QUEUE_TYPE(1),
	PEOPLE_AMOUNT(2),
	CLOCK_SPEED(3);

    private final int value;
    Config_Names(int value) { this.value = value; }
    public int getValue() { return value; }
}
