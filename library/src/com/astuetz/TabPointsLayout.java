package com.astuetz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.astuetz.pagerslidingtabstrip.R;

import java.util.ArrayList;
import java.util.List;

public class TabPointsLayout extends PagerSlidingTabStrip {

    private static final int DEFAULT_RADIUS_POSITION = 3;
    private static final int DEFAULT_RADIUS_INDICATOR = 6;

    private int mRadiusPosition;
    private int mRadiusIndicator;
    private int mTextGravity = Gravity.BOTTOM;
    private int mMarginVerticalIndicator;
    private Typeface mTabCheckedTypeface;
    private int mTabCheckedTypefaceStyle;
    private int mMarginTopIcon;
    private List<Bitmap> mListIcon = new ArrayList<>();

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
            mMarginTopIcon = a.getDimensionPixelSize(R.styleable.TabPointsLayout_pstsMarginTopIcon, mMarginTopIcon);
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
            float dx = tab.getRight() - tab.getWidth() / 2;
            float dy = height / 2 - mMarginVerticalIndicator;
            canvas.drawCircle(dx, dy, mRadiusPosition, rectPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mListIcon.isEmpty()) {
            return;
        }

        rectPaint.setColorFilter(new LightingColorFilter(tabTextColorSelected, 1));

        for (int i = 0; i < tabCount; i++) {
            View tab = tabsContainer.getChildAt(i);
            float dx = tab.getRight() - tab.getWidth() / 2 - mListIcon.get(i).getWidth() / 2;
            float dy = mMarginTopIcon;
            if (currentPosition == i) {
                rectPaint.setAlpha(Color.alpha(tabTextColorSelected));
            } else {
                rectPaint.setAlpha(Color.alpha(tabTextColor));
            }
            canvas.drawBitmap(mListIcon.get(i), dx, dy, rectPaint);
        }
    }

    public int getRadiusPosition() {
        return mRadiusPosition;
    }

    public void setRadiusPosition(int mRadiusPosition) {
        this.mRadiusPosition = mRadiusPosition;
    }

    public int getRadiusIndicator() {
        return mRadiusIndicator;
    }

    public void setRadiusIndicator(int mRadiusIndicator) {
        this.mRadiusIndicator = mRadiusIndicator;
    }

    public void setTextGravity(int mTextGravity) {
        this.mTextGravity = mTextGravity;
    }

    public int getMarginVerticalIndicator() {
        return mMarginVerticalIndicator;
    }

    public void setMarginVerticalIndicator(int mMarginVerticalIndicator) {
        this.mMarginVerticalIndicator = mMarginVerticalIndicator;
    }

    @Override
    public void setTypeface(Typeface typeface, int style) {
        setTypeface(typeface, typeface, style, style);
    }

    public void setTypeface(Typeface typeface, Typeface typefaceChecked) {
        setTypeface(typeface, typefaceChecked, 0, 0);
    }

    public void setTypeface(Typeface typeface, Typeface checkedTypeface, int style, int checkedStyle) {
        this.tabTypeface = typeface;
        this.mTabCheckedTypeface = checkedTypeface;
        this.tabTypefaceStyle = style;
        this.mTabCheckedTypefaceStyle = checkedStyle;
        updateTabStyles();
    }

    @Override
    protected void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            v.setBackgroundResource(tabBackgroundResId);

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                tab.setTypeface(i == currentPosition ? mTabCheckedTypeface : tabTypeface,
                        i == currentPosition ? mTabCheckedTypefaceStyle : tabTypefaceStyle);
                tab.setTextColor((i == currentPosition) ? tabTextColorSelected : tabTextColor);

                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {
            addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            if (pager.getAdapter() instanceof IconTabProvider) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
                mListIcon.add(bitmap);
            }
        }

        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                currentPosition = pager.getCurrentItem();
                updateTabStyles();
                scrollToChild(currentPosition, 0);
            }
        });

    }
}
