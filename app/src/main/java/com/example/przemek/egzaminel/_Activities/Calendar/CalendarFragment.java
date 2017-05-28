package com.example.przemek.egzaminel._Activities.Calendar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.przemek.egzaminel.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarFragment extends Fragment {

    private static final int fromYYYY = 2015;
    private static final int fromMM = 1;
    private static final int fromDD = 1;
    private static final int toYYYY = 2020;
    private static final int toMM = 12;
    private static final int toDD = 31;

    static final String TAG = CalendarFragment.class.getSimpleName();

    @BindView(R.id.calendar_fragment_calendar)
    MaterialCalendarView calendar;

    int color;
    HashSet<CalendarDay> importantDates = new HashSet<>();
    OnDateSelectedListener listener;


    public static CalendarFragment getInstance(HashSet<CalendarDay> importantDates, OnDateSelectedListener listener) {
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.importantDates = importantDates;
        calendarFragment.listener = listener;
        return calendarFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calendar_fragment_calendar, container, false);
        ButterKnife.bind(this, view);

        initCalendar();

        return view;
    }

    public void initCalendar() {

        calendar.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(fromYYYY, fromMM, fromDD))
                .setMaximumDate(CalendarDay.from(toYYYY, toMM, toDD))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

        DayViewDecorator decorator = new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return importantDates.contains(day);
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new DotSpan(5, color));
            }
        };

        calendar.addDecorator(decorator);
        calendar.setOnDateChangedListener(listener);
    }

}
