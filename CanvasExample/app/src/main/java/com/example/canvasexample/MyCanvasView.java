package com.example.canvasexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Path;

import androidx.core.content.res.ResourcesCompat;

public class MyCanvasView extends View {
    private Paint mPaint;
    private Path mPath;
    private int mDrawColor;
    private int mBackgroundColor;
    private Canvas mExtraCanvas;
    private Bitmap mExtraBitmap;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    private Rect mFrame;

    MyCanvasView(Context context){
        this(context, null);
    }

    public MyCanvasView(Context context, AttributeSet attributeSet){
        super(context);

        mBackgroundColor = ResourcesCompat.getColor(getResources(), R.color.opaque_orange, null);
        mDrawColor = ResourcesCompat.getColor(getResources(), R.color.opaque_yellow, null);

        //Holds the path we are currently drawing
        mPath = new Path();

        //Set up the paint with which to draw
        mPaint = new Paint();
        mPaint.setColor(mDrawColor);

        //Smooths  out edges of what is drawn without affecting shape
        mPaint.setAntiAlias(true);

        /*Dithering affects how colors with higher precision device
        * than they are down-sampled
        */
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE); //Default: FILL
        mPaint.setStrokeJoin(Paint.Join.ROUND); //default: miter
        mPaint.setStrokeCap(Paint.Cap.ROUND);//Default: BUTT
        mPaint.setStrokeWidth(12);//default: Hairline-width (really thin)
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight){
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        //Create bitmap, create canvas with bitmap, fill canvas with color.
        mExtraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mExtraCanvas = new Canvas(mExtraBitmap);

        //Fill the bitmap with the background color.
        mExtraCanvas.drawColor(mBackgroundColor);

        //Calculate the rect a frame around the picture
        int inset = 40;
        mFrame = new Rect(inset, inset, (width - inset), (height - inset));
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        /*
        * Draw the bitmap that stores the path the user has drawn.
        * Initially the user has not draw anything
        * so we see only the colored bitmap.*/
        canvas.drawBitmap(mExtraBitmap, 0, 0, null);

        //Draw the frame around the picture
        canvas.drawRect(mFrame, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        /*
        Invalidate() is inside the case statements because there are many
        other types of motion events passed into this listener,
        and we don't want to invalidate the view for those.
        * */

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                //No need to invalidate because we are not drawing anythong
                break;

            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                touchUp();
                //No need to invalidate because we are not drawing anything
                break;

            default:
                //do nothing
        }
        return true;
    }

    private void touchStart(float x, float y){
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(x - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            //QuadTo() adds a quadratic bezier from the last point,
            //approaching control point (x1, y1), and ending at (x2, y2).
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);

            //Reset mX and mY to the last drawn point
            mX = x;
            mY = y;

            //Save the path in the extra bitmap
            //Which we access through its canvas
            mExtraCanvas.drawPath(mPath, mPaint);
        }
    }

    private void touchUp(){
        //Reset the path so that it doesn't get drawn again
        mPath.reset();
    }
}
