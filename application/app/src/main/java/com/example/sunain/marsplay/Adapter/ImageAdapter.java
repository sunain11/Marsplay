package com.example.sunain.marsplay.Adapter;

import android.database.DataSetObserver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.sunain.marsplay.Fragment.ImageFragment;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private boolean b;
    public ImageAdapter(FragmentManager manager,boolean b)
    {
        super(manager);
        this.b=b;
//        mFragmentList.add(new AddImageFragment());
//        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }
    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public long getItemId(int position) {
        return System.currentTimeMillis();
    }

    public void addFragment(Uri uri)
    {
        mFragmentList.add(ImageFragment.newInstance(uri.toString(),b));
        notifyDataSetChanged();
    }

    public void clearList()
    {
        mFragmentList.clear();
        notifyDataSetChanged();
    }

    public void addFragment(Fragment fragment)
    {
        try
        {
            mFragmentList.add(fragment);

        }catch (Exception e)
        {Log.e("Adapter 1",e.toString());}

        notifyDataSetChanged();
    }
}
