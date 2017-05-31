package com.example.przemek.egzaminel.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.przemek.egzaminel.DataExchanger.SessionManager;
import com.example.przemek.egzaminel.Database.DatabaseHelper;
import com.example.przemek.egzaminel.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_user_input_username)
    EditText inputUsername;
    @BindView(R.id.main_user_input_password)
    EditText inputPassword;
    @BindView(R.id.main_login_button)
    Button signInButton;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dontLogIn();
    }

    public void dontLogIn() {

        //load data to SessionManager
        SessionManager.importDataFromDB(getApplicationContext());

        //start new activity
        Intent i = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(i);
    }

//    @OnClick(R.id.main_login_button)
//    public void onSignInButtonClick() {
//        HashMap<String, String> params = new HashMap<>();
//        params.put(AppConfig.LOG_IN_USERNAME, String.valueOf(inputUsername.getText()));
//        params.put(AppConfig.LOG_IN_PASSWORD, String.valueOf(inputPassword.getText()));
//
//        AppNetworkHandler.getStringAnswer(
//                TAG, AppConfig.LOG_IN_BY_USERNAME_AND_PASSWORD, params, new OnResponseListener() {
//                    @Override
//                    public void onResponse(String tag, Object... params) {
//                        String respone = (String) params[0];
//                        id = AnswerParser.loginAnswer(respone);
//
//                        Tools.makeLongToast(getApplicationContext(), String.valueOf(id));
//                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
//                        SynchronizeHelper synchronizeHelper = new SynchronizeHelper(databaseHelper);
//                        DataImporter di = new DataImporter(
//                                getApplicationContext(),
//                                id,
//                                databaseHelper,
//                                synchronizeHelper);
//                        di.importData();
//
//                        //store data in session manager
//                        SessionManager.getGroups().putAll(databaseHelper.getAllGroups());
//                        SessionManager.getExams().putAll(databaseHelper.getAllExams());
//                        SessionManager.getTerms().putAll(databaseHelper.getAllTerms());
//
//
//                        Intent i = new Intent(getApplicationContext(), MainMenu.class);
//                        startActivity(i);
//
//                    }
//
//                    @Override
//                    public void onResponseError(String tag, Object... params) {
//                        //TODO hardcoded string (temporary xD)
//                        Tools.makeLongToast(getApplicationContext(), "Network or db error");
//                    }
//                }
//        );
//
//    }


}
