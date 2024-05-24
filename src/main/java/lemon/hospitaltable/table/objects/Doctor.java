package lemon.hospitaltable.table.objects;

import java.sql.Date;

public class Doctor {
    private int id;
    private String name;
    private Date date_of_birth;
    private String position;
    private int department_id;

    public Doctor(int id, String name, Date date_of_birth, String position, int department_id) {
        this.id = id;
        this.name = name;
        this.date_of_birth = date_of_birth;
        this.position = position;
        this.department_id = department_id;
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

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", position='" + position + '\'' +
                ", department_id=" + department_id +
                '}';
    }
}
