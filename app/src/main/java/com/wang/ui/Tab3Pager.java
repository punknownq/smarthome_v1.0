package com.wang.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpeng.jptabbar.JPTabBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wang.R;
import com.wang.util.HttpAsyncTask;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab3Pager extends Fragment {
    private JPTabBar mTabBar;
    private MessageBroadcastReceiver receiver;

    private RecyclerView mRecyclerView2;
    private SimpleAdapter mAdapter2;
    private List<String> mDatas2;


    private static final String url_androidHistory = "androidHistory";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.tab3,null);
        init(layout);
        return layout;

    }

    /**
     * 初始化
     */
    private void init(View layout) {
        mTabBar = ((MainActivity)getActivity()).getTabbar();

        mRecyclerView2 =layout.findViewById(R.id.id_recyclerView2);

        registerBroadcast();
        initDatas2();

         /*这是历史消息刷新的接口*/
        RefreshLayout refreshLayout3 = (RefreshLayout)layout.findViewById(R.id.refreshLayout3);
        refreshLayout3.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                Toast.makeText(getActivity(),"refresh！",Toast.LENGTH_SHORT).show();
                String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
                OkHttpUtils
                        .post()
                        .url(getResources().getString(R.string.BaseURL)+url_androidHistory)
                        .addParams("userName",string)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("message", response);
                                Gson gson=new Gson();
                                ArrayList<String> message =gson.fromJson(response, ArrayList.class);
                                mDatas2.clear();
                                Log.i("message",""+ message);
                                mDatas2.addAll(message);
                                //更新数据
                                mAdapter2.notifyDataSetChanged();
                                mRecyclerView2.setAdapter(mAdapter2);

                            }
                        });
            }
        });
        refreshLayout3.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

         /*这是消息列表列表*/
        mAdapter2 =new SimpleAdapter(getActivity(),mDatas2);
        mRecyclerView2.setAdapter(mAdapter2);

        //设置RecycleView的布局管理
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView2.setLayoutManager(linearLayoutManager);
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
        mAdapter2.setOnItemClickLisener(new SimpleAdapter.OnItemClickLisener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(),"click:"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(),"click:"+position,Toast.LENGTH_SHORT).show();
            }
        });

    }
    //动态注册广播
    private void registerBroadcast() {
        receiver = new MessageBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.ssy.mina.broadcast");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
    }

    //接收发送的广播
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        private PowerManager.WakeLock mWakeLock;

        @Override
        public void onReceive(Context context, Intent intent) {

            String msg = intent.getStringExtra("message");
            sendNotification(msg,getView());
            String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
            OkHttpUtils
                    .post()
                    .url(getResources().getString(R.string.BaseURL)+url_androidHistory)
                    .addParams("userName",string)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i("message", response);
                            Gson gson=new Gson();
                            ArrayList<String> message =gson.fromJson(response, ArrayList.class);
                            mDatas2.clear();
                            Log.i("message",""+ message);
                            mDatas2.addAll(message);
                            //更新数据
                            mAdapter2.notifyDataSetChanged();
                            mRecyclerView2.setAdapter(mAdapter2);

                        }
                    });
            /*这里是唤醒屏幕*/
            PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "SimpleTimer");
            mWakeLock.acquire(5);//这里唤醒锁，用这种方式要记得在适当的地方关闭锁，
            //Toast.makeText(getActivity(), "esp8266发送过来的数据："+msg, Toast.LENGTH_SHORT).show();
        }
    }
    private void serviceCONN(byte[] bytes, String username, String sessionId) {
        //将byte数组转化成字符串
        String data_msg = Arrays.toString(bytesToInt(bytes));
        //服务器的url
        String url = "http://192.168.2.176:8080/BackStageSystem/servlet/AppControlServlet";
        //将数据拼接起来
        String data = "username=" + username + "&sessionId=" + sessionId + "&data=" + data_msg;
        String[] str = new String[]{url, data};
        //发出一个请求
        new HttpAsyncTask(getActivity(), new HttpAsyncTask.PriorityListener() {

            @Override
            public void setActivity(int code) {
                switch (code) {
                    case 200:
                        //如果返回的resultCode是200,那么说明APP的数据传送成功，并成功解析返回的json数据
                        Toast.makeText(getActivity(), "发送数据：[0x01,0x02,0x03]", Toast.LENGTH_SHORT).show();
                        break;
                    case 202:
                        Toast.makeText(getActivity(), "设备离线状态", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getActivity(), "网络传输异常", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        }).execute(str);
    }

    /**
     * byte转化为int
     */
    public static int[] bytesToInt(byte[] src) {
        int value[] = new int[src.length];
        for (int i = 0; i < src.length; i++) {
            value[i] = src[i] & 0xFF;
        }
        return value;
    }
    public void initDatas2() {
        //TODO:这里是要获取最新的历史消息列表
        mDatas2=new ArrayList<>();
        String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
        OkHttpUtils
                .post()
                .url(getResources().getString(R.string.BaseURL)+url_androidHistory)
                .addParams("userName",string)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("message", response);
                        Gson gson=new Gson();
                        ArrayList<String> message =gson.fromJson(response, ArrayList.class);
                        mDatas2.clear();
                        mAdapter2.notifyDataSetChanged();
                        Log.i("message",""+ message);
                        Iterator i=message.iterator();
                        while(i.hasNext() ){
                            String s= (String) i.next();
                            mDatas2.add(s);
                        }
                        //更新数据
                        mAdapter2.notifyDataSetChanged();

                    }
                });


    }
    public void sendNotification(String msg,View view){
        //实例化通知管理器
        NotificationManager notificationManager= (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化通知
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getActivity());
        builder.setContentTitle("智居");//设置通知标题
        builder.setContentText(msg);//设置通知内容
        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);//设置通知的方式，震动、LED灯、音乐等
        builder.setAutoCancel(true);//点击通知后，状态栏自动删除通知
        builder.setSmallIcon(android.R.drawable.ic_media_play);//设置小图标
        builder.setContentIntent(PendingIntent.getActivity(getActivity(),0x102,new Intent(getActivity(),MainActivity.class),0));//设置点击通知后将要启动的程序组件对应的PendingIntent
        Notification notification=builder.build();


        //发送通知
        notificationManager.notify(0x101,notification);

    }




}
