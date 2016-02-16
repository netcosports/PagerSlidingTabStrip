package com.astuetz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.pagerslidingtabstrip.R;

public class TabPointsLayout extends PagerSlidingTabStrip {

    private static final int DEFAULT_RADIUS_POSITION = 3;
    private static final int DEFAULT_RADIUS_INDICATOR = 6;
    private static final int DEFAULT_WIDTH_IMAGE = 16;
    private static final int DEFAULT_HEIGHT_IMAGE = 16;

    private int mRadiusPosition;
    private int mRadiusIndicator;
    private int mTextGravity;
    private int mMarginVerticalIndicator;
    private Typeface mTabCheckedTypeface;
    private int mTabCheckedTypefaceStyle;
    private int mMarginTopIcon;
    private int mWidthImage;
    private int mHeightImage;
    private int mColorImageTint;
    private int mColorImageTintSelector;

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
        mWidthImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_WIDTH_IMAGE, dm);
        mHeightImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEIGHT_IMAGE, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabPointsLayout);
        try {
            mColorImageTint = a.getColor(R.styleable.TabPointsLayout_pstsImageTint, tabTextColor);
            mColorImageTintSelector = a.getColor(R.styleable.TabPointsLayout_pstsImageTintSelector, tabTextColorSelected);

            mWidthImage = a.getDimensionPixelSize(R.styleable.TabPointsLayout_pstsWidthImage, mWidthImage);
            mHeightImage = a.getDimensionPixelSize(R.styleable.TabPointsLayout_pstsHeightImage, mHeightImage);
            mTextGravity = a.getInteger(R.styleable.TabPointsLayout_pstsTextGravity, Gravity.BOTTOM);
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
            TextView tv;

            if (v instanceof ViewGroup) {
                tv = (TextView) v.findViewById(R.id.text_tab);
                AppCompatImageView iv = (AppCompatImageView) v.findViewById(R.id.image_tab);
                iv.setColorFilter((i == currentPosition) ? mColorImageTintSelector : mColorImageTint, PorterDuff.Mode.MULTIPLY);
            } else if (v instanceof TextView) {
                tv = (TextView) v;
            } else {
                return;
            }

            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
            tv.setTypeface(i == currentPosition ? mTabCheckedTypeface : tabTypeface,
                    i == currentPosition ? mTabCheckedTypefaceStyle : tabTypefaceStyle);
            tv.setTextColor((i == currentPosition) ? tabTextColorSelected : tabTextColor);

            // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
            // pre-ICS-build
            if (textAllCaps) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    tv.setAllCaps(true);
                } else {
                    tv.setText(tv.getText().toString().toUpperCase(locale));
                }
            }
        }
    }

    @Override
    protected void addItemTab(int tabCount) {
        for (int i = 0; i < tabCount; i++) {

            if (pager.getAdapter() instanceof IconTabProvider) {
                LinearLayout llContainer = new LinearLayout(getContext());
                llContainer.setOrientation(LinearLayout.VERTICAL);
                llContainer.setGravity(Gravity.CENTER_HORIZONTAL);
                llContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

                AppCompatImageView iv = new AppCompatImageView(getContext());
                iv.setId(R.id.image_tab);
                iv.setLayoutParams(new LinearLayout.LayoutParams(mWidthImage, mHeightImage));
                iv.setColorFilter(mColorImageTintSelector, PorterDuff.Mode.MULTIPLY);
                iv.setImageResource(((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
                llContainer.addView(iv);

                TextView tv = new TextView(getContext());
                tv.setId(R.id.text_tab);
                tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                tv.setText(pager.getAdapter().getPageTitle(i).toString());
                tv.setGravity(getTextGravity());
                tv.setSingleLine();
                llContainer.addView(tv);

                addTab(i, llContainer);
            } else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }

        }
    }

    @Override
    protected PageListener cratePagerListener() {
        return new TabPointsPageListener();
    }

    protected class TabPointsPageListener extends PageListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            boolean updateStyles = currentPosition != position;

            currentPosition = position;
            currentPositionOffset = positionOffset;

            if (tabsContainer != null && position < tabsContainer.getChildCount()) {
                scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            }

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            if (updateStyles) {
                updateTabStyles();
            }
        }

    }
}
