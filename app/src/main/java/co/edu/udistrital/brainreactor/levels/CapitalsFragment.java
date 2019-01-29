package co.edu.udistrital.brainreactor.levels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.Thread;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class CapitalsFragment extends Fragment implements Level, Runnable {

    private List<Capital> CAPITALS;
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

        CAPITALS = new ArrayList<>();
        listCapitals();

        game1 = v.findViewById(R.id.game1);
        game2 = v.findViewById(R.id.game2);

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
                        num1 = new Random().nextInt(CAPITALS.size());
                        num2 = new Random().nextInt(CAPITALS.size());

                        game1.setText(getResources().getString(R.string.capital_text, CAPITALS.get(num1).getCountry(), CAPITALS.get(num2).getCapital()));
                        game2.setText(getResources().getString(R.string.capital_text, CAPITALS.get(num1).getCountry(), CAPITALS.get(num2).getCapital()));
                    }
                });

                if (!thread.isPaused()) {
                    Thread.sleep(3000);
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
