package com.example.subway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.subway.database.UserDBHelper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class SearchStationActivity extends AppCompatActivity implements View.OnClickListener
{
    private UserDBHelper mHelper;
    private EditText et_station;
    private TextView tv_result;

    private SQLiteDatabase db;
    private Cursor cursor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);
        et_station = findViewById(R.id.et_station);
        tv_result = findViewById(R.id.tv_result);
        findViewById(R.id.btn_station).setOnClickListener(this);
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
        db = mHelper.getReadableDatabase();
        String station_name=et_station.getText().toString();
        ArrayList<String> list = new ArrayList<>();
        if (view.getId() == R.id.btn_station)
        {
            tv_result.setText(station_name+"经过以下线路:\n");
            cursor = db.query("subway_info", new String[]{"line_name", "station_name"}, "station_name = ?", new String[]{station_name}, null, null, null, null);
            while (cursor.moveToNext())
            {
                list.add(cursor.getString(0));
            }
            List<String> listNew = new ArrayList<String>(new LinkedHashSet<String>(list));
            for (int i = 0; i <listNew.size(); i++)
            {
                tv_result.append(listNew.get(i)+" ");
            }
        }

    }
}