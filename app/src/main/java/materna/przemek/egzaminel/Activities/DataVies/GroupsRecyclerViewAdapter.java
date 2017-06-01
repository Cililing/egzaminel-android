package materna.przemek.egzaminel.Activities.DataVies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import materna.przemek.egzaminel.Database.Group;
import materna.przemek.egzaminel.Interfaces.OnGroupItemClickListener;
import materna.przemek.egzaminel.Interfaces.OnRWItemClickListener;
import materna.przemek.egzaminel.R;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.GroupHolder> {

    private Context context;
    private List<Group> groupList;
    private OnRWItemClickListener<Group> rwItemListener;
    private OnGroupItemClickListener deleteButtonClickListener;

    public GroupsRecyclerViewAdapter(List<Group> groupList, Context context,
                                     OnRWItemClickListener<Group> rwItemListener,
                                     OnGroupItemClickListener deleteButtonClickListener) {
        this.groupList = groupList;
        this.context = context;
        this.rwItemListener = rwItemListener;
        this.deleteButtonClickListener = deleteButtonClickListener;
    }

    @Override
    public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_groups_row, parent, false);

        return new GroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GroupHolder holder, int position) {
        final Group group = groupList.get(position);
        holder.name.setText(group.getName());
        holder.description.setText(group.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rwItemListener.onClick(group, holder.getAdapterPosition());

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return rwItemListener.onLongClick(group, holder.getAdapterPosition());
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButtonClickListener.onClick(group, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


    class GroupHolder extends RecyclerView.ViewHolder {

        TextView name, description;
        ImageView button;

        public GroupHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.groups_list_row_title);
            description = (TextView) itemView.findViewById(R.id.groups_list_row_descr);
            button = (ImageView) itemView.findViewById(R.id.groups_list_row_delete_button);
        }

    }
}
