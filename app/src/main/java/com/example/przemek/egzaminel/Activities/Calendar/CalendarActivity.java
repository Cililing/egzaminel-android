package com.example.przemek.egzaminel.Activities.Calendar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.example.przemek.egzaminel.Database.Exam;
import com.example.przemek.egzaminel.Database.Term;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Tools.Tools;
import com.example.przemek.egzaminel.Activities.DataVies.ExamsRecyclerViewFragment;
import com.example.przemek.egzaminel.DataExchanger.SessionManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity {

    //TODO w chuj cale sprawdzic i ogarnac

    private static final String TAG = CalendarActivity.class.getSimpleName();

    private HashMap<Integer, Term> terms;
    private HashMap<Integer, Exam> exams;
    private FragmentManager fragmentManager;

    private HashMap<Integer, CalendarDay> userTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();

        getData();
        initFragments();


    }

    private void initFragments() {

        HashSet<CalendarDay> dates = getExamsDates();
        Fragment calendar = CalendarFragment.getInstance(dates, new CustomDateSelectedListener());

        Tools.reloadFragment(fragmentManager, calendar, null, R.id.calendar_calendar_container, CalendarFragment.TAG);
    }

    private void getData() {
        terms = SessionManager.getTerms(); //all terms
        exams = SessionManager.getExams(); //all exams
    }

    private HashSet<CalendarDay> getExamsDates() {

        HashSet<CalendarDay> days = new HashSet<>();
        userTerms = new HashMap<>();

        //check all exams and add term if user has set his term
        for (Exam exam : exams.values()) {
            if (exam.getUserTermId() != -1) {
                //user set his term id, add this term to calendar
                days.add(CalendarDay.from(terms.get(exam.getUserTermId()).getFormattedDate()));
            }
            else {
                //FOR TESTS
                //days.add(CalendarDay.from(exam.getUserTermOrDefault(getApplicationContext()).getFormattedDate()));
            }
        }

        return days;
    }

    private class CustomDateSelectedListener implements OnDateSelectedListener {

        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

            Calendar selectedDate = date.getCalendar();


            ArrayList<Exam> selectedDayExams = new ArrayList<>();
            for (Term term : terms.values()) {
                Calendar calendarTerm = Calendar.getInstance();
                calendarTerm.setTimeInMillis(term.getDate());

                if (selectedDate.get(Calendar.YEAR) == calendarTerm.get(Calendar.YEAR) &&
                        selectedDate.get(Calendar.MONTH) == calendarTerm.get(Calendar.MONTH) &&
                        selectedDate.get(Calendar.DAY_OF_MONTH) == calendarTerm.get(Calendar.DAY_OF_MONTH)) {

                    //find exam with ID, clone it....
                    Exam exam = exams.get(term.getExam_id()).clone();

                    //and change USER_ID to show proper date in recyclerview
                    exam.setUserTermId(term.getId());

                    //the same day, add
                    selectedDayExams.add(exam);
                }
            }

            //load fragment
            ExamsRecyclerViewFragment fragment = ExamsRecyclerViewFragment.getInstance(selectedDayExams, null);
            Tools.reloadFragment(fragmentManager, fragment, null, R.id.calendar_details_container, TAG);

        }

    }
}
