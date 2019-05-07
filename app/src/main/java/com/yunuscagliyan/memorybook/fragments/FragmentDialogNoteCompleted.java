package com.yunuscagliyan.memorybook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.yunuscagliyan.memorybook.R;
import com.yunuscagliyan.memorybook.dataEventBus.DataEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import es.dmoral.toasty.Toasty;

public class FragmentDialogNoteCompleted extends DialogFragment {
    ImageView btnClose;
    Button btnCompleted;
    int completedNotePosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dialog_completed,container,false);
        btnClose=view.findViewById(R.id.btnClose);
        btnCompleted=view.findViewById(R.id.btn_completed);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DataEvent.CompleteNotePosition(completedNotePosition));
                dismiss();


            }
        });
        return view;
    }

    @Subscribe(sticky = true)
    public void onDialogNotePositionComplete(DataEvent.DialogPositionComplete event){
        completedNotePosition=event.getPosition();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
