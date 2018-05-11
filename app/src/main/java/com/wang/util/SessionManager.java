package com.wang.util;

import android.util.Log;

import org.apache.mina.core.session.IoSession;

/**
 * Created by 28724 on 2018/4/30.
 */

public class SessionManager {
    private static SessionManager mInstance=null;

    private IoSession mSession;
    public static SessionManager getInstance(){
        if(mInstance==null){
            synchronized (SessionManager.class){
                if(mInstance==null){
                    mInstance = new SessionManager();
                }
            }
        }
        return mInstance;
    }

    private SessionManager(){}

    public void setSession(IoSession session){
        this.mSession = session;
    }

    public void writeToServer(Object msg){
        if(mSession!=null){
            Log.e("tag", "客户端准备发送消息");
            mSession.write(msg);
        }
    }

    public void closeSession(){
        if(mSession!=null) {
            mSession.getCloseFuture().setClosed();
        }
    }

    public void removeSession(){
        this.mSession=null;
    }
}
