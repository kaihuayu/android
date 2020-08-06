package com.example.flylsb.myyy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class HellotowActivity extends AppCompatActivity {
    private final int REQUESTCODE = 101;
    private Button btn_speak;
    private TextView textView;

    private TextView tv1;//item.xml里的TextView：Textviewname
    private TextView tv2;//item.xml里的TextView：Textviewage
    private Button bt;//activity_main.xml里的Button
    private ListView lv;//activity_main.xml里的ListView
    private BaseAdapter adapter;//要实现的类
    private List<User> userList = new ArrayList<User>();//实体类


    SQLiteDatabase db;


    public static final String		DATABASE_FILENAME	= "english.db";				// 这个是DB文件名字
    public static final String		PACKAGE_NAME		= "com.example.flylsb.myyy";	// 这个是自己项目包路径

    public static final String		DATABASE_PATH		= android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
            +"abcdefg";	// 获取存储位置地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hellotow);
        bt = (Button) findViewById(R.id.btn_speak);
        lv = (ListView) findViewById(R.id.listView1);



    }



    private SQLiteDatabase openDatabase()
    {
        try {
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists())
                dir.mkdir();
            if (!(new File(databaseFilename)).exists()) {
                InputStream is = getResources().openRawResource(R.raw.english);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
            return db;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    //跳转页面按钮
    public void Click_return(View view) {

        // startActivity(new Intent(this, HellotowActivity.class));//红色部分为要打开的心窗口的类名

        try
        {
            startActivity(new Intent(this, Mainhello.class));//红色部分为要打开的心窗口的类名
        }
        catch (Exception ex)
        {
            // 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    public void Click_sqlite(View view) {
       // create("abcddr");
        String sql = "select * from eng where english=man";
        Vector<String> list = new Vector<String>();
        list = new Vector<String>();
        list.add("");
        SQLiteDatabase db = openDatabase();
       Cursor c = db.rawQuery("select * from eng " ,null);
       // Cursor c = db.rawQuery("select * from eng where english=?" ,new String[]{"man"});
       c.moveToFirst();

        ArrayList<String> alist = new ArrayList<String>();//动态数组
       while (!c.isAfterLast())
        {
            int id =c.getInt(0);
           // Log.i("tran", "success"+name);
           // Toast.makeText(HellotowActivity.this,name,Toast.LENGTH_LONG).show();
            String en = c.getString(c.getColumnIndex("english"));
            String name_str = c.getString(c.getColumnIndex("cn"));
            //String phone = c.getString(2);
            //System.out.println("_id:"+id+";name:"+name_str+";phone:");
            alist.add(name_str);
            list.add(name_str);
            User ue = new User();//给实体类赋值
            ue.setName(en);
            ue.setAge(name_str);
            userList.add(ue);
            c.moveToNext();
        }
        System.out.printf(list.toString());

        for(String a:alist)
            System.out.println(a);



        //模拟数据库
      /*  for (int i = 0; i < 5; i++) {
            User ue = new User();//给实体类赋值
            ue.setName("小米"+i);
            ue.setAge("18");
            userList.add(ue);
        }*/

        /*  bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


            }
        });*/

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return userList.size();//数目
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = HellotowActivity.this.getLayoutInflater();
                View view;

                if (convertView==null) {
                    //因为getView()返回的对象，adapter会自动赋给ListView
                    view = inflater.inflate(R.layout.item, null);
                }else{
                    view=convertView;
                    Log.i("info","有缓存，不需要重新生成"+position);
                }
                tv1 = (TextView) view.findViewById(R.id.Textviewname);//找到Textviewname
                tv1.setText(userList.get(position).getName());//设置参数

                tv2 = (TextView) view.findViewById(R.id.Textviewage);//找到Textviewage
                tv2.setText(userList.get(position).getAge());//设置参数
                return view;
            }
            @Override
            public long getItemId(int position) {//取在列表中与指定索引对应的行id
                return 0;
            }
            @Override
            public Object getItem(int position) {//获取数据集中与指定索引对应的数据项
                return null;
            }



        };
        lv.setAdapter(adapter);
        //获取当前ListView点击的行数，并且得到该数据
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv1 = (TextView) view.findViewById(R.id.Textviewname);//找到Textviewname
              String   str = tv1.getText().toString();//得到数据
                Toast.makeText(HellotowActivity.this, "" + str, Toast.LENGTH_SHORT).show();//显示数据
                BdTextToSpeech.getInstance(HellotowActivity.this).speak(str);

             /*   Intent it = new Intent(HellotowActivity.this, list0.class); //
                Bundle b = new Bundle();
                b.putString("we",str);  //string
                // b.putSerializable("dd",str);
                // it.putExtra("str_1",str);
                it.putExtras(b);
                startActivity(it);*/


            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODE) {
            //询问用户权限
            Toast.makeText(HellotowActivity.this,"需要读取sdcard权限",Toast.LENGTH_LONG).show();
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                //用户同意
                Toast.makeText(HellotowActivity.this,"已同意",Toast.LENGTH_LONG).show();
            } else {
                //用户不同意
                Toast.makeText(HellotowActivity.this,"已拒绝",Toast.LENGTH_LONG).show();
            }
        }
    }

   //创建文件夹方法
    public void create(String fileName){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            int checkSelfPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkSelfPermission == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUESTCODE);
            }
        }
        //Environment.getExternalStorageDirectory().getAbsolutePath():SD卡根目录
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+fileName);
        if (!file.exists()){
            boolean isSuccess = file.mkdirs();
            Toast.makeText(HellotowActivity.this,"文件夹创建成功",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(HellotowActivity.this,"文件夹已存在",Toast.LENGTH_LONG).show();
        }
    }


}
