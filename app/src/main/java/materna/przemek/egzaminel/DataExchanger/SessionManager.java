package materna.przemek.egzaminel.DataExchanger;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import materna.przemek.egzaminel.Database.DatabaseHelper;
import materna.przemek.egzaminel.Database.Exam;
import materna.przemek.egzaminel.Database.Group;
import materna.przemek.egzaminel.Database.Term;

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

    @Nullable
    public static Term getUserTermOrTheFirst(int examId) {
        Term term = terms.get(userTerms.get(examId));

        if (term != null) {
            return term;
        }

        //if not get first term
        ArrayList<Term> terms = new ArrayList<>(getExams().get(examId).getTerms().values());
        Collections.sort(terms);
        return terms.size() > 0 ? terms.get(0) : null;
}

    public static void importDataFromDB(Context context) {

        DatabaseHelper helper = new DatabaseHelper(context);

        groups.clear();
        exams.clear();
        terms.clear();
        userTerms.clear();

        groups.putAll(helper.getAllGroups());
        exams.putAll(helper.getAllExams());
        terms.putAll(helper.getAllTerms());
        userTerms.putAll(helper.getAllUserTerms());
    }

    public static void commitChangesUserterms(Context context) {

        DatabaseHelper helper = new DatabaseHelper(context);
        helper.updateAllUserterms(userTerms);

    }

}
