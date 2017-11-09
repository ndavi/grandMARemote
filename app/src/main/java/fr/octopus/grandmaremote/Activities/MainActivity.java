package fr.octopus.grandmaremote.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import fr.octopus.grandmaremote.R;
import fr.octopus.grandmaremote.artnet.ArtNetSender;
import fr.octopus.grandmaremote.helper.MainActivityCallBack;
import fr.octopus.grandmaremote.helper.TypeMachine;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainActivityCallBack {

    private MovableActivity movable;
    private LayoutInflater layoutInflater;
    private RelativeLayout sceneLayout;
    private ArtNetSender artNetService;

    private SelectableImageView selectedImage;
    private SlidingUpPanelLayout mLayout;

    private TextView machineSelected;

    public static int nbrMachinesOnStage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artNetService = new ArtNetSender(1,"192.168.1.1");
        setContentView(R.layout.activity_main);
        layoutInflater = getLayoutInflater();
        sceneLayout = (RelativeLayout) findViewById(R.id.sceneLayout);
        mLayout = findViewById(R.id.sliding_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        Button settingsBtn = (Button) findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(this);

        machineSelected = findViewById(R.id.machineSelectLabel);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fab:
                Boolean firstMovable = false;
                if(movable == null) {
                    movable = new MovableActivity(this);
                    firstMovable = true;
                }
                ImageView image = new SelectableImageView(this, TypeMachine.PARLED,1);

                image.setImageBitmap(resizeBitmap(R.drawable.parled_test));

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(0,200,0,200);

                movable.setWrapContent(true);
                movable.setDropOnScale(true);
                movable.addView(image);
                nbrMachinesOnStage++;
                if(firstMovable) {
                    sceneLayout.addView(movable, params);
                }
                break;
            case R.id.settingsBtn:
                Intent a = new Intent(this,ConfigMachineActivity.class);
                startActivity(a);
                break;
        }
    }



    public Bitmap resizeBitmap(Integer resource) {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),resource), 240, 240, false);
    }

    @Override
    public void selectedItem(SelectableImageView selectableImageView) {
        if(selectedImage != null) {
            this.selectedImage.setColorFilter(Color.TRANSPARENT);
        }
        this.selectedImage = selectableImageView;
        this.selectedImage.setColorFilter(Color.RED);
        this.machineSelected.setText("Machine selectionnée : " + selectableImageView.getTypeMachine().toString() + " " + "Machine numéro : " + selectableImageView.getNumMachine());
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
