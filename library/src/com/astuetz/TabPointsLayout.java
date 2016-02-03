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

    private int mCirclePositionRadius = 3;
    private int mCircleIndicatorRadius = 6;
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

        mCirclePositionRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mCirclePositionRadius, dm);
        mCircleIndicatorRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mCircleIndicatorRadius, dm);
        if (mCircleIndicatorRadius < mCirclePositionRadius) {
            mCircleIndicatorRadius = mCirclePositionRadius;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip_TabPointsLayout);
        try {
            mUnderlinePositions = a.getInteger(R.styleable.PagerSlidingTabStrip_TabPointsLayout_pstsUnderlinePositions, POSITION_BOTTOM);

            mCirclePositionRadius = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_TabPointsLayout_pstsCirclePositionRadius, mCirclePositionRadius);
            mCircleIndicatorRadius = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_TabPointsLayout_pstsCircleIndicatorRadius, mCircleIndicatorRadius);

            if (mCircleIndicatorRadius < mCirclePositionRadius) {
                mCircleIndicatorRadius = mCirclePositionRadius;
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void drawIndicator(Canvas canvas, float lineLeft, float lineRight) {
        float dy;
        if (mUnderlinePositions == POSITION_TOP) {
            dy = mCircleIndicatorRadius;
        } else {
            dy = getHeight() - mCircleIndicatorRadius;
        }
        canvas.drawCircle((lineRight + lineLeft) / 2, dy, mCircleIndicatorRadius, rectPaint);
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
            top = mCircleIndicatorRadius - underlineHeight / 2;
            bottom = mCircleIndicatorRadius + underlineHeight / 2;
        } else {
            top = height - mCircleIndicatorRadius - underlineHeight / 2;
            bottom = height - mCircleIndicatorRadius + underlineHeight / 2;
        }

        canvas.drawRect(left, top, right, bottom, rectPaint);

        for (int i = 0; i < tabCount; i++) {
            View tab = tabsContainer.getChildAt(i);
            float dx = tab.getRight() - (tab.getWidth() / 2);
            float dy;
            if (mUnderlinePositions == POSITION_TOP) {
                dy = mCircleIndicatorRadius;
            } else {
                dy = height - mCircleIndicatorRadius;
            }
            canvas.drawCircle(dx, dy, mCirclePositionRadius, rectPaint);
        }
    }
}
