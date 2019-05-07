package com.yunuscagliyan.memorybook.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunuscagliyan.memorybook.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class MyCustomDatePicker extends LinearLayout implements View.OnTouchListener {
    TextView mTextDay;
    TextView mTextMonth;
    TextView mTextYear;

    Calendar mCalendar;
    SimpleDateFormat formatter;

    public MyCustomDatePicker(Context context) {
        super(context);
        init(context);
    }


    public MyCustomDatePicker(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public MyCustomDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }
    private void init(Context context) {
        View view=LayoutInflater.from(context).inflate(R.layout.date_picker_view,this);
        mCalendar=Calendar.getInstance();
        formatter=new SimpleDateFormat("MMMM");

    }

    @Override
    protected void onFinishInflate() {
        mTextDay=this.findViewById(R.id.tv_date_day);
        mTextMonth=this.findViewById(R.id.tv_date_month);
        mTextYear=this.findViewById(R.id.tv_date_year);

        mTextDay.setOnTouchListener(this);
        mTextMonth.setOnTouchListener(this);
        mTextYear.setOnTouchListener(this);
        super.onFinishInflate();
        int day=mCalendar.get(Calendar.DATE);
        int month=mCalendar.get(Calendar.MONTH);
        int year=mCalendar.get(Calendar.YEAR);

        update(day,month,year,0,0,0);
    }
    private void update(int day, int month, int year, int hour, int minute, int second){
        mTextDay.setText(""+day);
        mTextMonth.setText(""+formatter.format(mCalendar.getTime()));
        mTextYear.setText(""+year);

    }
    public long getTime(){
        return mCalendar.getTimeInMillis();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.tv_date_day:
                processEventFor(mTextDay,event);

                break;
            case R.id.tv_date_month:
                processEventFor(mTextMonth,event);

                break;
            case R.id.tv_date_year:
                processEventFor(mTextYear,event);

                break;
        }
        return true;
    }

    private void processEventFor(TextView textView,MotionEvent event){
        /**
         * LEFT=0,UP=1,RIGHT=2,DOWN=3
         */
        Drawable[] drawables=textView.getCompoundDrawables();
        if(isDrawableUP(drawables)&&isDrawableDOWN(drawables)){
            Rect upCanvas=drawables[1].getBounds();
            Rect downCanvas=drawables[3].getBounds();

            float x=event.getX();
            float y=event.getY();
            if(upDrawableTouched(textView,upCanvas,x,y)){
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    //Toasty.error(getContext(),"touched top Side", Toasty.LENGTH_LONG,true).show();
                    increase(textView.getId());
                    changeBackgroundColor(textView,1);

                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    changeBackgroundOldColor(textView);
                }

            }else if(downDrawableTouched(textView,downCanvas,x,y)){

                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    //Toasty.error(getContext(),"touched down Side", Toasty.LENGTH_LONG,true).show();
                    decrease(textView.getId());
                    changeBackgroundColor(textView,0);
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    changeBackgroundOldColor(textView);
                }

            }else {

            }
        }
    }

    private void changeBackgroundOldColor(TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_normal,0,R.drawable.down_normal);

    }

    private void changeBackgroundColor(TextView textView, int i) {
        if(i==1){
            textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_normal_pressed,0,R.drawable.down_normal);
        }else {
            textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_normal,0,R.drawable.down_normal_pressed);

        }
    }


    private void increase(int id) {
        switch (id){
            case R.id.tv_date_day:
                mCalendar.add(Calendar.DATE,1);
                updateTextView(mCalendar);

                break;
            case R.id.tv_date_month:
                mCalendar.add(Calendar.MONTH,1);
                updateTextView(mCalendar);

                break;
            case R.id.tv_date_year:
                mCalendar.add(Calendar.YEAR,1);
                updateTextView(mCalendar);

                break;
        }

    }
    private void decrease(int id) {
        switch (id){
            case R.id.tv_date_day:
                mCalendar.add(Calendar.DATE,-1);
                updateTextView(mCalendar);

                break;
            case R.id.tv_date_month:
                mCalendar.add(Calendar.MONTH,-1);
                updateTextView(mCalendar);

                break;
            case R.id.tv_date_year:
                mCalendar.add(Calendar.YEAR,-1);
                updateTextView(mCalendar);

                break;
        }

    }
    private void updateTextView(Calendar mCalendar){
        int day=mCalendar.get(Calendar.DATE);
        int month=mCalendar.get(Calendar.MONTH);
        int year=mCalendar.get(Calendar.YEAR);
        mTextDay.setText(""+day);
        mTextMonth.setText(""+formatter.format(mCalendar.getTime()));
        mTextYear.setText(""+year);
    }

    private boolean downDrawableTouched(TextView textView, Rect downCanvas, float x, float y) {

        int xmin=textView.getPaddingLeft();
        int xmax=textView.getWidth()-textView.getPaddingRight();

        int ymax=textView.getHeight();
        int ymin=ymax-downCanvas.height();

        return  x>xmin && x<xmax && y>ymin &&y<ymax;

    }

    private boolean upDrawableTouched(TextView textView, Rect upCanvas, float x, float y) {
        int xmin=textView.getPaddingLeft();
        int xmax=textView.getWidth()-textView.getPaddingRight();

        int ymin=textView.getPaddingTop();
        int ymax=textView.getPaddingTop()+upCanvas.height();

        return  x>xmin && x<xmax && y>ymin &&y<ymax;
    }

    private boolean isDrawableUP(Drawable[] drawables){
        return drawables[1]!=null;

    }
    private boolean isDrawableDOWN(Drawable[] drawables){
        return drawables[3]!=null;

    }

}
