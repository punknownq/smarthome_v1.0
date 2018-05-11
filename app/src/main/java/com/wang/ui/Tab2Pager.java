package com.wang.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.animate.AnimationType;
import com.suke.widget.SwitchButton;
import com.wang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab2Pager extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup mGroup;

    private JPTabBar mTabBar;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout =inflater.inflate(R.layout.tab2,null);
        init(layout);
        return layout;
    }

    private void init(View layout) {
        mTabBar = ((MainActivity)getActivity()).getTabbar();
//        mGroup = (RadioGroup) layout.findViewById(R.id.radioGroup);
//        mGroup.setOnCheckedChangeListener(this);
        com.suke.widget.SwitchButton switchButton = layout.findViewById(R.id.switch_button0);

          /*这是第三个界面的按钮*/
        switchButton.setChecked(false);
        switchButton.isChecked();
        switchButton.toggle();     //switch state
        //switchButton.toggle(false);//switch without animation
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(true);//disable button
        switchButton.setEnableEffect(true);//disable the switch animation
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if(isChecked==true){
                    Toast.makeText(getActivity(),"true click.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(),"flase click.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//        switch (checkedId){
//            case R.id.radioButton1:
//                mTabBar.setAnimation(AnimationType.SCALE);
//                break;
//            case R.id.radioButton2:
//                mTabBar.setAnimation(AnimationType.SCALE2);
//                break;
//            case R.id.radioButton3:
//                mTabBar.setAnimation(AnimationType.JUMP);
//                break;
//            case R.id.radioButton4:
//                mTabBar.setAnimation(AnimationType.FLIP);
//
//                break;
//            case R.id.radioButton5:
//                mTabBar.setAnimation(AnimationType.ROTATE);
//                break;
//        }
    }

}