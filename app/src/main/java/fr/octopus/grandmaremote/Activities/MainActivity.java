package fr.octopus.grandmaremote.Activities;

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
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import fr.octopus.grandmaremote.R;
import fr.octopus.grandmaremote.Service.ArtNetService;
import fr.octopus.grandmaremote.helper.MainActivityCallBack;
import fr.octopus.grandmaremote.helper.TypeMachine;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainActivityCallBack {

    private MovableActivity movable;
    private LayoutInflater layoutInflater;
    private RelativeLayout sceneLayout;
    private ArtNetService artNetService;

    private ViewFlipper flipper;
    private SelectableImageView selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artNetService = new ArtNetService("artnet");
        setContentView(R.layout.activity_main);
        movable = new MovableActivity(this);
        layoutInflater = getLayoutInflater();
        flipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        sceneLayout = (RelativeLayout) findViewById(R.id.sceneLayout);

        flipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.card_flip_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.card_flip_left_out));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        Button switcherBtn = (Button) findViewById(R.id.switcher);
        switcherBtn.setOnClickListener(this);
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
                ImageView image = new SelectableImageView(this, TypeMachine.PARLED,1);
                ImageView image2 = new SelectableImageView(this, TypeMachine.PARLED,2);

                image.setImageBitmap(resizeBitmap(R.drawable.parled_test));
                image2.setImageBitmap(resizeBitmap(R.drawable.parled2));

                movable.setWrapContent(true);
                movable.setDropOnScale(true);
                //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                movable.addView(image);
                movable.addView(image2);
                // Look at the RegionView.
                //ViewGroup.LayoutParams paramstest = this.getWindow().getDecorView().getLayoutParams();
                RelativeLayout.LayoutParams paramstest = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                //paramstest.height = paramstest.height + 100;
                sceneLayout.addView(movable, paramstest);
                break;
            case R.id.switcher:
                flipper.showNext();
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

    }
}
