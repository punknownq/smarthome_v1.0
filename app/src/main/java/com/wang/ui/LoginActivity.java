package com.wang.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wang.R;
import com.wang.base.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import net.steamcrafted.loadtoast.LoadToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;


/**
 * Created by 28724 on 2018/3/23.
 */

public class LoginActivity extends AppCompatActivity {

    /*声明一个登录TAG*/
    private final static String SIGNIN_TAG = "Signin ===> ";

    /*声明登录所用URL*/
    private static final String url ="androidLoginValide";
   // private static String baseUrl;
    /*绑定控件*/
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    //@BindView(R.id.et_ipaddress)
   // EditText etIpaddress;
    @BindView(R.id.sign_in_button)
    Button signInButton;
    @BindView(R.id.register_button)
    Button registerButton;
    @BindView(R.id.cb_mima)
    CheckBox cbMima;
    @BindView(R.id.cb_auto)
    CheckBox cbAuto;

    /*声明用于UI更新的ItHandler*/
    private Handler ltHandler;

    /*声明用于记住最后一次输入的账号的SharedPreferences以及其Editor*/
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
         /*使用butterknife绑定此activity*/
        ButterKnife.bind(this);

        /*UI控件LoadToast*/
        final LoadToast lt;
        lt = new LoadToast(this).setProgressColor(Color.RED).setText("登录中").setTranslationY(600);
        View v = new View(this);
        v.setBackgroundColor(Color.RED);

          /*利用Handler进行UI更新*/
        ltHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: {
                        lt.success();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userName",etUsername.getText().toString());
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        lt.error();

                        break;
                    }
                    case 2: {
                        lt.show();
                        break;
                    }
                }
            }
        };

        /*还原最后一次输入的账号*/
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        //etIpaddress.setText(pref.getString("ipaddress", ""));
        //判断记住密码多选框的状态
        if(pref.getBoolean("ISCHECK", false))
        {
            //设置默认是记录密码状态
            cbMima.setChecked(true);
            etUsername.setText(pref.getString("username", ""));
            etPassword.setText(pref.getString("password", ""));
//            //判断自动登陆多选框状态
//            if(pref.getBoolean("AUTO_ISCHECK", false))
//            {
//                //设置默认是自动登录状态
//                cbAuto.setChecked(true);
//                //跳转界面
//                Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
//                LoginActivity.this.startActivity(intent);
//
//            }
        }
        //监听记住密码多选框按钮事件
        cbMima.setOnCheckedChangeListener(new  CompoundButton.OnCheckedChangeListener(){
            public void  onCheckedChanged(CompoundButton buttonView,boolean  isChecked){
                if  (cbMima.isChecked()){
                    System.out.println("记住密码已选中" );
                    pref.edit().putBoolean("ISCHECK" ,  true ).commit();

                } else  {

                    System.out.println("记住密码没有选中" );
                    pref.edit().putBoolean("ISCHECK" ,  false ).commit();

                }

            }
        });
    }
    /*点击事件*/
    @OnClick({R.id.sign_in_button, R.id.register_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                if (TextUtils.isEmpty(etUsername.getText())) {
                    etUsername.setError("请输入用户名");
                } else if (TextUtils.isEmpty(etPassword.getText())) {
                    etPassword.setError("请输入密码");
                } else {
                    Log.i(SIGNIN_TAG, "Login Button clicked!");
                    /*记住最后一次输入的账号*/
                    if(pref.getBoolean("ISCHECK", true))
                    {
                        editor = pref.edit();
                        editor.putString("username", etUsername.getText().toString());
                        editor.putString("password", etPassword.getText().toString());
                        // editor.putString("ipaddress", etIpaddress.getText().toString());
                        editor.commit();
                    }
                    /*将Message送到Handler进行UI更新*/
                    Message msg = new Message();
                    msg.what = 2;
                    ltHandler.sendMessage(msg);
                    OkHttpUtils
                            .post()
                            .url(getResources().getString(R.string.BaseURL)+url)
                            .addParams("userName", etUsername.getText().toString())
                            .addParams("passWord", etPassword.getText().toString())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    int str = Integer.parseInt(response);
                                    Log.i("long", str+"");

                                    Log.i("boolean_context", "success");
                                    /*将Message送到Handler进行UI更新*/
                                    if(str==1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Message msg = new Message();
                                        msg.what = 0;
                                        ltHandler.sendMessage(msg);
                                    }
                                    else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Message msg = new Message();
                                        msg.what = 1;
                                        ltHandler.sendMessage(msg);
                                    }
                                }
                            });
                   }

                break;
            case R.id.register_button:
                /*启动RegisterActivity*/
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }


}
