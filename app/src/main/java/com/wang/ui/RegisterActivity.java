package com.wang.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wang.R;
import com.wang.base.User;
import com.wang.util.checkEmail;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 28724 on 2018/3/29.
 */

public class RegisterActivity extends AppCompatActivity {

    /*创建一个注册TAG*/
    private final static String REGISETR_TAG = "Register ===> ";

    /*注册所用URL*/
    private static final String url = "http://16n39847j2.51mypc.cn:16129/register";
    private static final String urlText = "http://192.168.1.11:8080/userNameValide";
    @BindView(R.id.et_re_username)
    EditText etReUsername;
    @BindView(R.id.et_re_password)
    EditText etRePassword;
    @BindView(R.id.et_re_email)
    EditText etReEmail;
    @BindView(R.id.register_button0)
    Button registerButton0;
    @BindView(R.id.register_step0)
    LinearLayout registerStep0;
    @BindView(R.id.register_button1)
    Button registerButton1;
    @BindView(R.id.register_step1)
    LinearLayout registerStep1;
    @BindView(R.id.register_button2)
    Button registerButton2;
    @BindView(R.id.register_step2)
    LinearLayout registerStep2;

    private Handler ltHandler;
    private String user_unused;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        ButterKnife.bind(this);

        /*UI控件LoadToast*/
        final LoadToast lt;
        lt = new LoadToast(this).setProgressColor(Color.RED).setText("注册中").setTranslationY(600);
        View v = new View(this);
        v.setBackgroundColor(Color.RED);

        /*邮箱格式检测*/
        final checkEmail checkemail = new checkEmail();

        etReUsername.addTextChangedListener(new TextWatcher() {//文本内容改变监听事件

            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (temp.length() > 0) {
                    if (temp.length() < 3) {
                        etReUsername.setError("用户帐号长度至少3个字符");
                    }
                    if (temp.length() > 20) {
                        etReUsername.setError("用户帐号长度最长不超过20个字符");
                    }
                }

            }
        });

        etReEmail.addTextChangedListener(new TextWatcher() {//文本内容改变监听事件

            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (temp.length() > 0) {
                    if (!checkemail.checkEmail(temp.toString())) {
                        etReEmail.setError("邮箱格式错误！");
                    }
                }

            }
        });

        etReUsername.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    OkHttpUtils
                            .post()
                            .url(urlText)
                            .addParams("userName", etReUsername.getText().toString())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    user_unused = response;
                                    Log.i("usertest", response);
                                    if (response.equals(R.string.SUCCESS)) {

                                    } else {

                                        etReUsername.setError("用户名不可用！");
                                    }

                                }
                            });
                }
            }
        });


        /*利用Handler进行UI更新*/
        ltHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: {
                        lt.success();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException E) {
                                }
                            }
                        }).start();
                        RegisterActivity.this.finish();
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
    }
    /*点击事件*/
    @OnClick({R.id.register_button0,R.id.register_button1,R.id.register_button2})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.register_button0:
//                final checkEmail checkemail = new checkEmail();
//                if (!getResources().getString(R.string.SUCCESS).equals(user_unused)) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "用户名重复，请重新注册", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    Message msg = new Message();
//                    msg.what = 1;
//                    ltHandler.sendMessage(msg);
//                } else if (checkemail.checkEmail(etRePassword.getText().toString())) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "邮箱格式错误，请重新注册", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    Message msg = new Message();
//                    msg.what = 1;
//                    ltHandler.sendMessage(msg);
//                } else {
//                    if (TextUtils.isEmpty(etReUsername.getText())) {
//                        etReUsername.setError("请输入用户名");
//                    } else if (TextUtils.isEmpty(etRePassword.getText())) {
//                        etRePassword.setError("请输入密码");
//                    } else if (TextUtils.isEmpty(etReEmail.getText())) {
//                        etReEmail.setError("请输入邮箱");
//                    } else {
//                        Log.i(REGISETR_TAG, "用户确认并提交了信息");
//                    /*将Message送到Handler进行UI更新*/
//                        Message msg = new Message();
//                        msg.what = 2;
//                        ltHandler.sendMessage(msg);
//                        /*与服务器通讯，验证*/
//                        post();
                        /*滑动效果，到下一个页面*/
                        Animator animatorleft = AnimatorInflater.loadAnimator(RegisterActivity.this, R.animator.animator_register_stepleftexit);
                        animatorleft.setTarget(registerStep0);
                        animatorleft.start();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                registerStep0.setVisibility(View.GONE);
                            }
                        }, 500);
                        registerStep1.setVisibility(View.VISIBLE);
                        Animator animatorright = AnimatorInflater.loadAnimator(RegisterActivity.this, R.animator.animator_register_steprightenter);
                        animatorright.setTarget(registerStep1);
                        animatorright.start();
//                    }
//                }
                break;
            case R.id.register_button1:
                /*滑动效果，到下一个页面*/
                Animator animatorleft1 = AnimatorInflater.loadAnimator(RegisterActivity.this, R.animator.animator_register_stepleftexit);
                animatorleft1.setTarget(registerStep1);
                animatorleft1.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        registerStep1.setVisibility(View.GONE);
                    }
                }, 500);
                registerStep2.setVisibility(View.VISIBLE);
                Animator animatorright1 = AnimatorInflater.loadAnimator(RegisterActivity.this, R.animator.animator_register_steprightenter);
                animatorright1.setTarget(registerStep2);
                animatorright1.start();
                break;
            case R.id.register_button2:
                Message msg = new Message();
                msg.what = 0;
                ltHandler.sendMessage(msg);
                break;
        }
    }

    private void post() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        User user = new User(etReUsername.getText().toString(), etRePassword.getText().toString(), etReEmail.getText().toString());
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
                .url(url)//请求的url
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
                final String success = "1";
                //int str = Integer.parseInt(string);
                Log.i("tian", string);
                Log.i("boolean_context", "success");
                                    /*将Message送到Handler进行UI更新*/
                if (string.equals(success)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "请验证邮箱", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    Message msg = new Message();
//                    msg.what = 0;
//                    ltHandler.sendMessage(msg);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "注册失败，请重新注册", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Message msg = new Message();
                    msg.what = 1;
                    ltHandler.sendMessage(msg);
                }
            }
        });
    }
}
