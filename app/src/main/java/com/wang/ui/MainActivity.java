package com.wang.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jpeng.jptabbar.BadgeDismissListener;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.animate.AnimationType;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;
import com.wang.R;
import com.wang.service.MinaService;
import com.wang.util.HttpAsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements BadgeDismissListener, OnTabSelectListener {

    @Titles
    private static final int[] mTitles = {R.string.tab1, R.string.tab2, R.string.tab3, R.string.tab4};

    @SeleIcons
    private static final int[] mSeleIcons = {R.mipmap.tab1_selected, R.mipmap.tab2_selected, R.mipmap.tab3_selected, R.mipmap.tab4_selected};

    @NorIcons
    private static final int[] mNormalIcons = {R.mipmap.tab1_normal, R.mipmap.tab2_normal, R.mipmap.tab3_normal, R.mipmap.tab4_normal};
    @BindView(R.id.view_pager)
    ViewPager mPager;
    @BindView(R.id.tabbar)
    JPTabBar mTabbar;

    private List<Fragment> list = new ArrayList<>();

//    private ViewPager mPager;

//    private JPTabBar mTabbar;

    private Tab1Pager mTab1;

    private Tab2Pager mTab2;

    private Tab3Pager mTab3;

    private Tab4Pager mTab4;

    private Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
//        mTabbar = (JPTabBar) findViewById(R.id.tabbar);
//        mPager = (ViewPager) findViewById(R.id.view_pager);
//        mTabbar.setTitles("asd","页面二","页面三","页面四").setNormalIcons(R.mipmap.tab1_normal,R.mipmap.tab2_normal,R.mipmap.tab3_normal,R.mipmap.tab4_normal)
//                .setSelectedIcons(R.mipmap.tab1_selected,R.mipmap.tab2_selected,R.mipmap.tab3_selected,R.mipmap.tab4_selected).generate();
        mTabbar.setTabTypeFace("fonts/Jaden.ttf");
        mTab1 = new Tab1Pager();
        mTab2 = new Tab2Pager();
        mTab3 = new Tab3Pager();
        mTab4 = new Tab4Pager();
        mTabbar.setGradientEnable(true);
        mTabbar.setTabListener(this);
        list.add(mTab1);
        list.add(mTab2);
        list.add(mTab3);
        list.add(mTab4);
        //开启长连接服务
        serviceIntent = new Intent(this, MinaService.class);
        startService(serviceIntent);



        mPager.setAdapter(new Adapter(getSupportFragmentManager(), list));
        mTabbar.setContainer(mPager);
        mTabbar.setPageAnimateEnable(false);
        mTabbar.setAnimation(AnimationType.SCALE2);
        //设置Badge消失的代理
        mTabbar.setDismissListener(this);
        mTabbar.setTabListener(this);
        if (mTabbar.getMiddleView() != null)
            mTabbar.getMiddleView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "中间点击", Toast.LENGTH_SHORT).show();
                    PackageManager packageManager = getPackageManager();
                    Intent intent;
                    intent = packageManager.getLaunchIntentForPackage("com.wang.ar1");
                    if (intent == null) {
                        Toast.makeText(MainActivity.this, "未安装", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(intent);
                    }

                }
            });
    }

    @Override
    public void onDismiss(int position) {
        mTab1.clearCount();
    }


    @Override
    public void onTabSelect(int index) {
       // Toast.makeText(MainActivity.this, "choose the tab index is " + index, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onInterruptSelect(int index) {
//        if(index==2){
//            //如果这里有需要阻止Tab被选中的话,可以return true
//            return true;
//        }
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时关掉长连接服务
        stopService(serviceIntent);
    }



    public JPTabBar getTabbar() {
        return mTabbar;
    }



}
