package com.verzqli.vmui.widget;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.WithHint;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.verzqli.vmui.utils.KeyboardUtils;

import java.util.ArrayList;

/**
 * <pre>
 *     author: XuPei
 *     time  : 2019/7/9
 *     desc  :
 * </pre>
 */
public class VMUIPayPasswordView extends AppCompatEditText {
    private int mLineColor = Color.parseColor("#D2D2D2");
    private int mWordColor = Color.parseColor("#333333");
    private int mLineWidth = dp2px(1f);
    private int mInputWidth = dp2px(50f);
    private Paint mLinePaint;
    private Paint mWordPaint;
    private RectF mInputRectF;
    private InputFinishListener mListener;
    private String result;//保存当前输入的密码

    private int mWidth;
    private int mStartX;
    private int mPwdHintRadius = dp2px(5f);
    private int mPasswordLength = 6;

    public VMUIPayPasswordView(Context context) {
        this(context, null);
    }

    public VMUIPayPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initInput();
        mWidth = mInputWidth * 6 + mLineWidth * 7;
    }

    private void initInput() {
    }

    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setAntiAlias(true);

        mWordPaint = new Paint();
        mWordPaint.setStyle(Paint.Style.FILL);
        mWordPaint.setColor(mWordColor);
        mWordPaint.setAntiAlias(true);
        setBackground(null);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mPasswordLength)});
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setCursorVisible(false);
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartX = (w - mWidth) >> 1;
        mInputRectF = new RectF(mStartX, mLineWidth >> 1, (w + mWidth - mLineWidth) >> 1, h - (mLineWidth >> 1));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(mInputWidth + (mLineWidth));
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(mInputRectF, mLinePaint);
        for (int i = 1; i < mPasswordLength; i++) {
            canvas.drawLine(mStartX + i * (mLineWidth + mInputWidth) + (mLineWidth >> 1), mLineWidth,
                    mStartX + i * (mLineWidth + mInputWidth) + (mLineWidth >> 1), mInputWidth, mLinePaint);
        }
        for (int i = 0; i < result.length(); i++) {
            canvas.drawCircle(mStartX + (mInputWidth >> 1) + i * (mInputWidth + mLineWidth),
                    (mInputWidth >> 1) + mLineWidth, mPwdHintRadius, mWordPaint);
        }

    }


    public void clearPassword() {
        setText("");
        result = "";
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        result = text.toString();
        invalidate();
        FinishInput();
    }


    private void FinishInput() {
        if (result.length() == mPasswordLength && mListener != null) {
            KeyboardUtils.hideSoftInput(getContext(), this);
            mListener.onInputFinish(result);
        }
    }


    public void setInputFinishListener(InputFinishListener mListener) {
        this.mListener = mListener;
    }

    public interface InputFinishListener {
        void onInputFinish(String password);

    }

    public int dp2px(float dpValue) {
        return (int) (dpValue * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}
