package com.yunuscagliyan.memorybook;

import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunuscagliyan.memorybook.adapter.Divider;
import com.yunuscagliyan.memorybook.adapter.NoteListAdapter;
import com.yunuscagliyan.memorybook.adapter.NotesRecyclerView;
import com.yunuscagliyan.memorybook.data.NoteProvider;
import com.yunuscagliyan.memorybook.data.Notes;
import com.yunuscagliyan.memorybook.fragments.DialogFragmentNewNote;
import com.yunuscagliyan.memorybook.listeners.AddListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AddListener {
    private static final Uri CONTENT_URI = NoteProvider.CONTENT_URI;

    Toolbar mToolbar;
    Button btnAddNote;
    NotesRecyclerView rVNoteList;
    View emptyList;
    FloatingActionButton fab;
    ArrayList<Notes> mAllNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         initialize();
        setBackgroundImage();

        //update recyclerView//
        updateAdapter();
        rVNoteList.isEmptyListInvisible(mToolbar);
        rVNoteList.isEmptyListVisible(emptyList);
    }
    private void initialize(){
        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        btnAddNote=findViewById(R.id.btn_addNote);
        fab=findViewById(R.id.fab);


        rVNoteList=findViewById(R.id.recyclerViewNoteList);
        rVNoteList.addItemDecoration(new Divider(this,LinearLayoutManager.VERTICAL));
        //rVNoteList.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        emptyList=findViewById(R.id.emptyList);


        LinearLayoutManager manager=new LinearLayoutManager(this);
        rVNoteList.setLayoutManager(manager);

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogNewNote();

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogNewNote();
            }
        });



    }
    public void updateAdapter(){
        mAllNotes=getAllNotesFromDatabase();
        NoteListAdapter adapter=new NoteListAdapter(this,mAllNotes);
        rVNoteList.setAdapter(adapter);
    }
    private ArrayList<Notes> getAllNotesFromDatabase(){
        String[] projection={
                NoteProvider.COLUMN_NOTE_ID,
                NoteProvider.COLUMN_NOTE_CONTENT,
                NoteProvider.COLUMN_NOTE_DATE,
                NoteProvider.COLUMN_NOTE_COMPLETE
        };
       Cursor cursor=getContentResolver().query(CONTENT_URI,projection,null,null,null);
       Notes temporaryNote;
       ArrayList<Notes> allNotes=new ArrayList<>();
       if(cursor!=null){
           while (cursor.moveToNext()){
               int contentColumnIndex=cursor.getColumnIndex(NoteProvider.COLUMN_NOTE_CONTENT);
               int dateColumnIndex=cursor.getColumnIndex(NoteProvider.COLUMN_NOTE_DATE);
               String noteContent=cursor.getString(contentColumnIndex);
               String noteDate=cursor.getString(dateColumnIndex);
               temporaryNote=new Notes(noteContent);
               temporaryNote.setNoteDate(noteDate);
               allNotes.add(temporaryNote);
           }
       }
       return allNotes;

    }

    private void showDialogNewNote() {
        DialogFragmentNewNote dialogFragmentNewNote=new DialogFragmentNewNote();
        dialogFragmentNewNote.show(getSupportFragmentManager(),"dialog_new_note");
    }

    private void setBackgroundImage() {
        /*
        ImageView imageView=findViewById(R.id.ivBackground);
        Glide.with(this)
                .load(R.drawable.blue_background)
                .apply(new RequestOptions().centerCrop())
                .into(imageView);
                */
    }

    @Override
    public void showAddDialog() {
        showDialogNewNote();
    }
}
