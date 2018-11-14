package co.edu.udistrital.brainreactor.levels;

import androidx.cardview.widget.CardView;
import android.view.MotionEvent;
import android.widget.TextView;

public interface Level {

    void setView(TextView p1, TextView p2);

    void touchPanel(CardView player, TextView text, TextView score, MotionEvent event);

    int getMessage();
}
