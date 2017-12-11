package fr.octopus.grandmaremote.Activities;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
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
        ft.add(android.R.id.content, new MachinePreference());
        ft.commit();
    }


public static class MachinePreference extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        SharedPreferences sharedPreferences;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            //sharedPreferences = getPreferenceManager().getSharedPreferences();
            //sharedPreferences.registerOnSharedPreferenceChangeListener(this);
            super.onCreate(savedInstanceState);
            if(MainActivity.selectedImage != null) {
                addPreferencesFromResource(R.xml.config_machine);
                PreferenceCategory listPreferenceCategory = (PreferenceCategory) findPreference("default_machine");
                listPreferenceCategory.setTitle("Machine numéro" + MainActivity.selectedImage.getNumMachine() + " : " + MainActivity.selectedImage.getTypeMachine());
                EditTextPreference editTextDimmer = (EditTextPreference) findPreference("canal_dimmer");
                EditTextPreference editTextCouleur = (EditTextPreference) findPreference("canal_couleur");
                editTextDimmer.setKey("canal_dimmer_machine_" + MainActivity.selectedImage.getNumMachine());
                editTextCouleur.setKey("canal_color_machine_" + MainActivity.selectedImage.getNumMachine());
            }
            addPreferencesFromResource(R.xml.config_stage);

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    }
}