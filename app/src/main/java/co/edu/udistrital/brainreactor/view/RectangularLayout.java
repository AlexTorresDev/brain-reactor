package co.edu.udistrital.brainreactor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RectangularLayout extends LinearLayout {

    public RectangularLayout(Context context) {
        super(context);
    }

    public RectangularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangularLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size / 2);
    }
}
