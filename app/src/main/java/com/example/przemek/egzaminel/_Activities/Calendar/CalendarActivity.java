package com.example.przemek.egzaminel._Activities.Calendar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.przemek.egzaminel.Database.DatabaseHelper;
import com.example.przemek.egzaminel.Database.Exam;
import com.example.przemek.egzaminel.Database.Term;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Tools.Tools;
import com.example.przemek.egzaminel._Activities.DataVies.ExamsRecyclerViewFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = CalendarActivity.class.getSimpleName();

    private HashMap<Integer, Term> terms;
    private HashMap<Integer, Exam> exams;
    private FragmentManager fragmentManager;

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
        terms = new HashMap<>();
        exams = new HashMap<>();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        terms.putAll(helper.getAllTerms());
        exams.putAll(helper.getAllExams());
    }

    private HashSet<CalendarDay> getExamsDates() {
        HashSet<CalendarDay> days = new HashSet<>();

        for (Term term : terms.values()) {
            days.add(CalendarDay.from(term.getFormattedDate()));
        }

        return days;
    }

    private class CustomDateSelectedListener implements OnDateSelectedListener {

        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

            Calendar selectedDate = date.getCalendar();


            ArrayList<Exam> selectedDayExams = new ArrayList<>();
            Iterator<Term> it = terms.values().iterator();
            while (it.hasNext()) {
                Term term = it.next();

                Calendar calendarTerm = Calendar.getInstance();
                calendarTerm.setTimeInMillis(term.getDate());

                if (selectedDate.get(Calendar.YEAR) == calendarTerm.get(Calendar.YEAR) &&
                        selectedDate.get(Calendar.MONTH) == calendarTerm.get(Calendar.MONTH) &&
                        selectedDate.get(Calendar.DAY_OF_MONTH) == calendarTerm.get(Calendar.DAY_OF_MONTH))
                {

                    //find exam with ID, clone it....
                    Exam exam = exams.get(term.getExam_id()).clone();

                    //and change USER_ID to show proper date in recyclerview
                    exam.setUserTermId(term.getId());

                    //the same day, add
                    selectedDayExams.add(exam);
                }
            }

            //load fragment
            ExamsRecyclerViewFragment fragment = ExamsRecyclerViewFragment.getInstance(new ArrayList<>(selectedDayExams));
            Tools.reloadFragment(fragmentManager, fragment, null, R.id.calendar_details_container, TAG);

        }

    }
}
