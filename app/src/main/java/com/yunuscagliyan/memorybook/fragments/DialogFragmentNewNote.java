package com.yunuscagliyan.memorybook.fragments;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.yunuscagliyan.memorybook.MainActivity;
import com.yunuscagliyan.memorybook.R;
import com.yunuscagliyan.memorybook.data.NoteProvider;


import es.dmoral.toasty.Toasty;
//
public class DialogFragmentNewNote extends DialogFragment {
    private static final Uri CONTENT_URI = NoteProvider.CONTENT_URI;

    private ImageButton btnClose;
    private EditText etNoteContent;
    private DatePicker noteDate;
    private Button btnAddNote;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnClose = view.findViewById(R.id.btnClose);
        btnAddNote = view.findViewById(R.id.btnAdd);

        noteDate = view.findViewWithTag(R.id.datePicker);
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
                ContentValues values = new ContentValues();
                values.put(NoteProvider.COLUMN_NOTE_CONTENT, etNoteContent.getText().toString());
                values.put(NoteProvider.COLUMN_NOTE_DATE, "22/05/2019");
                Uri uri=getActivity().getContentResolver().insert(CONTENT_URI, values);
                ((MainActivity)getActivity()).updateAdapter();
                Toasty.success(getContext(),"Uri:"+uri,Toasty.LENGTH_LONG,true).show();
                dismiss();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

}
