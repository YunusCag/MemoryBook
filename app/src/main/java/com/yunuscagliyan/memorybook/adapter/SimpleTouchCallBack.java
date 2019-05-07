package com.yunuscagliyan.memorybook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.yunuscagliyan.memorybook.dataEventBus.DataEvent;
import com.yunuscagliyan.memorybook.listeners.SwipeListener;

import org.greenrobot.eventbus.EventBus;

public class SimpleTouchCallBack extends ItemTouchHelper.Callback {
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        if(viewHolder instanceof NoteListAdapter.NoteViewHolder){
            return makeMovementFlags(0,ItemTouchHelper.END);
        }else {
            return makeMovementFlags(0,0);
        }

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        EventBus .getDefault().post(new DataEvent.SwipedNotePosition(viewHolder.getAdapterPosition()));
        //mSwipeListener.onSwipe(viewHolder.getAdapterPosition());



    }
}
