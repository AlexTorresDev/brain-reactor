package co.edu.udistrital.brainreactor.levels;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.animation.Animations;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class TimerFragment extends Fragment implements Level {

    private TextView game1, game2;
    private int seconds, randomTime, localScore;

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

        GameActivity.millis = 1000;
        if(GameActivity.thread.isStopped()) {
            GameActivity.thread.start();
        } else {
            GameActivity.thread.resume();
        }
    }

    private void setView() {
        if (seconds == -1) {
            GameActivity.thread.pause();
            setTime();
            GameActivity.thread.resume();
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

    @Override
    public void touchPanel(CardView player, TextView text, TextView score, MotionEvent event) {
        int background = (seconds == 0) ? R.color.colorSuccess : R.color.colorWrong;
        int message = (seconds == 0) ? R.string.success : R.string.wrong;
        int s = Integer.parseInt(score.getText().toString());

        new Animations(getContext()).startAnimationTo(player, background, (int) event.getRawX(), (int) event.getRawY());
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
            GameActivity.thread.pause();
            setTime();
            GameActivity.thread.resume();
        }

    }

    @Override
    public void setView(TextView p1, TextView p2) {
        setView();
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
