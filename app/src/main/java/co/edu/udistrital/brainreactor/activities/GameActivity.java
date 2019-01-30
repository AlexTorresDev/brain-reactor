package co.edu.udistrital.brainreactor.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.animation.Animation;
import co.edu.udistrital.brainreactor.levels.*;
import co.edu.udistrital.brainreactor.view.SquareLayout;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private SquareLayout container;
    private MaterialCardView player1, player2;
    private TextView scorePlayer1, scorePlayer2, textPlayer1, textPlayer2;
    private List<Level> fragmentList;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);

        Objects.requireNonNull(getSupportActionBar()).hide();

        initComponents();
        setFragmentList();
        setFragment();
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
        if (Integer.parseInt(String.valueOf(scorePlayer1.getText())) != 0 || Integer.parseInt(String.valueOf(scorePlayer2.getText())) != 0) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exit_title)
                    .setMessage(R.string.exit_message)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(GameActivity.this, MainActivity.class));
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initComponents() {
        level = 0;

        container = findViewById(R.id.container);
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        textPlayer1 = findViewById(R.id.textPlayer1);
        textPlayer2 = findViewById(R.id.textPlayer2);
        scorePlayer1 = findViewById(R.id.scorePlayer1);
        scorePlayer2 = findViewById(R.id.scorePlayer2);

        player1.setOnClickListener(this);
        player2.setOnClickListener(this);
    }

    private void setFragmentList() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new OperationsFragment());
        fragmentList.add(new ColorsFragment());
        fragmentList.add(new TimerFragment());
        fragmentList.add(new ImagesFragment());
        fragmentList.add(new CapitalsFragment());

        Collections.shuffle(fragmentList, new Random());
    }

    public void setFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (Fragment) getFragment())
                .commit();

        if (level > 0) {
            new Animation(this).createAnimation(container, R.color.colorPanel).start();
        }

        setDefaultAttr();
    }

    private Level getFragment() {
        return fragmentList.get(level);
    }

    private void setDefaultAttr() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                player1.setBackgroundColor(ContextCompat.getColor(GameActivity.this, R.color.colorPanel));
                player2.setBackgroundColor(ContextCompat.getColor(GameActivity.this, R.color.colorPanel));
                textPlayer1.setText(getFragment().getMessage());
                textPlayer2.setText(getFragment().getMessage());
            }
        }, 400);
    }

    public void nextLevel() {
        level++;

        if (level < fragmentList.size()) {
            setFragment();
        } else {
            Intent intent = new Intent(GameActivity.this, ResultActivity.class);
            intent.putExtra("score1", scorePlayer1.getText());
            intent.putExtra("score2", scorePlayer2.getText());
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player1:
                getFragment().touchPanel(player1, textPlayer1, scorePlayer1);
                break;
            case R.id.player2:
                getFragment().touchPanel(player2, textPlayer2, scorePlayer2);
                break;
        }

        setDefaultAttr();
    }

}
