package co.edu.udistrital.brainreactor.animation;

import android.animation.Animator;
import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

public class Animations {

    private Context context;

    public Animations(Context context) {
        this.context = context;
    }

    public void startAnimation(ViewGroup viewRoot, @ColorRes int color) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        startAnimationTo(viewRoot, color, cx, cy);
    }

    public void startAnimationTo(ViewGroup viewRoot, @ColorRes int color, int x, int y) {
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(context, color));
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }
}
