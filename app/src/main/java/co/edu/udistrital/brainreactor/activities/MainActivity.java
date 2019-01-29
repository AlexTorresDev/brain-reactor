package co.edu.udistrital.brainreactor.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import co.edu.udistrital.brainreactor.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        Button play = findViewById(R.id.play);
        Button about = findViewById(R.id.about);

        play.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            default:
            case R.id.play:
                intent = new Intent(MainActivity.this, GameActivity.class);
                break;
            case R.id.about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                break;
        }

        startActivity(intent);
    }
}
