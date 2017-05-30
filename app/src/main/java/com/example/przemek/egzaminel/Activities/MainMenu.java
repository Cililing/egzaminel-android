package com.example.przemek.egzaminel.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.przemek.egzaminel.Activities.Calendar.CalendarActivity;
import com.example.przemek.egzaminel.Activities.DataVies.GroupsActivity;
import com.example.przemek.egzaminel.Activities.Exam.ExamActivity;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Activities.DataVies.ExamsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenu extends AppCompatActivity {

    @BindView(R.id.main_menu_calendar_button)
    ViewGroup calendar;
    @BindView(R.id.main_menu_list_button)
    ViewGroup list;
    @BindView(R.id.main_menu_datasrc_button)
    ViewGroup dataSrc;
    @BindView(R.id.main_menu_settigins_button)
    ViewGroup settigins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.main_menu_calendar_button)
    public void onCalendarButtonClick() {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_menu_list_button)
    public void onListButtonClick() {
        Intent intent = new Intent(getApplicationContext(), ExamsActivity.class);

        startActivity(intent);
    }

    @OnClick(R.id.main_menu_datasrc_button)
    public void onDataSrcButtonClick() {
        Intent intent = new Intent(getApplicationContext(), GroupsActivity.class);
        startActivity(intent);
    }
}
