package com.yunuscagliyan.memorybook.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yunuscagliyan.memorybook.R;

public class Divider extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mOrientation;
    public Divider(Context context,int orientation) {
        mDivider =context.getDrawable(R.drawable.divider);
        //mDivider= ContextCompat.getDrawable(context,R.drawable.divider);
        //mDivider
        if(orientation!=LinearLayoutManager.VERTICAL){
           throw new IllegalArgumentException("this argument don't belong here");
        }
        mOrientation= orientation;


    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(mOrientation==LinearLayoutManager.VERTICAL){
            drawHorizontalDivider(c,parent,state);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left,up,right,down;
        left=parent.getPaddingLeft();
        right=parent.getWidth()-parent.getPaddingRight();
        int elementCount=parent.getChildCount();
        for (int i=0;i<elementCount;i++){
            View currentView=parent.getChildAt(i);
            CoordinatorLayout.LayoutParams params= (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
            up=currentView.getTop()-params.topMargin;
            down=up+mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,up,right,down);
            mDivider.draw(c);

        }

    }
}
