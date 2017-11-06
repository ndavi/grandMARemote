package fr.octopus.grandmaremote.Activities;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by nico on 11/6/17.
 */

public class SelectableImageView extends android.support.v7.widget.AppCompatImageView implements MainActivityCallBack {
    private MainActivityCallBack selectedCallBack;
    private TypeMachine typeMachine;

    public SelectableImageView(Context context, TypeMachine typeMachine) {
        super(context);
        this.typeMachine = typeMachine;
        selectedCallBack = (MainActivityCallBack) context;
    }

    @Override
    public void selectedItem(SelectableImageView imageView) {
        selectedCallBack.selectedItem(imageView);
    }
}
