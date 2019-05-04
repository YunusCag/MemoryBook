package com.yunuscagliyan.memorybook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NotesRecyclerView extends RecyclerView {
    List<View> isEmptyInvisible= Collections.EMPTY_LIST;
    List<View> isEmptyVisible= Collections.EMPTY_LIST;
    private AdapterDataObserver mObserver=new AdapterDataObserver() {
        @Override
        public void onChanged() {
            showOrHideView();

        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            showOrHideView();

        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            showOrHideView();

        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            showOrHideView();

        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            showOrHideView();

        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            showOrHideView();

        }
    };
    public void showOrHideView(){
        if(getAdapter()!=null && !isEmptyVisible.isEmpty() &&!isEmptyInvisible.isEmpty()){
            //no element state
            if(getAdapter().getItemCount()==0){

                for(View view:isEmptyInvisible){
                    view.setVisibility(View.GONE);
                }
                for(View view:isEmptyVisible){
                    view.setVisibility(View.VISIBLE);
                }
                setVisibility(View.GONE);
            }else {
                setVisibility(View.VISIBLE);
                for(View view:isEmptyInvisible){
                    view.setVisibility(View.VISIBLE);
                }
                for(View view:isEmptyVisible){
                    view.setVisibility(View.GONE);
                }

            }

        }
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        if(adapter!=null){
            adapter.registerAdapterDataObserver(mObserver);
        }
        mObserver.onChanged();
    }

    public NotesRecyclerView(@NonNull Context context) {
        super(context);
    }

    public NotesRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NotesRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void isEmptyListInvisible(View... invisibleView) {
        isEmptyInvisible=Arrays.asList(invisibleView);
        mObserver.onChanged();

    }

    public void isEmptyListVisible(View visibleView) {
        isEmptyVisible=Arrays.asList(visibleView);
        mObserver.onChanged();

    }
}
