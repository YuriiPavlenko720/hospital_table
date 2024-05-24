package lemon.hospitaltable.table.objects;

public class Department {
    private int id;
    private String name;
    private int chief_id;
    private int capacity;
    private int taken;
    private int free;

    public Department(int id, String name, int capacity, int taken, int free) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.taken = taken;
        this.free = free;
    }

    public Department() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chief_id=" + chief_id +
                ", capacity=" + capacity +
                ", taken=" + taken +
                ", free=" + free +
                '}';
    }
}
