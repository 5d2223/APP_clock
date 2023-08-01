package com.example.tomatoalarmclock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomatoalarmclock.R;
import com.example.tomatoalarmclock.bean.message;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    //    上下文
    private Context context;

    //    数据
    public List<message> list;

    //    构造方法将所在的上下文和数据传入
    public MyAdapter(List<message> list, Context text) {
        this.list = list;
        this.context = text;
    }

    //    数据更新方法
    public void setData(List<message> data){
        list = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.event.setText(list.get(position).getEvent());
        holder.time.setText(String.valueOf(list.get(position).getTime())+" 小时");

        if (list.get(position).getState()==1){
            holder.state.setImageResource(R.drawable.finish);
        }


        holder.itemView.setTag(position);
//        item的长按监听
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (null != mOnLongClickListener) {
                    mOnLongClickListener.onLongClick(view, holder.getAdapterPosition());
                }
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnClickListener) {
                    mOnClickListener.onClick(view, holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //    设置的长按监听接口，长按后实现其中的方法
    public interface OnLongClickListener {
        void onLongClick(View view, int position);
    }

    public interface OnClickListener {
        void onClick(View view, int position);
    }
    private OnLongClickListener mOnLongClickListener = null;
    private OnClickListener mOnClickListener = null;
    //    长按监听接口的设置方法
    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    public  void setmOnClickListener(OnClickListener listener){
        mOnClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView event,time;
        ImageView state;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            event=itemView.findViewById(R.id.tv1);
            time=itemView.findViewById(R.id.tv2);
            state=itemView.findViewById(R.id.iv);
        }
    }
}
