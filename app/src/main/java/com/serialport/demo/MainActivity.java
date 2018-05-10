package com.serialport.demo;

import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.serialport.demo.manager.SenderManager;
import com.serialport.library.SerialportManager;
import com.serialport.library.listener.OnS3DataReceiverListener;
import com.serialport.library.listener.OnS6DataReceiverListener;
import com.serialport.library.utils.TypeConversion;
/**
 * Created by Jian on 2017/8/7.
 */
public class MainActivity extends AppCompatActivity implements OnS3DataReceiverListener,OnS6DataReceiverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debug.startMethodTracing("GithubApp");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        SerialportManager.getInstance().setOnS3DataReceiverListener(this);
        SerialportManager.getInstance().setOnS6DataReceiverListener(this);
        SerialportManager.getInstance().InitThread();
        SenderManager.getInstance().getSender().sendStartDetect();
        Debug.stopMethodTracing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SerialportManager.getInstance().removeOnS3DataReceiverListener();
        SerialportManager.getInstance().removeOnS6DataReceiverListener();
    }

    @Override
    public void onS3DataReceive(byte[] buffer, int size) {
        byte[] mBufferTemp = new byte[size];
        System.arraycopy(buffer, 0, mBufferTemp, 0, size);
        int length = mBufferTemp.length - 1;
        String tempdata = TypeConversion.bytes2HexString(mBufferTemp);
        Log.i("serialport",tempdata);
    }

    @Override
    public void onS6DataReceive(byte[] buffer, int size) {

    }
}
