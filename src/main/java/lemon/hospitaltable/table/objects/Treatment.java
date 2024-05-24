package lemon.hospitaltable.table.objects;

import java.sql.Date;

public class Treatment {
    private int id;
    private int patient_id;
    private int doctor_id;
    private int ward_id;
    private Date date_in;
    private Date date_out;
    private String notation;

    public Treatment(int id, int patient_id, int doctor_id, int ward_id, Date date_in, Date date_out, String notation) {
        this.id = id;
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.ward_id = ward_id;
        this.date_in = date_in;
        this.date_out = date_out;
        this.notation = notation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
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

    public Date getDate_in() {
        return date_in;
    }

    public void setDate_in(Date date_in) {
        this.date_in = date_in;
    }

    public Date getDate_out() {
        return date_out;
    }

    public void setDate_out(Date date_out) {
        this.date_out = date_out;
    }

    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "id=" + id +
                ", patient_id=" + patient_id +
                ", doctor_id=" + doctor_id +
                ", ward_id=" + ward_id +
                ", date_in=" + date_in +
                ", date_out=" + date_out +
                ", notation='" + notation + '\'' +
                '}';
    }
}
