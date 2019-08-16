package com.verzqli.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.verzqli.vmui.widget.QQBlurView;
import com.verzqli.vmui.widget.VMUIPayPasswordView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QQBlurView vb = findViewById(R.id.blur);
        View view = findViewById(R.id.view);
        vb.setEnableBlur(true);
        vb.b(vb);
        vb.a(view);
        vb.a(8);
        vb.c(-1);
        vb.d();
        vb.a();
    }
}
