package lemon.hospitaltable.table.objects;

import java.sql.Date;

public class Patient {
    private int id;
    private String name;
    private Date birth;
    private String address;
    private String status;
    private int doctor_id;
    private int ward_id;
    private String notation;

    public Patient(String notation, int ward_id, int doctor_id, String status, String address, Date birth, String name, int id) {
        this.notation = notation;
        this.ward_id = ward_id;
        this.doctor_id = doctor_id;
        this.status = status;
        this.address = address;
        this.birth = birth;
        this.name = name;
        this.id = id;
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

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getWard_id() {
        return ward_id;
    }

    public void setWard_id(int ward_id) {
        this.ward_id = ward_id;
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
                ", doctor_id=" + doctor_id +
                ", ward_id=" + ward_id +
                ", notation='" + notation + '\'' +
                '}';
    }
}
