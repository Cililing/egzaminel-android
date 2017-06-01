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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import materna.przemek.egzaminel.DataExchanger.SessionManager;
import materna.przemek.egzaminel.Database.Group;
import materna.przemek.egzaminel.Interfaces.OnSwitchChangedListener;
import materna.przemek.egzaminel.R;

public class GroupChooserFragment extends Fragment {


    @BindView(R.id.group_chooser_recycler_view)
    RecyclerView recyclerView;
    GroupChooserAdapter mAdapter;


    List<Group> groups;
    OnSwitchChangedListener listener;
    HashMap<Integer, Boolean> activeGroups;

    public static GroupChooserFragment getInstance(OnSwitchChangedListener listener, HashMap<Integer, Boolean> activeGroups) {
        GroupChooserFragment fragment = new GroupChooserFragment();
        fragment.listener = listener;
        fragment.activeGroups = activeGroups;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chooser, container, false);
        ButterKnife.bind(this, view);

        groups = new ArrayList<>();
        groups.addAll(SessionManager.getGroups().values());
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {

        mAdapter = new GroupChooserAdapter(groups, listener, activeGroups);
        recyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

}
