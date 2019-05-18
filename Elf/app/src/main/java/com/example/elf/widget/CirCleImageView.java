package com.example.elf.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CirCleImageView extends ImageView {
    protected Path clipPath = new Path();
    protected RectF mRectF = new RectF();

    public CirCleImageView(Context context) {
        super(context);
    }

    public CirCleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirCleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRectF.set(0, 0, getWidth(), getHeight());
        clipPath.reset();
        clipPath.addOval(mRectF, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}