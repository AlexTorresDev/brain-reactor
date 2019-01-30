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

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.Thread;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class OperationsFragment extends Fragment implements Level, Runnable {

    private List<Operation> OPERATIONS;
    private TextView game1, game2;
    private int result, localScore;
    private String operation, sign;
    private Thread thread;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);

        initComponents(view);

        return view;
    }

    private void initComponents(View v) {
        localScore = 0;

        OPERATIONS = new ArrayList<>();
        listOperations();

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
                        int randomSign = new Random().nextInt(3);

                        operation = OPERATIONS.get(new Random().nextInt(OPERATIONS.size())).getOperation();
                        sign = (randomSign == 1) ? " = " : (randomSign == 2) ? " < " : " > ";
                        result = OPERATIONS.get(new Random().nextInt(OPERATIONS.size())).getResult();

                        game1.setText(String.valueOf(operation + sign + result));
                        game2.setText(String.valueOf(operation + sign + result));
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
        int background = (isCorrect()) ? R.color.colorSuccess : R.color.colorWrong;
        int message = (isCorrect()) ? R.string.success : R.string.wrong;
        int s = Integer.parseInt(score.getText().toString());

        player.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), background));
        text.setText(message);

        if (isCorrect()) {
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

    private boolean isCorrect() {
        int r = 0;

        try {
            r = Integer.parseInt(new ScriptEngineManager().getEngineByName("rhino").eval(operation).toString().replace(".0",""));
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        switch (sign) {
            case " = ":
                return r == result;
            case " < ":
                return r < result;
            default:
                return r > result;
        }

    }

    @Override
    public int getMessage() {
        return R.string.operation_message;
    }

    private void listOperations() {
        OPERATIONS.add(new Operation("4*3", 10));
        OPERATIONS.add(new Operation("3+(6/2)", 6));
        OPERATIONS.add(new Operation("14/7", 29));
        OPERATIONS.add(new Operation("6+2*5", 15));
        OPERATIONS.add(new Operation("3-2", 1));
        OPERATIONS.add(new Operation("5-7", -2));
        OPERATIONS.add(new Operation("12/3*2", 2));
        OPERATIONS.add(new Operation("(5*4)/2", 25));
        OPERATIONS.add(new Operation("4*4*2", 13));
        OPERATIONS.add(new Operation("2*2*2*2", 8));
    }

    class Operation {
        private String operation;
        private int result;

        Operation(String operation, int result) {
            this.operation = operation;
            this.result = result;
        }

        String getOperation() {
            return operation;
        }

        int getResult() {
            return result;
        }
    }
}
