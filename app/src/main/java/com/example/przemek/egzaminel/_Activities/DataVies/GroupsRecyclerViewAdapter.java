package com.example.przemek.egzaminel._Activities.DataVies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.przemek.egzaminel.Database.Group;
import com.example.przemek.egzaminel.Interfaces.OnRWItemClickListener;
import com.example.przemek.egzaminel.R;

import java.util.List;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.GroupHolder> {

    private Context context;
    private List<Group> groupList;
    private OnRWItemClickListener listener;

    public GroupsRecyclerViewAdapter(List<Group> groupList, Context context, OnRWItemClickListener listener) {
        this.groupList = groupList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_groups_fragment_row, parent, false);

        return new GroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GroupHolder holder, int position) {
        Group group = groupList.get(position);
        holder.name.setText(group.getName());
        holder.description.setText(group.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return listener.onLongClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


    class GroupHolder extends RecyclerView.ViewHolder {

        TextView name, description;

        public GroupHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.groups_list_row_title);
            description = (TextView) itemView.findViewById(R.id.groups_list_row_descr);
        }

    }
}
