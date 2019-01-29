package co.edu.udistrital.brainreactor.levels;

import androidx.cardview.widget.CardView;
import co.edu.udistrital.brainreactor.animation.Animation;

import android.animation.Animator;
import android.view.MotionEvent;
import android.widget.TextView;

public interface Level {

    void touchPanel(CardView player, TextView text, TextView score);

    int getMessage();
}
