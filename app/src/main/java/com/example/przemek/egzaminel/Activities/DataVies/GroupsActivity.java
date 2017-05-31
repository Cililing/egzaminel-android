package com.example.przemek.egzaminel.Activities.DataVies;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.przemek.egzaminel.Activities.Static.BundleTags;
import com.example.przemek.egzaminel.Interfaces.OnTaskExecutedListener;
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

    private static boolean addedIsWaiting = false;


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

        if (addedIsWaiting) {
            Tools.makeLongToast(getApplicationContext(), getString(R.string.groups_is_waiting));
            return;
        }

        //set true...
        addedIsWaiting = true;

        int id = -1;
        try {
            id = Integer.parseInt(String.valueOf(inputId.getText()));
        } catch (NumberFormatException ignored) {
            Tools.makeLongToast(getApplicationContext(), getString(R.string.wrong_data_input));
            return;
        } finally {
            addedIsWaiting = false;
        }

        //check if you already have group with that id in your data
        if (SessionManager.getGroups().containsKey(id)) {

            Tools.makeLongToast(getApplicationContext(), getString(R.string.groups_group_already_in_dataset));
            addedIsWaiting = false;
        }

       String password = String.valueOf(inputPassword.getText());
       Synchronizer synchronizer = new Synchronizer(getApplicationContext(), new OnTaskExecutedListener() {
           @Override
           public void onTaskFinished(Object... params) {
               Tools.makeLongToast(getApplicationContext(), getString(R.string.synchronization_finished));
               groupsRWFragment.updateDataSet(new ArrayList<>(SessionManager.getGroups().values()));
               addedIsWaiting = false;
               //Tools.reloadFragment(fragmentManager, groupsRWFragment, null, R.id.groups_main_container, TAG);
           }

           @Override
           public void onTaskFailed(Object... params) {
               Tools.makeLongToast(getApplicationContext(), getString(R.string.wrong_data_input));
               addedIsWaiting = false;
           }
       });

       synchronizer.addGroup(id, password);

    }


    private OnGroupItemClickListener deleteButtonClickListener = new OnGroupItemClickListener() {

        @Override
        public void onClick(final Group group, int position, Object... params) {

            Synchronizer synchronizer = new Synchronizer(getApplicationContext(), null);
            synchronizer.removeGroup(group.getId());

            //update your groups
            groups = SessionManager.getGroups();

            //inform recyclerView groupsRWFragment about change
            groupsRWFragment.updateDataSet(new ArrayList<>(groups.values()));

            /* Yes-No dialog (but it doesnt work :( )
            //remove group
            //show yes no dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());

            //set title
            alertDialog.setTitle(R.string.groups_yes_no_title);
            //set dialog message
            alertDialog.setMessage(R.string.groups_yes_no_message);
            alertDialog.setPositiveButton(R.string.groups_yes_no_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Synchronizer synchronizer = new Synchronizer(getApplicationContext(), null);
                    synchronizer.removeGroup(group.getId());

                    //update your groups
                    groups = SessionManager.getGroups();

                    //inform recyclerView groupsRWFragment about change
                    groupsRWFragment.updateDataSet(new ArrayList<>(groups.values()));
                }
            });

            alertDialog.setNegativeButton(R.string.groups_yes_no_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing
                }
            });

            alertDialog.show();
            */
        }

    };

}
