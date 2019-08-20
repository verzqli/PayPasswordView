package com.verzqli.vmui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.verzqli.vmui.R;
import com.verzqli.vmui.widget.blur.BlurPreDraw;
import com.verzqli.vmui.widget.blur.QQblurManager;
import com.verzqli.vmui.widget.blur.Unkonow1;
import com.verzqli.vmui.widget.blur.Unkonow2;

/**
 * <pre>
 *     author: XuPei
 *     time  : 2019/8/15
 *     desc  :
 * </pre>
 */
public class QQBlurView extends View {
    private Drawable a;
    /* renamed from: a */
    private BlurPreDraw mBlurPrewDraw = new BlurPreDraw(this);
    /* renamed from: a */
    public QQblurManager mManager = new QQblurManager();
    /* renamed from: a */
    private boolean mEnableBlur = true;

    public static Bitmap testBitmap;
    public QQBlurView(Context context) {
        super(context);
        init();
    }

    public QQBlurView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }
    Paint paint;
    public QQBlurView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();

    }

    private void init() {
        paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        testBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bb);
    }

    protected void onDraw(Canvas canvas) {
        if (!m50a()) {
            if (this.mEnableBlur) {
                setBackgroundDrawable(null);
                this.mManager.onDraw((View) this, canvas);
                super.onDraw(canvas);
                return;
            }
            setBackgroundDrawable(this.a);
            super.onDraw(canvas);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mManager != null) {
            a();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mManager != null) {
            b();
        }
    }

    public void a() {
        this.mManager.b();
    }

    public void b() {
        this.mManager.a();
    }

    public void c() {
        getViewTreeObserver().removeOnPreDrawListener(this.mBlurPrewDraw);
        this.mManager.onDestroy();
    }

    public void a(View view) {
        this.mManager.a(view);
    }

    public void b(View view) {
        this.mManager.b(view);
    }

    public void a(Unkonow2 azle) {
        this.mManager.a(azle);
    }

    public void d() {
        getViewTreeObserver().addOnPreDrawListener(this.mBlurPrewDraw);
        this.mManager.a();
    }

    public void a(Drawable drawable) {
        this.mManager.a(drawable);
    }

    public void a(float f) {
        this.mManager.a(f);
    }

    public void a(int i) {
        this.mManager.setRaius(i);
    }

    public void setDirtyListener(Unkonow1 azld) {
        this.mManager.a(azld);
    }

    public void draw(Canvas canvas) {
        if (m50a()) {
            this.mManager.draw();
        } else {
            super.draw(canvas);
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        if (!m50a()) {
            super.dispatchDraw(canvas);
        }
    }

    public void setDebugTag(String str) {
        this.mManager.a(str);
    }

    public void b(int i) {
        this.mManager.b(i);
    }

    public void setEnableBlur(boolean z) {
        this.mEnableBlur = z;
    }

    public void setDisableBlurDrawableRes(int i) {
        this.a = getResources().getDrawable(i);
    }

    public void c(int i) {
        this.mManager.c(i);
    }

    /* renamed from: a */
    public boolean m50a() {
        return this.mManager.m56b();
    }
    private ImageView imageView;
    public void setImageView(ImageView imageView) {
        this.imageView =imageView;
    }

    float moveX;
    float moveY;
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = event.getX();
                moveY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                setTranslationX(getX() + (event.getX() - moveX));
                setTranslationY(getY() + (event.getY() - moveY));
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }


}