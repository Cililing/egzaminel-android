package com.example.przemek.egzaminel.Activities.DataVies;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.przemek.egzaminel.Activities.Static.BundleTags;
import com.example.przemek.egzaminel.DataExchanger.SessionManager;
import com.example.przemek.egzaminel.Database.Exam;
import com.example.przemek.egzaminel.Interfaces.OnSwitchChangedListener;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Tools.Tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExamsActivity extends AppCompatActivity {

    private static final String TAG = GroupsActivity.class.getSimpleName();

    HashMap<Integer, Exam> examsMap; //all exams
    HashMap<Integer, Boolean> activeGroupsIds;

    @BindView(R.id.exams_groups_chooser)
    ImageView groupsChooser;
    @BindView(R.id.exams_groups_list)
    View groupListContainer;

    //fragments
    FragmentManager fragmentManager;
    ExamsRecyclerViewFragment examsRecyclerViewFragment;
    GroupChooserFragment groupChooserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();
        examsMap = SessionManager.getExams();



        activeGroupsIds = (HashMap<Integer, Boolean>) getIntent().getSerializableExtra(BundleTags.ACTIVE_GROUPS_TAG);
        Set<Integer> groupsSet = SessionManager.getGroups().keySet();

        if (activeGroupsIds == null) {
            //add all ids as true
            activeGroupsIds = new HashMap<>();
            for (Integer id : groupsSet) {
                activeGroupsIds.put(id, true);
            }
        } else {
            //if id isnt in set add it with flag flase
            for (Integer id : groupsSet) {
                if (!activeGroupsIds.containsKey(id)) {
                    activeGroupsIds.put(id, false);
                }
            }
        }

        tryLoadFragment();
    }

    private void tryLoadFragment() {

//        //fragment1
//        List<Exam> list = new ArrayList<>();
//        list.addAll(examsMap.values());

        examsRecyclerViewFragment = ExamsRecyclerViewFragment.getInstance(getActiveExams(), new Comparator<Exam>() {
            @Override
            public int compare(Exam o1, Exam o2) {
                return (int)
                        (o1.getUserTermOrDefault().getDate()
                                - o2.getUserTermOrDefault().getDate());
            }
        });
        Tools.reloadFragment(fragmentManager, examsRecyclerViewFragment, null, R.id.exams_main_container, TAG);

        //fragemtn2
        groupChooserFragment = GroupChooserFragment.getInstance(listener, activeGroupsIds);
        Tools.reloadFragment(fragmentManager, groupChooserFragment, null, R.id.exams_groups_list, TAG);
        groupListContainer.setVisibility(View.GONE);

    }

    OnSwitchChangedListener listener = new OnSwitchChangedListener() {
        @Override
        public void OnCheckedOn(Object... params) {
            int id = (int) params[0];

            if (activeGroupsIds.containsKey(id)) {
                //set group.active to true
                activeGroupsIds.put(id, true);
                //Tools.makeLongToast(getApplicationContext(), String.valueOf(id) + " wlaczone");
            }
            updateDataSet();
        }

        @Override
        public void onCheckedOff(Object... params) {
            int id = (int) params[0];

            if (activeGroupsIds.containsKey(id)) {
                //set group.active to true
                activeGroupsIds.put(id, false);
                //Tools.makeLongToast(getApplicationContext(), String.valueOf(id));
            }
            updateDataSet();
        }
    };

    private void updateDataSet() {

        ArrayList<Exam> newDataSet = getActiveExams();
        examsRecyclerViewFragment.updateExamList(newDataSet);

    }

    private ArrayList<Exam> getActiveExams() {
        ArrayList<Exam> dataSet = new ArrayList<>();
        for (Exam exam : examsMap.values()) {

            if (activeGroupsIds.get(exam.getGroupID())) {
                dataSet.add(exam);
            }
        }
        return dataSet;
    }


    @OnClick(R.id.exams_groups_chooser)
    public void onExamChooserClick() {

        switch (groupListContainer.getVisibility()) {
            case View.GONE: {
                groupListContainer.setVisibility(View.VISIBLE);
                break;
            }
            case View.VISIBLE: {
                groupListContainer.setVisibility(View.GONE);
                break;
            }
            case View.INVISIBLE:
                //cant be
                break;
        }

    }
}
