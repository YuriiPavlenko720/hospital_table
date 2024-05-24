package lemon.hospitaltable.table.objects;

public class Ward {
    private int id;
    private int level;
    private String name;
    private int department_id;
    private int capacity;
    private int taken;
    private int free;

    public Ward(int id, int level, String name, int department_id, int capacity) {
        this.id = id;
        this.level = level;
        this.name = name;
        this.department_id = department_id;
        this.capacity = capacity;
        this.taken = 0;
        this.free = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getTaken() {
        return taken;
    }

    public void setTaken(int taken) {
        this.taken = taken;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    @Override
    public String toString() {
        return "Ward{" +
                "id=" + id +
                ", level=" + level +
                ", name='" + name + '\'' +
                ", department_id=" + department_id +
                ", capacity=" + capacity +
                ", taken=" + taken +
                ", free=" + free +
                '}';
    }
}
