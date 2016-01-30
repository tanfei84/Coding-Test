package com.example.tf.fiboapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final Messenger clientMessenger = new Messenger(new mHandler());
    Messenger serviceMessenger = null;
    private ListView listview;
    private ArrayAdapter<Integer> adapter;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            serviceMessenger = new Messenger(service);
            Message msg = Message.obtain(null, FibonacciService.MSG_READY_MAIN);
            msg.replyTo = clientMessenger;
            try {
                serviceMessenger.send(msg);
                Log.i("Main", "send a message to service with sender name" + clientMessenger.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            serviceMessenger = null;

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(this, FibonacciService.class), mConnection,
                Context.BIND_AUTO_CREATE);
        listview = (ListView) findViewById(R.id.listView1);
        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FibonacciService.MSG_FIBO_SEQ:
                    adapter.clear();
                    adapter.addAll((ArrayList<Integer>) msg.obj);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

}
