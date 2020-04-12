package com.ekamard.mofyiv2.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ekamard.mofyiv2.R;


public class ViewPagerAdapter extends PagerAdapter {

    private Context myContext;
    private Integer[] IMAGE_BANNER = {R.drawable.chernobyl, R.drawable.breakingbad, R.drawable.sherlock};

    public ViewPagerAdapter(Context myContext) {
        this.myContext = myContext;
    }

    @Override
    public int getCount() {
        return IMAGE_BANNER.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int pos) {

        LayoutInflater myCustomLayout = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert myCustomLayout != null;
        View myView = myCustomLayout.inflate(R.layout.item_toprated_tvshow, container, false);
        ImageView myBanner = myView.findViewById(R.id.img_tv_toprated);
        myBanner.setImageResource(IMAGE_BANNER[pos]);

        ViewPager myViewPager = (ViewPager) container;
        myViewPager.addView(myView, 0);
        return myView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager myViewPager = (ViewPager) container;
        View view = (View) object;
        myViewPager.removeView(view);
    }

}
