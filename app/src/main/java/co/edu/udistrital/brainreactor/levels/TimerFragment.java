package co.edu.udistrital.brainreactor.levels;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.Thread;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class TimerFragment extends Fragment implements Level, Runnable {

    private TextView game1, game2;
    private int seconds, randomTime, localScore;
    private Thread thread;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);

        initComponents(view);

        return view;
    }

    private void initComponents(View v) {
        localScore = 0;

        game1 = v.findViewById(R.id.game1);
        game2 = v.findViewById(R.id.game2);

        setTime();

        thread = new Thread(this);
    }

    @Override
    public void onPause() {
        thread.stop();
        super.onPause();
    }

    @Override
    public void onResume() {
        thread.start();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        thread.stop();
        super.onDestroy();
    }

    @Override
    public void run() {
        try {
            while (!thread.isStopped()) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!thread.isPaused())
                            if (seconds < 0) {
                                thread.pause();
                                setTime();
                                thread.resume();
                            }

                        if(seconds >= randomTime) {
                            game1.setText(String.format("%2s", seconds));
                            game2.setText(String.format("%2s", seconds));
                        } else {
                            game1.setText("?");
                            game2.setText("?");
                        }

                        seconds--;
                    }
                });

                if (!thread.isPaused()) {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void touchPanel(CardView player, TextView text, TextView score) {
        int background = (seconds == 0) ? R.color.colorSuccess : R.color.colorWrong;
        int message = (seconds == 0) ? R.string.success : R.string.wrong;
        int s = Integer.parseInt(score.getText().toString());

        player.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), background));
        text.setText(message);

        if (seconds == 0) {
            localScore++;
            s++;
        } else {
            localScore--;
            s--;
        }

        score.setText(String.valueOf(s));

        if (localScore == 3) {
            ((GameActivity) Objects.requireNonNull(getActivity())).nextLevel();
        } else {
            thread.pause();
            setTime();
            thread.resume();
        }

    }

    @Override
    public int getMessage() {
        return R.string.timer_message;
    }

    private void setTime() {
        seconds = 10;
        randomTime = new Random().nextInt(7) + 4;
    }
}
