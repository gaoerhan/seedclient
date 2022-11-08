package com.seedreality.seedclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.seedreality.seedserver.Track;

public class MainActivity extends AppCompatActivity {


    Button btnGet;
    TextView tvGet;
    private Track myBinder;//定义AIDL

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = Track.Stub.asInterface(iBinder);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btnGet = (Button) findViewById(R.id.btn_getdata);
        tvGet = findViewById(R.id.tv_getdata);

        //绑定service
        Intent intent = new Intent();
        intent.setAction("com.seedreality.seedserver.SeedService");
        intent.setPackage("com.seedreality.seedserver");
        bindService(intent, conn, BIND_AUTO_CREATE);//开启Service

        btnGet.setOnClickListener((View view) -> {
                    try {
                        String content = myBinder.getData();
                        tvGet.setText(content);
                    } catch (RemoteException e) {
                        //因为是跨程序调用服务，可能会出现远程异常
                        e.printStackTrace();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
        );

    }
}