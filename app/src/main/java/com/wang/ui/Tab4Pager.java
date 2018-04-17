package com.wang.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jpeng.jptabbar.JPTabBar;
import com.wang.R;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab4Pager extends Fragment implements View.OnClickListener {
    private JPTabBar mTabBar;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.tab4,null);
        layout.findViewById(R.id.button1).setOnClickListener(this);
        return layout;
    }
    private void init(View layout) {
        mTabBar = ((MainActivity)getActivity()).getTabbar();
    }
    @Override
    public void onClick(View view) {
        //JPTabBar tabBar = (JPTabBar) ((Activity)getContext()).findViewById(R.id.tabbar);
        mTabBar.setTabTypeFace("fonts/Jaden.ttf");
    }

}
