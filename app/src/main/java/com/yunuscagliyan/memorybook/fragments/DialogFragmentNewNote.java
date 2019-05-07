package com.yunuscagliyan.memorybook.fragments;

import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.yunuscagliyan.memorybook.MainActivity;
import com.yunuscagliyan.memorybook.R;
import com.yunuscagliyan.memorybook.data.NoteProvider;
import com.yunuscagliyan.memorybook.dataEventBus.DataEvent;
import com.yunuscagliyan.memorybook.ui.MyCustomDatePicker;


import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;
//
public class DialogFragmentNewNote extends DialogFragment {
    private static final Uri CONTENT_URI = NoteProvider.CONTENT_URI;

    private ImageButton btnClose;
    private EditText etNoteContent;
    private MyCustomDatePicker noteDate;
    private Button btnAddNote;


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL,R.style.dialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnClose = view.findViewById(R.id.btnClose);
        btnAddNote = view.findViewById(R.id.btnAdd);

        noteDate = view.findViewById(R.id.noteDate);
        etNoteContent = view.findViewById(R.id.etNote);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
                    ContentValues values = new ContentValues();
                    values.put(NoteProvider.COLUMN_NOTE_CONTENT, etNoteContent.getText().toString());
                    values.put(NoteProvider.COLUMN_NOTE_DATE, noteDate.getTime());
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
