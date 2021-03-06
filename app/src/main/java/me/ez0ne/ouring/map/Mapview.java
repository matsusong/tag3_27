package me.ez0ne.ouring.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.litepal.crud.DataSupport;

import java.util.List;

import me.ez0ne.ouring.R;
import me.ez0ne.ouring.bean.StringSMS;
import me.ez0ne.ouring.ui.MainActivity;
import me.ez0ne.ouring.utils.mapContactUtils;

/**
 * Created by Cerian on 2018/2/11.
 */

public class Mapview extends AppCompatActivity implements View.OnClickListener{
    ChinaMapView lView;
    private Context context;
    private Button bt_back;
    private TextView tv_maxPlace,tv_maxCount,tv_minPlace,tv_minCount,tv_choPlace,tv_choCount;
    private TextView tv_selectMap,tv_selectDetails;
    private static final int REQUECT_CODE_SDCARD =2 ;
    private String permission[]={Manifest.permission.READ_SMS,Manifest.permission.READ_CONTACTS};
    private mapContactUtils contactUtils=new mapContactUtils(Mapview.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
/**
 * 改变返回时页面按钮颜色
 */



        MPermissions.requestPermissions(Mapview.this, REQUECT_CODE_SDCARD,permission);

        tv_maxCount= (TextView) findViewById(R.id.maxCount);
        tv_maxPlace= (TextView) findViewById(R.id.maxPlace);
        tv_minCount= (TextView) findViewById(R.id.minCount);
        tv_minPlace= (TextView) findViewById(R.id.minPlace);
        tv_choPlace= (TextView) findViewById(R.id.choPlace);
        tv_choCount= (TextView) findViewById(R.id.choCount);
        tv_selectMap= (TextView) findViewById(R.id.selectmap);
        tv_selectDetails= (TextView) findViewById(R.id.selectordetail);
        bt_back= (Button) findViewById(R.id.back2);
        bt_back.setOnClickListener(this);
        tv_selectDetails.setOnClickListener(this);
        String from=getIntent().getStringExtra("from");
        if(from!=null) {
            if (from.equals("detail")) {
                tv_selectMap.setTextColor(ContextCompat.getColor(Mapview.this, R.color.application_white));
                tv_selectMap.setBackgroundColor(ContextCompat.getColor(this, R.color.application_green));
                tv_selectDetails.setTextColor(ContextCompat.getColor(this, R.color.application_text_gray));
                tv_selectDetails.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_border));
            }
        }

/*        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        lView = (ChinaMapView)findViewById(R.id.vp);
        lView.setOnProvinceSelectedListener(new ChinaMapView.OnProvinceSelectedListener() {
            @Override
            public void onprovinceSelected(ChinaMapView.Area pArea) {
                //int i= contactUtils.getCount(pArea.name());
              /*  ToastUtil toastUtil=new ToastUtil();

                toastUtil.Short(Mapview.this,"您短信中来自"+pArea.name()+"的数目是"+i).
                        setToastBackground(Color.rgb(31,31,31),R.drawable.toast_radius).show();*/
                StringSMS strsms =new StringSMS();
                strsms= contactUtils.getInfo(pArea.name());
                tv_choPlace.setText(strsms.getPlace());
                tv_choCount.setText(strsms.getCount()+"");

            }
        });
/*        List<StringSMS> sms= DataSupport.findAll(StringSMS.class);
        if(sms.size()==0) {
            contactUtils.initDB();
        }
        contactUtils.getPlaceToDB();*/

        StringSMS strsms0 =new StringSMS();
        strsms0 = contactUtils.getIndexInfo(0);
        StringSMS strsms1 =new StringSMS();
        strsms1 = contactUtils.getIndexInfo(1);
        StringSMS strsms2 =new StringSMS();
        strsms2 = contactUtils.getIndexInfo(2);
        StringSMS minsms=new StringSMS();
        minsms=contactUtils.getMinInfo();
        if(strsms0.getCount()>0)
            lView.setPaintColor(ChinaMapView.Area.getArea(strsms0.getPlace()), Color.rgb(255,0,0),true);
        if(strsms1.getCount()>0)
            lView.setPaintColor(ChinaMapView.Area.getArea(strsms1.getPlace()), Color.rgb(135,206,235), true);
        if(strsms2.getCount()>0)
            lView.setPaintColor(ChinaMapView.Area.getArea(strsms2.getPlace()), Color.rgb(0,250,154), true);

        lView.setMapColor(Color.rgb(89,176,156));
        if(strsms0.getCount()==0) {
            tv_maxPlace.setText("无");
            tv_maxCount.setText(String.valueOf(strsms0.getCount()));
        }
        else{
            tv_maxPlace.setText(strsms0.getPlace());
            tv_maxCount.setText(String.valueOf(strsms0.getCount()));
        }
        if(minsms.getCount()==0){
            tv_minPlace.setText("无");
            tv_minCount.setText(String.valueOf(minsms.getCount()));
        }
        else {
            tv_minPlace.setText(minsms.getPlace());
            tv_minCount.setText(String.valueOf(minsms.getCount()));
        }
        Log.i("qqq",minsms.getPlace());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @PermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess()
    {

    }

    @PermissionDenied(REQUECT_CODE_SDCARD)
    public void requestSdcardFailed()
    {

    }
    private void initScreen(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.selectordetail:
                tv_selectDetails.setTextColor(ContextCompat.getColor(this, R.color.application_white));
                tv_selectDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.application_green));
                tv_selectMap.setTextColor(ContextCompat.getColor(this, R.color.application_text_gray));
                tv_selectMap.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_border));
                Intent intent=new Intent(Mapview.this,DetailSearch.class);
                startActivity(intent);
                break;

            case R.id.back2:
                Intent intent1=new Intent(Mapview.this, MainActivity.class);
                startActivity(intent1);
                break;
        }
    }
}

