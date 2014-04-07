package com.comp3111.ui;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

@SuppressLint("NewApi")
public class ExpandAnimation extends Animation {

    public final static int COLLAPSE = 1;
    public final static int EXPAND = 0;

    private View mView;
    private int mEndHeight;
    private int mEndWidth;
    private int mType;
    private RelativeLayout.LayoutParams mLayoutParams;

    public ExpandAnimation(View view, int duration, int type, int x, int y, int screenW, int screenH) {

        setDuration(duration);
        mView = view;
        mEndHeight = mView.getHeight();
        mEndWidth = mView.getWidth();
        mLayoutParams = ((RelativeLayout.LayoutParams) view.getLayoutParams());
        mView.setX(x);
        mView.setY(y);
        mType = type;
        if(mType == EXPAND) {
            mLayoutParams.height = 0;
            mLayoutParams.width = 0;
        } else {
            mLayoutParams.height = screenH + y;
            mLayoutParams.width = screenW + x;
        }
        view.setVisibility(View.VISIBLE);
    }

    public int getHeight(){
        return mView.getHeight();
    }

    public void setHeight(int height){
        mEndHeight = height;
    }

    public int getWidth(){
        return mView.getWidth();
    }

    public void setWidth(int width){
        mEndWidth = width;
    }

    @SuppressLint("NewApi")
	@Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if(mType == EXPAND) {
                mLayoutParams.height =  (int)(mEndHeight * interpolatedTime);
                mLayoutParams.width =  (int)(mEndWidth * interpolatedTime);
                mView.setX(mView.getX() * (1 - interpolatedTime));
                mView.setY(mView.getY() * (1 - interpolatedTime));
                mView.setAlpha(interpolatedTime*2);
            } else {
                mLayoutParams.height = (int) (mEndHeight * (1 - interpolatedTime));
                mLayoutParams.width = (int) (mEndWidth * (1 - interpolatedTime));
            }
            mView.requestLayout();
        } else {
            if(mType == EXPAND) {
                mLayoutParams.height = LayoutParams.WRAP_CONTENT;
                mLayoutParams.width = LayoutParams.WRAP_CONTENT;
                mView.requestLayout();
            }else{
                mLayoutParams.height = 0;
                mLayoutParams.width = 0;
                mView.requestLayout();
            }
        }
    }
}