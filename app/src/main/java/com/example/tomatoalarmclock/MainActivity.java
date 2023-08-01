package com.example.tomatoalarmclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.example.tomatoalarmclock.adapter.MyAdapter;
import com.example.tomatoalarmclock.bean.message;
import com.getbase.floatingactionbutton.FloatingActionButton;
import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<message> data = new ArrayList<>();

    public RecyclerView recyclerView;
    FloatingActionButton fb1;
    public MyAdapter adapter;
    public int mSpace=15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        data = LitePal.findAll(message.class);

        recyclerView = findViewById(R.id.RV);
        adapter=new MyAdapter(data,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
//        设置recycleview的边界大小
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = mSpace;
                outRect.right = mSpace;
                outRect.bottom = mSpace;
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = mSpace;
                }

            }
        });

//        设置recycleview的item的长按监听
        adapter.setOnLongClickListener(new MyAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                Dialog(MainActivity.this,position);
            }
        });

        adapter.setmOnClickListener(new MyAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("请下滑设置飞行模式并点击确定进入学习状态！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this,clock.class);

                        message m = LitePal.where("event=?",data.get(position).getEvent()).findLast(message.class);
                        m.setState(1);
                        m.save();

                        float time = data.get(position).getTime();
                        intent.putExtra("Time",time);
                        startActivity(intent);
                    }
                });
                builder.show();

            }
        });

        fb1 = findViewById(R.id.fb1);
        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogLayout = getLayoutInflater().inflate(R.layout.dialogshow, null);
                builder.setView(dialogLayout);
                AlertDialog dialog = builder.create();

                EditText text1 = dialogLayout.findViewById(R.id.text1);
                text1.setHint("请输入待办事件");
                EditText text2 = dialogLayout.findViewById(R.id.text2);
                text2.setHint("请输入学习时间/小时");

                Button button1 = dialogLayout.findViewById(R.id.right);
                Button button2 = dialogLayout.findViewById(R.id.quit);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data1 = text1.getText().toString();
                        float data2 = Float.parseFloat(text2.getText().toString());
                        int state =0;
                        Connector.getDatabase();
                        if (!TextUtils.isEmpty(text1.getText().toString())&&!TextUtils.isEmpty(text2.getText().toString())){
                            message message = new message(data1,data2,state);
                            message.save();
                        }
                        dialog.dismiss();
                        data = LitePal.findAll(message.class);
                        adapter.setData(data);
                        adapter.notifyDataSetChanged();

                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                // 创建对话框并显示
                dialog.show();
            }
        });

    }

    public void Dialog(Context context, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示")
                .setMessage("是否删除对应事件？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        data.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0,data.size());
                    }
                })
                .setNegativeButton("否", null)
                .show();

        LitePal.deleteAll(message.class, "event = ?", data.get(position).getEvent());
    }


}