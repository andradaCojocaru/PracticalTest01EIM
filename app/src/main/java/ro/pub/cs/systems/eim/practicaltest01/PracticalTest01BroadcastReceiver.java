package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public class PracticalTest01BroadcastReceiver extends BroadcastReceiver {
    private TextView messageTextView;

    public PracticalTest01BroadcastReceiver(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        if (messageTextView != null) {
            messageTextView.setText(messageTextView.getText().toString() + "\n" + message);
        }
        Log.d("PracticalTest01BroadcastReceiver", message);
    }
}