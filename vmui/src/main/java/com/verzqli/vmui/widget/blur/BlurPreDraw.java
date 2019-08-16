package com.verzqli.vmui.widget.blur;

import android.view.ViewTreeObserver;

import com.verzqli.vmui.widget.QQBlurView;

/**
 * <pre>
 *     author: XuPei
 *     time  : 2019/8/15
 *     desc  :
 * </pre>
 */
public class BlurPreDraw implements ViewTreeObserver.OnPreDrawListener {
    final  QQBlurView blurView;

    public BlurPreDraw(QQBlurView qQBlurView) {
        this.blurView = qQBlurView;
    }

    public boolean onPreDraw() {
        if (this.blurView.mManager!=null) {
             return this.blurView.mManager.m371a();
        }
        return true;
    }
}