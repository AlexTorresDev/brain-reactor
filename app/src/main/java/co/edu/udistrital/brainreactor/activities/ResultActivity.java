package co.edu.udistrital.brainreactor.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Objects;

import co.edu.udistrital.brainreactor.R;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView containerResultTop, containerResultBottom;
    private TextView resultMessageTop, scoreTop, resultMessageBottom, scoreBottom;
    private int score1, score2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_result);

        Objects.requireNonNull(getSupportActionBar()).hide();

        getScores();
        initComponents();
        setResults();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void getScores() {
        Intent intent = getIntent();
        score1 = Integer.parseInt(intent.getStringExtra("score1"));
        score2 = Integer.parseInt(intent.getStringExtra("score2"));
    }

    private void initComponents() {
        containerResultTop = findViewById(R.id.container_result_top);
        containerResultBottom = findViewById(R.id.container_result_bottom);
        resultMessageTop = findViewById(R.id.result_message_top);
        resultMessageBottom = findViewById(R.id.result_message_bottom);
        scoreTop = findViewById(R.id.score_top);
        scoreBottom = findViewById(R.id.score_bottom);

        FloatingActionButton fab_top = findViewById(R.id.fab_top);
        FloatingActionButton fab_bottom = findViewById(R.id.fab_bottom);
        fab_top.setOnClickListener(this);
        fab_bottom.setOnClickListener(this);
    }

    private void setResults() {
        containerResultTop.setBackgroundResource((score1 == score2) ? R.color.colorTie : (score1 > score2) ? R.color.colorSuccess : R.color.colorWrong);
        containerResultBottom.setBackgroundResource((score1 == score2) ? R.color.colorTie : (score1 < score2) ? R.color.colorSuccess : R.color.colorWrong);
        resultMessageTop.setText((score1 == score2) ? R.string.tie : (score1 > score2) ? R.string.you_are_win : R.string.you_are_loser);
        resultMessageBottom.setText((score1 == score2) ? R.string.tie : (score1 < score2) ? R.string.you_are_win : R.string.you_are_loser);
        scoreTop.setText(getResources().getString(R.string.score, score1));
        scoreBottom.setText(getResources().getString(R.string.score, score2));
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
