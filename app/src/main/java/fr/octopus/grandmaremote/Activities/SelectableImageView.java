package fr.octopus.grandmaremote.Activities;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import fr.octopus.grandmaremote.R;
import fr.octopus.grandmaremote.helper.MainActivityCallBack;
import fr.octopus.grandmaremote.helper.TypeMachine;

/**
 * Created by nico on 11/6/17.
 */

public class SelectableImageView extends android.support.v7.widget.AppCompatImageView implements MainActivityCallBack {
    private MainActivityCallBack selectedCallBack;
    private TypeMachine typeMachine;
    private int numMachine;
    private PopupWindow popUpWindow;
    private MainActivity activity;


    public SelectableImageView(MainActivity context, TypeMachine typeMachine, Integer numMachine) {
        super(context);
        this.activity = context;
        this.typeMachine = typeMachine;
        this.numMachine = numMachine;
        selectedCallBack = (MainActivityCallBack) context;
    }

    @Override
    public void selectedItem(SelectableImageView imageView) {
        selectedCallBack.selectedItem(imageView);
    }

    public TypeMachine getTypeMachine() {
        return typeMachine;
    }

    public int getNumMachine() {
        return numMachine;
    }
}
