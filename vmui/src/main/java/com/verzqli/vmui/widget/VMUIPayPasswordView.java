package com.verzqli.vmui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

/**
 * <pre>
 *     author: XuPei
 *     time  : 2019/7/9
 *     desc  :
 * </pre>
 */
public class VMUIPayPasswordView extends View {
    private int mLineColor = Color.parseColor("#D2D2D2");
    private int mWordColor = Color.parseColor("#333333");
    private int mLineWidth = dp2px(1f);
    private int mInputWidth = dp2px(50f);
    private Paint mLinePaint;
    private Paint mWordPaint;
    private RectF mInputRectF;
    private InputMethodManager input;//输入法管理
    private InputFinishListener mListener;
    private ArrayList<Integer> result;//保存当前输入的密码

    private int mWidth;
    private int mStartX;
    private int mPwdHintRadius = dp2px(5f);
    private int mPasswordLength = 6;

    public VMUIPayPasswordView(Context context) {
        this(context, null);
    }

    public VMUIPayPasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMUIPayPasswordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
        initInput();
        mWidth = mInputWidth * 6 + mLineWidth * 7;
//        Rect rect = new Rect();
//        mWordPaint.getTextBounds(mPasswordHint, 0, 1, rect);
//        mPwdHintWidth = rect.width();
//        mPwdHintHeight = rect.height();

    }

    private void initInput() {
        this.setOnKeyListener(new NumKeyListener());
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        input = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        result = new ArrayList<>();
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
        super.onDraw(canvas);
        canvas.drawRect(mInputRectF, mLinePaint);
        for (int i = 1; i < mPasswordLength; i++) {
            canvas.drawLine(mStartX + i * (mLineWidth + mInputWidth) + (mLineWidth >> 1), mLineWidth,
                    mStartX + i * (mLineWidth + mInputWidth) + (mLineWidth >> 1), mInputWidth, mLinePaint);
        }
        for (int i = 0; i < result.size(); i++) {
            canvas.drawCircle(mStartX + (mInputWidth >> 1) + i * (mInputWidth + mLineWidth),
                    (mInputWidth >> 1) + mLineWidth, mPwdHintRadius, mWordPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//点击弹出键盘
            requestFocus();
            input.showSoftInput(this, InputMethodManager.SHOW_FORCED);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            input.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;//只允许输入数字
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return new NumInputConnection(this, false);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    class NumInputConnection extends BaseInputConnection {

        public NumInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            //这里是接收文本的输入法，我们只允许输入数字，则不做任何处理
            return super.commitText(text, newCursorPosition);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            //屏蔽返回键，发送自己的删除事件
            if (beforeLength == 1 && afterLength == 0) {
                return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    /**
     * 输入监听
     */
    class NumKeyListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.isShiftPressed()) {//处理*#等键
                    return false;
                }
                if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {//只处理数字
                    if (result.size() < mPasswordLength) {
                        result.add(keyCode - 7);
                        invalidate();
                        FinishInput();
                    }
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (!result.isEmpty()) {//不为空时，删除最后一个数字
                        result.remove(result.size() - 1);
                        invalidate();
                    }
                    return true;
                }

            }
            return false;
        }


        private void FinishInput() {
            if (result.size() == mPasswordLength && mListener != null) {//输入已完成
                StringBuilder sb = new StringBuilder();
                for (int i : result) {
                    sb.append(i);
                }
                mListener.onInputFinish(sb.toString());
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindowToken(), 0); //输入完成后隐藏键盘
            }
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
