package com.example.przemek.egzaminel._Activities.DataVies;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.przemek.egzaminel._Activities.BundleTags;
import com.example.przemek.egzaminel.Database.Group;
import com.example.przemek.egzaminel.Interfaces.OnRWItemClickListener;
import com.example.przemek.egzaminel.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupsRecyclerViewFragment extends Fragment{

    @BindView(R.id.groups_recycler_view)
    RecyclerView recyclerView;


    private ArrayList<Group> groups;
    private GroupsRecyclerViewAdapter mAdapter;

    public static GroupsRecyclerViewFragment getInstance(ArrayList<Group> groups) {

        GroupsRecyclerViewFragment fragment = new GroupsRecyclerViewFragment();
        fragment.groups = groups;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_groups_fragment, container, false);
        ButterKnife.bind(this, view);

        initializeRecyclerView();
        mAdapter.notifyDataSetChanged();

        return view;
    }

    private void initializeRecyclerView() {
        mAdapter = new GroupsRecyclerViewAdapter(groups, getActivity().getApplicationContext(), rwItemClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    private OnRWItemClickListener rwItemClickListener = new OnRWItemClickListener() {
        @Override
        public void onClick(int pos) {
            Intent i = new Intent(getActivity().getApplicationContext(), ExamsActivity.class);
            int id = groups.get(pos).getId();
            i.putExtra(BundleTags.GROUPS_ID_TAG, id);
            startActivity(i);
        }

        @Override
        public boolean onLongClick(int pos) {
            return false;
        }
    };
}
