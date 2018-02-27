package com.example.xmlpull;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

//Xml文件读取和写入
public class MainActivity extends AppCompatActivity {
    ArrayList<HashMap<String,String>> dataList;
    HashMap<String,String> itemHashMap;
    private ListView xmlParserList;
    private XmlAdapter adapter;
    private Button addBtn;
    private Button nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xmlParserList = (ListView)findViewById(R.id.xmlParserList);
        addBtn = (Button)findViewById(R.id.addBtn);
        nextBtn = (Button)findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,TaskControllerActivity.class));
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataList != null){
                    addXmlItem(dataList);
                }
            }
        });
        checkPermission();
        xmlParser();
    }
    //权限检查
    private void checkPermission(){
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
        }
    }

    //读xml文档
    private void xmlParser(){
        try {
//            FileInputStream fileInputStream = openFileInput("info.xml");
            InputStream inputStream = getResources().getAssets().open("info.xml");
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(inputStream,"UTF-8");
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        dataList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("item")){
                            itemHashMap = new HashMap<>();
                        }else if(xmlPullParser.getName().equals("date")){
                            eventType = xmlPullParser.next();
                            itemHashMap.put("date",xmlPullParser.getText().trim());
                        }else if(xmlPullParser.getName().equals("remark")){
                            eventType = xmlPullParser.next();
                            itemHashMap.put("remark",xmlPullParser.getText().trim());
                        }
                        break;
                        case XmlPullParser.END_TAG:
                            if(xmlPullParser.getName().equals("item")){
                                dataList.add(itemHashMap);
                            }
                            break;
                }
                eventType = xmlPullParser.next();
            }
            if(dataList != null) {
                adapter = new XmlAdapter(MainActivity.this, dataList);
                xmlParserList.setAdapter(adapter);
            }
        }catch (Exception e){
            Log.i("wanlijun",e.toString());
            e.printStackTrace();
        }
    }

    //写XML文档
    private void addXmlItem(ArrayList<HashMap<String,String>> list){
        try {
            //用openFileOutput写入到data/data/包名/files文件时，没写进去
            FileWriter writer = new FileWriter(new File(Environment.getExternalStorageDirectory()+"/info.txt"));
//           OutputStream outputStream =  openFileOutput("info.xml",MODE_PRIVATE+MODE_APPEND);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(writer);
//            xmlSerializer.setOutput(outputStream,"UTF-8");
            xmlSerializer.startDocument("UTF-8",true);
            xmlSerializer.startTag("","baklist");
            for(HashMap<String,String> hashMap:list){
                xmlSerializer.startTag("","item");
                xmlSerializer.startTag("","date");
                xmlSerializer.text(hashMap.get("date"));
                xmlSerializer.endTag("","date");
                xmlSerializer.startTag("","remark");
                xmlSerializer.text(hashMap.get("remark"));
                xmlSerializer.endTag("","remark");
                xmlSerializer.endTag("","item");
            }
            xmlSerializer.endTag("","baklist");
            xmlSerializer.endDocument();
        }catch (Exception e){
            Log.i("wanlijun",e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    class XmlAdapter extends BaseAdapter{
        private Context mContext;
        private ArrayList<HashMap<String,String>> mXmlTagParserList;
        public XmlAdapter(Context mContext,ArrayList<HashMap<String,String>> hashMapArrayList){
            this.mContext = mContext;
            this.mXmlTagParserList = hashMapArrayList;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.item_layout,null);
                holder = new ViewHolder(view);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            if(holder != null){
                HashMap<String,String> item = mXmlTagParserList.get(i);
                holder.remarkTv.setText(item.get("remark"));
                holder.dateTv.setText(item.get("date"));
            }
            return view;
        }

        @Override
        public int getCount() {
            return mXmlTagParserList.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return mXmlTagParserList.get(i);
        }
    }
    static class ViewHolder{
        TextView remarkTv;
        TextView dateTv;
        public ViewHolder(View view){
            remarkTv = (TextView)view.findViewById(R.id.remarkTv);
            dateTv = (TextView)view.findViewById(R.id.dateTV);
            view.setTag(this);
        }
    }
}
