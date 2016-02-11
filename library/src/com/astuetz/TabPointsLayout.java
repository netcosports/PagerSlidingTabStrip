package com.astuetz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.astuetz.pagerslidingtabstrip.R;

public class TabPointsLayout extends PagerSlidingTabStrip {

    private static final int DEFAULT_RADIUS_POSITION = 3;
    private static final int DEFAULT_RADIUS_INDICATOR = 6;

    private int mRadiusPosition;
    private int mRadiusIndicator;
    private int mTextGravity = Gravity.BOTTOM;
    private int mMarginVerticalIndicator;

    public TabPointsLayout(Context context) {
        this(context, null);
    }

    public TabPointsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabPointsLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        DisplayMetrics dm = getResources().getDisplayMetrics();

        mRadiusPosition = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS_POSITION, dm);
        mRadiusIndicator = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS_INDICATOR, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabPointsLayout);
        try {
            mTextGravity = a.getInteger(R.styleable.TabPointsLayout_android_gravity, Gravity.BOTTOM);

            mRadiusPosition = a.getDimensionPixelSize(R.styleable.TabPointsLayout_pstsCirclePositionRadius, mRadiusPosition);
            mRadiusIndicator = a.getDimensionPixelSize(R.styleable.TabPointsLayout_pstsCircleIndicatorRadius, mRadiusIndicator);
            mMarginVerticalIndicator = a.getDimensionPixelSize(R.styleable.TabPointsLayout_pstsMarginVerticalIndicator, 0);

            if (mRadiusIndicator < mRadiusPosition) {
                mRadiusIndicator = mRadiusPosition;
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void drawIndicator(Canvas canvas, float lineLeft, float lineRight) {
        canvas.drawCircle((lineRight + lineLeft) / 2, getHeight() / 2 - mMarginVerticalIndicator, mRadiusIndicator, rectPaint);
    }

    @Override
    protected int getTextGravity() {
        return mTextGravity;
    }

    @Override
    protected void drawUnderline(Canvas canvas) {
        int height = getHeight();
        rectPaint.setColor(underlineColor);

        View firstTab = tabsContainer.getChildAt(0);
        float left = firstTab.getRight() - firstTab.getWidth() / 2;

        View lastTab = tabsContainer.getChildAt(tabCount - 1);
        float right = lastTab.getRight() - (lastTab.getWidth() / 2);

        float top = height / 2 - underlineHeight / 2 - mMarginVerticalIndicator;
        float bottom = height / 2 + underlineHeight / 2 - mMarginVerticalIndicator;
        canvas.drawRect(left, top, right, bottom, rectPaint);

        for (int i = 0; i < tabCount; i++) {
            View tab = tabsContainer.getChildAt(i);
            float dx = tab.getRight() - (tab.getWidth() / 2);
            float dy = height / 2 - mMarginVerticalIndicator;
            canvas.drawCircle(dx, dy, mRadiusPosition, rectPaint);
        }
    }
}
