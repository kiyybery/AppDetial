package com.xyn.appdetial.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class ImageViewWithGesture extends ImageView {

    private static final String TAG = ImageViewWithGesture.class.getSimpleName();

    public void setGestureDetector(Context context, GestureDetector.OnGestureListener gestureListener) {
        gestureDetector = new GestureDetector(context, gestureListener);
    }

    GestureDetector gestureDetector;

    public ImageViewWithGesture(Context context) {
        super(context);
    }

    public ImageViewWithGesture(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i(TAG, "x=" + getCurrentTouchX() + ",y=" + getCurrentTouchY());
        if (gestureDetector == null) {
//            Log.w(TAG, "gestureDetector == null");
            return super.onTouchEvent(event);
        }

//        setCurrentTouchXY(event.getX(),event.getY());
        return gestureDetector.onTouchEvent(event);
    }
}
