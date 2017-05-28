package com.example.przemek.egzaminel._Activities.Exam;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.przemek.egzaminel._Activities.BundleTags;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Database.Exam;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExamActivity extends AppCompatActivity {


    @BindView(R.id.exam_subject)
    TextView examSubject;
    @BindView(R.id.exam_teacher)
    TextView examTecher;
    @BindView(R.id.exam_topic)
    TextView examTopic;
    @BindView(R.id.exam_date_spinner)
    Spinner dateSpinner;
    @BindView(R.id.exam_description)
    TextView examDescription;

    private Exam exam;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        fragmentManager = getFragmentManager();
        ButterKnife.bind(this);

        exam = (Exam) getIntent().getSerializableExtra(BundleTags.EXAM_TAG);
        initExamValues();
    }


    private void initExamValues() {
        examSubject.setText(exam.getSubject());
        examTecher.setText(exam.getTeacher());
        examTopic.setText(exam.getType());
        examDescription.setMovementMethod(new ScrollingMovementMethod());
        examDescription.setText(exam.getDescription());
    }

}
