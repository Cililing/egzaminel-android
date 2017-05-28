package com.example.przemek.egzaminel.Tools;


import android.content.Context;

import com.example.przemek.egzaminel.Database.DatabaseHelper;
import com.example.przemek.egzaminel.Database.Exam;
import com.example.przemek.egzaminel.Database.Group;
import com.example.przemek.egzaminel.Database.SynchronizeHelper;
import com.example.przemek.egzaminel.Database.Term;
import com.example.przemek.egzaminel.Interfaces.OnResponseListener;
import com.example.przemek.egzaminel.Network.AnswerParser;
import com.example.przemek.egzaminel.Network.AppConfig;
import com.example.przemek.egzaminel.Network.AppNetworkHandler;

import org.json.JSONException;

import java.util.HashMap;

public class DataImporter {

    private static final String TAG = DataImporter.class.getSimpleName();
    Context context;
    int userId;

    private int[] isDownloading = new int[3];

    DatabaseHelper dbHelper;
    SynchronizeHelper synchronizeHelper;

    public DataImporter(Context context, int userId, DatabaseHelper dbHelper, SynchronizeHelper synchronizeHelper) {

        this.context = context;
        this.userId = userId;
        this.dbHelper = dbHelper;
        this.synchronizeHelper = synchronizeHelper;
        importData();
    }

    public void importData() {

        isDownloading = new int[]{1, 1, 1};
        importGroups(0);
        importExams(1);
        importTerms(2);

    }

    private void importGroups(final int code) {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConfig.USER_ID, String.valueOf(userId));
        AppNetworkHandler.getStringAnswer(TAG, AppConfig.GROUPS_BY_USER_ID, params, new OnResponseListener() {
            @Override
            public void onResponse(String tag, Object... params) {

                try {
                    HashMap<Integer, Group> groups = AnswerParser.parseGroups((String) params[0]);
                    synchronizeHelper.synchronizeGroups(groups);
                    isDownloading[code] = 0;
                } catch (JSONException e) {
                    Tools.makeLongToast(context, "Error");
                    isDownloading[code] = -2;
                }

            }

            @Override
            public void onResponseError(String tag, Object... params) {
                isDownloading[code] = -1;
            }
        });
    }


    private void importExams(final int code) {

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConfig.USER_ID, String.valueOf(userId));

        AppNetworkHandler.getStringAnswer(TAG, AppConfig.EXAMS_BY_USER_ID, params, new OnResponseListener() {
            @Override
            public void onResponse(String tag, Object... params) {

                try {
                    HashMap<Integer, Exam> exams = AnswerParser.parseExams((String) params[0]);
                    synchronizeHelper.synchronizeExams(exams);
                    isDownloading[code] = 0;
                } catch (JSONException e) {
                    Tools.makeLongToast(context, "Error");
                    isDownloading[code] = -2;
                }

            }

            @Override
            public void onResponseError(String tag, Object... params) {
                isDownloading[code] = -1;
            }
        });
    }

    private void importTerms(final int code) {

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConfig.USER_ID, String.valueOf(userId));

        AppNetworkHandler.getStringAnswer(TAG, AppConfig.TERMS_BY_USER_ID, params, new OnResponseListener() {
            @Override
            public void onResponse(String tag, Object... params) {

                try {
                    HashMap<Integer, Term> terms = AnswerParser.parseTerms((String) params[0]);
                    synchronizeHelper.synchronizeTerms(terms);
                    isDownloading[code] = 0;
                } catch (JSONException e) {
                    Tools.makeLongToast(context, "Error");
                    isDownloading[code] = -2;
                }

            }

            @Override
            public void onResponseError(String tag, Object... params) {
                isDownloading[code] = -1;
            }
        });
    }

}
