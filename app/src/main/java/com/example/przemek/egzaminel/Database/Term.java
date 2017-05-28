package com.example.przemek.egzaminel.Database;


import java.util.Date;
import java.util.Objects;

public class Term {

    private int id = 0;
    private int exam_id = 0;
    private long date = 0;
    private String place = "";
    private long entryDate;
    private long lastUpdate;


    public Term(int id, int exam_id, long date, String place, long entryDate, long lastUpdate) {
        this.id = id;
        this.exam_id = exam_id;
        this.date = date;
        this.place = place;
        this.entryDate = entryDate;
        this.lastUpdate = lastUpdate;
    }

    public boolean hasTheSameId(Term t) {
        return t.id == this.id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Term)) {
            return false;
        }

        Term t = (Term) object;
        return (this.id == t.id && this.lastUpdate == t.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastUpdate, Group.class.getName());
    }

    @Override
    public String toString() {
        String s = "Term(";
        s+=     id + ", "
                + exam_id+ ", "
                + getFormattedDate() + ", "
                + place + ", "
                + getFormattedEntryDate() + ", "
                + getFormattedLastUpdate();
        s+= ")";
        return s;
    }

    public int getId() {
        return id;
    }

    public int getExam_id() {
        return exam_id;
    }

    public long getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public long getEntryDate() {
        return entryDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public Date getFormattedDate() {
        return new Date(date);
    }

    public Date getFormattedEntryDate() {
        return new Date(entryDate);
    }

    public Date getFormattedLastUpdate() {
        return new Date(lastUpdate);
    }
}
