package net.oilchem.www.refreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by developer on 16/8/9.
 */
public class RefreshView extends FrameLayout {
    public double obstruction = 1.8;
    public int HeaderHeight = 150;
    public int FooterHeight = 100;
    Scroller scroller;

    public RefreshView(Context context) {
        super(context);
        init();
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

        scroller = new Scroller(getContext());
    }

    float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.e("DOWN", event.getRawY() + "");
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() <= FooterHeight && getScrollY() >= -HeaderHeight) {
                    float lastY = event.getRawY();
                    scrollBy(0, (int) ((downY - lastY) / obstruction));
                    downY = event.getRawY();
                }
//                Log.e("move", getScrollY() + "");



                break;
            case MotionEvent.ACTION_UP:
                smoothScrollClose();
//                smoothScrollOpen();
                break;
        }
        return true;
    }

    private void smoothScrollClose() {
        scroller.startScroll(0, getScrollY(), 0, -getScrollY(), 1000);
        invalidate();

    }

    private void smoothScrollOpen() {
        scroller.startScroll(0, 0, 0, -HeaderHeight, 1000);
        invalidate();

    }

    @Override
    public void computeScroll() {

        if (scroller.computeScrollOffset()) {
            Log.e("EVEVT", "computeScroll===" + scroller.getCurrY());
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();

        }

        super.computeScroll();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


}
