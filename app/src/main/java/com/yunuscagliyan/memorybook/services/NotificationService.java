package com.yunuscagliyan.memorybook.services;

import android.app.ActivityManager;
import android.app.Application;
import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.yunuscagliyan.memorybook.MainActivity;
import com.yunuscagliyan.memorybook.R;
import com.yunuscagliyan.memorybook.data.NoteProvider;
import com.yunuscagliyan.memorybook.data.Notes;

import java.util.ArrayList;
import java.util.List;

import br.com.goncalves.pugnotification.notification.PugNotification;



public class NotificationService extends IntentService {
    private static final Uri CONTENT_URI = NoteProvider.CONTENT_URI;
    private Notes notes;
    private long twoHour=7200000;
    private long fifteen=900000;
    private long notifyLong;
    private long currentLong;
    final static String SERVICE_NAME="NotificationService";


    public static final String TAG=Thread.currentThread().getName();
    ArrayList<Notes> notCompletedTaskList=new ArrayList<>();

    public NotificationService() {
        super("NotificationService");
        Log.e("YYYY","Notification Service :Constructor:");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("YYYY","Notification Service :onHandleIntent:");
        notCompletedTaskList=getNotCompletedTaskFromDatabase();
        //sendNotification();
        for(Notes temporary:notCompletedTaskList){
            if(notificationRequired(temporary.getNoteDate())){
                notes=temporary;
               sendNotification();

            }
        }

    }

    private void sendNotification() {
        if(currentLong==notifyLong){
            PugNotification.with(this)
                    .load()
                    .title(notes.getNoteContent())
                    .message(notes.getNoteContent())
                    .bigTextStyle("Tap to finish the Task")
                    .smallIcon(R.drawable.icon_check)
                    .largeIcon(R.drawable.icon_check)
                    .flags(Notification.DEFAULT_ALL)
                    .click(MainActivity.class)
                    .vibrate(new long[]{700, 100, 400 ,600})
                    .autoCancel(true)
                    .simple()
                    .build();
            notifyLong+=fifteen;
        }else {
            currentLong=System.currentTimeMillis();
        }

    }

    private boolean notificationRequired(long noteInsertionDate) {
        long now=System.currentTimeMillis();

        long better=noteInsertionDate+twoHour;
        if(now==noteInsertionDate){
            notifyLong=System.currentTimeMillis();
            currentLong=System.currentTimeMillis();
            return true;
        }else if(now<better){
            notifyLong=System.currentTimeMillis();
            currentLong=System.currentTimeMillis();
            return true;
        }else {
            return false;
        }

    }

    private ArrayList<Notes> getNotCompletedTaskFromDatabase(){
        String selection= NoteProvider.COLUMN_NOTE_COMPLETE+"=?";
        String[] selectionArgs={"0"};

        String[] projection={
                NoteProvider.COLUMN_NOTE_ID,
                NoteProvider.COLUMN_NOTE_CONTENT,
                NoteProvider.COLUMN_NOTE_INSERTION_TIME,
                NoteProvider.COLUMN_NOTE_DATE,
                NoteProvider.COLUMN_NOTE_COMPLETE
        };
        Cursor cursor=getContentResolver().query(CONTENT_URI,projection,selection,selectionArgs,null);
        Notes temporaryNote;
        ArrayList<Notes> allNotes=new ArrayList<>();
        if(cursor!=null){
            while (cursor.moveToNext()){
                int idColumnIndex=cursor.getColumnIndex(NoteProvider.COLUMN_NOTE_ID);
                int contentColumnIndex=cursor.getColumnIndex(NoteProvider.COLUMN_NOTE_CONTENT);
                int dateColumnIndex=cursor.getColumnIndex(NoteProvider.COLUMN_NOTE_DATE);
                int completedColumnIndex=cursor.getColumnIndex(NoteProvider.COLUMN_NOTE_COMPLETE);

                String noteContent=cursor.getString(contentColumnIndex);
                long noteDate=cursor.getLong(dateColumnIndex);
                int noteId=cursor.getInt(idColumnIndex);
                int noteCompleted=cursor.getInt(completedColumnIndex);

                temporaryNote=new Notes(noteContent);
                temporaryNote.setId(noteId);
                temporaryNote.setNoteDate(noteDate);
                temporaryNote.setNoteComplete(noteCompleted);
                allNotes.add(temporaryNote);
            }
        }
        return allNotes;

    }



}
