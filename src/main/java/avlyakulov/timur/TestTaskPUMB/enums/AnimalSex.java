package avlyakulov.timur.TestTaskPUMB.enums;

public enum AnimalSex {

    MALE("male"),
    FEMALE("female");

    private final String sex;

    AnimalSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }
}