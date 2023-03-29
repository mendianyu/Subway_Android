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

public class ChangeStationActivity extends AppCompatActivity implements View.OnClickListener
{
    private UserDBHelper mHelper;
    private EditText et_start;
    private EditText et_end;
    private TextView tv_result;
    private SQLiteDatabase db;
    private Cursor cursor;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_station);
        et_start = findViewById(R.id.et_start);
        et_end = findViewById(R.id.et_end);
        tv_result = findViewById(R.id.tv_result);
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

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view)
    {
        db = mHelper.getReadableDatabase();
        String start = et_start.getText().toString();
        String end = et_end.getText().toString();

        if (view.getId() == R.id.btn_change)
        {
            tv_result.setText("\n\n");
            cursor=db.rawQuery(" WITH RECURSIVE transfer(start_station, stop_station, stops, paths) AS (\n" +
                    " SELECT station_name, next_station, 1 stops, \n" +
                    " line_name||station_name||'->'||line_name||next_station AS paths\n" +
                    " FROM subway_info\n" +
                    " WHERE station_name = ?\n" +
                    " UNION ALL \n" +
                    " SELECT t.start_station, s.next_station, stops+1, paths||'->'||s.line_name||s.next_station\n" +
                    " FROM transfer t\n" +
                    " JOIN subway_info s \n" +
                    " ON (t.stop_station = s.station_name AND instr(paths, s.next_station)=0)\n" +
                    " )\n" +
                    " SELECT *\n" +
                    " FROM transfer\n" +
                    " WHERE stop_station = ?;" , new String[]{start,end});
            while (cursor.moveToNext())
            {
                int sum=cursor.getInt(2)+1;
                tv_result.append("\n" + "起点:" + cursor.getString(0) + " 终点:" + cursor.getString(1) + " 经过站数:" + sum +"\n\n"+ "路线:" + cursor.getString(3));
                break;
            }
            cursor.close();
        }
    }
}