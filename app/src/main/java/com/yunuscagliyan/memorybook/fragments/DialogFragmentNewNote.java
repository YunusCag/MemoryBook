package com.yunuscagliyan.memorybook.fragments;

import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.yunuscagliyan.memorybook.MainActivity;
import com.yunuscagliyan.memorybook.R;
import com.yunuscagliyan.memorybook.data.NoteProvider;
import com.yunuscagliyan.memorybook.data.TimeViewModel;
import com.yunuscagliyan.memorybook.dataEventBus.DataEvent;
import com.yunuscagliyan.memorybook.ui.MyCustomDatePicker;


import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
//
public class DialogFragmentNewNote extends BottomSheetDialogFragment {
    private static final Uri CONTENT_URI = NoteProvider.CONTENT_URI;

    private ImageButton btnClose;
    private EditText etNoteContent;
    private MyCustomDatePicker noteDate;
    private Button btnAddNote;
    private Button btnSetTime;
    private Button btnSetDate;
    TimeViewModel timeModel;
    private Calendar mCalendar;


    @Override
    public void onStart() {
        super.onStart();
        ///getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mCalendar=Calendar.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnClose = view.findViewById(R.id.btnClose);
        btnAddNote = view.findViewById(R.id.btnAdd);

        etNoteContent = view.findViewById(R.id.etNote);

        btnSetTime=view.findViewById(R.id.btn_setTime);
        btnSetDate=view.findViewById(R.id.btn_setDate);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=((MainActivity)getContext()).getSupportFragmentManager();
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {

                                mCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                mCalendar.set(Calendar.MINUTE,minute);
                                mCalendar.set(Calendar.SECOND,0);


                            }
                        })
                        .setStartTime(10, 10)
                        .setDoneText("Yes")
                        .setCancelText("No");
                rtpd.show(manager, "time_picker");
            }

        });
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=((MainActivity)getContext()).getSupportFragmentManager();
                Calendar tempCalender=Calendar.getInstance();
                int day=tempCalender.get(Calendar.DAY_OF_MONTH);
                int month=tempCalender.get(Calendar.MONTH);
                int year=tempCalender.get(Calendar.YEAR);
                MonthAdapter.CalendarDay date=new MonthAdapter.CalendarDay();
                date.setDay(year,month,day);

                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                mCalendar=Calendar.getInstance();
                                Log.e("YYYY","Day:"+dayOfMonth);
                                mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                mCalendar.set(Calendar.MONTH,monthOfYear);
                                mCalendar.set(Calendar.YEAR,year);

                                mCalendar.set(Calendar.HOUR,0);
                                mCalendar.set(Calendar.MINUTE,0);
                                mCalendar.set(Calendar.SECOND,0);

                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(year,month,day)
                        .setDateRange(date, null)
                        .setDoneText("Yes")
                        .setCancelText("No");
                cdp.show(manager, "date_picker");
            }

        });
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH,noteDate.getDayOfMonth());
                    calendar.set(Calendar.MONTH,noteDate.getMonth());
                    calendar.set(Calendar.YEAR,noteDate.getYear());

                    calendar.set(Calendar.HOUR,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                */

                String note=etNoteContent.getText().toString();
                if(!note.equals("")){
                    int day=mCalendar.get(Calendar.DAY_OF_MONTH);
                    int hour=mCalendar.get(Calendar.HOUR_OF_DAY);
                    Log.e("YYYYY","Day eklenmeden:"+day+" Saat eklenmeden :"+hour);
                    ContentValues values = new ContentValues();
                    values.put(NoteProvider.COLUMN_NOTE_CONTENT, etNoteContent.getText().toString());
                    values.put(NoteProvider.COLUMN_NOTE_INSERTION_TIME,mCalendar.getTimeInMillis());
                    values.put(NoteProvider.COLUMN_NOTE_DATE, mCalendar.getTimeInMillis());
                    Uri uri=getActivity().getContentResolver().insert(CONTENT_URI, values);
                    //Toasty.success(getContext(),"Uri:"+uri,Toasty.LENGTH_LONG,true).show();
                    EventBus.getDefault().post(new DataEvent.UpdateDataTrigger(1));
                }


                dismiss();
            }
        });
        super.onViewCreated(view, savedInstanceState);


}



}
