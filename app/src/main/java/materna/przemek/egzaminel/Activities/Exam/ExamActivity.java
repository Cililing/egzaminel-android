package materna.przemek.egzaminel.Activities.Exam;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import materna.przemek.egzaminel.Activities.Static.BundleTags;
import materna.przemek.egzaminel.DataExchanger.SessionManager;
import materna.przemek.egzaminel.Database.Exam;
import materna.przemek.egzaminel.Database.Term;
import materna.przemek.egzaminel.R;

public class ExamActivity extends AppCompatActivity {


    @BindView(R.id.exam_subject)
    TextView examSubject;
    @BindView(R.id.exam_teacher)
    TextView examTecher;
    @BindView(R.id.exam_topic)
    TextView examTopic;
    @BindView(R.id.exam_description)
    TextView examDescription;
    @BindView(R.id.exam_terms_container)
    RadioGroup termsRadioGroup;

    private Exam exam;
    HashMap<Integer, Term> termsMap;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        fragmentManager = getFragmentManager();
        ButterKnife.bind(this);

        exam = (Exam) getIntent().getSerializableExtra(BundleTags.EXAM_TAG);
        initExamValues();
        initTerms();
    }

    private void initTerms() {

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());


        termsMap = new HashMap<>();
        HashMap<Integer, Term> terms = SessionManager.getTerms();

        for (Map.Entry<Integer, Term> entry : terms.entrySet()) {
            if (entry.getValue().getExam_id() == exam.getExamID()) {
                Term term = entry.getValue();
                termsMap.put(entry.getKey(), term);


                //add button...
                RadioButton radioButtonView = new RadioButton(this);

                String s = dateFormat.format(term.getDate())+ ", "
                        + timeFormat.format(term.getDate()) + ", "
                        + term.getPlace();

                radioButtonView.setText(s);
                radioButtonView.setId(entry.getKey());

                termsRadioGroup.addView(radioButtonView);
            }
        }

        if (SessionManager.getUserTerms().containsKey(exam.getExamID())) {
            termsRadioGroup.check(SessionManager.getUserTerms().get(exam.getExamID()));
        }


        termsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                SessionManager.getUserTerms().put(exam.getExamID(), checkedId);
                SessionManager.commitChangesUserterms(getApplicationContext());
                //Tools.makeLongToast(getApplicationContext(), String.valueOf(checkedId));
            }
        });
    }


    private void initExamValues() {
        examSubject.setText(exam.getSubject());
        examTecher.setText(exam.getTeacher());
        examTopic.setText(exam.getType());
        examDescription.setMovementMethod(new ScrollingMovementMethod());
        examDescription.setText(exam.getDescription());
    }

}
