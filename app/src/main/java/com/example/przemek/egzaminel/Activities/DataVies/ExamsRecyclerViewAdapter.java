package com.example.przemek.egzaminel.Activities.DataVies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.przemek.egzaminel.DataExchanger.SessionManager;
import com.example.przemek.egzaminel.Database.Exam;
import com.example.przemek.egzaminel.Database.Term;
import com.example.przemek.egzaminel.Interfaces.OnRWItemClickListener;
import com.example.przemek.egzaminel.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


class ExamsRecyclerViewAdapter extends RecyclerView.Adapter<ExamsRecyclerViewAdapter.ExamHolder> {

    private List<Exam> examsList;
    private Context context;
    OnRWItemClickListener<Exam> listener;
    SimpleDateFormat dateFormat;

    public ExamsRecyclerViewAdapter(List<Exam> examsList, Context context, OnRWItemClickListener<Exam> listener) {
        this.context = context;
        this.examsList = examsList;
        this.listener = listener;
    }

    @Override
    public ExamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exam_row, parent, false);

        return new ExamHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExamHolder holder, final int position) {

        final Exam exam = examsList.get(position);
        holder.subject.setText(exam.getSubject());
        holder.type.setText(exam.getType());

        //get term
        Term term = SessionManager.getUserTermOrTheFirst(exam.getExamID());
        if (term != null) {
            long time = term.getDate();
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
            Date date = new Date();
            date.setTime(time);

            String dateAndTime = dateFormat.format(date) + ", " + timeFormat.format(date);
            holder.date.setText(dateAndTime);

        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(exam, holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return listener.onLongClick(exam, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return examsList.size();
    }


    class ExamHolder extends RecyclerView.ViewHolder {

        TextView subject, type, date;

        ExamHolder(View itemView) {
            super(itemView);

            subject = (TextView) itemView.findViewById(R.id.exams_list_row_subject);
            type = (TextView) itemView.findViewById(R.id.exams_list_row_type);
            date = (TextView) itemView.findViewById(R.id.exams_list_row_date);
        }
    }
}
