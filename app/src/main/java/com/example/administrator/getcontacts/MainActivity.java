package com.example.administrator.getcontacts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<String> list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


        public void click(View view){


            new Thread(){
                public void run() {
                    String path ="http://192.168.1.30/login";
                    String path1 ="http://192.168.1.30/contacts";
                    String log ="";
                    String token="";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("account","admin");
                        jsonObject.put("password","admin");
                        log = jsonObject.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String json = HttpUtils.doPost(path,log);
                    Log.i("Test",json);

                   JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = new JSONObject(json);
                        String data = jsonObject1.getString("data");
                        Log.i("Test",data);
                        JSONObject jsonObject2 = new JSONObject(data);
                         token = jsonObject2.getString("token");
                        Log.i("Test",token);

                        String s = HttpUtils.doPost(path1, token);
                        Log.i("Test",s);
                        JSONObject jsonObject3 = new JSONObject(s);
                        JSONArray data1 = jsonObject3.getJSONArray("data");
                        list = new ArrayList<>();
                        for (int i =0;i<data1.length();i++){

                            list.add(i,data1.get(i).toString());

                            Log.i("Test",data1.get(i).toString());
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
            }.start();

        }


    public void click1(View view){

        for (int i =0;i<list.size();i++ ){
            String s = list.get(i);
            insertContacts(s);
            Log.i("Test",s);
        }

        Toast.makeText(this, "插入联系人成功", Toast.LENGTH_LONG).show();
    }

  /*  try {
        String path ="http://192.168.1.30/contacts";

        // http://192.168.1.30/login
        // http://192.168.1.30/contacts
        // String path ="www.baidu.com";

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000);


        int code = conn.getResponseCode();
        if(code == 200){
            Log.d("Test","请求成功");
            InputStream is = conn.getInputStream();
            String data = StreamTools.readStream(is);
            Log.d("Test",data);

        }

    } catch (Exception e) {
        e.printStackTrace();
    }*/


    private void insertContacts(String contact) {
        ContentValues values = new ContentValues();
        //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        Uri rawContactUri = MainActivity.this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);

        //往data表入姓名数据
        values.clear();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact);
        MainActivity.this.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //往data表入电话数据
        values.clear();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "5554");
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        MainActivity.this.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //往data表入Email数据
        values.clear();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Email.DATA, "XXXXX@126.com");
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        MainActivity.this.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
    }


}

