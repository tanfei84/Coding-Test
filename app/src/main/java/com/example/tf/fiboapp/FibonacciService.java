package com.example.tf.fiboapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by TF on 1/30/2016.
 */
public class FibonacciService extends Service {
    static final int MSG_READY_MAIN = 1;
    static final int MSG_FIBO_SEQ = 2;
    private long newElement = 0;
    private Timer timer = new Timer();
    private Messenger clientMessenger = null;
    private ArrayList<Long> FibonacciSequence = new ArrayList<Long>() {{
        add((long) 1);
        add((long) 1);
    }};

    final Messenger serviceMessenger = new Messenger(new IncomingHandler());
    private final IBinder mBinder = new LocalBinder();

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_READY_MAIN:
                    clientMessenger = msg.replyTo;
                    //Log.i("Service", "Got the Ready Message from Main");
                    startCalculation();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public class LocalBinder extends Binder {
        public FibonacciService getService() {
            return FibonacciService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceMessenger.getBinder();
        //Used for running test
        //return mBinder;
    }

    private void startCalculation() {
        timer.scheduleAtFixedRate(new TimerTask(){ public void run() {FibonacciCalculation();}}, 0, 500);
    }

    private void sendMessageToUI(ArrayList<Long> fibonacciSequence) {
        Message msg = Message.obtain(null, MSG_FIBO_SEQ, fibonacciSequence);
        try {
            clientMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void FibonacciCalculation() {
        newElement = FibonacciSequence.get(FibonacciSequence.size()-1) +
                FibonacciSequence.get(FibonacciSequence.size()-2);
        if(newElement <= Long.MAX_VALUE && newElement >= 0) {
            FibonacciSequence.add(newElement);
            sendMessageToUI(FibonacciSequence);//Not used in testing
        } else {
            timer.cancel();//Not used in testing
        }

    }

    public ArrayList<Long> getFibonacciSequence() {
        return FibonacciSequence;
    }

}
