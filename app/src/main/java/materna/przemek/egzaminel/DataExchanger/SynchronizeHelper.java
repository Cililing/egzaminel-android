package materna.przemek.egzaminel.DataExchanger;

import java.util.HashMap;
import java.util.HashSet;

import materna.przemek.egzaminel.Database.DatabaseHelper;
import materna.przemek.egzaminel.Database.Exam;
import materna.przemek.egzaminel.Database.Group;
import materna.przemek.egzaminel.Database.Term;


public class SynchronizeHelper {

    private static final String TAG = SynchronizeHelper.class.getSimpleName();
    private DatabaseHelper dbHelper;

    public SynchronizeHelper(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Deprecated
    public int synchronizeGroups(HashMap<Integer, Group> newGroups) {

        int counter = 0;
        HashMap<Integer, Group> localGroups = dbHelper.getAllGroups();
        HashSet<Integer> ids = new HashSet<>();
        if (newGroups == null) newGroups = new HashMap<>();

        ids.addAll(localGroups.keySet());
        ids.addAll(newGroups.keySet());

        for (Integer id : ids) {
            Group local = localGroups.get(id);
            Group online = newGroups.get(id);

            if (local != null && online != null) {
                if (!local.equals(online)) {
                    dbHelper.updateGroup(online);
                    counter++; //update newer version of group
                }
            } else if (local == null && online != null) {
                dbHelper.createGroup(online);
                counter++;
            } else if (local != null && online == null) {
                dbHelper.deleteGroup(id);
                counter++;
            }

        }

        return counter;
    }

    public int synchronizeExams(HashMap<Integer, Exam> newExams) {

        int counter = 0;
        HashMap<Integer, Exam> localExams = dbHelper.getAllExams();
        HashSet<Integer> ids = new HashSet<>();
        if (newExams == null) newExams = new HashMap<>();

        ids.addAll(localExams.keySet());
        ids.addAll(newExams.keySet());


        for (Integer id : ids) {
            Exam local = localExams.get(id);
            Exam online = newExams.get(id);

            if (local != null && online != null) { //both exists
                if (!local.equals(online)) {    //exams has changed
                    dbHelper.updateExam(online);
                    counter++; //update newer version of exam
                }
            } else if (local == null && online != null) { //new exam...
                dbHelper.createExam(online);
                counter++;
            } else { //local != null and online == null //only local
                dbHelper.deleteExam(id);
                counter++;
            }

        }

        return counter;
    }

    public int synchronizeTerms(HashMap<Integer, Term> newTerms) {

        int counter = 0;
        HashMap<Integer, Term> localTerms = dbHelper.getAllTerms();
        HashSet<Integer> ids = new HashSet<>();
        if (newTerms == null) newTerms = new HashMap<>();

        ids.addAll(newTerms.keySet());
        ids.addAll(localTerms.keySet());


        for (Integer id : ids) {
            Term local = localTerms.get(id);
            Term online = newTerms.get(id);

            if (local != null && online != null) { //both exists
                if (!local.equals(online)) {    //exams has changed
                    dbHelper.updateTerm(online);
                    counter++; //update newer version of term
                }
            } else if (local == null && online != null) { //new exam...
                dbHelper.createTerm(online);
                counter++;
            } else { //local != null and online == null //only local
                dbHelper.deleteTerm(id);
                counter++;
            }

        }

        return counter;
    }

}
