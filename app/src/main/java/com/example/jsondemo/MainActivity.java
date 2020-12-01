package com.example.jsondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    public void getWeather(View view){

        try {

            String cityName = URLEncoder.encode(editText.getText().toString(),"UTF-8");

            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=5deff0fb6765b85347340c89d9680d43");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            textView.setText("could not find weather");


        }

    }

    public class DownloadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            String result ="";
            URL url;
            HttpURLConnection urlConnection=null;
            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection != null) {
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();
                    while (data != -1) {
                        result = result + (char) data;
                        data = reader.read();

                    }
                    return result;
                }else{
                    Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_SHORT).show();
                    return null;
                }

                } catch(Exception e){
                    e.printStackTrace();
                Thread thread = new Thread(){
                    public void run(){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                textView.setText("could not find weather");
                            }
                        });
                    }
                };
                thread.start();
                    return null;
                }

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            try {
                JSONObject jsonObject=new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("JSON", weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);
                String m="";
                String d="";

                for(int i=0;i< jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                     m= jsonObject1.getString("main");
                     d= jsonObject1.getString("description");

                }

               if(!m.equals("") && !d.equals("")){
                   textView.setText(m + "\n" + d);
               }
               else{
                   textView.setText("could not find weather");
               }

            } catch (Exception e) {
                e.printStackTrace();
                textView.setText("could not find weather");
            }

        }

    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            editText=findViewById(R.id.editText);
            textView=findViewById(R.id.textView2);


        }

}
