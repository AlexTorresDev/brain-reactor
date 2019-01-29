package co.edu.udistrital.brainreactor.levels;

import android.graphics.Color;
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
import co.edu.udistrital.brainreactor.Colors;
import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.Thread;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class ColorsFragment extends Fragment implements Level, Runnable {

    private List<Colors> COLORS;
    private TextView game1, game2;
    private int num1, num2, oldNum1, oldNum2, localScore;
    private Thread thread;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);

        initComponents(view);

        return view;
    }

    private void initComponents(View v) {
        localScore = 0;

        COLORS = new ArrayList<>();
        listColors();

        game1 = v.findViewById(R.id.game1);
        game2 = v.findViewById(R.id.game2);

        oldNum1 = -1;
        oldNum2 = -1;

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
                        do {
                            num1 = new Random().nextInt(COLORS.size());
                        } while(num1 == oldNum1);

                        oldNum1 = num1;

                        do {
                            num2 = new Random().nextInt(COLORS.size());
                        } while(num2 == oldNum2);

                        oldNum2 = num2;

                        game1.setText(COLORS.get(num1).getName());
                        game1.setTextColor(Color.parseColor(COLORS.get(num2).getColor()));

                        game2.setText(COLORS.get(num1).getName());
                        game2.setTextColor(Color.parseColor(COLORS.get(num2).getColor()));
                    }
                });

                if (!thread.isPaused()) {
                    Thread.sleep(2000);
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

    private void listColors() {
        int length = Objects.requireNonNull(getContext()).getResources().getStringArray(R.array.colors).length;

        for (int i = 0; i < length; i++) {
            COLORS.add(new Colors(
                Objects.requireNonNull(getContext()).getResources().getStringArray(R.array.colors_name)[i],
                Objects.requireNonNull(getContext()).getResources().getStringArray(R.array.colors)[i]
            ));
        }
    }

    class Colors {
    
        private String name, color;
    
        private Colors(String name, String color) {
            this.name = name;
            this.color = color;
        }
    
        public String getName() {
            return name;
        }
    
        public String getColor() {
            return color;
        }
    }
}
