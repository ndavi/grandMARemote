package fr.octopus.grandmaremote.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import fr.octopus.grandmaremote.R;
import fr.octopus.grandmaremote.artnet.ArtNetSender;
import fr.octopus.grandmaremote.helper.MainActivityCallBack;
import fr.octopus.grandmaremote.helper.TypeMachine;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainActivityCallBack, SeekBar.OnSeekBarChangeListener {

    private MovableActivity movable;
    private RelativeLayout sceneLayout;
    private ArtNetSender artNetService;

    public static SelectableImageView selectedImage;
    private SlidingUpPanelLayout mLayout;
    private TextView machineSelected;
    private List<SelectableImageView> machinesOnStage = new ArrayList<>();
    private SharedPreferences prefs;

    ToggleButton chaseBtn;
    ToggleButton strobeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        artNetService = new ArtNetSender(1,prefs.getString("adresse_ip","192.168.1.1"));
        setContentView(R.layout.activity_main);
        sceneLayout = findViewById(R.id.sceneLayout);
        mLayout = findViewById(R.id.sliding_layout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        Button settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(this);
        SeekBar dimmerBar = findViewById(R.id.seekBarDimmer);
        dimmerBar.setOnSeekBarChangeListener(this);
        SeekBar colorBar = findViewById(R.id.seekBarColor);
        colorBar.setOnSeekBarChangeListener(this);
        Button clearBtn = findViewById(R.id.btn_clear);
        clearBtn.setOnClickListener(this);
        Button saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(this);

        machineSelected = findViewById(R.id.machineSelectLabel);
        chaseBtn = findViewById(R.id.chaseToggle);
        strobeBtn = findViewById(R.id.strobeToggle);
        strobeBtn.setOnClickListener(this);
        chaseBtn.setOnClickListener(this);
        this.loadConfig();




    }

    public boolean loadConfig() {
        this.movable = new MovableActivity(this);
        int nbrMachines = prefs.getInt("nbrMachines", 0);
        if(nbrMachines != 0) {
            for (int i = 0; i < nbrMachines; i++) {
                SelectableImageView image = new SelectableImageView(this, TypeMachine.PARLED,i);
                image.setImageBitmap(resizeBitmap(R.drawable.parled_test));
                image.setX(prefs.getFloat("machine" + i + "X",0));
                image.setY(prefs.getFloat("machine" + i + "Y",0));
                movable.addView(image);
                machinesOnStage.add(image);
            }
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0,200,0,200);
        movable.setWrapContent(true);
        movable.setDropOnScale(true);
        sceneLayout.addView(movable, params);
        return true;
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
                SelectableImageView image = new SelectableImageView(this, TypeMachine.PARLED,machinesOnStage.size());
                image.setImageBitmap(resizeBitmap(R.drawable.parled_test));
                movable.addView(image);
                machinesOnStage.add(image);
                break;
            case R.id.settingsBtn:
                Intent a = new Intent(this,ConfigMachineActivity.class);
                startActivity(a);
                break;
            case R.id.btn_clear:
                prefs.edit().clear().apply();
                movable.removeAllViews();
                machinesOnStage = new ArrayList<>();
                break;
            case R.id.btn_save:
                SharedPreferences.Editor editor = prefs.edit();
                for (SelectableImageView machine : machinesOnStage) {
                    editor = editor
                            .putFloat("machine" + machine.getNumMachine() + "X", selectedImage.getX())
                            .putFloat("machine" + machine.getNumMachine() + "Y", selectedImage.getY());
                }
                editor
                        .putInt("nbrMachines", machinesOnStage.size())
                        .apply();
                break;
            case R.id.strobeToggle:
                Integer canal_strobe = Integer.parseInt(prefs.getString("canal_strobe","0"));
                if(strobeBtn.isActivated()) {
                    artNetService.send(canal_strobe, 0);
                } else {
                    artNetService.send(canal_strobe, 255);
                }
                break;
            case R.id.chaseToggle:
                Integer canal_chase = Integer.parseInt(prefs.getString("canal_chase","0"));
                if(chaseBtn.isActivated()) {
                    artNetService.send(canal_chase, 0);
                } else {
                    artNetService.send(canal_chase, 255);
                }
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
        this.selectedImage.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()) {
            case R.id.seekBarColor:
                int canal_color = Integer.parseInt(prefs.getString("canal_color_machine_" + selectedImage.getNumMachine(),"0"));
                artNetService.send(canal_color,(int)(progress * 2.55));
                break;
            case R.id.seekBarDimmer:
                int canal_dimmer = Integer.parseInt(prefs.getString("canal_dimmer_machine_" + selectedImage.getNumMachine(),"0"));
                artNetService.send(canal_dimmer,(int)(progress * 2.55));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
