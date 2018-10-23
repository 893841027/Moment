package com.example.zzy4f5da2.mysqlconntest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * Created by Akari on 2018/7/6.
 */

public class Recycle extends RecyclerView.Adapter<Recycle.ViewHolder>{

    private final List mlist;
    private final Context ctx;

    public Recycle(List list, Context context) {
        mlist = list;
        ctx = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(ctx).inflate(R.layout.newitem,null);
        ViewHolder viewHolder = new ViewHolder(myview);
        return viewHolder;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Data data = (Data) mlist.get(position);

        holder.date.setText("-  他的动态来自  "+data.getDate().substring(0,data.getDate().length()-2));
        holder.content.setText(data.getContent()+"");
        //holder.title.setText(data.getTitle()+"");
        holder.date2.setText("-  她的动态消息来自  "+data.getDate().substring(0,data.getDate().length()-2));
        holder.content2.setText(data.getContent()+"");
        //holder.title2.setText(data.getTitle()+"");
        holder.date3.setText("更新时间 - "+data.getDate());
        holder.content3.setText(data.getContent()+"");
        if(data.getColor()==1){
            holder.nice_v1.setText(data.getNice()+"");
            holder.card.setVisibility(View.VISIBLE);
            holder.card2.setVisibility(View.GONE);
            holder.card3.setVisibility(View.GONE);
        }else if(data.getColor()==2){
            holder.nice_v2.setText(data.getNice()+"");
            holder.card2.setVisibility(View.VISIBLE);
            holder.card.setVisibility(View.GONE);
            holder.card3.setVisibility(View.GONE);
        }else if(data.getColor()==3){
            holder.card3.setVisibility(View.VISIBLE);
            holder.card2.setVisibility(View.GONE);
            holder.card.setVisibility(View.GONE);
        }

        if(data.getNice()>0){
            holder.nice1.setImageResource(R.mipmap.niceed);
            holder.nice2.setImageResource(R.mipmap.niceed);
        }else{
            holder.nice1.setImageResource(R.mipmap.nice);
            holder.nice2.setImageResource(R.mipmap.nice);
        }


        if (data.getSEX()==1) {//如果为男性
            holder.del1.setVisibility(View.VISIBLE);
            holder.del2.setVisibility(View.GONE);
        }else if (data.getSEX()==2) {//如果为女性
            holder.del2.setVisibility(View.VISIBLE);
            holder.del1.setVisibility(View.GONE);
        }else if (data.getSEX()==3) {//如果是管理员
            holder.del2.setVisibility(View.VISIBLE);
            holder.del1.setVisibility(View.VISIBLE);
        }

        if(data.getImg()!=null){
            if(data.getColor()==2)
                holder.nv_img.setImageBitmap(data.getImg());
            if (data.getColor()==1){
                holder.bo_img.setImageBitmap(data.getImg());
            }
        }else{
            holder.bo_img.setImageResource(R.mipmap.nv);
            holder.nv_img.setImageResource(R.mipmap.nv);
        }

        if(position==mlist.size()-1){
            holder.card4.setVisibility(View.VISIBLE);
        }else{
            holder.card4.setVisibility(View.GONE);
        }

        final Intent intent = new Intent("com.osc.zzy.del");
        holder.del1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ctx).setMessage("删除这一条说说？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notifyItemRemoved(position);
                                mlist.remove(position);
                                notifyItemRangeChanged(0,mlist.size());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Statement stat = null;
                                        try {
                                            stat = new SQLMan().getConnection().createStatement();
                                            String sqls = "delete from db_manager where content=\'"+new Encrypt().encryptPassword(data.getContent())+"\'";
                                            stat.execute(sqls);
                                            stat.close();
                                            ctx.sendBroadcast(intent);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }).start();

                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });

        holder.del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ctx).setMessage("删除这一条说说？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notifyItemRemoved(position);
                                mlist.remove(position);
                                notifyItemRangeChanged(0,mlist.size());
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Statement stat = null;
                                            try {
                                                stat = new SQLMan().getConnection().createStatement();
                                                String sqls = "delete from db_manager where content=\'"+new Encrypt().encryptPassword(data.getContent())+"\'";
                                                stat.execute(sqls);
                                                stat.close();
                                                ctx.sendBroadcast(intent);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }).start();


                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(ctx).inflate(R.layout.detail_log,null);
                RoundedImageView roimg = view1.findViewById(R.id.detail_img);
                TextView deed = view1.findViewById(R.id.detail);
                roimg.setImageBitmap(data.getImg());
                deed.setText(data.getContent());
                new AlertDialog.Builder(ctx)
                        .setView(view1).setNegativeButton("关闭",null).show();
            }
        });

        holder.card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(ctx).inflate(R.layout.detail_log,null);
                RoundedImageView roimg = view1.findViewById(R.id.detail_img);
                TextView deed = view1.findViewById(R.id.detail);
                if(data.getImg()==null){
                    roimg.setVisibility(View.GONE);
                }else{
                    roimg.setVisibility(View.VISIBLE);
                    roimg.setImageBitmap(data.getImg());
                }

                deed.setText(data.getContent());
                new AlertDialog.Builder(ctx)
                        .setView(view1).setNegativeButton("关闭",null).show();
            }
        });

        holder.copy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cd = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cp = ClipData.newPlainText("text",data.getContent());
                cd.setPrimaryClip(cp);
                String tip = "已将内容["+(data.getContent().length()>6?(data.getContent().substring(0,5)+"...").toString():data.getContent().toString())+"]复制到剪贴板";
                Toast.makeText(ctx, tip, Toast.LENGTH_SHORT).show();
            }
        });

        holder.copy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cd = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cp = ClipData.newPlainText("text",data.getContent());
                cd.setPrimaryClip(cp);
                String tip = "已将内容["+(data.getContent().length()>6?(data.getContent().substring(0,5)+"...").toString():data.getContent().toString())+"]复制到剪贴板";
                Toast.makeText(ctx, tip, Toast.LENGTH_SHORT).show();
            }
        });

        holder.nice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.getSEX()==1){
                    Toast.makeText(ctx, "不能帮自己点赞哟~", Toast.LENGTH_SHORT).show();
                }else{
                    data.setNice(data.getNice()+1);
                    notifyItemChanged(position);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Statement stat = null;
                            try {
                                stat = new SQLMan().getConnection().createStatement();
                                String sqls = "update db_manager set nice="+(data.getNice())
                                        +" where content=\'"+new Encrypt().encryptPassword(data.getContent())+"\'";
                                stat.execute(sqls);
                                stat.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }

            }
        });

        holder.nice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.getSEX()==2){
                    Toast.makeText(ctx, "不能帮自己点赞哟~", Toast.LENGTH_SHORT).show();
                }else{
                    data.setNice(data.getNice()+1);
                    notifyItemChanged(position);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Statement stat = null;
                            try {
                                stat = new SQLMan().getConnection().createStatement();
                                String sqls = "update db_manager set nice="+(data.getNice())
                                        +" where content=\'"+new Encrypt().encryptPassword(data.getContent())+"\'";
                                stat.execute(sqls);
                                stat.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView bo_img;
        RoundedImageView nv_img;
        TextView title;
        TextView content;
        TextView date;
        CardView card;
        TextView title2;
        TextView content2;
        TextView content3;
        TextView date2;
        TextView date3;
        CardView card2;
        LinearLayout card3;
        TextView card4;
        ImageView del1;
        ImageView del2;
        ImageView copy1;
        ImageView copy2;
        ImageView nice1;
        ImageView nice2;
        TextView nice_v1;
        TextView nice_v2;
        public ViewHolder(View itemView) {
            super(itemView);
            //title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            card = itemView.findViewById(R.id.card);
            //title2 = itemView.findViewById(R.id.title2);
            content2 = itemView.findViewById(R.id.content2);
            content3 = itemView.findViewById(R.id.content3);
            date2 = itemView.findViewById(R.id.date2);
            date3 = itemView.findViewById(R.id.date3);
            card2 = itemView.findViewById(R.id.card2);
            card3 = itemView.findViewById(R.id.card3);
            card4 = itemView.findViewById(R.id.card4);
            del1 = itemView.findViewById(R.id.item_del1);
            del2 = itemView.findViewById(R.id.item_del2);
            nv_img = itemView.findViewById(R.id.nv_img);
            bo_img = itemView.findViewById(R.id.bo_img);
            copy1 = itemView.findViewById(R.id.copy1);
            copy2 = itemView.findViewById(R.id.copy2);
            nice1 = itemView.findViewById(R.id.nice1);
            nice2 = itemView.findViewById(R.id.nice2);
            nice_v1 = itemView.findViewById(R.id.nice1_v);
            nice_v2 = itemView.findViewById(R.id.nice2_v);
        }
    }
}
