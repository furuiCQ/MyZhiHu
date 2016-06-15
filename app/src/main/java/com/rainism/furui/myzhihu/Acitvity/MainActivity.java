package com.rainism.furui.myzhihu.Acitvity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.rainism.furui.myzhihu.Adapter.MainNewsAdpater;
import com.rainism.furui.myzhihu.Fragment.ContentFragment;
import com.rainism.furui.myzhihu.Fragment.MainFragment;
import com.rainism.furui.myzhihu.Model.News;
import com.rainism.furui.myzhihu.Model.TopNews;
import com.rainism.furui.myzhihu.R;
import com.rainism.furui.myzhihu.View.CustomViewPager;
import com.rainism.furui.myzhihu.View.MainListview;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends FragmentActivity {

    ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    ContentFragment contentFragment = new ContentFragment();
    MainFragment mainFragment = new MainFragment();
    CustomViewPager viewPager;
    News news;
    int pagerNumb=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        viewPager = (CustomViewPager) findViewById(R.id.main_viewpager);

        fragments.add(mainFragment);
        fragments.add(contentFragment);

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(pagerNumb);
        viewPager.setOffscreenPageLimit(1);
    }

    public void setPagerNumb(int numb){
        pagerNumb=numb;
        viewPager.setCurrentItem(pagerNumb);

    }
    public void setFragmentData(News news){
        this.news=news;
        contentFragment.loadData();

    }
    public News getFragmentData(){
        return this.news;
    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(pagerNumb==1){
                setPagerNumb(0);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}