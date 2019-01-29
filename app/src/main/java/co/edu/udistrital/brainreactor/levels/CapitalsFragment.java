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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.animation.Animations;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class CapitalsFragment extends Fragment implements Level {

    private List<Capital> CAPITALS = new ArrayList<>();
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
        listCapitals();

        game1 = v.findViewById(R.id.game1);
        game2 = v.findViewById(R.id.game2);

        GameActivity.millis = 3000;
        if(GameActivity.thread.isStopped()) {
            GameActivity.thread.start();
        } else {
            GameActivity.thread.resume();
        }
    }

    @Override
    public void setView(TextView p1, TextView p2) {
        num1 = new Random().nextInt(CAPITALS.size());
        num2 = new Random().nextInt(CAPITALS.size());

        game1.setText(getResources().getString(R.string.capital_text, CAPITALS.get(num1).getCountry(), CAPITALS.get(num2).getCapital()));
        game2.setText(getResources().getString(R.string.capital_text, CAPITALS.get(num1).getCountry(), CAPITALS.get(num2).getCapital()));
    }

    @Override
    public void touchPanel(CardView player, TextView text, TextView score, MotionEvent event) {
        int background = (num1 == num2) ? R.color.colorSuccess : R.color.colorWrong;
        int message = (num1 == num2) ? R.string.success : R.string.wrong;
        int s = Integer.parseInt(score.getText().toString());

        new Animations(getContext()).startAnimationTo(player, background, (int) event.getRawX(), (int) event.getRawY());
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
        return R.string.capital_message;
    }

    private void listCapitals() {
        CAPITALS.add(new Capital("Colombia", "Bogota"));
        CAPITALS.add(new Capital("EE.UU", "Washington D. C"));
        CAPITALS.add(new Capital("Rusia", "Moscú"));
        CAPITALS.add(new Capital("Alemania", "Berlín"));
        CAPITALS.add(new Capital("China", "Pekín"));
        CAPITALS.add(new Capital("Costa Rica", "San José"));
        CAPITALS.add(new Capital("Líbano", "Beirut"));
        CAPITALS.add(new Capital("Países Bajos", "Ámsterdam"));
        CAPITALS.add(new Capital("Suiza", "Berna"));
    }


    class Capital {
        private String country;
        private String capital;

        Capital(String country, String capital) {
            this.country = country;
            this.capital = capital;
        }

        String getCountry() {
            return this.country;
        }

        String getCapital() {
            return this.capital;
        }
    }
}
