package com.yunuscagliyan.memorybook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yunuscagliyan.memorybook.R;
import com.yunuscagliyan.memorybook.data.Notes;
import com.yunuscagliyan.memorybook.listeners.AddListener;
import com.yunuscagliyan.memorybook.listeners.SwipeListener;

import java.util.List;


public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {
    private static final int ITEM = 0;
    public static final int FOOTER=1;
    private Context mContext;
    private List<Notes> mAllNotes;
    AddListener mAddListener;


    public NoteListAdapter(Context context, List<Notes> allNotes) {
        this.mContext=context;
        this.mAllNotes=allNotes;
        mAddListener= (AddListener) mContext;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(i==ITEM){
            View view= LayoutInflater.from(mContext).inflate(R.layout.one_row_note,viewGroup,false);
            RecyclerView.ViewHolder noteViewHolder=new NoteViewHolder(view);
            return noteViewHolder;
        }if (i==FOOTER){
            View view=LayoutInflater.from(mContext).inflate(R.layout.footer,viewGroup,false);
            RecyclerView.ViewHolder  footerViewHolder=new FooterViewHolder(view);
            return footerViewHolder;

        }
        return null;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if(holder instanceof NoteViewHolder && i!=1){
            Notes temporaryNotes=mAllNotes.get(i);
            NoteViewHolder noteViewHolder= (NoteViewHolder) holder;
            noteViewHolder.noteContent.setText(temporaryNotes.getNoteContent());
            noteViewHolder.noteDate.setText(temporaryNotes.getNoteDate());
        }



    }

    @Override
    public int getItemCount() {
        if(mAllNotes==null||mAllNotes.isEmpty()){
            return 0;

        }else {
            return mAllNotes.size()+1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mAllNotes== null|| mAllNotes.size()==0){
            return ITEM;

        }else if(position<mAllNotes.size()){
            return ITEM;

        }else {
            return FOOTER;

        }

    }

    @Override
    public void swipeItem(int position) {

    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView noteContent;
        TextView noteDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteContent=itemView.findViewById(R.id.tvNoteContent);
            noteDate=itemView.findViewById(R.id.tvNoteDate);

        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Button btn_footer;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_footer=itemView.findViewById(R.id.btn_footer);
            btn_footer.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            mAddListener.showAddDialog();
        }
    }
}
