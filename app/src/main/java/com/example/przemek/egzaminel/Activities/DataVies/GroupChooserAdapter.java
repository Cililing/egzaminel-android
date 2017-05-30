package com.example.przemek.egzaminel.Activities.DataVies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.przemek.egzaminel.Database.Group;
import com.example.przemek.egzaminel.Interfaces.OnSwitchChangedListener;
import com.example.przemek.egzaminel.R;

import java.util.HashMap;
import java.util.List;

public class GroupChooserAdapter extends RecyclerView.Adapter<GroupChooserAdapter.GroupSwitchHolder> {


    private List<Group> groupList;
    OnSwitchChangedListener listener;
    HashMap<Integer, Boolean> activeGroups;

    public GroupChooserAdapter(List<Group> groupList, OnSwitchChangedListener listener, HashMap<Integer, Boolean> activeGroups) {

        this.groupList = groupList;
        this.listener = listener;
        this.activeGroups = activeGroups;
    }

    @Override
    public GroupSwitchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_group_chooser_row, parent, false);

        return new GroupSwitchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GroupSwitchHolder holder, int position) {

        final Group group = groupList.get(position);
        holder.groupSwitch.setText(group.getName());

        //set active or uncative
        holder.groupSwitch.setChecked(activeGroups.get(group.getId()));

        holder.groupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //buttonView.setChecked(isChecked);
                if (isChecked) listener.OnCheckedOn(group.getId());
                if (!isChecked) listener.onCheckedOff(group.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class GroupSwitchHolder extends RecyclerView.ViewHolder {

        Switch groupSwitch;

        public GroupSwitchHolder(View itemView) {
            super(itemView);
            groupSwitch = (Switch) itemView.findViewById(R.id.group_chooser);
        }
    }
}
