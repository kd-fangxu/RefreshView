package net.oilchem.www.refreshview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    View mTarget;

    private View getTargetView() {

        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!(child.equals(HeaderView) || child.equals(FooterView))) {
                    mTarget = child;
                    break;
                }
            }
        }
        return mTarget;
    }


//    /**
//     * @return Whether it is possible for the child view of this layout to
//     * scroll up. Override this if the child view is a custom view.
//     */
//    public boolean canChildScrollUp(View mTarget) {
//        if (android.os.Build.VERSION.SDK_INT < 14) {
//            if (mTarget instanceof AbsListView) {
//                final AbsListView absListView = (AbsListView) mTarget;
//                return absListView.getChildCount() > 0
//                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
//                        .getTop() < absListView.getPaddingTop());
//            } else {
//                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
//            }
//        } else {
//            return ViewCompat.canScrollVertically(mTarget, -1);
//        }
//    }

    Float downYIntercept;
    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("Event", "actionDown");
            downY = e.getRawY();
            downYIntercept = e.getRawY();
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            boolean canMoveup = ViewCompat.canScrollVertically(getTargetView(), -1);
            boolean canMoveDown = ViewCompat.canScrollVertically(getTargetView(), 1);
            if (e2.getRawY() - e1.getRawY() > 0) {//下滑
                Log.e("move", "手指下滑界面上滑");
                if (canMoveDown && !canMoveup && refreshListener != null) {//界面上滑到最顶部
                    return true;
                }
                if (!canMoveDown && canMoveup) {
                    return false;
                }
            } else {

                Log.e("move", "手指上滑界面下滑");
                if (!canMoveup) {
                    return false;
                }
                if (!canMoveDown && canMoveup && loadListener != null) {//界面下滑到最底部
                    return true;
                }
            }
            if (canMoveDown && canMoveup) {
                return false;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    });

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

//        switch (ev.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//
//
//            case MotionEvent.ACTION_MOVE:
//                boolean canMoveup = ViewCompat.canScrollVertically(getTargetView(), -1);
//                boolean canMoveDown = ViewCompat.canScrollVertically(getTargetView(), 1);
//                Log.e("Move", "可以下滑:" + canMoveDown + "\n可以上滑动" + canMoveup);
//
//                if (ev.getRawY() - downYIntercept > 0) {//手指下滑
//                    Log.e("move", "下滑");
//                    if (canMoveDown && !canMoveup) {
//                        return true;
//                    }
//                    if (!canMoveDown && canMoveup) {
//                        return false;
//                    }
//
//                } else {
//                    Log.e("move", "上滑");
//                    if (!canMoveup) {
//                        return false;
//                    }
//                    if (!canMoveDown && canMoveup) {
//                        return true;
//                    }
//                }
//                if (canMoveDown && canMoveup) {
//                    return false;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }

        return gestureDetector.onTouchEvent(ev);//默认拦截
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.e("DOWN", event.getRawY() + "");
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isRefreshing && !isLoading) {
                    if (getScrollY() <= FooterHeight && getScrollY() >= -HeaderHeight) {
                        float lastY = event.getRawY();
                        scrollBy(0, (int) ((downY - lastY) / obstruction));
                        layoutHeaderOrFooter();
                        downY = event.getRawY();
                    }
                }

//
                break;
            case MotionEvent.ACTION_UP:

                if (getScrollY() <= -HeaderHeight) {//满足刷新条件
                    isActivedByTouch = true;
                    setRefreshing(true);
                    return true;
                }

                if (getScrollY() >= FooterHeight) {//满足load条件
                    isActivedByTouch = true;
                    setLoading(true);
                    return true;
                }
                smoothScrollClose();
//                smoothScrollOpen();
                break;
        }
        return true;
    }

    private void layoutHeaderOrFooter() {
        if (getScrollY() <= 0)//下拉操作
        {
            if (refreshListener != null) {
                HeaderView.layout(HeaderView.getLeft(), getScrollY(), HeaderView.getRight(), getScrollY() + HeaderView.getHeight());
            }

        } else {
            if (loadListener != null) {
                FooterView.layout(FooterView.getLeft(), getMeasuredHeight() - FooterHeight + getScrollY(), FooterView.getRight(), getMeasuredHeight() + getScrollY());
            }

        }
    }


    private void smoothScrollClose() {
        isRefreshing = false;
        isLoading = false;
        scroller.startScroll(0, getScrollY(), 0, -getScrollY(), 800);
        invalidate();

    }

    private void smoothScrollOpenHeader() {
        scroller.startScroll(0, 0, 0, -HeaderHeight, 800);
        invalidate();

    }

    private void smoothScrollOpenFooter() {
        scroller.startScroll(0, 0, 0, FooterHeight, 800);
        invalidate();

    }

    @Override
    public void computeScroll() {

        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            layoutHeaderOrFooter();
            postInvalidate();

        }

        super.computeScroll();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        HeaderHeight = HeaderView.getHeight();
        FooterHeight = FooterView.getHeight();
        layoutHeaderOrFooter();

    }

    boolean isActivedByTouch = false;//是否是用户操作
    boolean isRefreshing = false;
    boolean isLoading = false;


    private onRefreshListener refreshListener;
    private onLoadListener loadListener;

    public void setRefreshing(boolean isRefresh) {
        if (isRefresh) {
            if (refreshListener == null) {
                return;
            }

            if (!isRefreshing && !isLoading) {
                if (isActivedByTouch) {//用户下滑激活刷新
                    doRefresh();
                    return;
                }
                smoothScrollOpenHeader();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doRefresh();
                    }
                }, 800);


            }
        } else {
            if (isRefreshing) {
                smoothScrollClose();
            }
        }
    }

    public void setLoading(boolean isLoad) {
        if (loadListener == null) {
            return;
        }
        if (isLoad) {
            if (!isRefreshing && !isLoading) {
                if (isActivedByTouch) {
                    doLoad();
                    return;
                }
                smoothScrollOpenFooter();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doLoad();
                    }
                }, 800);
            }
        } else {
            if (isLoading) {
                smoothScrollClose();
            }
        }
    }


    private void doRefresh() {
        isActivedByTouch = false;
        isRefreshing = true;
        if (refreshListener != null) {
            refreshListener.onRefresh(RefreshView.this);
            return;
        }


    }

    private void doLoad() {
        isActivedByTouch = false;
        isLoading = true;
        if (loadListener != null) {
            loadListener.onLoad(this);
            return;
        }
    }


    public void setRefreshListener(onRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void setLoadListener(onLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    interface onRefreshListener {
        void onRefresh(RefreshView refreshView);
    }

    interface onLoadListener {
        void onLoad(RefreshView refreshView);
    }

}
