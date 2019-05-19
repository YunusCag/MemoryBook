package com.yunuscagliyan.memorybook;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yunuscagliyan.memorybook.adapter.Divider;
import com.yunuscagliyan.memorybook.adapter.MemoryBookApp;
import com.yunuscagliyan.memorybook.adapter.NoteListAdapter;
import com.yunuscagliyan.memorybook.adapter.NotesRecyclerView;
import com.yunuscagliyan.memorybook.adapter.SimpleTouchCallBack;
import com.yunuscagliyan.memorybook.data.NoteProvider;
import com.yunuscagliyan.memorybook.data.Notes;
import com.yunuscagliyan.memorybook.dataEventBus.DataEvent;
import com.yunuscagliyan.memorybook.fragments.DialogFragmentNewNote;
import com.yunuscagliyan.memorybook.fragments.FragmentDialogNoteCompleted;
import com.yunuscagliyan.memorybook.listeners.Filters;
import com.yunuscagliyan.memorybook.receivers.BootReceiver;
import com.yunuscagliyan.memorybook.services.NotificationService;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final Uri CONTENT_URI = NoteProvider.CONTENT_URI;
    public static final String NOTE_NOT_INLINE="UNORDERED";
    public static final String NOTE_COMPLETED_UNIMPORTANT ="UNIMPORTANT_COMPLETED";

    BootReceiver receiver;

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    Button btnAddNote;
    NavigationView navView;


    NotesRecyclerView rVNoteList;
    NoteListAdapter adapter;


    View emptyList;
    FloatingActionButton fab;
    ArrayList<Notes> mAllNotes;

    NotificationService service;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setBackgroundImage();
        initialize();

        //update recyclerView//


        rVNoteList.isEmptyListInvisible(mToolbar,fab);
        rVNoteList.isEmptyListVisible(emptyList);
        implementFilter();
        new BootReceiver();

        /*

        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(this,NotificationService.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,1000,5000,pendingIntent);
        */
        receiver=new BootReceiver();
        runMyService();


    }

    private void runMyService() {
        service=new NotificationService();
        if(isMyServiceRunning(service)==false){
            Intent intent=new Intent(this,NotificationService.class);
            startService(intent);
        }
    }

    private boolean isMyServiceRunning(NotificationService serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.TAG.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void implementFilter(){
        int selectedFilter= MemoryBookApp.readShared(this);
        switch (selectedFilter){
            case Filters.NOFILTER:
                updateAdapter(NOTE_NOT_INLINE, NOTE_COMPLETED_UNIMPORTANT);
                break;
            case Filters.LONG_TIME:
                updateAdapter(NoteProvider.COLUMN_NOTE_DATE+" DESC", NOTE_COMPLETED_UNIMPORTANT);
                break;
            case Filters.SHORT_TIME:
                updateAdapter(NoteProvider.COLUMN_NOTE_DATE+" ASC", NOTE_COMPLETED_UNIMPORTANT);
                break;
            case Filters.COMPLETED_NOTE:
                updateAdapter(NOTE_NOT_INLINE,"1");
                break;
            case Filters.NOT_COMPLETED_NOTE:
                updateAdapter(NoteProvider.COLUMN_NOTE_DATE+" ASC","0");
                break;
        }
    }
    private void initialize(){
        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout=findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle drawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open_drawer,R.string.close_drawer);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navView=findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(this);


        Calendar cal= Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        int day=cal.get(Calendar.DAY_OF_MONTH);
        String month_name = month_date.format(cal.getTime());
        String currentDate=day+"/"+month_name;

        mToolbar.setSubtitle(currentDate);

        btnAddNote=findViewById(R.id.btn_addNote);
        fab=findViewById(R.id.fab);

        mAllNotes=getAllNotesFromDatabase(NOTE_NOT_INLINE, NOTE_COMPLETED_UNIMPORTANT);

        rVNoteList=findViewById(R.id.recyclerViewNoteList);
        rVNoteList.addItemDecoration(new Divider(this,LinearLayoutManager.VERTICAL));
        //rVNoteList.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        emptyList=findViewById(R.id.emptyList);


        LinearLayoutManager manager=new LinearLayoutManager(this);
        rVNoteList.setLayoutManager(manager);
        adapter=new NoteListAdapter(this,mAllNotes);
        adapter.setHasStableIds(true);
        rVNoteList.setAdapter(adapter);

        /**
         *
         * Swipe Processing Stuff
         *
         * */
        SimpleTouchCallBack callBack=new SimpleTouchCallBack();
        ItemTouchHelper helper=new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(rVNoteList);

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

        /*
        *Alarm Manager
         */



    }
    public void updateAdapter(String order,String completed){
        mAllNotes.clear();
        mAllNotes=getAllNotesFromDatabase(order,completed);
        adapter.updateData(mAllNotes);

    }
    private ArrayList<Notes> getAllNotesFromDatabase(String order,String completed){
        String orderQuery=order;
        String selection=NoteProvider.COLUMN_NOTE_COMPLETE+"=?";
        String[] selectionArgs={completed};

        if(order.equals(NOTE_NOT_INLINE)){
            orderQuery=null;
        }
        if(completed.equals(NOTE_COMPLETED_UNIMPORTANT)){
            selection=null;
            selectionArgs=null;
        }
        String[] projection={
                NoteProvider.COLUMN_NOTE_ID,
                NoteProvider.COLUMN_NOTE_CONTENT,
                NoteProvider.COLUMN_NOTE_DATE,
                NoteProvider.COLUMN_NOTE_COMPLETE
        };
       Cursor cursor=getContentResolver().query(CONTENT_URI,projection,selection,selectionArgs,orderQuery);
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

    private void showDialogNewNote() {
        DialogFragmentNewNote dialogFragmentNewNote=new DialogFragmentNewNote();
        dialogFragmentNewNote.show(getSupportFragmentManager(),"dialog_new_note");
    }

    private void showDialogNoteCompleted(int position) {
        EventBus.getDefault().postSticky(new DataEvent.DialogPositionComplete(position));
        FragmentDialogNoteCompleted dialogNoteCompleted=new FragmentDialogNoteCompleted();
        dialogNoteCompleted.show(getSupportFragmentManager(),"dialog_note_complete");
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();



    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        this.registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Subscribe
    public void onDialogFragmentNewNote(DataEvent.ShowAddNoteDialog event){
        if(event.getTrigger()==1){
            showDialogNewNote();
        }

    }
    @Subscribe
    public void onUpdateDate(DataEvent.UpdateDataTrigger event){
        if(event.getTrigger()==1){
            updateAdapter(NOTE_NOT_INLINE, NOTE_COMPLETED_UNIMPORTANT);
        }
    }
    @Subscribe
    public void onDialogComplete(DataEvent.DialogNotePositionComplete event){
        showDialogNoteCompleted(event.getPosition());

    }


    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result=true;
        switch (item.getItemId()){
            case R.id.menu_new_note:
                showDialogNewNote();
                break;
            case R.id.menu_no_filter:
                MemoryBookApp.writeShared(this,Filters.NOFILTER);
                updateAdapter(NOTE_NOT_INLINE, NOTE_COMPLETED_UNIMPORTANT);

                break;
            case R.id.menu_long_time:
                MemoryBookApp.writeShared(this,Filters.LONG_TIME);
                updateAdapter(NoteProvider.COLUMN_NOTE_DATE+" DESC", NOTE_COMPLETED_UNIMPORTANT);
                break;
            case R.id.menu_short_time:
                MemoryBookApp.writeShared(this,Filters.SHORT_TIME);
                updateAdapter(NoteProvider.COLUMN_NOTE_DATE+" ASC", NOTE_COMPLETED_UNIMPORTANT);
                break;
            case R.id.menu_completed_notes:
                MemoryBookApp.writeShared(this,Filters.COMPLETED_NOTE);
                updateAdapter(NOTE_NOT_INLINE,"1");
                break;
            case R.id.menu_not_completed_notes:
                MemoryBookApp.writeShared(this,Filters.NOT_COMPLETED_NOTE);
                updateAdapter(NoteProvider.COLUMN_NOTE_DATE+" ASC","0");
                break;
                default:
                    result=false;
                    MemoryBookApp.writeShared(this,Filters.NOFILTER);
        }

        return result;


    }



    private void setBackgroundImage() {
        /*

        ImageView imageView=findViewById(R.id.collapsingImage);
        Glide.with(this)
                .load(R.drawable.collapsing_image)
                .apply(new RequestOptions().centerCrop())
                .into(imageView);
                */

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_my_day:
                updateAdapter(NoteProvider.COLUMN_NOTE_DATE+" ASC", NOTE_COMPLETED_UNIMPORTANT);
                mDrawerLayout.closeDrawers();
            case R.id.menu_long_time:
                MemoryBookApp.writeShared(this,Filters.LONG_TIME);
                updateAdapter(NoteProvider.COLUMN_NOTE_DATE+" DESC", NOTE_COMPLETED_UNIMPORTANT);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_short_time:
                MemoryBookApp.writeShared(this,Filters.SHORT_TIME);
                updateAdapter(NoteProvider.COLUMN_NOTE_DATE+" ASC", NOTE_COMPLETED_UNIMPORTANT);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_completed_notes:
                MemoryBookApp.writeShared(this,Filters.COMPLETED_NOTE);
                updateAdapter(NOTE_NOT_INLINE,"1");
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_not_completed_notes:
                MemoryBookApp.writeShared(this,Filters.NOT_COMPLETED_NOTE);
                updateAdapter(NoteProvider.COLUMN_NOTE_DATE+" ASC","0");
                mDrawerLayout.closeDrawers();
                break;
            default:


                break;
        }
        return false;
    }
}
