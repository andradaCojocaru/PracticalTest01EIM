package ro.pub.cs.systems.eim.practicaltest01;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;
import java.util.Random;

public class ProcessingThread extends Thread {
    private Context context = null;
    private boolean isRunning = true;

    private Random random = new Random();

    private double arithmeticMean;
    private double geometricMean;


    public ProcessingThread(Context context, int firstNumber, int secondNumber) {
        this.context = context;
        arithmeticMean = (firstNumber + secondNumber) / 2;
        geometricMean = Math.sqrt(firstNumber * secondNumber);
    }

    @Override
    public void run() {
        Log.d("thread", "Thread has started!  ");
        while (isRunning) {
            sendMessage();
            sleep();
        }
        Log.d("thread", "Thread has stopped!");
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(Constant.actions[new Random().nextInt(Constant.actions.length)]);
        intent.putExtra("message", new Date(System.currentTimeMillis()) + " Arithmetic mean: " +
                arithmeticMean + ", Geometric mean: " +
                geometricMean);
        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
