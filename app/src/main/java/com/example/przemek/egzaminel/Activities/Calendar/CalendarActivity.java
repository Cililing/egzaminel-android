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
import java.util.Map;

import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = CalendarActivity.class.getSimpleName();

    private HashMap<Integer, Integer> userTermsIds; //exam, term id
    HashMap<Integer, Exam> exams;
    HashMap<Integer, Term> terms;



    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();

        userTermsIds = SessionManager.getUserTerms();
        exams = SessionManager.getExams();
        terms = SessionManager.getTerms();

        initFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //reaload userterms
        userTermsIds = SessionManager.getUserTerms();
        //reload framgnet
        initFragments();
    }

    private void initFragments() {
        HashSet<CalendarDay> dates = getExamsDates();
        Fragment calendar = CalendarFragment.getInstance(dates, new CustomDateSelectedListener());
        Tools.reloadFragment(fragmentManager, calendar, null, R.id.calendar_calendar_container, CalendarFragment.TAG);
    }


    //calendar days to be signed
    private HashSet<CalendarDay> getExamsDates() {

        HashSet<CalendarDay> days = new HashSet<>();


        //get those terms xD
        for (int id : userTermsIds.values()) {
            //iterate by userTermsIds.TermId ids and add those days
            days.add(CalendarDay.from(terms.get(id).getFormattedDate()));
        }

        //now you have in days only those days, when user has exams (in his saved terms, ofc)
        return days;
    }

    private class CustomDateSelectedListener implements OnDateSelectedListener {

        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

            Calendar selectedDate = date.getCalendar();


            ArrayList<Exam> selectedDayExams = new ArrayList<>();

            for (Map.Entry<Integer, Integer> entry : userTermsIds.entrySet()) { //map entry -> exam_id, user_id

                //iterate by term...
                //check if that day is selected day
                Calendar calendarTerm = Calendar.getInstance();
                calendarTerm.setTimeInMillis(terms.get(entry.getValue()).getDate());

                if (selectedDate.get(Calendar.YEAR) == calendarTerm.get(Calendar.YEAR) &&
                        selectedDate.get(Calendar.MONTH) == calendarTerm.get(Calendar.MONTH) &&
                        selectedDate.get(Calendar.DAY_OF_MONTH) == calendarTerm.get(Calendar.DAY_OF_MONTH)) {

                    //add exam with that id
                    selectedDayExams.add(exams.get(entry.getKey()));
                }
            }

            //load fragment
            ExamsRecyclerViewFragment fragment = ExamsRecyclerViewFragment.getInstance(selectedDayExams, null);
            Tools.reloadFragment(fragmentManager, fragment, null, R.id.calendar_details_container, TAG);

        }

    }
}
