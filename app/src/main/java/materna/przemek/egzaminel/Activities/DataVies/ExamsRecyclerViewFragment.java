package materna.przemek.egzaminel.Activities.DataVies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import materna.przemek.egzaminel.Activities.Exam.ExamActivity;
import materna.przemek.egzaminel.Activities.Static.BundleTags;
import materna.przemek.egzaminel.Database.Exam;
import materna.przemek.egzaminel.Interfaces.OnRWItemClickListener;
import materna.przemek.egzaminel.R;


public class ExamsRecyclerViewFragment extends Fragment {

    @BindView(R.id.exams_recycler_view)
    RecyclerView recyclerView;


    private List<Exam> exams;
    private ExamsRecyclerViewAdapter mAdapter;

    public static ExamsRecyclerViewFragment getInstance(List<Exam> exams, Comparator<Exam> comparator) {

        ExamsRecyclerViewFragment fragment = new ExamsRecyclerViewFragment();
        fragment.exams = exams;
        if (comparator != null) {
            Collections.sort(exams, comparator);
        }
        return fragment;
    }

    public void updateExamList(List<Exam> examsList) {
        exams.clear();
        exams.addAll(examsList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exams, container, false);
        ButterKnife.bind(this, view);

        initializeRecyclerView();
        mAdapter.notifyDataSetChanged();

        return view;
    }

    private void initializeRecyclerView() {
        mAdapter = new ExamsRecyclerViewAdapter(exams, getActivity().getApplicationContext(), itemListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    OnRWItemClickListener<Exam> itemListener = new OnRWItemClickListener<Exam>() {
        @Override
        public void onClick(Exam item, int pos) {
            Intent i = new Intent(getActivity().getApplicationContext(), ExamActivity.class);
            i.putExtra(BundleTags.EXAM_TAG, item);
            startActivity(i);
        }

        @Override
        public boolean onLongClick(Exam item, int pos) {
            return false;
        }
    };

}
