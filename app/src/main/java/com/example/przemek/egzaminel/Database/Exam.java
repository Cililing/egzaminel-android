package com.example.przemek.egzaminel.Database;

import com.example.przemek.egzaminel.DataExchanger.SessionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Exam implements Serializable, Cloneable {

    private int examID = 0;
    private int groupID = 0;
    private String subject = "";
    private String type = "";
    private String description = "";
    private String teacher = "";
    private String materialsPath = "";
    private long entryDate = 0;
    private long lastUpdate = 0;

    private int userTermId = -1;
    private String pdfSrc = "";

    public Exam(int examID, int groupID,
                String subject, String type, String teacher, String description, String materialsPath,
                long entryDate, long lastUpdate, int userTermId, String pdfSrc) {
        this.examID = examID;
        this.groupID = groupID;
        this.subject = subject;
        this.type = type;
        this.teacher = teacher;
        this.description = description;
        this.materialsPath = materialsPath;
        this.entryDate = entryDate;
        this.lastUpdate = lastUpdate;
        this.userTermId = userTermId;
        this.pdfSrc = pdfSrc;
    }

    public Exam(Exam exam) {
        this.examID = exam.examID;
        this.groupID = exam.groupID;
        this.subject = exam.subject;
        this.type = exam.type;
        this.teacher = exam.teacher;
        this.description = exam.description;
        this.materialsPath = exam.materialsPath;
        this.entryDate = exam.entryDate;
        this.lastUpdate = exam.lastUpdate;
        this.userTermId = exam.userTermId;
        this.pdfSrc = exam.pdfSrc;
    }

    public boolean hasTheSameId(Exam e) {
        return e.examID == this.examID;
    }

    @Override
    public Exam clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }

        return new Exam(this);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Exam)) {
            return false;
        }

        Exam e = (Exam) object;
        return (this.examID == e.examID && this.lastUpdate == e.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(examID, lastUpdate, Group.class.getName());
    }

    @Override
    public String toString() {
        String s = "Exam(";
        s +=    examID + ", "
                + groupID + ", "
                + subject + ", "
                + type + ", "
                + teacher + ", "
                + description + ", "
                + materialsPath + ", "
                + getFormattedEntryDate() + ", "
                + getFormattedLastUpdate() + ", "
                + userTermId;
        s += ")";
        return s;
    }

    public int getExamID() {
        return examID;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getMaterialsPath() {
        return materialsPath;
    }

    public String getDescription() {
        return description;
    }

    public int getGroupID() {
        return groupID;
    }

    public long getEntryDate() {
        return entryDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public Date getFormattedEntryDate() {
        return new Date(entryDate);
    }

    public Date getFormattedLastUpdate() {
        return new Date(lastUpdate);
    }

    public String getPdfSrc() {
        return pdfSrc;
    }

    public void setPdfSrc(String pdfSrc) {
        this.pdfSrc = pdfSrc;
    }

    public int getUserTermId() {
        return userTermId;
    }

    public void setUserTermId(int userTermId) {
        this.userTermId = userTermId;
    }

    public Term getUserTermOrDefault() {


        HashMap<Integer, Term> termsMap = getTerms();

        if (termsMap.containsKey(userTermId)) {
            return termsMap.get(userTermId);
        }

        List<Term> terms = new ArrayList<>();
        terms.addAll(termsMap.values());
        Collections.sort(terms, new Comparator<Term>() {
            @Override
            public int compare(Term o1, Term o2) {
                return (int) (o1.getDate() - o2.getDate());
            }
        });

        if (terms.size() > 0) return terms.get(0);
        return terms.size() == 0 ? null : terms.get(0);
    }

    public HashMap<Integer, Term> getTerms() {

        HashMap<Integer, Term> savedTerms = SessionManager.getTerms();
        HashMap<Integer, Term> terms = new HashMap<>();
        for (Map.Entry<Integer, Term> entry : savedTerms.entrySet()) {
            if (entry.getValue().getExam_id() == examID) {
                terms.put(entry.getValue().getId(), entry.getValue());
            }
        }

        return terms;

    }
}
