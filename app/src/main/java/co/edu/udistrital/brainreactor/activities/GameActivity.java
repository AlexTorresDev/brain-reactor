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

import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import co.edu.udistrital.brainreactor.levels.Level;
import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.Thread;
import co.edu.udistrital.brainreactor.animation.Animations;
import co.edu.udistrital.brainreactor.levels.*;
import co.edu.udistrital.brainreactor.view.SquareLayout;

@SuppressLint("ClickableViewAccessibility")
public class GameActivity extends AppCompatActivity implements View.OnTouchListener, Runnable {

    private SquareLayout container;
    private MaterialCardView player1, player2;
    private TextView scorePlayer1, scorePlayer2, textPlayer1, textPlayer2;
    private List<Level> fragmentList;
    private int level;

    public static Thread thread;
    public static long millis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_game);

        initComponents();
        setFragmentList();
        setFragment();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        thread.stop();
                        startActivity(new Intent(GameActivity.this, MainActivity.class));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onPause() {
        thread.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        thread.resume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        thread.stop();
        super.onDestroy();
    }

    private void initComponents() {
        level = 0;

        thread = new Thread(this);

        container = findViewById(R.id.container);
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        textPlayer1 = findViewById(R.id.textPlayer1);
        textPlayer2 = findViewById(R.id.textPlayer2);
        scorePlayer1 = findViewById(R.id.scorePlayer1);
        scorePlayer2 = findViewById(R.id.scorePlayer2);

        player1.setOnTouchListener(this);
        player2.setOnTouchListener(this);
    }

    private void setFragmentList() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new OperationsFragment());
        fragmentList.add(new ColorsFragment());
        fragmentList.add(new TimerFragment());
        fragmentList.add(new ImagesFragment());
        //fragmentList.add(new BackgroundFragment());

        Collections.shuffle(fragmentList, new Random());
    }

    public void setFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (Fragment) getFragment())
                .commit();

        if (level > 0)
            new Animations(this).startAnimation(container, R.color.colorPanel);

        setDefaultAttr();
    }

    private Level getFragment() {
        return fragmentList.get(level);
    }

    private void setDefaultAttr() {
        player1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPanel));
        player2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPanel));
        textPlayer1.setText(getFragment().getMessage());
        textPlayer2.setText(getFragment().getMessage());
    }

    public void nextLevel() {
        level++;
        thread.stop();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (level < fragmentList.size()) {
                    setFragment();
                } else {
                    Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                    intent.putExtra("score1", scorePlayer1.getText());
                    intent.putExtra("score2", scorePlayer2.getText());
                    startActivity(intent);
                }
            }
        }, 800);
    }

    @Override
    public void run() {
        try {
            while (!thread.isStopped()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!thread.isPaused())
                            getFragment().setView(textPlayer1, textPlayer2);
                    }
                });

                if (!thread.isPaused()) {
                    Thread.sleep(millis);
                }
            }
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        thread.pause();

        switch (v.getId()) {
            case R.id.player1:
                getFragment().touchPanel(player1, textPlayer1, scorePlayer1, event);
                break;
            case R.id.player2:
                getFragment().touchPanel(player2, textPlayer2, scorePlayer2, event);
                break;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setDefaultAttr();
                thread.resume();
            }
        }, 1500);

        return false;
    }

}
