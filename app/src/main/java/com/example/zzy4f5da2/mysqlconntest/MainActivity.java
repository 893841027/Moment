package com.example.zzy4f5da2.mysqlconntest;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yalantis.ucrop.UCrop;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    public int SEX = 0;
    private RecyclerView rec;
    private List list;
    private Recycle adapter;
    private RelativeLayout load;
    private FloatingActionButton add;
    private IntentFilter del;
    private DelMoment delMoment;
    private RoundedImageView ropic;
    Bitmap updateBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sp = getPreferences(MODE_PRIVATE);
        setSEX(sp.getInt("sex",2));

        ProgressBar setsex = findViewById(R.id.setsex);

        setsex.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] str  = {"1", "2"};
                new AlertDialog.Builder(MainActivity.this)
                        .setItems(str, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sp.edit().putInt("sex",Integer.parseInt(str[i])).apply();
                                Toast.makeText(MainActivity.this, "你的性别已设置为"+str[i], Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                return false;
            }
        });

        del = new IntentFilter("com.osc.zzy.del");
        delMoment = new DelMoment();
        registerReceiver(delMoment, del);



        load = findViewById(R.id.load);
        add = findViewById(R.id.add);


        list = new ArrayList();
        rec = findViewById(R.id.recycle);

        rec.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
        load.setVisibility(View.VISIBLE);

        getData();
        adapter = new Recycle(list,MainActivity.this);
        rec.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        DefaultItemAnimator df = new DefaultItemAnimator();
        df.setRemoveDuration(1000);
        df.setAddDuration(1000);
        rec.setItemAnimator(df);
        rec.setAdapter(adapter);


    }

    public void setSEX(int sex_set){
        Data data = new Data(sex_set);    //设置性别标识
        SEX = data.getSEX();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(delMoment);

    }

    class DelMoment extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            //getData();
        }



    }
/*
    Uri finaluri = null;

    void cut(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        finaluri = uri;
        //intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data，", true);  //处理大图片false,小图片true
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //图片格式
        //裁剪大图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT,finaluri);
        // 开启一个带有返回值的Activity，请求码为1
        startActivityForResult(intent, 10010);
    }
*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null&&requestCode==0){
            Uri tempuri = data.getData();   //选择图片得到的uri
            Bitmap bitmap = null;//转换选择图片得到的bitmap
            Uri sourceUri = data.getData();
            //裁剪后保存到文件中
            Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.jpeg"));
            UCrop.of(sourceUri, destinationUri).withMaxResultSize(700,700).withAspectRatio(1,1).start(this);
            //cut(tempuri);
        }
        if (resultCode == RESULT_OK) {
            //裁切成功
            if (requestCode == UCrop.REQUEST_CROP) {
                Uri croppedFileUri = UCrop.getOutput(data);
                try {
                    Bitmap imgbit = BitmapFactory.decodeStream(getContentResolver().openInputStream(croppedFileUri));
                    updateBitmap = imgbit;
                    ropic.setImageBitmap(imgbit);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            //裁切失败
            if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(this, "裁切图片失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void add(View b){
        View views = LayoutInflater.from(MainActivity.this).inflate(R.layout.adddialog,null);
        //final MaterialEditText tit = views.findViewById(R.id.add_title);
        final MaterialEditText con = views.findViewById(R.id.add_content);

        ropic = views.findViewById(R.id.updatePic);

        ropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用系统相册库选择，包括第三方相册
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("尽可能选择比例接近1:1(正方形)的图片，否则现实可能不正确，也可通过自主剪切后重新上传，不在意的请忽略此提示")
                        .setPositiveButton("我明白了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent,0);
                            }
                        })
                        .setNegativeButton("我回头再来",null)
                        .show();

            }
        });
        updateBitmap = null;


        new AlertDialog.Builder(MainActivity.this)
                .setView(views)
                .setTitle("发表小动态")
                .setCancelable(false)
                .setPositiveButton("确认发送(发送信息已加密)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(updateBitmap!=null){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        updateBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                                        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                                        PreparedStatement ps = new SQLMan().getConnection().prepareStatement(
                                                "INSERT INTO db_manager(title,content,date,color,pic,nice) values(?,?,?,?,?,?)");
                                        ps.setString(1,"0");
                                        ps.setString(2,new Encrypt().encryptPassword(con.getText().toString()));
                                        ps.setString(3,new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date()));
                                        ps.setInt(4,SEX);
                                        ps.setBlob(5,isBm);
                                        ps.setInt(6,0);
                                        ps.execute();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                list.add(0,new Data(con.getText().toString(),
                                                        new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date()),SEX,updateBitmap,SEX,0));
                                                adapter.notifyItemInserted(0);
                                                adapter.notifyItemRangeChanged(0,list.size());
                                                rec.scrollToPosition(0);
                                                Toast.makeText(MainActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                                                updateBitmap = null;    //初始化
                                            }
                                        });
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }else{
                            String sql = "INSERT INTO db_manager(title,content,date,color,nice) VALUES(\'0\',\'"+new Encrypt().encryptPassword(con.getText().toString())+"\',\'"+
                                    new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date())+"\',"+SEX+",0);";
                            insertData(sql);
                            list.add(0,new Data(con.getText().toString(),
                                    new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date()),SEX,SEX,0));
                            adapter.notifyItemInserted(0);
                            adapter.notifyItemRangeChanged(0,list.size());
                            rec.scrollToPosition(0);
                            Toast.makeText(MainActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        .setNegativeButton("取消发送",null)
        .show();
    }

    void insertData(final String sql){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stmt = new SQLMan().getConnection().createStatement();
                    stmt.execute(sql);
                    //stmt.executeUpdate(sql);
                    stmt.close();
                    //getData();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }



    void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sql = "select * from db_manager order by id desc";
                    Statement stmt = new SQLMan().getConnection().createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    list.clear();
                    while(rs.next()) {
                        Bitmap dis_img = null;
                        byte[] by = blobToBytes(rs.getBlob("pic"));
                        if(by!=null){
                            dis_img = BitmapFactory.decodeByteArray(by,0,by.length);
                            list.add(new Data(new Encrypt().decryptPassword(rs.getString("content"))
                                    ,rs.getTimestamp("date").toString(),rs.getInt("color"),dis_img,SEX,rs.getInt("nice")));
                        }else{
                           list.add(new Data(new Encrypt().decryptPassword(rs.getString("content"))
                                    ,rs.getTimestamp("date").toString(),rs.getInt("color"),SEX,rs.getInt("nice")));
                        }

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            rec.scrollToPosition(0);
                            rec.setVisibility(View.VISIBLE);
                            add.setVisibility(View.VISIBLE);
                            load.setVisibility(View.GONE);

                            Toast.makeText(MainActivity.this,"列表加载完成！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.refresh) {
            getData();
            rec.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            load.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this,"正在刷新...请稍候",Toast.LENGTH_SHORT).show();
        }else if (item.getItemId()==R.id.exit){
            finish();
        }else if (item.getItemId()==R.id.su){
            View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.su,null);
            final EditText ed = view2.findViewById(R.id.su);
           new AlertDialog.Builder(MainActivity.this)
                   .setView(view2)
                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           if(ed.getText().toString().equals("zzy2osc")){
                               Toast.makeText(MainActivity.this, "You have successfully obtained the highest permissions!", Toast.LENGTH_SHORT).show();
                               SEX = 3;
                               setSEX(SEX);
                               getData();
                           }else{
                               Toast.makeText(MainActivity.this, "password error!", Toast.LENGTH_SHORT).show();
                               finish();
                           }
                       }
                   })
                   .setNegativeButton("Cancel",null)
                   .show();



            }


        return super.onOptionsItemSelected(item);

        }




    private byte[] blobToBytes(Blob blob) {

        BufferedInputStream is = null;

        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            byte[] bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;

            while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
                offset += read;
            }
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }







}
