package materna.przemek.egzaminel.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import materna.przemek.egzaminel.Database.Exam;
import materna.przemek.egzaminel.Database.Group;
import materna.przemek.egzaminel.Database.Term;

public class AnswerParser {

    public static int loginAnswer(String rawText) {
        if (rawText.equals(AppConfig.EMPTY_ANSWER)) return -1;

        try {
            JSONObject reader = new JSONObject(rawText);
            if (reader.optBoolean(AppConfig.ERROR_CODE, true)) {
                return -1;
            }

            //there is user...
            return reader.getJSONObject(AppConfig.RESULT_CODE).optInt(AppConfig.LOG_IN_ID, -1);

        } catch (JSONException e) {
            //ignored
        }

        return -1;
    }

    public static HashMap<Integer, Group> parseGroups(String rawText) throws JSONException {
        //check if res is empty answer
        if (rawText.equals(AppConfig.EMPTY_ANSWER)) return null;

        JSONObject reader = new JSONObject(rawText);

        //check errors
        if (reader.optBoolean(AppConfig.ERROR_CODE, true)) return null;

        HashMap<Integer, Group> groups = new HashMap<>();
        JSONArray rawGroups = reader.getJSONArray(AppConfig.RESULT_CODE);
        for (int i = 0; i < rawGroups.length(); i++) {
            JSONObject rawGroup = rawGroups.optJSONObject(i);

            //convert date
            String lastUpdateString = rawGroup.optString(AppConfig.EXAM_LAST_UPDATE, AppConfig.DATE_DEFAULT);
            long lastUpdate = convertDateStringToLong(lastUpdateString);

            int id = rawGroup.optInt(AppConfig.ID, -1);
            Group group = new Group(
                    id,
                    rawGroup.optString(AppConfig.GROUP_NAME, ""),
                    rawGroup.optString(AppConfig.GROUP_DESCRIPTION, ""),
                    lastUpdate
            );

            groups.put(id, group);
        }

        return groups;
    }

    public static HashMap<Integer, Exam> parseExams(String rawText) throws JSONException {
        if (rawText.equals(AppConfig.EMPTY_ANSWER)) return null;
        JSONObject reader = new JSONObject(rawText);
        if (reader.optBoolean(AppConfig.ERROR_CODE, true)) return null;

        HashMap<Integer, Exam> exams = new HashMap<>();
        JSONArray rawExams = reader.getJSONArray(AppConfig.RESULT_CODE);
        for (int i = 0; i < rawExams.length(); i++) {
            JSONObject rawExam = rawExams.optJSONObject(i);

            //convert date
            String lastUpdateString = rawExam.optString(AppConfig.EXAM_LAST_UPDATE, AppConfig.DATE_DEFAULT);
            long lastUpdate = convertDateStringToLong(lastUpdateString);

            int id = rawExam.optInt(AppConfig.ID, -1);
            Exam exam = new Exam(
                    id,
                    rawExam.optInt(AppConfig.EXAM_GROUP_ID, -1),
                    rawExam.optString(AppConfig.EXAM_SUBJECT, ""),
                    rawExam.optString(AppConfig.EXAM_TYPE, ""),
                    rawExam.optString(AppConfig.EXAM_TEACHER, ""),
                    rawExam.optString(AppConfig.EXAM_DESCRIPTION, ""),
                    rawExam.optString(AppConfig.EXAM_MATERIALS, ""),
                    lastUpdate
            );
            exams.put(id, exam);
        }
        return exams;
    }

    public static HashMap<Integer, Term> parseTerms(String rawText) throws JSONException {
        if (rawText.equals(AppConfig.EMPTY_ANSWER)) return null;
        JSONObject reader = new JSONObject(rawText);
        if (reader.optBoolean(AppConfig.ERROR_CODE, true)) return null;

        HashMap<Integer, Term> terms = new HashMap<>();
        JSONArray rawTerms = reader.getJSONArray(AppConfig.RESULT_CODE);
        for (int i  = 0; i < rawTerms.length(); i++) {
            JSONObject rawTerm = rawTerms.getJSONObject(i);

            //convert dates
            String dateText = rawTerm.optString(AppConfig.TERM_DATE, AppConfig.DATE_DEFAULT);
            long time = convertDateStringToLong(dateText);
            String lastUpdateString = rawTerm.optString(AppConfig.EXAM_LAST_UPDATE, AppConfig.DATE_DEFAULT);
            long lastUpdate = convertDateStringToLong(lastUpdateString);

            int id = rawTerm.optInt(AppConfig.ID, -1);
            Term term = new Term(
                    rawTerm.optInt(AppConfig.ID, -1),
                    rawTerm.optInt(AppConfig.TERM_EXAM_ID, -1),
                    time,
                    rawTerm.optString(AppConfig.TERM_PLACE, ""),
                    lastUpdate
            );
            terms.put(id, term);
        }
        return terms;
    }

    public static void parseExamsAndTerms(String rawText, HashMap<Integer, Exam> outExams, HashMap<Integer, Term> outTerms) throws JSONException {
        if (rawText.equals(AppConfig.EMPTY_ANSWER)) return;
        JSONObject reader = new JSONObject(rawText);
        if (reader.optBoolean(AppConfig.ERROR_CODE, true)) return;


        JSONArray examsArray = reader.getJSONArray(AppConfig.RESULT_CODE_MUL_EXAMS);
        JSONArray termsArray = reader.getJSONArray(AppConfig.RESULT_CODE_MUL_TERMS);

        JSONObject exams = new JSONObject();
        JSONObject terms = new JSONObject();

        //add "error -> false" to jsonobject
        exams.put(AppConfig.ERROR_CODE, false);
        terms.put(AppConfig.ERROR_CODE, false);

        //and now add an array...
        exams.put(AppConfig.RESULT_CODE, examsArray);
        terms.put(AppConfig.RESULT_CODE, termsArray);

        HashMap<Integer, Exam> examsHashMap = parseExams(exams.toString());
        HashMap<Integer, Term> termsHashMap = parseTerms(terms.toString());


        if (examsHashMap != null) {
            outExams.putAll(examsHashMap);
        }
        if (termsHashMap != null) {
            outTerms.putAll(termsHashMap);
        }

    }

    private static long convertDateStringToLong(String dateText) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(AppConfig.BASADATE_DATETIME_REG);
        long time = 0;
        Date date;
        try {
            date = dateFormat.parse(dateText);
        } catch (ParseException ignored) {
            //do nothing, no expected exceptions here
            date = new Date();
        }
        time = date.getTime();
        return time;
    }
}
