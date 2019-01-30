package co.edu.udistrital.brainreactor.levels;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.Thread;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class ColorsFragment extends Fragment implements Level, Runnable {

    private String[] colors, names;
    private TextView game1, game2;
    private int num1, num2, localScore;
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

        colors = Objects.requireNonNull(getContext()).getResources().getStringArray(R.array.colors);
        names = Objects.requireNonNull(getContext()).getResources().getStringArray(R.array.colors_name);

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
                        if (!new Random().nextBoolean()) {
                            num1 = new Random().nextInt(names.length);
                            num2 = new Random().nextInt(names.length);
                        } else  {
                            num1 = num2 = new Random().nextInt(names.length);
                        }

                        game1.setText(names[num1]);
                        game1.setTextColor(Color.parseColor(colors[num2]));

                        game2.setText(names[num1]);
                        game2.setTextColor(Color.parseColor(colors[num2]));
                    }
                });

                if (!thread.isPaused()) {
                    Thread.sleep(900);
                }
            }
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void touchPanel(CardView player, TextView text, TextView score) {
        int background = (num1 == num2) ? R.color.colorSuccess : R.color.colorWrong;
        int message = (num1 == num2) ? R.string.success : R.string.wrong;
        int s = Integer.parseInt(score.getText().toString());

        player.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), background));
        text.setText(message);

        if (num1 == num2) {
            localScore++;
            s++;
        } else {
            localScore--;
            s--;
        }

        score.setText(String.valueOf(s));

        if (localScore == 3) {
            ((GameActivity) Objects.requireNonNull(getActivity())).nextLevel();
        }
    }

    @Override
    public int getMessage() {
        return R.string.color_message;
    }

}
