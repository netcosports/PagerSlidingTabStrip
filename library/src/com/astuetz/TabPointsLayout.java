package com.astuetz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.astuetz.pagerslidingtabstrip.R;

public class TabPointsLayout extends PagerSlidingTabStrip {

    private static final int POSITION_BOTTOM = 0;
    private static final int POSITION_TOP = 1;

    private static final int DEFAULT_RADIUS_POSITION = 3;
    private static final int DEFAULT_RADIUS_INDICATOR = 6;

    private int mRadiusPosition;
    private int mRadiusIndicator;
    private int mUnderlinePositions = POSITION_BOTTOM;

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
            mUnderlinePositions = a.getInteger(R.styleable.TabPointsLayout_pstsUnderlinePositions, POSITION_BOTTOM);

            mRadiusPosition = a.getDimensionPixelSize(R.styleable.TabPointsLayout_pstsCirclePositionRadius, mRadiusPosition);
            mRadiusIndicator = a.getDimensionPixelSize(R.styleable.TabPointsLayout_pstsCircleIndicatorRadius, mRadiusIndicator);

            if (mRadiusIndicator < mRadiusPosition) {
                mRadiusIndicator = mRadiusPosition;
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void drawIndicator(Canvas canvas, float lineLeft, float lineRight) {
        float dy;
        if (mUnderlinePositions == POSITION_TOP) {
            dy = mRadiusIndicator;
        } else {
            dy = getHeight() - mRadiusIndicator;
        }
        canvas.drawCircle((lineRight + lineLeft) / 2, dy, mRadiusIndicator, rectPaint);
    }

    @Override
    protected void drawUnderline(Canvas canvas) {
        int height = getHeight();
        rectPaint.setColor(underlineColor);

        View firstTab = tabsContainer.getChildAt(0);
        float left = firstTab.getRight() - firstTab.getWidth() / 2;

        View lastTab = tabsContainer.getChildAt(tabCount - 1);
        float right = lastTab.getRight() - (lastTab.getWidth() / 2);

        float top;
        float bottom;
        if (mUnderlinePositions == POSITION_TOP) {
            top = mRadiusIndicator - underlineHeight / 2;
            bottom = mRadiusIndicator + underlineHeight / 2;
        } else {
            top = height - mRadiusIndicator - underlineHeight / 2;
            bottom = height - mRadiusIndicator + underlineHeight / 2;
        }

        canvas.drawRect(left, top, right, bottom, rectPaint);

        for (int i = 0; i < tabCount; i++) {
            View tab = tabsContainer.getChildAt(i);
            float dx = tab.getRight() - (tab.getWidth() / 2);
            float dy;
            if (mUnderlinePositions == POSITION_TOP) {
                dy = mRadiusIndicator;
            } else {
                dy = height - mRadiusIndicator;
            }
            canvas.drawCircle(dx, dy, mRadiusPosition, rectPaint);
        }
    }
}
