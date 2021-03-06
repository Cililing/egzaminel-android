package materna.przemek.egzaminel.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    // <editor-fold defaultstate="collapsed" desc="TAGS, NAMES and STATEMENTS">

    //TAGS AND NAMES
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 21;
    private static final String DATABASE_NAME = "UserExams";

    static final String TABlE_GROUPS = "groups";
    static final String TABLE_EXAMS = "exams";
    static final String TABLE_TERMS = "terms";
    static final String TABLE_USERTERMS = "userterms";

    //COLUMN NAMES
    static final String GROUP_ID = "ID";
    static final String GROUP_NAME = "name";
    static final String GROUP_DESCRIPTION = "description";
    static final String GROUP_LAST_UPDATE = "last_update";

    static final String EXAM_ID = "exam_id";
    static final String EXAM_GROUP_ID = "group_id";
    static final String EXAM_SUBJECT = "subject";
    static final String EXAM_TYPE = "type";
    static final String EXAM_TEACHER = "teacher";
    static final String EXAM_DESCRIPTION = "description";
    static final String EXAM_MATERIALS = "materials";
    static final String EXAM_LAST_UPDATE = "last_update";

    static final String TERMS_ID = "terms_id";
    static final String TERMS_EXAM_ID = "terms_exam_id";
    static final String TERMS_DATE = "date";
    static final String TERMS_PLACE = "term_place";
    static final String TERMS_LAST_UPDATE = "last_update";


    //user terms id
    static final String USERTERMS_EXAM_ID = "term_id";
    static final String USERTERMS_TERM_ID = "exam_id";

    //statements
    static final String CREATE_TABLE_GROUPS = "CREATE TABLE "
            + TABlE_GROUPS + "("
            + GROUP_ID + " INTEGER PRIMARY KEY, "
            + GROUP_NAME + " TEXT, "
            + GROUP_DESCRIPTION + " TEXT, "
            + GROUP_LAST_UPDATE + " INTEGER" + ")";

    static final String CREATE_TABLE_EXAMS = "CREATE TABLE "
            + TABLE_EXAMS + "("
            + EXAM_ID + " INTEGER PRIMARY KEY, "
            + EXAM_GROUP_ID + " INTEGER, "
            + EXAM_SUBJECT + " TEXT, "
            + EXAM_TYPE + " TEXT, "
            + EXAM_DESCRIPTION + " TEXT, "
            + EXAM_TEACHER + " TEXT, "
            + EXAM_MATERIALS + " TEXT, "
            + EXAM_LAST_UPDATE + " INTEGER, "
            + " FOREIGN KEY (" + EXAM_GROUP_ID + ") REFERENCES "
            + TABlE_GROUPS + "(" + GROUP_ID + ")"
            + "ON DELETE CASCADE" + ")";

    static final String CREATE_TABLE_TERMS = "CREATE TABLE "
            + TABLE_TERMS + "("
            + TERMS_ID + " INTEGER PRIMARY KEY, "
            + TERMS_EXAM_ID + " INTEGER, "
            + TERMS_DATE + " INTEGER, " //STORING DATE AS NUMBER OF SECOND SINCE 1970-01-01
            + TERMS_PLACE + " TEXT, "
            + TERMS_LAST_UPDATE + " INTEGER, "
            + "FOREIGN KEY (" + TERMS_EXAM_ID + ") REFERENCES "
            + TABLE_EXAMS + "(" + EXAM_ID + ") "
            + "ON DELETE CASCADE" + ")";



    static final String CREATE_TABLE_USERTERMS = "CREATE TABLE "
            + TABLE_USERTERMS + "("
            + USERTERMS_EXAM_ID + " INTEGER, "
            + USERTERMS_TERM_ID + " INTEGER, "
            + "PRIMARY KEY (" + USERTERMS_EXAM_ID + ", " + USERTERMS_TERM_ID + "),"
            + "FOREIGN KEY (" + USERTERMS_EXAM_ID + ") REFERENCES "
            + TABLE_EXAMS + "(" + EXAM_ID + "), "
            + "FOREIGN KEY (" + USERTERMS_TERM_ID + ") REFERENCES "
            + TABLE_TERMS + "(" + TERMS_ID + ") "
            + "ON DELETE CASCADE" + ")";

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DB methods"
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create tables
        db.execSQL(CREATE_TABLE_GROUPS);
        db.execSQL(CREATE_TABLE_EXAMS);
        db.execSQL(CREATE_TABLE_TERMS);
        db.execSQL(CREATE_TABLE_USERTERMS);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABlE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTERMS);
        onCreate(db);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Groups methods">
    public long createGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setGroupContnentValues(group);
        long res = db.insert(TABlE_GROUPS, null, values);
        db.close();
        return res;
    }

    public long[] createGroups(Group... groups) {
        SQLiteDatabase db = this.getWritableDatabase();
        long[] res = new long[groups.length];
        for (int i = 0; i < groups.length; i++) {
            ContentValues values = setGroupContnentValues(groups[i]);
            res[i] = db.insert(TABlE_GROUPS, null, values);
        }
        db.close();
        return res;
    }

    public Group getGroup(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = " SELECT * FROM " + TABlE_GROUPS + "WHERE " + GROUP_ID + " = " + id;

        Log.e(LOG, query);
        Cursor c = db.rawQuery(query, null);

        if (c == null) return null;

        c.moveToFirst();
        Group group = initGroup(c);

        c.close();
        db.close();
        return group;
    }

    public HashMap<Integer, Group> getAllGroups() {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<Integer, Group> groups = new HashMap<>();

        String query = "SELECT * FROM " + TABlE_GROUPS;
        Log.e(LOG, query);

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(GROUP_ID));
                Group g = initGroup(c);
                groups.put(id, g);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return groups;
    }

    public int updateGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setGroupContnentValues(group);
        //update row
        int res = db.update(TABlE_GROUPS, values, GROUP_ID + " = ? ", new String[]{String.valueOf(group.getId())});
        db.close();
        return res;
    }

    public void deleteGroup(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABlE_GROUPS, GROUP_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Exam methods">

    public long createExam(Exam exam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setExamContentValues(exam);
        long res = db.insert(TABLE_EXAMS, null, values);
        db.close();
        return res;
    }

    public long[] createExams(Exam... exams) {
        SQLiteDatabase db = this.getWritableDatabase();
        long[] ids = new long[exams.length];
        for (int i = 0; i < exams.length; i++) {
            ContentValues values = setExamContentValues(exams[i]);
            ids[i] = db.insert(TABLE_EXAMS, null, values);
        }
        db.close();
        return ids;
    }

    public Exam getExam(int examId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String qurey = "SELECT * FROM " + TABLE_EXAMS + " WHERE " + EXAM_ID + " = " + examId;
        Log.e(LOG, qurey);

        Cursor c = db.rawQuery(qurey, null);
        if (c == null) return null;

        c.moveToFirst();
        Exam exam = initExam(c);

        c.close();
        db.close();
        return exam;
    }

    public HashMap<Integer, Exam> getAllExams() {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<Integer, Exam> exams = new HashMap<>();

        String query = "SELECT * FROM " + TABLE_EXAMS;
        Log.e(LOG, query);

        Cursor c = db.rawQuery(query, null);
        if (c == null) return null;

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(EXAM_ID));
                Exam e = initExam(c);
                exams.put(id, e);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return exams;
    }

    public HashMap<Integer, Exam> getExamsByGroupID(int groupId) {

        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<Integer, Exam> exams = new HashMap<>();

        String query = "SELECT * FROM " + TABLE_EXAMS + " WHERE " + EXAM_GROUP_ID + " = " + groupId;
        Log.e(LOG, query);

        Cursor c = db.rawQuery(query, null);
        if (c == null) return null;

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(EXAM_ID));
                Exam e = initExam(c);
                exams.put(id, e);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return exams;
    }

    public int updateExam(Exam exam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setExamContentValues(exam);
        //update row
        int res = db.update(TABLE_EXAMS, values, EXAM_ID + " = ? ", new String[]{String.valueOf(exam.getExamID())});
        db.close();
        return res;
    }

    public void deleteExam(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXAMS, EXAM_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Terms methods">
    public long createTerm(Term term) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setTermContentValues(term);
        long res = db.insert(TABLE_TERMS, null, values);
        db.close();
        return res;
    }

    public long[] createTerms(Term... terms) {
        SQLiteDatabase db = this.getWritableDatabase();
        long[] res = new long[terms.length];
        for (int i = 0; i < terms.length; i++) {
            ContentValues values = setTermContentValues(terms[i]);
            res[i] = db.insert(TABLE_TERMS, null, values);
        }
        db.close();
        return res;
    }

    public Term getTerm(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = " SELECT * FROM " + TABLE_TERMS + "WHERE " + TERMS_ID + " = " + id;

        Log.e(LOG, query);
        Cursor c = db.rawQuery(query, null);

        if (c == null) return null;

        c.moveToFirst();
        Term term = initTerm(c);

        c.close();
        db.close();
        return term;
    }

    public HashMap<Integer, Term> getTermsByExamID(int examID) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<Integer, Term> terms = new HashMap<>();

        String query = "SELECT * FROM " + TABLE_TERMS + " WHERE " + TERMS_EXAM_ID + " = " + examID;
        Log.e(LOG, query);

        Cursor c = db.rawQuery(query, null);
        if (c == null) return null;

        if(c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(TERMS_ID));
                Term term = initTerm(c);
                terms.put(id, term);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return terms;
    }

    public HashMap<Integer, Term> getAllTerms() {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<Integer, Term> terms = new HashMap<>();
        String query = "SELECT * FROM " + TABLE_TERMS;
        Log.e(LOG, query);

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(TERMS_ID));
                Term t = initTerm(c);
                terms.put(id, t);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return terms;
    }

    public int updateTerm(Term term) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setTermContentValues(term);
        //update row
        int res = db.update(TABLE_TERMS, values, TERMS_ID + " = ? ", new String[]{String.valueOf(term.getId())});
        db.close();
        return res;
    }

    public void deleteTerm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TERMS, TERMS_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Userterms methods">

    public long createUserterm(int examId, int termId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERTERMS_EXAM_ID, examId);
        values.put(USERTERMS_TERM_ID, termId);

        long res = db.insert(TABLE_USERTERMS, null, values);
        db.close();
        return res;
    }

    public long[] updateAllUserterms(HashMap<Integer, Integer> newUserTerms) {

        SQLiteDatabase db = this.getWritableDatabase();

        //drop old data
        db.execSQL("DELETE FROM " + TABLE_USERTERMS);

        long[] res = new long[newUserTerms.size()];

        int k = 0;
        for (Map.Entry<Integer, Integer> entry : newUserTerms.entrySet()) {
            ContentValues values = new ContentValues();
            values.put(USERTERMS_EXAM_ID, entry.getKey());
            values.put(USERTERMS_TERM_ID, entry.getValue());
            res[k] = db.insert(TABLE_USERTERMS, null, values);
            k++;
        }

        db.close();
        return res;
    }

    public HashMap<Integer, Integer> getAllUserTerms() {

        SQLiteDatabase db = getReadableDatabase();
        HashMap<Integer, Integer> userTerms = new HashMap<>();

        String query = "SELECT * FROM " + TABLE_USERTERMS;
        Log.e(LOG, query);

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                int examId = c.getInt(c.getColumnIndex(USERTERMS_EXAM_ID));
                int termId = c.getInt(c.getColumnIndex(USERTERMS_TERM_ID));
                userTerms.put(examId, termId);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return userTerms;
    }

    public int updateUserterm(int examId, int termId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        return 0;
    }

    public void deleteUserTerm(int examId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERTERMS, USERTERMS_EXAM_ID + " = ?", new String[]{String.valueOf(examId)});
        db.close();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Init/ContentValues methods">
    private Group initGroup(Cursor c) {
        return new Group(
                c.getInt(c.getColumnIndex(GROUP_ID)),
                c.getString(c.getColumnIndex(GROUP_NAME)),
                c.getString(c.getColumnIndex(GROUP_DESCRIPTION)),
                c.getLong(c.getColumnIndex(GROUP_LAST_UPDATE))
        );
    }

    private Exam initExam(Cursor c) {
        return new Exam(
                c.getInt(c.getColumnIndex(EXAM_ID)),
                c.getInt(c.getColumnIndex(EXAM_GROUP_ID)),
                c.getString(c.getColumnIndex(EXAM_SUBJECT)),
                c.getString(c.getColumnIndex(EXAM_TYPE)),
                c.getString(c.getColumnIndex(EXAM_TEACHER)),
                c.getString(c.getColumnIndex(EXAM_DESCRIPTION)),
                c.getString(c.getColumnIndex(EXAM_MATERIALS)),
                c.getLong(c.getColumnIndex(EXAM_LAST_UPDATE))
        );
    }

    private Term initTerm(Cursor c) {
        return new Term(
                c.getInt(c.getColumnIndex(TERMS_ID)),
                c.getInt(c.getColumnIndex(TERMS_EXAM_ID)),
                c.getLong(c.getColumnIndex(TERMS_DATE)),
                c.getString(c.getColumnIndex(TERMS_PLACE)),
                c.getLong(c.getColumnIndex(TERMS_LAST_UPDATE))
        );
    }

    private ContentValues setGroupContnentValues(Group group) {
        ContentValues values = new ContentValues();
        values.put(GROUP_ID, group.getId());
        values.put(GROUP_NAME, group.getName());
        values.put(GROUP_DESCRIPTION, group.getDescription());
        values.put(GROUP_LAST_UPDATE, group.getLastUpdate());
        return values;
    }

    private ContentValues setExamContentValues(Exam exam) {
        ContentValues values = new ContentValues();
        values.put(EXAM_ID, exam.getExamID());
        values.put(EXAM_GROUP_ID, exam.getGroupID());
        values.put(EXAM_SUBJECT, exam.getSubject());
        values.put(EXAM_TYPE, exam.getType());
        values.put(EXAM_TEACHER, exam.getTeacher());
        values.put(EXAM_DESCRIPTION, exam.getDescription());
        values.put(EXAM_MATERIALS, exam.getMaterialsPath());
        values.put(EXAM_LAST_UPDATE, exam.getLastUpdate());
        return values;
    }

    private ContentValues setTermContentValues(Term term) {
        ContentValues values = new ContentValues();
        values.put(TERMS_ID, term.getId());
        values.put(TERMS_EXAM_ID, term.getExam_id());
        values.put(TERMS_DATE, term.getDate());
        values.put(TERMS_PLACE, term.getPlace());
        values.put(TERMS_LAST_UPDATE, term.getLastUpdate());
        return values;
    }
    // </editor-fold>

}
