package fr.octopus.grandmaremote.Activities;

import android.content.Context;

import fr.octopus.grandmaremote.helper.MainActivityCallBack;
import fr.octopus.grandmaremote.helper.TypeMachine;

/**
 * Created by nico on 11/6/17.
 */

public class SelectableImageView extends android.support.v7.widget.AppCompatImageView implements MainActivityCallBack {
    private MainActivityCallBack selectedCallBack;
    private TypeMachine typeMachine;
    private int numMachine;

    public SelectableImageView(Context context, TypeMachine typeMachine, Integer numMachine) {
        super(context);
        this.typeMachine = typeMachine;
        this.numMachine = numMachine;
        selectedCallBack = (MainActivityCallBack) context;
    }

    @Override
    public void selectedItem(SelectableImageView imageView) {
        selectedCallBack.selectedItem(imageView);
    }
}
