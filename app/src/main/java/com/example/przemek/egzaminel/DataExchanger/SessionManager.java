package com.example.przemek.egzaminel.DataExchanger;

import com.example.przemek.egzaminel.Database.DatabaseHelper;
import com.example.przemek.egzaminel.Database.Exam;
import com.example.przemek.egzaminel.Database.Group;
import com.example.przemek.egzaminel.Database.Term;

import java.util.HashMap;

public class SessionManager {

    public static final String TAG = SessionManager.class.getSimpleName();

    private static int userId = 0;

    private static HashMap<Integer, Group> groups = new HashMap<>();
    private static HashMap<Integer, Exam> exams = new HashMap<>();
    private static HashMap<Integer, Term> terms = new HashMap<>();
    private static HashMap<Integer, Integer> userTerms = new HashMap<>(); //examId, termId


    public static int getUserId() {
        return userId;
    }

    public static HashMap<Integer, Group> getGroups() {
        return groups;
    }

    public static HashMap<Integer, Exam> getExams() {
        return exams;
    }

    public static HashMap<Integer, Term> getTerms() {
        return terms;
    }

    public static HashMap<Integer, Integer> getUserTerms() {
        return userTerms;
    }


    public static void importDataFromDB(DatabaseHelper helper) {

        groups.clear();
        exams.clear();
        terms.clear();

        groups.putAll(helper.getAllGroups());
        exams.putAll(helper.getAllExams());
        terms.putAll(helper.getAllTerms());
    }

}
