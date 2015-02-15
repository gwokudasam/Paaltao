package com.paaltao.Controller;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

/**
 * Created by Arindam on 28-Jan-15.
 */
public class ViewPagerAdapter extends PagerAdapter {
    // private Context mContext;
    private Vector<View> pages;

    public ViewPagerAdapter(Context context, Vector<View> pages) {
        // this.mContext=context;
        this.pages = pages;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View page = pages.get(position);
        container.addView(page);
        return page;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}