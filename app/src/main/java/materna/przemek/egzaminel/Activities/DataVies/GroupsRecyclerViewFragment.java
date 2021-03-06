package materna.przemek.egzaminel.Activities.DataVies;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import materna.przemek.egzaminel.Database.Group;
import materna.przemek.egzaminel.Interfaces.OnGroupItemClickListener;
import materna.przemek.egzaminel.Interfaces.OnRWItemClickListener;
import materna.przemek.egzaminel.R;

public class GroupsRecyclerViewFragment extends Fragment{

    @BindView(R.id.groups_recycler_view)
    RecyclerView recyclerView;


    private ArrayList<Group> groups;
    private GroupsRecyclerViewAdapter mAdapter;
    OnRWItemClickListener<Group> rwItemClickListener;
    OnGroupItemClickListener groupItemClickListener;

    public static GroupsRecyclerViewFragment getInstance(ArrayList<Group> groups, OnRWItemClickListener<Group> rwItemClickListener, OnGroupItemClickListener groupItemClickListener) {

        GroupsRecyclerViewFragment fragment = new GroupsRecyclerViewFragment();
        fragment.groups = groups;
        fragment.rwItemClickListener = rwItemClickListener;
        fragment.groupItemClickListener = groupItemClickListener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        ButterKnife.bind(this, view);

        initializeRecyclerView();
        mAdapter.notifyDataSetChanged();

        return view;
    }

    private void initializeRecyclerView() {
        mAdapter = new GroupsRecyclerViewAdapter(groups, getActivity().getApplicationContext(), rwItemClickListener, groupItemClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    void updateDataSet(ArrayList<Group> newDataSet) {
        groups.clear();
        groups.addAll(newDataSet);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
