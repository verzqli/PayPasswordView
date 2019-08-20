package com.verzqli.vmui.widget.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


import com.verzqli.vmui.R;
import com.verzqli.vmui.stackblur.StackBlurManager;
import com.verzqli.vmui.widget.QQBlur;


/**
 * <pre>
 *     author: XuPei
 *     time  : 2019/8/15
 *     desc  :
 * </pre>
 */
public class QQblurManager {
    public static int a = 0;
    /* renamed from: a */
    private static HandlerThread mHandlerThread = ThreadManagerV2.newFreeHandlerThread("QQBlur", -8);
    /* renamed from: a */
    private static Field f328a;
    /* renamed from: a mScale*/
    private float mScale = 8.0f;
    /* renamed from: a */
    private long f330a;
    /* renamed from: a */
    private Context f331a;
    /* renamed from: a */
    public volatile Bitmap mBitmap;
    /* renamed from: a */
    private Canvas mCanvas;
    /* renamed from: a */
    private Paint mPaint;
    /* renamed from: a */
    private RectF mRectF = new RectF();
    /* renamed from: a */
    private Drawable mDrawable = new ColorDrawable(Color.parseColor("#DAFAFAFC"));
    /* renamed from: a */
    private Handler mHandler;
    /* renamed from: a */
    private View mView;
    /* renamed from: a */
    private Unkonow1 mUnkonw1;
    /* renamed from: a */
    private Unkonow2 mUnknow2;
    /* renamed from: a */
    private String mStr;
    /* renamed from: a */
    private List<View> mViewList = new ArrayList();
    /* renamed from: a */
    private volatile boolean f343a = true;
    private float b = 1.0f;
    /* renamed from: b 模糊半径*/
    public int mRadius = 6;
    /* renamed from: b */
    private long f345b;
    /* renamed from: b */
    public volatile View mRootView;
    /* renamed from: b */
    public boolean isDrawing;
    private float c = 1.0f;
    /* renamed from: c */
    private int f348c = 0;
    /* renamed from: c */
    public long unKnowNumber;
    /* renamed from: c */
    private boolean f350c;
    private float d;
    /* renamed from: d  并发数*/
    private int mThreadCount = 2;
    /* renamed from: d */
    public long mTime;
    private float e;
    /* renamed from: e */
    public long f353e;
    private long f;
    private long g;
    private long h;
    private long mCache;
    private long j;
    private long k;

    static {
        mHandlerThread.start();
    }

    private void e() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mView != null && this.mRootView != null && this.mRootView.getWidth() > 0 && this.mRootView.getHeight() > 0) {
            Bitmap createBitmap;
            int a = QQblurManager.a((float) this.mRootView.getWidth(), this.mScale);
            int a2 = QQblurManager.a((float) this.mRootView.getHeight(), this.mScale);
            int a3 = QQblurManager.a(a);
            int a4 = QQblurManager.a(a2);
            this.c = ((float) a2) / ((float) a4);
            this.b = ((float) a) / ((float) a3);
            float f = this.mScale * this.b;
            float f2 = this.mScale * this.c;
            try {
                createBitmap = Bitmap.createBitmap(a3, a4, Config.ARGB_8888);
//                createBitmap = BitmapFactory.decodeResource(mView.getResources(),R.drawable.bb);
            } catch (Throwable e) {
                Log.e("QQBlur", "prepareBlurBitmap: ", e);
                createBitmap = null;
            }
            if (createBitmap != null) {
                this.g = (long) createBitmap.getWidth();
                this.h = (long) createBitmap.getHeight();
                if (VERSION.SDK_INT >= 19) {
                    mCache = (long) createBitmap.getAllocationByteCount();
                } else {
                    mCache = (long) createBitmap.getByteCount();
                }
                createBitmap.eraseColor(-1);
                this.mCanvas.setBitmap(createBitmap);
                int[] iArr = new int[2];
                this.mRootView.getLocationInWindow(iArr);
                int[] iArr2 = new int[2];
                this.mView.getLocationInWindow(iArr2);
                this.mCanvas.save();
                this.mCanvas.translate(((float) (-(iArr[0] - iArr2[0]))) / f, ((float) (-(iArr[1] - iArr2[1]))) / f2);
                this.mCanvas.scale(1.0f / f, 1.0f / f2);
                StackBlurManager stackBlurManager = new StackBlurManager(createBitmap);
                stackBlurManager.setDbg(true);
                stackBlurManager.setExecutorThreads(stackBlurManager.getExecutorThreads());
                Bundle bundle = new Bundle();
                if (this.mUnknow2 != null) {
                    this.mUnknow2.a(bundle);
                }
                this.f350c = true;
                if (VERSION.SDK_INT <= 27 || this.mRootView.getContext().getApplicationInfo().targetSdkVersion <= 27) {
                    Rect clipBounds = this.mCanvas.getClipBounds();
                    clipBounds.inset(-createBitmap.getWidth(), -createBitmap.getHeight());
                    if (this.mCanvas.clipRect(clipBounds, Op.REPLACE)) {
                        this.mView.draw(this.mCanvas);
                    } else {
                        Log.e("QQBlur", "prepareBlurBitmap: canvas clip rect empty. Cannot draw!!!");
                    }
                } else {
                    this.mView.draw(this.mCanvas);
//                    mRootView.setAlpha(0.2f);
                }
                this.mCanvas.restore();
                g();
                this.f350c = false;
                if (this.mUnknow2 != null) {
                    this.mUnknow2.b(bundle);
                }
                this.mHandler.post(new QQBlur(this, stackBlurManager));
            } else {
                return;
            }
        }
        long elapsedRealtime2 = SystemClock.elapsedRealtime();
        this.f330a++;
        this.f345b = (elapsedRealtime2 - elapsedRealtime) + this.f345b;
    }

    public QQblurManager a(View view) {
        this.mView = view;
        return this;
    }

    public QQblurManager b(View view) {
        this.mRootView = view;
        return this;
    }

    public QQblurManager a() {
        Log.d("QQBlur", "onCreate() called");
        this.f331a = this.mRootView.getContext();
        this.mCanvas = new Canvas();
        this.mHandler = new Handler(mHandlerThread.getLooper());
        this.isDrawing = true;
        f();
        return this;
    }

    private void f() {
        if (this.f331a != null && this.mView != null && this.mRootView != null) {
        }
    }

    public void onDraw(View view, Canvas canvas) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            canvas.save();
            canvas.scale((((float) view.getWidth()) * 1.0f) / ((float) bitmap.getWidth()), (((float) view.getHeight()) * 1.0f) / ((float) bitmap.getHeight()));
            if (this.mPaint == null) {
                this.mPaint = new Paint(1);
            }
            this.mPaint.setShader(new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP));
            this.mRectF.set(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight());
            canvas.drawRoundRect(this.mRectF, this.d, this.e, this.mPaint);
            if (this.mDrawable != null) {
                this.mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                this.mDrawable.draw(canvas);
            }
            canvas.restore();
        }
        long elapsedRealtime2 = SystemClock.elapsedRealtime();
        this.unKnowNumber++;
        this.mTime = (elapsedRealtime2 - elapsedRealtime) + this.mTime;
    }

    private static int a(float f, float f2) {
        return (int) Math.ceil((double) (f / f2));
    }

    public static int a(int i) {
        return i % 16 == 0 ? i : (i - (i % 16)) + 16;
    }

    public QQblurManager a(Unkonow2 unkonow2) {
        this.mUnknow2 = unkonow2;
        return this;
    }

    /* renamed from: a */
    public void m53a() {
        this.f343a = true;
    }

    /* renamed from: a */
    private CharSequence m51a(int i) {
        switch (i) {
            case 1:
                return "StackBlur.Native";
            case 2:
                return "StackBlur.RS";
            case 3:
                return "GaussBlur.RS";
            default:
                return "StackBlur.Java";
        }
    }

    public void b() {
        this.f343a = false;
    }

    public void onDestroy() {
        Log.d("QQBlur", "onDestroy() called");
        if (this.isDrawing) {
            this.isDrawing = false;
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
            this.mView = null;
            this.mRootView = null;
            this.mCanvas.setBitmap(null);
            this.mCanvas = null;
            this.mPaint = null;
            this.mUnknow2 = null;
            this.f331a = null;
        }
    }

    /* renamed from: a */
    public boolean m371a() {
        boolean z = true;
        if (this.mUnkonw1 != null) {
            z = this.mUnkonw1.a();
        } else if (this.mView != null) {
            z = this.mView.isDirty();
        }
        View view = this.mRootView;
        if (!this.f343a  && view != null && view.getVisibility() == View.VISIBLE) {
            e();
            view.invalidate();
        }
        return true;
    }

    public void a(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public void a(float f) {
        this.mScale = f;
    }

    /* renamed from: a */
    public void setRaius(int i) {
        this.mRadius = i;
    }

    public void a(Unkonow1 Unkonow1) {
        this.mUnkonw1 = Unkonow1;
    }

    public void a(String str) {
        this.mStr = str;
    }

    public void b(int i) {
        a = i;
    }

    public void setIntNumber(int i, int i2) {
        Log.d("QQBlur", "onPolicyChange() called with: from = [" + i + "], to = [" + i2 + "]");
        this.f330a = 0;
        this.f345b = 0;
        this.f353e = 0;
        this.f = 0;
    }

//    /* renamed from: a */
//    public String m52a() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("方案=").append(a(a)).append(ThemeConstants.THEME_SP_SEPARATOR);
//        stringBuilder.append("缩放倍数=").append(this.mScale).append(ThemeConstants.THEME_SP_SEPARATOR);
//        stringBuilder.append("模糊半径=").append(this.mRadius).append(ThemeConstants.THEME_SP_SEPARATOR);
//        stringBuilder.append("尺寸=" + this.g + VideoMaterialUtil.CRAZYFACE_X + this.h).append(ThemeConstants.THEME_SP_SEPARATOR);
//        stringBuilder.append("空间=" + (mCache / 1000) + "KB").append(ThemeConstants.THEME_SP_SEPARATOR);
//        stringBuilder.append("并发数=" + this.mThreadCount).append(ThemeConstants.THEME_SP_SEPARATOR);
//        stringBuilder.append("主线程采样=[" + String.format("%.2f", new Object[]{Float.valueOf(((float) this.f345b) / ((float) this.f330a))}) + "]ms").append(ThemeConstants.THEME_SP_SEPARATOR);
//        stringBuilder.append("后台线程处理=[" + String.format("%.2f", new Object[]{Float.valueOf(((float) this.f) / ((float) this.f353e))}) + "]ms");
//        return stringBuilder.toString();
//    }

    public void c(int i) {
        this.f348c = i;
    }

    /* renamed from: b */
    public boolean m56b() {
        return this.f350c;
    }

    public void draw() {
        this.mViewList.clear();
        a(this.mView.getRootView(), this.mViewList);
    }

    private void g() {
        for (View view : this.mViewList) {
            if (view != null) {
                a(view, 0);
            }
        }
    }

    private void a(View view, List<View> list) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            list.add(view);
            a(view, 4);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    a(viewGroup.getChildAt(i), (List) list);
                }
            }
        }
    }

    private void a(View view, int i) {
        long uptimeMillis = SystemClock.uptimeMillis();
        try {
            if (f328a == null) {
                f328a = View.class.getDeclaredField("mViewFlags");
                f328a.setAccessible(true);
            }
            f328a.setInt(view, (f328a.getInt(view) & -13) | (i & 12));
        } catch (Throwable e) {
            Log.e("QQBlur", "setViewInvisible: ");
        }
        long uptimeMillis2 = SystemClock.uptimeMillis() - uptimeMillis;
        if (this.j >= 100000) {
            this.j = 0;
            this.k = 0;
        }
        this.j++;
        this.k = uptimeMillis2 + this.k;
        if (this.j % 2000 != 0) {
        }
    }
}
