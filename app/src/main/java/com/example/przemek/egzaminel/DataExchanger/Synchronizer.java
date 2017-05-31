package com.example.przemek.egzaminel.DataExchanger;

import android.content.Context;

import com.example.przemek.egzaminel.Database.DatabaseHelper;
import com.example.przemek.egzaminel.Database.Exam;
import com.example.przemek.egzaminel.Database.Group;
import com.example.przemek.egzaminel.Database.Term;
import com.example.przemek.egzaminel.Interfaces.OnResponseListener;
import com.example.przemek.egzaminel.Interfaces.OnTaskExecutedListener;
import com.example.przemek.egzaminel.Network.AnswerParser;
import com.example.przemek.egzaminel.Network.AppConfig;
import com.example.przemek.egzaminel.Network.AppNetworkHandler;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Tools.Tools;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class Synchronizer {


    public static final String TAG = Synchronizer.class.getSimpleName();

    private Context context;
    DatabaseHelper databaseHelper;
    SynchronizeHelper synchronizeHelper;
    OnTaskExecutedListener onTaskExecutedListenerListener;

    public Synchronizer(Context context, OnTaskExecutedListener onTaskExecutedListenerListener) {
        databaseHelper = new DatabaseHelper(context);
        this.onTaskExecutedListenerListener = onTaskExecutedListenerListener;
        synchronizeHelper = new SynchronizeHelper(databaseHelper);
        this.context = context;
    }

    public synchronized boolean removeGroup(int id) {

        if (SessionManager.getGroups().containsKey(id)) {
            //remove from memory
            //SessionManager.getGroups().remove(id);
            //remove from internal database
            databaseHelper.deleteGroup(id);
            //all data connected with "id" will be removed.
            SessionManager.importDataFromDB(context);
            return true;
        }
        else return false;
    }


    public void addGroup(int id, String password) {

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConfig.GROUP_ID, String.valueOf(id));
        params.put(AppConfig.GROUP_PASSWORD, password);

        AppNetworkHandler.getStringAnswer(TAG, AppConfig.GROUP_BY_GROUP_ID_AND_PASSWORD, params, new OnResponseListener() {
            @Override
            public void onResponse(String tag, Object... params) {

                try {

                    HashMap<Integer, Group> groupHashMap = AnswerParser.parseGroups((String) params[0]);

                    //you have groups, now add it to SessionManager
                    if (groupHashMap != null) {
                        SessionManager.getGroups().putAll(groupHashMap);
                        downloadTermsAndExamData(SessionManager.getGroups());
                    }

                    //groupHashMap is null, so parsing wasnt successful
                    //that means user put wrong data
                    if (groupHashMap == null) {
                        onTaskExecutedListenerListener.onTaskFailed();
                    }


                } catch (JSONException e) {
                    Tools.makeLongToast(context, context.getString(R.string.database_error));
                    onTaskExecutedListenerListener.onTaskFailed();
                }
            }

            @Override
            public void onResponseError(String tag, Object... params) {
                Tools.makeLongToast(context, context.getString(R.string.network_error));
                onTaskExecutedListenerListener.onTaskFailed();
            }
        });
    }


    private void downloadTermsAndExamData(final HashMap<Integer, Group> groups) {

        HashMap<String, String> params = new HashMap<>();
        JSONArray jsonArray = new JSONArray(groups.keySet());
        //int[] groupsIds = Ints.toArray(groups.keySet());

        //params.put(AppConfig.GROUPS_IDS, Arrays.toString(groupsIds));
        params.put(AppConfig.GROUPS_IDS, jsonArray.toString());

        AppNetworkHandler.getStringAnswer(TAG, AppConfig.EXAMS_AND_TERMS_BY_GROUPS_IDS, params, new OnResponseListener() {
            @Override
            public void onResponse(String tag, Object... params) {

                String response = (String) params[0];
                HashMap<Integer, Exam> exams = new HashMap<>();
                HashMap<Integer, Term> terms = new HashMap<>();

                //download data
                try {

                    AnswerParser.parseExamsAndTerms(response, exams, terms);

                    //synchronize groups and exams
                    synchronizeHelper.synchronizeGroups(groups);
                    synchronizeHelper.synchronizeExams(exams);
                    synchronizeHelper.synchronizeTerms(terms);

                    //add those data into session manager
                    SessionManager.importDataFromDB(context);


                    //tell them you finished your job
                    if (onTaskExecutedListenerListener != null) onTaskExecutedListenerListener.onTaskFinished();


                } catch (JSONException e) {
                    Tools.makeLongToast(context, context.getString(R.string.database_error));
                    onTaskExecutedListenerListener.onTaskFailed();
                }

            }

            @Override
            public void onResponseError(String tag, Object... params) {
                Tools.makeLongToast(context, context.getString(R.string.network_error));
                onTaskExecutedListenerListener.onTaskFailed();
            }
        });

    }
}
