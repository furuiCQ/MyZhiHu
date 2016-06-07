package com.rainism.furui.myzhihu.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ContentWebView extends WebView {
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
        canvas.save();
        if(mTitleBar != null) {
            int sy = getScrollY();//返回滚动顶部位置
            int sx = getScrollX();//返回滚动左边缘的位置
            mClipBounds.top = sy;
            mClipBounds.left = sx;
            mClipBounds.right = mClipBounds.left + getWidth();//获取控件的宽度
            mClipBounds.bottom = mClipBounds.top + getHeight();//获取控件的高度
            mMatrix.set(canvas.getMatrix());//设置缩放
            Log.d("-getScrollY()",-getScrollY()+"");
            int titleBarOffs = mTitleBar.getHeight() - sy;
            if(titleBarOffs < 0){
                titleBarOffs = 0;
            }
            mMatrix.postTranslate(0, titleBarOffs);
            canvas.setMatrix(mMatrix);
            canvas.clipRect(mClipBounds);//以上下左右画一个矩形

        }
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if(child == mTitleBar) {
            mClipBounds.top = getScrollY();
            mClipBounds.left = 0;
            mClipBounds.right = mClipBounds.left + child.getWidth();
            mClipBounds.bottom = child.getHeight();
            canvas.save();
            child.setDrawingCacheEnabled(true);
            mMatrix.set(canvas.getMatrix());
            int titleBarOffs=-getScrollY();
            mMatrix.postTranslate(getScrollX(), titleBarOffs);
            canvas.setMatrix(mMatrix);
            canvas.clipRect(mClipBounds);
            child.draw(canvas);
            canvas.restore();
            return false;
        }
        return super.drawChild(canvas, child, drawingTime);
    }


}