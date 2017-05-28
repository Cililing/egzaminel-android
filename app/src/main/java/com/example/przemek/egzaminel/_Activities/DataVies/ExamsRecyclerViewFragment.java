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
import com.example.przemek.egzaminel._Activities.Exam.ExamActivity;
import com.example.przemek.egzaminel.Database.Exam;
import com.example.przemek.egzaminel.Interfaces.OnRWItemClickListener;
import com.example.przemek.egzaminel.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExamsRecyclerViewFragment extends Fragment {

    @BindView(R.id.exams_recycler_view)
    RecyclerView recyclerView;


    private List<Exam> exams;
    private ExamsRecyclerViewAdapter mAdapter;

    public static ExamsRecyclerViewFragment getInstance(List<Exam> exams) {

        ExamsRecyclerViewFragment fragment = new ExamsRecyclerViewFragment();
        fragment.exams = exams;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_exams_fragment, container, false);
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

    OnRWItemClickListener itemListener = new OnRWItemClickListener() {
        @Override
        public void onClick(int pos) {
            Exam exam = exams.get(pos);
            Intent i = new Intent(getActivity().getApplicationContext(), ExamActivity.class);
            i.putExtra(BundleTags.EXAM_TAG, exam);
            startActivity(i);
        }

        @Override
        public boolean onLongClick(int pos) {
            return false;
        }
    };

}
