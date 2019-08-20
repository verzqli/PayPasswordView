package com.verzqli.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.verzqli.vmui.widget.QQBlurView;
import com.verzqli.vmui.widget.VMUIPayPasswordView;
import com.verzqli.vmui.widget.blur.TouchImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final List<String> LIST = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QQBlurView vb = findViewById(R.id.blur);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("" + i);
        }
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new FeedsMainAdapter(this, list));
        vb.setEnableBlur(true);
        vb.b(vb);
        vb.a(listView);
        vb.a(8);
        vb.c(-1);
        vb.d();
        vb.a();
//        Bitmap bitmap  = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
//        bitmap.eraseColor(0);
//        mm.setImageBitmap(bitmap);


    }

    public class FeedsMainAdapter extends BaseAdapter {

        private List<String> mList;
        private Context mContext;

        public FeedsMainAdapter(Context context, List<String> list) {
            this.mContext = context;
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public String getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            return convertView;
        }

        class ViewHolder {
            public TextView mTextView;
        }
    }

}
