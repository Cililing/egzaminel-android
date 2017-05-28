package com.example.przemek.egzaminel._Activities.DataVies;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.przemek.egzaminel.Database.DatabaseHelper;
import com.example.przemek.egzaminel.Database.Group;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Tools.Tools;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class GroupsActivity extends AppCompatActivity {

    private static final String TAG = GroupsActivity.class.getSimpleName();

    ArrayList<Group> groups;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        ButterKnife.bind(this);

        fragmentManager = getFragmentManager();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        groups = new ArrayList<>(dbHelper.getAllGroups().size());
        groups.addAll(dbHelper.getAllGroups().values());

        if (groups.size() > 0) {
            GroupsRecyclerViewFragment fragment = GroupsRecyclerViewFragment.getInstance(groups);
            Tools.reloadFragment(fragmentManager, fragment, null, R.id.groups_main_container, TAG);
        }


    }


}
