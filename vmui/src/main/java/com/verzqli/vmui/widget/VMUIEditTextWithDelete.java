package com.verzqli.vmui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.verzqli.vmui.R;

/**
 * <pre>
 *     author: XuPei
 *     time  : 2019/7/15
 *     desc  :
 * </pre>
 */
public class VMUIEditTextWithDelete extends AppCompatEditText implements TextWatcher {
    private Drawable mDeleteDrawable;
    /**
     * 輸入内容分割類型
     * -1:不分割
     * 0:334手机号码分割
     * 1:443银行卡号分割
     */
    private int mType;

    private int mWidth;

    private int mMaxLength;
    //分隔符,一般为空格
    private final char mSeparator = ' ';
    //保留上一次修改的内容
    private StringBuilder mHistoryText;

    private int mChangeCursorIndex = 0;

    private boolean isShowDeleteIcon = false;

    public VMUIEditTextWithDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public VMUIEditTextWithDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.VMUIEditTextWithDelete);
        mDeleteDrawable = array.getDrawable(R.styleable.VMUIEditTextWithDelete_deleteIcon);
        mType = array.getInt(R.styleable.VMUIEditTextWithDelete_inputNumberType, 0);
        if (mDeleteDrawable != null) {
            mWidth = array.getDimensionPixelSize(R.styleable.VMUIEditTextWithDelete_deleteIconWidth, dp2px(15));
            mDeleteDrawable.setBounds(0, 0, mWidth, mWidth);
        }
        array.recycle();
        mHistoryText = new StringBuilder();
        if (mType == 0) {
            mMaxLength = 13;
        }
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
        setInputType(InputType.TYPE_CLASS_PHONE);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            setDrawable();
        } else {
            setCompoundDrawables(null, null, null, null);
        }

    }

    private void setDrawable() {
        isShowDeleteIcon = length() > 0 && mDeleteDrawable != null;
        setCompoundDrawables(null, null, isShowDeleteIcon ? mDeleteDrawable : null, null);
    }

    public int dp2px(float dpValue) {
        return (int) (dpValue * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (mHistoryText != null && !TextUtils.equals(s.toString(), mHistoryText.toString())) {

            mHistoryText = new StringBuilder();
            //记录当前光标位置
            int startCursorIndex = getSelectionStart();
            mChangeCursorIndex = startCursorIndex;
            //这里只考虑了手机号码的，后面需要添加更多的话需要修改这里规则
            boolean isDelete = before > 0 && count == 0;
            boolean isSeparatorPosition = (startCursorIndex == 3 || startCursorIndex == 8);
            String newInputStr = transformEditStr(s.toString(), startCursorIndex, isDelete && isSeparatorPosition);
            int endCursorIndex = mChangeCursorIndex;
            int index = 0;
            if (mType == 0) {//电话号码显示方式
                if ((index + 3) < newInputStr.length()) {
                    mHistoryText.append(newInputStr.substring(index, index + 3)).append(mSeparator);
                    index += 3;
                    if (index < endCursorIndex) {
                        mChangeCursorIndex++;
                    }
                }
            }
            while ((index + 4) < newInputStr.length()) {
                mHistoryText.append(newInputStr.substring(index, index + 4)).append(mSeparator);
                index += 4;
                if (index < endCursorIndex) {
                    mChangeCursorIndex++;
                }
            }
            mHistoryText.append(newInputStr.substring(index));
            this.setText(mHistoryText.toString().trim());

            setText(mHistoryText.toString());
            //光标位置,做最小值判断是为防止越界,也是为了调整光标位
            setSelection(Math.min(mChangeCursorIndex, mHistoryText.length()));
            if (!isShowDeleteIcon || mHistoryText.length() == 0) {
                setDrawable();
            }
        }
    }

    private String transformEditStr(String content, int cursorIndex, boolean isDelete) {

        final int len = content.length();
        StringBuilder sb = new StringBuilder(len);
        char c;
        for (int i = 0; i < len; ++i) {
            c = content.charAt(i);
            if (c != mSeparator) {
                if (isDelete && cursorIndex - 1 == i) {
                    mChangeCursorIndex--;
                } else {
                    sb.append(c);
                }
            } else {
                if (i < cursorIndex) {
                    mChangeCursorIndex--;
                }
            }
        }
        return sb.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDeleteDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int startX = getWidth() - getTotalPaddingLeft();
            int startY = (getHeight() - getTotalPaddingLeft()) >> 1;
            Rect rect = mDeleteDrawable.getBounds();
            if ((x > startX - rect.width() && x < startX) && (y > startY - rect.top && y < startY + rect.bottom)) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }
}


