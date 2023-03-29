package com.example.subway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.subway.database.UserDBHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private Intent intent;
    private UserDBHelper mHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_line).setOnClickListener(this);
        findViewById(R.id.btn_station).setOnClickListener(this);
        findViewById(R.id.btn_change).setOnClickListener(this);
    }

    protected void onStart()
    {
        super.onStart();
        // 获得数据库帮助器的实例
        mHelper = UserDBHelper.getInstance(this);
        // 打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        // 关闭数据库连接
        mHelper.closeLink();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_line:
                intent = new Intent(this, SearchLineActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_station:
                intent = new Intent(this, SearchStationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_change:
                intent = new Intent(this, ChangeStationActivity.class);
                startActivity(intent);
                break;
        }
    }
}