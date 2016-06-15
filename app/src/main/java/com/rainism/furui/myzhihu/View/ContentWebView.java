package com.rainism.furui.myzhihu.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.lzy.widget.HeaderScrollHelper.ScrollableContainer;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ContentWebView extends WebView implements ScrollableContainer{
   // https://github.com/jeasonlzy0216/HeaderViewPager 参考
    private View mTitleBar;
    private RelativeLayout.LayoutParams mTitleBarLayoutParams;
    private Matrix mMatrix = new Matrix();
    private Rect mClipBounds = new Rect();//矩形

    public ContentWebView(Context context) {
        super(context);

    }

    public ContentWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEmbeddedTitleBar(View v) {
        if(mTitleBar == v) return;
        if(mTitleBar != null) {
            removeView(mTitleBar);
        }
        if(null != v) {
            mTitleBarLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    500);
            addView(v, mTitleBarLayoutParams);
            setInitialScale(100);
        }
        mTitleBar = v;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {

        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    public View getScrollableView() {
        return this;
    }
}