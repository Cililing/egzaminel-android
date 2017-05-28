package com.example.przemek.egzaminel._Activities.DataVies;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.przemek.egzaminel._Activities.BundleTags;
import com.example.przemek.egzaminel.Database.DatabaseHelper;
import com.example.przemek.egzaminel.Database.Exam;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class ExamsActivity extends AppCompatActivity {

    private static final String TAG = GroupsActivity.class.getSimpleName();

    HashMap<Integer, Exam> exams = new HashMap<>();
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

        ButterKnife.bind(this);

        fragmentManager = getFragmentManager();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        @SuppressWarnings("unchecked")
        ArrayList<Integer> groupsIds = (ArrayList<Integer>) getIntent().getSerializableExtra(BundleTags.GROUPS_ID_TAG);
        if (groupsIds == null || groupsIds.size() <= 0) {
            groupsIds = new ArrayList<>();
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            groupsIds.addAll(databaseHelper.getAllGroups().keySet());
        }


        if (groupsIds.size() > 0) {
            for (Integer i : groupsIds) {
                exams.putAll(dbHelper.getExamsByGroupID(i));
            }
        }

        tryLoadFragment();

    }

    private void tryLoadFragment() {

        if (exams.size() > 0) {
            List<Exam> list = new ArrayList<>();
            list.addAll(exams.values());
            ExamsRecyclerViewFragment fragment = ExamsRecyclerViewFragment.getInstance(list);
            Tools.reloadFragment(fragmentManager, fragment, null, R.id.exams_main_container, TAG);
        }
    }
}
