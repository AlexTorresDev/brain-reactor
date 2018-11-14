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

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.animation.Animations;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class OperationsFragment extends Fragment implements Level {

    private List<Operation> OPERATIONS = new ArrayList<>();
    private TextView game1, game2;

    private int result, localScore;
    private String operation, sign;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);

        initComponents(view);

        return view;
    }

    private void initComponents(View v) {
        localScore = 0;
        listOperations();

        game1 = v.findViewById(R.id.game1);
        game2 = v.findViewById(R.id.game2);

        GameActivity.millis = 4000;
        if(GameActivity.thread.isStopped()) {
            GameActivity.thread.start();
        } else {
            GameActivity.thread.resume();
        }
    }

    private void setView() {
        int randomSign = new Random().nextInt(3);

        operation = OPERATIONS.get(new Random().nextInt(OPERATIONS.size())).getOperation();
        sign = (randomSign == 1) ? " = " : (randomSign == 2) ? " < " : " > ";
        result = OPERATIONS.get(new Random().nextInt(OPERATIONS.size())).getResult();

        game1.setText(String.valueOf(operation + sign + result));
        game2.setText(String.valueOf(operation + sign + result));
    }

    @Override
    public void touchPanel(CardView player, TextView text, TextView score, MotionEvent event) {
        int background = (isCorrect()) ? R.color.colorSuccess : R.color.colorWrong;
        int message = (isCorrect()) ? R.string.success : R.string.wrong;
        int s = Integer.parseInt(score.getText().toString());

        //new Animations(getContext()).startAnimationTo(player, background, (int) event.getRawX(), (int) event.getRawY());
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
    public void setView(TextView p1, TextView p2) {
        setView();
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
