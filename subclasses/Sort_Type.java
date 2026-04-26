package CS2420_Semester_Project.subclasses;

public enum Sort_Type {
	RANDOM(0),
	BACK_TO_FRONT(1),
	FRONT_TO_BACK(2),
	GROUPS_6(3),
	GROUPS_12(4),
	GROUPS_18(5);

	private final int value; // Super compact code time!
    Sort_Type(int value) { this.value = value; }
    public int getvalue() { return value; }
	public static Sort_Type fromvalue(int value) {
		for (Sort_Type s : Sort_Type.values()) {if (s.value == value) return s;}
		throw new IllegalArgumentException("Invalid code");
	}
}
