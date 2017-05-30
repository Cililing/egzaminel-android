package com.example.przemek.egzaminel.Activities.DataVies;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.przemek.egzaminel.Activities.Static.BundleTags;
import com.example.przemek.egzaminel.Interfaces.OnTaskExecuted;
import com.example.przemek.egzaminel.DataExchanger.Synchronizer;
import com.example.przemek.egzaminel.DataExchanger.SessionManager;
import com.example.przemek.egzaminel.Database.Group;
import com.example.przemek.egzaminel.Interfaces.OnGroupItemClickListener;
import com.example.przemek.egzaminel.Interfaces.OnRWItemClickListener;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupsActivity extends AppCompatActivity {

    private static final String TAG = GroupsActivity.class.getSimpleName();

    HashMap<Integer, Group> groups;
    FragmentManager fragmentManager;
    GroupsRecyclerViewFragment groupsRWFragment;

    @BindView(R.id.groups_input_button_add)
    ImageView addButton;
    @BindView(R.id.groups_input_group_id)
    TextView inputId;
    @BindView(R.id.groups_input_group_password)
    TextView inputPassword;


    private OnRWItemClickListener<Group> rwItemClickListener = new OnRWItemClickListener<Group>() {
        @Override
        public void onClick(Group item, int pos) {
            //set only that group active and then open activity "ExamsActivity"
            HashMap<Integer, Boolean> active = new HashMap<>();
            active.put(item.getId(), true);

            Intent i = new Intent(getApplicationContext(), ExamsActivity.class);
            i.putExtra(BundleTags.ACTIVE_GROUPS_TAG, active);
            startActivity(i);
        }

        @Override
        public boolean onLongClick(Group item, int pos) {
            //do nothing
            return false;
        }
    };


    private OnGroupItemClickListener deleteButtonClickListener = new OnGroupItemClickListener() {

        @Override
        public void onClick(Group group, int position, Object... params) {
            //remove group
            Synchronizer synchronizer = new Synchronizer(getApplicationContext(), null);
            synchronizer.removeGroup(group.getId());

            //update your groups
            groups = SessionManager.getGroups();

            //inform recyclerView groupsRWFragment about change
            groupsRWFragment.updateDataSet(new ArrayList<>(groups.values()));
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        ButterKnife.bind(this);

        fragmentManager = getFragmentManager();
        groups = SessionManager.getGroups();

        initFragment();

    }

    private void initFragment() {
        groupsRWFragment = GroupsRecyclerViewFragment.getInstance(new ArrayList<>(groups.values()), rwItemClickListener, deleteButtonClickListener);
        Tools.reloadFragment(fragmentManager, groupsRWFragment, null, R.id.groups_main_container, TAG);
    }

    @OnClick(R.id.groups_input_group_id)
    public void onTextLabelClick() {
        inputId.setText("");
    }

    @OnClick(R.id.groups_input_button_add)
    public void onAddButton() {

        int id = -1;
        try {
            id = Integer.parseInt(String.valueOf(inputId.getText()));
        } catch (NumberFormatException ignored) {
            Tools.makeLongToast(getApplicationContext(), getString(R.string.wrong_data_input));
            return;
        }

       String password = String.valueOf(inputPassword.getText());

       Synchronizer synchronizer = new Synchronizer(getApplicationContext(), new OnTaskExecuted() {
           @Override
           public void onTaskFinished(Object... params) {
               Tools.makeLongToast(getApplicationContext(), getString(R.string.synchronization_finished));
               groupsRWFragment.updateDataSet(new ArrayList<>(SessionManager.getGroups().values()));

               //Tools.reloadFragment(fragmentManager, groupsRWFragment, null, R.id.groups_main_container, TAG);
           }

           @Override
           public void onTaskFailed(Object... params) {
               Tools.makeLongToast(getApplicationContext(), getString(R.string.wrong_data_input));
           }
       });

       synchronizer.addGroup(id, password);

    }

}
