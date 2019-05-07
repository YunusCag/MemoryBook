package com.yunuscagliyan.memorybook.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunuscagliyan.memorybook.MainActivity;
import com.yunuscagliyan.memorybook.R;
import com.yunuscagliyan.memorybook.data.NoteProvider;
import com.yunuscagliyan.memorybook.data.Notes;
import com.yunuscagliyan.memorybook.dataEventBus.DataEvent;
import com.yunuscagliyan.memorybook.listeners.Filters;




import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final Uri CONTENT_URI = NoteProvider.CONTENT_URI;

    private static final int ITEM = 0;
    public static final int FOOTER=1;
    public static final int EMPTY_FILTER=2;

    public static final int ADD_FOOTER=1;
    public static final int ADD_EMPTY_FILTER=1;
    private Context mContext;
    private ArrayList<Notes> mAllNotes;
    private ContentResolver resolver;
    private int mFilter;


    public NoteListAdapter(Context context, ArrayList<Notes> allNotes) {
        this.mContext=context;
        this.mAllNotes=allNotes;
        resolver=mContext.getContentResolver();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(i==ITEM){
            View view= LayoutInflater.from(mContext).inflate(R.layout.one_row_note,viewGroup,false);
            RecyclerView.ViewHolder noteViewHolder=new NoteViewHolder(view);
            return noteViewHolder;
        }else if(i==EMPTY_FILTER){

            View view= LayoutInflater.from(mContext).inflate(R.layout.empty_filter,viewGroup,false);
            EmptyFilterViewHolder filterViewHolder=new EmptyFilterViewHolder(view);
            return filterViewHolder;
        }
        else{
            View view=LayoutInflater.from(mContext).inflate(R.layout.footer,viewGroup,false);
            RecyclerView.ViewHolder  footerViewHolder=new FooterViewHolder(view);
            return footerViewHolder;

        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if(holder instanceof NoteViewHolder){
            Notes temporaryNotes=mAllNotes.get(i);
            NoteViewHolder noteViewHolder= (NoteViewHolder) holder;
            noteViewHolder.noteContent.setText(temporaryNotes.getNoteContent());
            noteViewHolder.noteDate.setText(""+temporaryNotes.getNoteDate());
            noteViewHolder.setBackgroundNote(temporaryNotes.getNoteComplete());
            noteViewHolder.setDate(temporaryNotes.getNoteDate());


        }



    }

    @Override
    public int getItemCount() {
        if(!mAllNotes.isEmpty()){
            return  mAllNotes.size()+ADD_FOOTER;
        }else {
            if(mFilter==Filters.SHORT_TIME || mFilter==Filters.LONG_TIME ||mFilter==Filters.NOFILTER){
                return 0;

            }else {
                return FOOTER+ADD_EMPTY_FILTER;
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(!mAllNotes.isEmpty()){
            if(position<mAllNotes.size()){
                return ITEM;
            }else {
                return FOOTER;
            }
        }else {
            if(mFilter==Filters.COMPLETED_NOTE ||mFilter==Filters.NOT_COMPLETED_NOTE){
                if(position==0){
                    return EMPTY_FILTER;
                }else {
                    return FOOTER;
                }

            }else {
                return ITEM;
            }
        }

    }

    @Override
    public long getItemId(int position) {
        if(position<mAllNotes.size()){
            return mAllNotes.get(position).getId();
        }
        return RecyclerView.NO_ID;
    }

    @Subscribe
    public void onSwipe(DataEvent.SwipedNotePosition event) {
        int position=event.getPosition();
        if(position<mAllNotes.size()){
            Notes deletedNote= mAllNotes.get(position);
            int deletedID=deletedNote.getId();
            String whereClause=NoteProvider.COLUMN_NOTE_ID+"=?";
            String[] args={String.valueOf(deletedID)};
            int affectedRowCount=resolver.delete(CONTENT_URI,whereClause,args);
            if(affectedRowCount>0){
                mAllNotes.remove(deletedNote);
                if(mAllNotes.isEmpty()&&mFilter==Filters.NOT_COMPLETED_NOTE||mFilter==Filters.COMPLETED_NOTE){
                    MemoryBookApp.writeShared(mContext,Filters.NOFILTER);
                    EventBus.getDefault().post(new DataEvent.UpdateDataTrigger(1));
                }
                updateData(mAllNotes);

            }
            Log.e("YYY","Will deleted note id:"+deletedID);
            //((MainActivity)mContext).updateAdapter(MainActivity.NOTE_NOT_INLINE,MainActivity.NOTE_COMPLETED_UNIMPORTANT);
        }
    }
    @Subscribe
    public void onCompleteNotePosition(DataEvent.CompleteNotePosition event){
        int position=event.getPosition();
        if(position<mAllNotes.size()){
            Notes temporary=mAllNotes.get(position);
            String completedNote=String.valueOf(temporary.getId());
            String whereClause=NoteProvider.COLUMN_NOTE_ID+"=?";
            String[] args={completedNote};
            ContentValues values=new ContentValues();
            values.put(NoteProvider.COLUMN_NOTE_COMPLETE,1);
            int affectedRowCount=resolver.update(CONTENT_URI,values,whereClause,args);
            if (affectedRowCount>0){
                temporary.setNoteComplete(1);
                mAllNotes.set(position,temporary);
                Log.e("DB","Completed ID:"+completedNote);
                notifyDataSetChanged();
            }
        }
    }




    public void updateData(ArrayList<Notes> mAllNotes) {
        this.mAllNotes=mAllNotes;
        mFilter=MemoryBookApp.readShared(mContext);
        notifyDataSetChanged();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView noteContent;
        TextView noteDate;
        View mItemView;
        LinearLayout container;

        public NoteViewHolder(@NonNull final View itemView) {
            super(itemView);
            mItemView=itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DataEvent.DialogNotePositionComplete(getAdapterPosition()));

                }
            });
            noteContent=itemView.findViewById(R.id.tvNoteContent);
            noteDate=itemView.findViewById(R.id.tvNoteDate);
            container=itemView.findViewById(R.id.containerNote);



        }

        public void setBackgroundNote(int noteComplete) {
            if (noteComplete==0){
                Drawable drawable1=mContext.getDrawable(android.R.color.transparent);
                container.setBackground(drawable1);


            }else {
                Drawable drawable1=mContext.getDrawable(R.drawable.one_note_background);
                container.setBackground(drawable1);
            }
        }

        public void setDate(long date) {
            noteDate.setText(DateUtils.getRelativeTimeSpanString(date,System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS,0));
        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder{
        Button btn_footer;


        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_footer=itemView.findViewById(R.id.btn_footer);
            btn_footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DataEvent.ShowAddNoteDialog(1));

                }
            });


        }


    }
    class EmptyFilterViewHolder extends RecyclerView.ViewHolder{


        public EmptyFilterViewHolder(@NonNull View itemView) {
            super(itemView);


        }


    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        EventBus.getDefault().unregister(this);
    }
}
