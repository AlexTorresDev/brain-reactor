package co.edu.udistrital.brainreactor.levels;

import android.graphics.Color;
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

import co.edu.udistrital.brainreactor.Colors;
import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.animation.Animations;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class ColorsFragment extends Fragment implements Level {

    private Colors colors;
    private TextView game1, game2;
    private int num1, num2, localScore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);

        initComponents(view);

        return view;
    }

    private void initComponents(View v) {
        localScore = 0;

        colors = new Colors(getContext());
        colors.listColors();

        game1 = v.findViewById(R.id.game1);
        game2 = v.findViewById(R.id.game2);

        GameActivity.millis = 3000;
        if(GameActivity.thread.isStopped()) {
            GameActivity.thread.start();
        } else {
            GameActivity.thread.resume();
        }

    }

    private void setView() {
        num1 = new Random().nextInt(colors.COLORS.size());
        num2 = new Random().nextInt(colors.COLORS.size());

        game1.setText(colors.COLORS.get(num1).getName());
        game1.setTextColor(Color.parseColor(colors.COLORS.get(num2).getColor()));

        game2.setText(colors.COLORS.get(num1).getName());
        game2.setTextColor(Color.parseColor(colors.COLORS.get(num2).getColor()));
    }

    @Override
    public void touchPanel(CardView player, TextView text, TextView score, MotionEvent event) {
        int background = (num1 == num2) ? R.color.colorSuccess : R.color.colorWrong;
        int message = (num1 == num2) ? R.string.success : R.string.wrong;
        int s = Integer.parseInt(score.getText().toString());

        //new Animations(getContext()).startAnimationTo(player, background, (int) event.getRawX(), (int) event.getRawY());
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
    public void setView(TextView p1, TextView p2) {
        setView();
    }

    @Override
    public int getMessage() {
        return R.string.color_message;
    }
}
