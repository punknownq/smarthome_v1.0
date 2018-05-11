package com.wang.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jpeng.jptabbar.JPTabBar;
import com.wang.R;
import com.wang.base.PersonInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab4Pager extends Fragment {


    @BindView(R.id.touxiang)
    ImageView touxiang;
    @BindView(R.id.id_userName)
    TextView idUserName;
    @BindView(R.id.id_email)
    TextView idEmail;
    @BindView(R.id.id_roomNum)
    TextView idRoomNum;
    @BindView(R.id.id_deviceNum)
    TextView idDeviceNum;
    @BindView(R.id.id_area)
    TextView idArea;
    @BindView(R.id.id_level)
    TextView idLevel;
    @BindView(R.id.bt6)
    TextView bt6;
    @BindView(R.id.bt8)
    TextView bt8;
    @BindView(R.id.bt9)
    TextView bt9;
    Unbinder unbinder;
    @BindView(R.id.bt7)
    Button bt7;
    @BindView(R.id.container)
    LinearLayout container;
    private JPTabBar mTabBar;
    private static final String url_person = "androidPersonInfo/getPersonInfo";
    private static final String url_download = "/androidImage/fileDownload";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.tab4, null);

        init(layout);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    private void init(View layout) {

        mTabBar = ((MainActivity) getActivity()).getTabbar();
        String string = getActivity().getIntent().getExtras().getString("userName", "linweiming");
        OkHttpUtils
                .post()
                .url(getResources().getString(R.string.BaseURL) + url_person)
                .addParams("userName", string)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("message", response);
                        Gson gson = new Gson();
                        PersonInfo personInfo = gson.fromJson(response, PersonInfo.class);
                        idUserName.setText(personInfo.getUsername());
                        idEmail.setText(personInfo.getEmail());
                        idRoomNum.setText("房间数：" + personInfo.getRoomNum());
                        idDeviceNum.setText("设备数：" + personInfo.getDeviceNum());
                        idLevel.setText("用户权限：普通用户");
                        idArea.setText("所在地区："+personInfo.getArea());


                    }
                });
        OkHttpUtils
                .get()
                .url(getResources().getString(R.string.BaseURL)+url_download)
                .build()
                .execute(new BitmapCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        touxiang.setImageBitmap(bitmap);
                    }

                });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bt7)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}

