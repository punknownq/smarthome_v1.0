package com.wang.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jpeng.jptabbar.JPTabBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.suke.widget.SwitchButton;
import com.wang.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab1Pager extends Fragment implements View.OnClickListener, TextWatcher {


    private JPTabBar mTabBar;
    private  RecyclerView mRecyclerView0;
    private Button add0;
    private Button delete0;
    private List<String> mDatas0;
    private List<String> mDatas1;
    private SimpleAdapter mAdapter0;
    private SimpleAdapter mAdapter1;
    private  RecyclerView mRecyclerView1;
    private Button add1;
    private Button delete1;
    private Button back0;
    private Button back1;
    private LinearLayout Tab1step0;
    private LinearLayout Tab1step1;
    private LinearLayout Tab1step2;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.tab1, null);
        init(layout);
        return layout;
    }

    private void init(View layout) {
        mTabBar = ((MainActivity)getActivity()).getTabbar();
        add0=layout.findViewById(R.id.id_add0);
        delete0=layout.findViewById(R.id.id_delete0);
        back0=layout.findViewById(R.id.id_back0);
        back1=layout.findViewById(R.id.id_back1);
        mRecyclerView0 =layout.findViewById(R.id.id_recyclerView0);
        add1=layout.findViewById(R.id.id_add1);
        delete1=layout.findViewById(R.id.id_delete1);
        mRecyclerView1 =layout.findViewById(R.id.id_recyclerView1);
        Tab1step0=layout.findViewById(R.id.Tab1_step0);
        Tab1step1=layout.findViewById(R.id.Tab1_step1);
        Tab1step2=layout.findViewById(R.id.Tab1_step2);
        com.suke.widget.SwitchButton switchButton = layout.findViewById(R.id.switch_button);

        RefreshLayout refreshLayout = (RefreshLayout)layout.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

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
                    Toast.makeText(getActivity(),"false click.",Toast.LENGTH_SHORT).show();
            }
        });


        add0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter0.addData(1);
            }
        });
        delete0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAdapter0.getItemCount()<=1){}
                else
                mAdapter0.deleteData(1);
            }
        });
        back0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*滑动效果，到下一个页面*/
                Animator animatorright = AnimatorInflater.loadAnimator(getActivity(), R.animator.animator_register_steprightexit);
                animatorright.setTarget(Tab1step1);
                animatorright.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tab1step1.setVisibility(View.GONE);
                    }
                }, 500);
                Tab1step0.setVisibility(View.VISIBLE);
                Animator animatorleft = AnimatorInflater.loadAnimator(getActivity(), R.animator.animator_register_stepleftenter);
                animatorleft.setTarget(Tab1step0);
                animatorleft.start();

            }
        });
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter1.addData(1);
            }
        });
        delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAdapter1.getItemCount()<=1){}
                else
                    mAdapter1.deleteData(1);
            }
        });
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*滑动效果，到下一个页面*/
                Animator animatorright = AnimatorInflater.loadAnimator(getActivity(), R.animator.animator_register_steprightexit);
                animatorright.setTarget(Tab1step2);
                animatorright.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tab1step2.setVisibility(View.GONE);
                    }
                }, 500);
                Tab1step1.setVisibility(View.VISIBLE);
                Animator animatorleft = AnimatorInflater.loadAnimator(getActivity(), R.animator.animator_register_stepleftenter);
                animatorleft.setTarget(Tab1step1);
                animatorleft.start();

            }
        });

        initDatas0();
        initDatas1();
        //  initViews();

        /*这是第一个列表*/
        mAdapter0 =new SimpleAdapter(getActivity(),mDatas0);
        mRecyclerView0.setAdapter(mAdapter0);
        //设置RecycleView的布局管理
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView0.setLayoutManager(linearLayoutManager);
       // mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView0.setItemAnimator(new DefaultItemAnimator());
        mAdapter0.setOnItemClickLisener(new SimpleAdapter.OnItemClickLisener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(),"click:"+position,Toast.LENGTH_SHORT).show();
                 /*滑动效果，到下一个页面*/
                Animator animatorleft = AnimatorInflater.loadAnimator(getActivity(), R.animator.animator_register_stepleftexit);
                animatorleft.setTarget(Tab1step0);
                animatorleft.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tab1step0.setVisibility(View.GONE);
                    }
                }, 500);
                Tab1step1.setVisibility(View.VISIBLE);
                Animator animatorright = AnimatorInflater.loadAnimator(getActivity(), R.animator.animator_register_steprightenter);
                animatorright.setTarget(Tab1step1);
                animatorright.start();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(),"long click:"+position,Toast.LENGTH_SHORT).show();
            }
        });
        //设置Recycleview的Irem间分割线
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

         /*这是第二个列表*/
        mAdapter1 =new SimpleAdapter(getActivity(),mDatas1);
        mRecyclerView1.setAdapter(mAdapter1);
        //设置RecycleView的布局管理
        //LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        //mRecyclerView.setLayoutManager(linearLayoutManager);
         mRecyclerView1.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView1.setItemAnimator(new DefaultItemAnimator());
        mAdapter1.setOnItemClickLisener(new SimpleAdapter.OnItemClickLisener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(),"click:"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                 /*滑动效果，到下一个页面*/
                Animator animatorleft = AnimatorInflater.loadAnimator(getActivity(), R.animator.animator_register_stepleftexit);
                animatorleft.setTarget(Tab1step1);
                animatorleft.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tab1step1.setVisibility(View.GONE);
                    }
                }, 500);
                Tab1step2.setVisibility(View.VISIBLE);
                Animator animatorright = AnimatorInflater.loadAnimator(getActivity(), R.animator.animator_register_steprightenter);
                animatorright.setTarget(Tab1step2);
                animatorright.start();
            }
        });
        //设置Recycleview的Irem间分割线
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }


    @Override
    public void onClick(View v) {


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s!=null&&s.toString().equals("0")){
            mTabBar.showBadge(0, ""+0,true);
            mTabBar.hideBadge(0);
            return;
        }
        if (s.toString().equals("")) {
            mTabBar.showBadge(0, ""+0,true);
            return;
        }
        int count = Integer.parseInt(s.toString());
        if(mTabBar!=null)
            mTabBar.showBadge(0, count+"",true);
    }

    public void clearCount() {
        //当徽章拖拽爆炸后,一旦View被销毁,不判断就会空指针异常
//        if(mNumberEt!=null)
//            mNumberEt.setText("0");
    }
    public void initDatas0() {
        mDatas0=new ArrayList<>();
//        for(int i='A';i<='z';i++){
//            mDatas0.add(""+(char)i);
//        }
        mDatas0.add(0,"浴室");

    }
    public void initDatas1() {
        mDatas1=new ArrayList<>();
//        for(int i='A';i<='z';i++){
//            mDatas0.add(""+(char)i);
//        }
        mDatas1.add("灯");

    }


}

