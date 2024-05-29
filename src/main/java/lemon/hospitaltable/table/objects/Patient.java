package lemon.hospitaltable.table.objects;

import java.sql.Date;

public class Patient {
    private int id;
    private String name;
    private Date birth;
    private String address;
    private String status;
    private String notation;

    public Patient(int id, String name, Date birth, String address, String status, String notation) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.address = address;
        this.status = status;
        this.notation = notation;
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

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", notation='" + notation + '\'' +
                '}';
    }
}
