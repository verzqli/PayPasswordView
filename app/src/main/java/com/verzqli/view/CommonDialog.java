package com.verzqli.view;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * <pre>
 *     author: XuPei
 *     time  : 2019/7/11
 *     desc  :
 * </pre>
 */
public class CommonDialog extends BaseDialog {
    private int mContent;

    public static CommonDialog newInstance() {
        return new CommonDialog();
    }

    @Override
    public int setUpLayoutId() {
        return R.layout.base_common_dialog;
    }

    @Override
    public void convertView(DialogViewHolder holder, BaseDialog dialog) {
        setMargin(40);
        holder.setOnClickListener(R.id.common_dialog_left_btn, v -> Log.i("aaa", "aaa"));
        holder.setOnClickListener(R.id.common_dialog_right_btn, v -> Log.i("aaa", "aaa"));

    }




    public CommonDialog setLayoutId(@LayoutRes int layoutId) {
        this.mLayoutResId = layoutId;
        return this;
    }

    public CommonDialog setContent(@StringRes int mContent) {
        this.mContent = mContent;
        return this;
    }


    public CommonDialog setMargin(int margin) {
        super.setMargin(margin);
        return this;
    }
}
