package co.edu.udistrital.brainreactor.levels;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.util.Log;
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
import co.edu.udistrital.brainreactor.view.RectangularLayout;

public class BackgroundFragment extends Fragment implements Level {

    private Colors colors;
    private RectangularLayout rectangular1, rectangular2;
    private int color, localScore;

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

        rectangular1 = v.findViewById(R.id.rectangular1);
        rectangular2 = v.findViewById(R.id.rectangular2);

        View separator = v.findViewById(R.id.separator_fragment);
        separator.setVisibility(View.GONE);
        separator.setLayoutParams(new LayoutParams(0, 0));

        GameActivity.millis = 5000;

        if(GameActivity.thread.isStopped()) {
            GameActivity.thread.start();
        } else {
            GameActivity.thread.resume();
        }
    }

    @Override
    public void setView(TextView p1, TextView p2) {
        color = new Random().nextInt(colors.COLORS.size());
        
        p1.setText(getResources().getString(R.string.background_message, colors.COLORS.get(color).getName()));
        p2.setText(getResources().getString(R.string.background_message, colors.COLORS.get(color).getName()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rectangular1.setBackgroundColor(Color.parseColor(colors.COLORS.get(color).getColor()));
                rectangular2.setBackgroundColor(Color.parseColor(colors.COLORS.get(color).getColor()));
                GameActivity.thread.pause();
            }
        }, (long) new Random().nextInt(4000) + 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rectangular1.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPanel));
                rectangular2.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPanel));
                GameActivity.thread.resume();
            }
        }, (long) new Random().nextInt(1000));
    }

    @Override
    public void touchPanel(CardView player, TextView text, TextView score, MotionEvent event) {
        Log.e("Hola", rectangular1.getBackground().toString());
        int background = (Integer.parseInt(player.getBackground().toString()) == color) ? R.color.colorSuccess : R.color.colorWrong;
        int message = (Integer.parseInt(player.getBackground().toString()) == color) ? R.string.success : R.string.wrong;
        int s = Integer.parseInt(score.getText().toString());

        new Animations(getContext()).startAnimationTo(player, background, (int) event.getRawX(), (int) event.getRawY());
        text.setText(message);

        if (Integer.parseInt(player.getBackground().toString()) == color) {
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
        return R.string.background_message;
    }
}
