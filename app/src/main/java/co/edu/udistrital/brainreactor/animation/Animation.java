package co.edu.udistrital.brainreactor.animation;

import android.animation.Animator;
import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

public class Animation {

    private Context context;

    public Animation(Context context) {
        this.context = context;
    }

    public Animator createAnimation(ViewGroup viewRoot, @ColorRes int color) {
        int x = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int y = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

        Animator animator = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(context, color));
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        
        return animator;
    }

}
