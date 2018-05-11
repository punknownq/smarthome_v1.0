package com.wang.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpeng.jptabbar.JPTabBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.suke.widget.SwitchButton;
import com.wang.R;
import com.wang.base.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab1Pager extends Fragment implements View.OnClickListener, TextWatcher,AddhomeDialogFragment.NameInputListener {


    private JPTabBar mTabBar;
    private List<String> mDatas0;
    private List<String> mDatas1;
    private  RecyclerView mRecyclerView0;
    private Button add0;
    private Button delete0;
    private Button check0;
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
    private EditText mHomename;
    private TextView itemname;
    /*声明登录所用URL*/
    private static final String url_lightct = "lightController";
    private static final String url_refresh = "androidData/rooms";
    private static final String url_addhome = "androidData/addRoom";
    private static final String url_deletehome = "androidData/deleteRoom";
    private static final String url_getitem = "androidDevice/getDevice";


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
        check0=layout.findViewById(R.id.id_check0);
        back0=layout.findViewById(R.id.id_back0);
        back1=layout.findViewById(R.id.id_back1);
        mRecyclerView0 =layout.findViewById(R.id.id_recyclerView0);
        add1=layout.findViewById(R.id.id_add1);
        delete1=layout.findViewById(R.id.id_delete1);
        mRecyclerView1 =layout.findViewById(R.id.id_recyclerView1);
        Tab1step0=layout.findViewById(R.id.Tab1_step0);
        Tab1step1=layout.findViewById(R.id.Tab1_step1);
        Tab1step2=layout.findViewById(R.id.Tab1_step2);
        final com.suke.widget.SwitchButton switchButton = layout.findViewById(R.id.switch_button);
        itemname=layout.findViewById(R.id.itemName);





        /*这是房间刷新的接口*/
        RefreshLayout refreshLayout0 = (RefreshLayout)layout.findViewById(R.id.refreshLayout0);
        refreshLayout0.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                //TODO:应该是获取userName
                String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
                OkHttpUtils
                        .post()
                        .url(getResources().getString(R.string.BaseURL)+url_refresh)
                        .addParams("userName",string)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("room", response);
                                Gson gson=new Gson();
                                ArrayList<String> room =gson.fromJson(response, ArrayList.class);
                                mDatas0.clear();
                                Log.i("room1",""+ room);
                                mDatas0.addAll(room);
                                //更新数据
                                mAdapter0.notifyDataSetChanged();
                                mRecyclerView0.setAdapter(mAdapter0);

                            }
                        });

            }
        });
        refreshLayout0.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });


        /*这是设备刷新的接口*/
        RefreshLayout refreshLayout2 = (RefreshLayout)layout.findViewById(R.id.refreshLayout2);
        refreshLayout2.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                Toast.makeText(getActivity(),"refresh！",Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout2.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });


        add0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view1 = inflater.inflate(R.layout.dialog_addhome, null);
                mHomename=(EditText) view1.findViewById(R.id.et_homename);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(view1)
                        // Add action buttons
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
                                        OkHttpUtils
                                                .post()
                                                .url(getResources().getString(R.string.BaseURL)+url_addhome)
                                                .addParams("userName",string)
                                                .addParams("roomName",mHomename.getText().toString())
                                                .build()
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {

                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        Log.i("addroom",response);
                                                        int str = Integer.parseInt(response);
                                                        if(str ==1 )
                                                        {
                                                            String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
                                                            OkHttpUtils
                                                                    .post()
                                                                    .url(getResources().getString(R.string.BaseURL)+url_refresh)
                                                                    .addParams("userName",string)
                                                                    .build()
                                                                    .execute(new StringCallback() {
                                                                        @Override
                                                                        public void onError(Call call, Exception e, int id) {

                                                                        }

                                                                        @Override
                                                                        public void onResponse(String response, int id) {
                                                                            Log.i("room", response);
                                                                            Gson gson=new Gson();
                                                                            ArrayList<String> room =gson.fromJson(response, ArrayList.class);
                                                                            mDatas0.clear();
                                                                            Log.i("room1",""+ room);
                                                                            mDatas0.addAll(room);
                                                                            //更新数据
                                                                            mAdapter0.notifyDataSetChanged();
                                                                            mRecyclerView0.setAdapter(mAdapter0);

                                                                        }
                                                                    });
                                                        }
                                                        else{
                                                            Toast.makeText(getActivity(),"添加房间失败!",Toast.LENGTH_SHORT).show();
                                                        }



                                                    }
                                                });

                                    }
                                }).setNegativeButton("取消", null);
                 builder.create().show();
            }
        });

        Drawable drawable_delete0=getResources().getDrawable(R.drawable.drable_delete);
        if (drawable_delete0!= null )
        {
            drawable_delete0.setBounds(0 , 0 ,45,45);
            delete0.setCompoundDrawables(drawable_delete0, null, null, null);
        }
        delete0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(mAdapter0.getItemCount()<=1){}
//                else
//                mAdapter0.deleteData(1);
                check0.setVisibility(View.VISIBLE);
                delete0.setVisibility(View.GONE);

            }
        });
        check0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(mAdapter0.getItemCount()<=1){}
//                else
//                mAdapter0.deleteData(1);
                check0.setVisibility(View.GONE);
                delete0.setVisibility(View.VISIBLE);
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
                mAdapter1.addData(1,"nihao");
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

                if(isChecked==true){
                    Toast.makeText(getActivity(),"true click.",Toast.LENGTH_SHORT).show();
                    OkHttpUtils
                            .post()
                            .url(getResources().getString(R.string.BaseURL)+url_lightct)
                            .addParams("macAddress","CONN_LED1")
                            .addParams("status","1")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    int str = Integer.parseInt(response);
                                    Log.i("lightText", str+"");
                                    /*将Message送到Handler进行UI更新*/
                                    if(str==1) {
                                        Toast.makeText(getActivity(),"success!",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getActivity(),"false!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    OkHttpUtils
                            .post()
                            .url(getResources().getString(R.string.BaseURL)+url_lightct)
                            .addParams("macAddress","CONN_LED1")
                            .addParams("status","0")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    int str = Integer.parseInt(response);
                                    Log.i("lightText", str+"");
                                    /*将Message送到Handler进行UI更新*/
                                    if(str==1) {
                                        Toast.makeText(getActivity(),"success!",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getActivity(),"false!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        /*初始化列表数据*/
        initDatas0();
        initDatas1();


        /*这是第一个列表即房间列表*/
        mAdapter0 =new SimpleAdapter(getActivity(),mDatas0);
        mRecyclerView0.setAdapter(mAdapter0);

        //设置RecycleView的布局管理
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView0.setLayoutManager(linearLayoutManager);
       // mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView0.setItemAnimator(new DefaultItemAnimator());
        mAdapter0.setOnItemClickLisener(new SimpleAdapter.OnItemClickLisener() {
            @Override
            public void onItemClick(View view, int position) {
                CardView room = (CardView) linearLayoutManager.findViewByPosition(position);
                TextView room1 = (TextView) room.getChildAt(0);
                String s = room1.getText().toString();
                Log.i("room3",s);
                String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
                OkHttpUtils
                        .post()
                        .url(getResources().getString(R.string.BaseURL)+url_getitem)
                        .addParams("userName",string)
                        .addParams("roomName",s)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("item", response);
                                Gson gson=new Gson();
                                ArrayList<String> item =gson.fromJson(response, ArrayList.class);
                                mDatas1.clear();
                                Log.i("item1",""+ item);
                                mDatas1.addAll(item);
                                //更新数据
                                mAdapter1.notifyDataSetChanged();
                                mRecyclerView1.setAdapter(mAdapter1);

                            }
                        });
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
            public void onItemLongClick(View view, final int position) {
                Toast.makeText(getActivity(),"long click:"+position,Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view1 = inflater.inflate(R.layout.dialog_deletehome, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(view1)
                        // Add action buttons
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        CardView room = (CardView) linearLayoutManager.findViewByPosition(position);
                                        TextView room1 = (TextView) room.getChildAt(0);
                                        String s = room1.getText().toString();
                                        String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
                                        OkHttpUtils
                                                .post()
                                                .url(getResources().getString(R.string.BaseURL)+url_deletehome)
                                                .addParams("userName",string)
                                                .addParams("roomName",s)
                                                .build()
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {

                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        Log.i("addroom",response);
                                                        int str = Integer.parseInt(response);
                                                        if(str ==1 )
                                                        {
                                                            String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
                                                            OkHttpUtils
                                                                    .post()
                                                                    .url(getResources().getString(R.string.BaseURL)+url_refresh)
                                                                    .addParams("userName",string)
                                                                    .build()
                                                                    .execute(new StringCallback() {
                                                                        @Override
                                                                        public void onError(Call call, Exception e, int id) {

                                                                        }

                                                                        @Override
                                                                        public void onResponse(String response, int id) {
                                                                            Log.i("room", response);
                                                                            Gson gson=new Gson();
                                                                            ArrayList<String> room =gson.fromJson(response, ArrayList.class);
                                                                            mDatas0.clear();
                                                                            Log.i("room1",""+ room);
                                                                            mDatas0.addAll(room);
                                                                            //更新数据
                                                                            mAdapter0.notifyDataSetChanged();
                                                                            mRecyclerView0.setAdapter(mAdapter0);

                                                                        }
                                                                    });
                                                        }  
                                                        else{
                                                            Toast.makeText(getActivity(),"删除房间失败!",Toast.LENGTH_SHORT).show();
                                                        }



                                                    }
                                                });

                                    }
                                }).setNegativeButton("取消", null);
                builder.create().show();

            }
        });
        //设置Recycleview的Irem间分割线
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

         /*这是第二个列表即设备列表*/
        mAdapter1 =new SimpleAdapter(getActivity(),mDatas1);
        mRecyclerView1.setAdapter(mAdapter1);
        //设置RecycleView的布局管理
        final LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView1.setLayoutManager(linearLayoutManager1);
        // mRecyclerView1.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView1.setItemAnimator(new DefaultItemAnimator());


        mAdapter1.setOnItemClickLisener(new SimpleAdapter.OnItemClickLisener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(),"click:"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //TODO：这里要写一个获取设备最新状态的事件
                /*改变下一个设备页面显示的设备名称*/
                CardView item = (CardView) linearLayoutManager1.findViewByPosition(position);
                TextView item1 = (TextView)item.getChildAt(0);
                String s = item1.getText().toString();
                itemname.setText(s);
                String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
                OkHttpUtils
                        .post()
                        .url(getResources().getString(R.string.BaseURL)+url_deletehome)
                        .addParams("userName",string)
                        .addParams("itemName",s)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("itemState",response);
                                int str = Integer.parseInt(response);
                                if(str ==1 )
                                {
                                    switchButton.setChecked(true);
                                }
                                else{
                                    switchButton.setChecked(false);
                                }



                            }
                        });
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
        String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
        OkHttpUtils
                .post()
                .url(getResources().getString(R.string.BaseURL)+url_refresh)
                .addParams("userName",string)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("room", response);
                        Gson gson=new Gson();
                        ArrayList<String> room =gson.fromJson(response, ArrayList.class);
                        mDatas0.clear();
                        mAdapter0.notifyDataSetChanged();
                        Log.i("room1",""+ room);
                        Iterator i=room.iterator();
                        while(i.hasNext() ){
                            String s= (String) i.next();
                            mDatas0.add(s);
                        }
                        //更新数据
                        mAdapter0.notifyDataSetChanged();

                    }
                });
    }
    public void initDatas1() {
        mDatas1=new ArrayList<>();
//        for(int i='A';i<='z';i++){
//            mDatas0.add(""+(char)i);
//        }
       // mDatas1.add("灯");

    }
    private void post() {
        String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        User user = new User(string, "2", "3");
//        user.setUsername("12ssssece6");
        //  book.setPrice(59+"");
        //使用Gson 添加 依赖 compile 'com.google.code.gson:gson:2.8.1'
        final Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        String json = gson.toJson(user);

        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);

        Request request = new Request.Builder()
                .url(getResources().getString(R.string.BaseURL)+url_lightct)//请求的url    ,这里要修改一下
                .post(requestBody)
                .build();

        //创建/Call
        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("boolean_context", "Error");
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                //这里不知道返回什么数据类型的，要问一下大亮
            }
        });
    }


    @Override
    public void onNameInputListener(String homeName) {

        Toast.makeText(getActivity(), "帐号：" + homeName ,
                Toast.LENGTH_SHORT).show();
    }
}

