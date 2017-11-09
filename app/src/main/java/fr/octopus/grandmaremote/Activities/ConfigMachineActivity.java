package fr.octopus.grandmaremote.Activities;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
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
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import fr.octopus.grandmaremote.R;
import fr.octopus.grandmaremote.artnet.ArtNetSender;
import fr.octopus.grandmaremote.helper.MainActivityCallBack;
import fr.octopus.grandmaremote.helper.TypeMachine;

public class ConfigMachineActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        for (int i = 0; i < 10; i++){
            ft.add(android.R.id.content, new MyPreferenceFragment());
        }

        ft.commit();
    }


public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            for (int i =0; i < MainActivity.nbrMachinesOnStage; i++) {
                addPreferencesFromResource(R.xml.config_machine);
            }
        }
    }
}
