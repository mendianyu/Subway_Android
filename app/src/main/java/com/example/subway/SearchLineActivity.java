package com.example.subway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subway.database.UserDBHelper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class SearchLineActivity extends AppCompatActivity implements View.OnClickListener
{
    private UserDBHelper mHelper;
    private int line;
    private TextView tv_result;

    private SQLiteDatabase db;
    private Cursor cursor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_line);
        tv_result = findViewById(R.id.tv_result);
        findViewById(R.id.btn_line).setOnClickListener(this);
        initSpinnerForDropdown(); // 初始化下拉模式的列表框
    }

    private void initSpinnerForDropdown()
    {
        // 声明一个下拉列表的数组适配器
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(this, R.layout.activity_list, starArray);
        // 从布局文件中获取名叫sp_dropdown的下拉框
        Spinner sp_dropdown = findViewById(R.id.sp_dropdown);
        // 设置下拉框的标题。对话框模式才显示标题，下拉模式不显示标题
        sp_dropdown.setPrompt("请选择线路");
        sp_dropdown.setAdapter(starAdapter); // 设置下拉框的数组适配器
        sp_dropdown.setSelection(0); // 设置下拉框默认显示第一项
        // 给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        sp_dropdown.setOnItemSelectedListener(new MySelectedListener());
    }

    // 定义下拉列表需要显示的文本数组
    private String[] starArray = {"1号线", "2号线", "3号线", "4号线", "5号线", "6号线", "7号线", "8号线"};

    // 定义一个选择监听器，它实现了接口OnItemSelectedListener
    class MySelectedListener implements AdapterView.OnItemSelectedListener
    {
        // 选择事件的处理方法，其中arg2代表选择项的序号
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        {
            line = arg2;
        }

        // 未选择时的处理方法，通常无需关注
        public void onNothingSelected(AdapterView<?> arg0)
        {
        }
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
        String line_name = starArray[line];
        ArrayList<String> list = new ArrayList<>();
        if (view.getId() == R.id.btn_line)
        {
            tv_result.setText("\n\n\n" + line_name + "包含站点如下:" + "\n");
            cursor = db.query("subway_info", new String[]{"line_name", "station_name"}, "line_name = ?", new String[]{line_name}, null, null, null, null);
            while (cursor.moveToNext())
            {
                list.add(cursor.getString(1));
            }
            List<String> listNew = new ArrayList<String>(new LinkedHashSet<String>(list));
            for (int i = 0; i < listNew.size(); i++)
            {
                tv_result.append(listNew.get(i) + " ");
            }
            cursor.close();
        }
    }
}