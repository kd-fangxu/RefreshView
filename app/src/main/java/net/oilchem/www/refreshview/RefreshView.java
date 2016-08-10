package net.oilchem.www.refreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by developer on 16/8/9.
 */
public class RefreshView extends FrameLayout {
    public double obstruction = 1.8;
    public int HeaderHeight = 110;
    public int FooterHeight = 110;
    Scroller scroller;
    public RelativeLayout HeaderView, FooterView;

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
        HeaderView = (RelativeLayout) View.inflate(getContext(), R.layout.view_header, null);
        FooterView = (RelativeLayout) View.inflate(getContext(), R.layout.view_footer, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(HeaderView, params);
        addView(FooterView, params);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

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
                    if (getScrollY() < 0)//下拉操作
                    {
                        HeaderView.layout(HeaderView.getLeft(), getScrollY(), HeaderView.getRight(), getScrollY() + HeaderView.getHeight());
                    } else {
                        FooterView.layout(FooterView.getLeft(), getMeasuredHeight() - FooterHeight + getScrollY(), FooterView.getRight(), getMeasuredHeight() + getScrollY());
                    }

                    downY = event.getRawY();
                }
//                Log.e("move", getScrollY() + "");


                break;
            case MotionEvent.ACTION_UP:
                if (getScrollY() <= -HeaderHeight) {//满足刷新条件
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollClose();
                        }
                    }, 1000);

                } else {
                    smoothScrollClose();
                }

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
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();

        }

        super.computeScroll();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        HeaderHeight=HeaderView.getHeight();
        FooterHeight=FooterView.getHeight();

    }


}
