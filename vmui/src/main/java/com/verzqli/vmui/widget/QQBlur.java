package com.verzqli.vmui.widget;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.verzqli.vmui.stackblur.StackBlurManager;
import com.verzqli.vmui.widget.blur.QQblurManager;

/**
 * <pre>
 *     author: XuPei
 *     time  : 2019/8/15
 *     desc  :
 * </pre>
 */
public class QQBlur implements Runnable {
    private int a = -1;
    /* renamed from: a */
    final /* synthetic */ StackBlurManager mStackBlurManager;
    final /* synthetic */ QQblurManager mQQblurManager;

    public QQBlur(QQblurManager QQblurManager, StackBlurManager stackBlurManager) {
        this.mQQblurManager = QQblurManager;
        this.mStackBlurManager = stackBlurManager;
    }

    public void run() {
        if (!this.mQQblurManager.m56b()) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (!(this.a == -1 || this.a == QQblurManager.a)) {
                this.mQQblurManager.setIntNumber(this.a, QQblurManager.a);
            }
            this.a = QQblurManager.a;
            int i = QQblurManager.a;
            Bitmap process = this.mStackBlurManager.process(this.mQQblurManager.mRadius);
            if (process != null) {
                this.mQQblurManager.mBitmap = process;
            } else {
                Log.e("QQBlur", "run: outBitmap is null. OOM ?");
            }
            long elapsedRealtime2 = SystemClock.elapsedRealtime();
            this.mQQblurManager.unKnowNumber++;
            this.mQQblurManager.mTime = (elapsedRealtime2 - elapsedRealtime) + this.mQQblurManager.mTime;
            View rootView = this.mQQblurManager.mRootView;
            if (rootView != null && this.mQQblurManager.isDrawing) {
                rootView.postInvalidate();
            }
        }
    }
}