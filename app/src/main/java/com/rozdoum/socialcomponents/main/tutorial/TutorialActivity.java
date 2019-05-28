package com.rozdoum.socialcomponents.main.tutorial;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.login.LoginActivity;
import com.rozdoum.socialcomponents.main.main.MainActivity;
import com.rozdoum.socialcomponents.utils.AndroidUtil;

public class TutorialActivity extends AppCompatActivity {

    private static final int MAX_STEP = 5;
    private Boolean isNotification;

    private ViewPager mViewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private RelativeLayout mRegister, mEmail;

    public static final String ARG_REGISTER_IS_COMPLETED = "ARG_REGISTER_IS_COMPLETED";
    public static final String ARG_PAYMENT_REQUIRED = "ARG_PAYMENT_REQUIRED";
    private boolean register_completed, payment_required;

    private String about_description_array[];
    private int about_images_array[] = {
            R.drawable.wizard_4,
            R.drawable.wizard_2,
            R.drawable.wizard_3,
            R.drawable.wizard_1,
            R.drawable.background_image,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        about_description_array = new String[]{
            String.valueOf(AndroidUtil.getSpannedText(TutorialActivity.this.getResources().getString(R.string.wizard_text_1))),
            String.valueOf(AndroidUtil.getSpannedText(TutorialActivity.this.getResources().getString(R.string.wizard_text_2))),
            String.valueOf(AndroidUtil.getSpannedText(TutorialActivity.this.getResources().getString(R.string.wizard_text_3))),
            String.valueOf(AndroidUtil.getSpannedText(TutorialActivity.this.getResources().getString(R.string.wizard_text_4))),
            String.valueOf(AndroidUtil.getSpannedText(TutorialActivity.this.getResources().getString(R.string.wizard_text_5))),
        };

        initialize();
        getArgs();
    }

    private void getArgs() {
        Intent i = getIntent();
        payment_required = i.getBooleanExtra(ARG_PAYMENT_REQUIRED, true);
        register_completed = i.getBooleanExtra(ARG_REGISTER_IS_COMPLETED, false);
    }

    private void initialize() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        mViewPager = findViewById(R.id.view_pager);
        mEmail = findViewById(R.id.btn_next2);

        // adding bottom dots
        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(myViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        mEmail.setOnClickListener(v -> {
            startActivity(new Intent(TutorialActivity.this, MainActivity.class));
            finish();
        });
    }

    private void textProgress(int current_index) {
        TextView description = findViewById(R.id.description);
        description.setText(about_description_array[current_index]);
    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 20;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.circle);
            dots[i].setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_IN);
        }
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
            textProgress(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {}

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.activity_wizard_item, container, false);

            ImageView image = view.findViewById(R.id.image);
            image.setImageResource(about_images_array[position]);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return about_description_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}