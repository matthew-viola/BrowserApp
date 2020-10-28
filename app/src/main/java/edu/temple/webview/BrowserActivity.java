package edu.temple.webview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BrowserActivity extends AppCompatActivity {
WebView wv;
String urlStr;
EditText urlTextBox;
Button go;
Button forward;
Button back;
ArrayList<String> history;
int index;
Handler responseHandler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(@NonNull Message msg) {

        wv.loadUrl(urlStr);

        return false;
    }

});

    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        wv.saveState(outState);
        outState.putStringArrayList("history", history);
        outState.putInt("index" , index);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        wv.restoreState(savedInstanceState);
        history = savedInstanceState.getStringArrayList("history");
        index = savedInstanceState.getInt("index");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        index = -1;
        history = new ArrayList<String>();
        back = findViewById(R.id.btnBack);
        forward = findViewById(R.id.btnFwd);
        go = findViewById(R.id.btnGo);
        urlTextBox = findViewById(R.id.txtURL);

        wv = findViewById(R.id.webview);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlStr = history.get(index-1);
                index--;
           //     history.add(urlStr);
                Thread t = new Thread() {public void run()
                {try{
                    URL url = new URL(urlStr);
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(
                                    url.openStream()));
                    String response = reader.readLine();
                    Message msg = Message.obtain();
                    msg.obj = response;
                    responseHandler.sendMessage(msg);


                } catch (Exception e) {}
                }
                };
                t.start();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlStr = history.get(index + 1);
                index++;
                history.add(urlStr);
                Thread t = new Thread() {public void run()
                {try{
                    URL url = new URL(urlStr);
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(
                                    url.openStream()));
                    String response = reader.readLine();
                    Message msg = Message.obtain();
                    msg.obj = response;
                    responseHandler.sendMessage(msg);


                } catch (Exception e) {}
                }
                };
                t.start();
            }
        });


        wv.setWebViewClient(new WebViewClient());

        wv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlStr = wv.getUrl();
            }
        });

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                urlTextBox.setText(url);
                history.add(urlTextBox.getText().toString());
                index++;
            }
        });


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                urlStr = urlTextBox.getText().toString();
                if(urlTextBox.getText().toString().contains("https://")){
                    Thread t = new Thread() {public void run()
                    {try{
                        URL url = new URL(urlStr);
                        BufferedReader reader = new BufferedReader
                                (new InputStreamReader(
                                        url.openStream()));
                        String response = reader.readLine();
                        Message msg = Message.obtain();
                        msg.obj = response;
                        responseHandler.sendMessage(msg);
                        history.add(urlStr);
                        index++;
                    } catch (Exception e) {}
                    }
                    };
                    t.start();

                }
                else {
                    urlStr = "https://" + urlTextBox.getText().toString();
                    Thread t = new Thread() {public void run()
                    {try{
                        URL url = new URL(urlStr);
                        BufferedReader reader = new BufferedReader
                                (new InputStreamReader(
                                        url.openStream()));
                        String response = reader.readLine();
                        Message msg = Message.obtain();
                        msg.obj = response;
                        responseHandler.sendMessage(msg);
                    } catch (Exception e) {}
                    }
                    };
                    t.start();
                    urlTextBox.setText(urlStr);
                    history.add(urlStr);
                    index++;
                }
            }
        });


    }
}
