package co.edu.udistrital.brainreactor.levels;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class TimerFragment extends Fragment implements Level {

    private TextView game1, game2;
    private int randomTime, localScore;
    private double millis;
    private CountDownTimer countDownTimer;

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
        countDown();
    }

    @Override
    public void onPause() {
        countDownTimer.cancel();
        super.onPause();
    }

    @Override
    public void onResume() {
        countDown();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    private void countDown() {

        countDownTimer = new CountDownTimer(11000, 1000) {

            public void onTick(long millisUntilFinished) {
                millis = millisUntilFinished;

                if (millisUntilFinished < 0) {
                    setTime();
                    countDownTimer.cancel();
                    countDown();
                }

                if (millisUntilFinished >= randomTime) {
                    game1.setText(String.format("%2s", millisUntilFinished / 1000));
                    game2.setText(String.format("%2s", millisUntilFinished / 1000));
                } else {
                    game1.setText("?");
                    game2.setText("?");
                }

            }

            public void onFinish() {
                setTime();
                countDownTimer.cancel();
                countDown();
            }
        }.start();

    }

    @Override
    public void touchPanel(CardView player, TextView text, TextView score) {
        int background = (millis / 1000 == 0) ? R.color.colorSuccess : R.color.colorWrong;
        String message = (millis / 1000 == 0) ? getContext().getResources().getString(R.string.success) : getContext().getResources().getString(R.string.wrong);
        int s = Integer.parseInt(score.getText().toString());

        player.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), background));
        text.setText(message + "\nSe presionó faltando " + millis / 1000 + " segundos");

        if (millis == 0) {
            localScore++;
            s++;
        } else {
            s--;
        }

        score.setText(String.valueOf(s));

        if (localScore == 2) {
            ((GameActivity) Objects.requireNonNull(getActivity())).nextLevel();
        } else {
            setTime();
            countDownTimer.cancel();
            countDown();
        }

    }

    @Override
    public int getMessage() {
        return R.string.timer_message;
    }

    private void setTime() {
        millis = 10;
        randomTime = (new Random().nextInt(7) + 4) * 1000;
    }
}
