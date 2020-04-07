package com.example.myapplicationfacedetectionfirebasemlkit.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;


public class RectOverlay extends  GraphicOverlay.Graphic{
    private int mRectColor = Color.GREEN;
    private float mStrokeWidth = 4.0f;
    private Paint mRectPain;
    private GraphicOverlay graphicOverlay;
    private Rect rect;

    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);

        mRectPain  = new Paint();
        mRectPain.setColor(mRectColor);
        mRectPain.setStyle(Paint.Style.STROKE);
        mRectPain.setStrokeWidth(mStrokeWidth);

        this.graphicOverlay = graphicOverlay;
        this.rect = rect;

        graphicOverlay.clear();
        graphicOverlay.setElevation(50F);

        postInvalidate();

    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectF = new RectF(rect);
        rectF.left = translateX(rectF.left);
        rectF.right = translateX(rectF.right);
        rectF.top = translateX(rectF.top);
        rectF.bottom = translateX(rectF.bottom);

        canvas.drawRect(rectF, mRectPain);


    }
}
