package me.ez0ne.ouring.tag;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

import me.ez0ne.ouring.R;

/**
 * Created by Cerian on 2018/2/11.
 */

public class ShowMessageActivity extends AppCompatActivity {

    private TextView title;
    private RecyclerView recyclerView;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Debug","ShowMessageActivity+"+"onCreate");
        setContentView(R.layout.activity_show_message);
        ActivityCollecter.AddActivity(this);
        initScreen();
        title = (TextView)findViewById(R.id.tv_showmessage_title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_showmessage);
        back = (Button)findViewById(R.id.bt_showmessage_back);

        String tag,phoneNumber;
        tag = getIntent().getStringExtra("tag");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        List<Message> messages;
      //  if(tag==null){
            messages = DataSupport.where("phoneNumber = ?",phoneNumber).find(Message.class);
            title.setText("选择信息");
       // }
       /* else{
            messages = DataSupport.where("tag = ? and phoneNumber = ?",tag,phoneNumber).find(Message.class);
            title.setText(tag);
        }*/
        MessageAdapter adapter = new MessageAdapter(messages,tag,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private void initScreen(){
        Log.d("Debug","ShowMessageActivity+"+"initScreen");
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.remove(this);
    }
}
