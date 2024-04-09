package ro.pub.cs.systems.eim.practicaltest01;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest01MainActivity extends AppCompatActivity {
    private static final int SECONDARY_ACTIVITY_REQUEST_CODE = 0;
        private IntentFilter intentFilter = new IntentFilter();
    private TextView messageTextView;
    private Button invokeButton;
    private int totalClicks = 0;
    private static final int THRESHOLD = 10;
    private Intent serviceIntent = null;

    private PracticalTest01BroadcastReceiver messageBroadcastReceiver;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("practical", "onSaveInstanceState() method was invoked");
        super.onSaveInstanceState(outState);
        TextView textView1 = findViewById(R.id.textView1);
        TextView textView2 = findViewById(R.id.textView2);
        outState.putString("textView1", textView1.getText().toString());
        outState.putString("textView2", textView2.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("practical", "onRestoreInstanceState() method was invoked");
        super.onRestoreInstanceState(savedInstanceState);
        TextView textView1 = findViewById(R.id.textView1);
        TextView textView2 = findViewById(R.id.textView2);
        textView1.setText(savedInstanceState.getString("textView1"));
        textView2.setText(savedInstanceState.getString("textView2"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("practical", "onCreate() method was invoked");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test01_main);

        messageTextView = (TextView)findViewById(R.id.message_text_view);
        messageTextView.setMovementMethod(new ScrollingMovementMethod());

        messageBroadcastReceiver = new PracticalTest01BroadcastReceiver(messageTextView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        for (int index = 0; index < 3; index++) {
            intentFilter.addAction(Constant.actions[index]);
        }

        invokeButton = (Button)findViewById(R.id.invokeButton);
        invokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                intent.putExtra("totalClicks", totalClicks);
                startService(intent);
            }
        });

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        TextView textView1 = findViewById(R.id.textView1);
        TextView textView2 = findViewById(R.id.textView2);

        button1.setOnClickListener(v -> {
            int count = Integer.parseInt(textView1.getText().toString());
            textView1.setText(String.valueOf(count + 1));
            totalClicks++;
            if (totalClicks > THRESHOLD && serviceIntent == null) {
                serviceIntent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                serviceIntent.putExtra("firstNumber", count + 1);
                serviceIntent.putExtra("secondNumber", Integer.parseInt(textView2.getText().toString()));
                startService(serviceIntent);
            }
        });

        button2.setOnClickListener(v -> {
            int count = Integer.parseInt(textView2.getText().toString());
            textView2.setText(String.valueOf(count + 1));
            totalClicks++;
            if (totalClicks > THRESHOLD && serviceIntent == null) {
                serviceIntent = new Intent(getApplicationContext(), PracticalTest01Service.class);
               serviceIntent.putExtra("firstNumber", Integer.parseInt(textView1.getText().toString()));
                serviceIntent.putExtra("secondNumber", count + 1);
                startService(serviceIntent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "The activity returned with result OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "The activity returned with result CANCELED", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("practical", "onRestart() method was invoked");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("practical", "onStart() method was invoked");
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
        Log.d("practical", "onResume() method was invoked");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(messageBroadcastReceiver);
        Log.d("practical", "onPause() method was invoked");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("practical", "onStop() method was invoked");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("practical", "onDestroy() method was invoked");
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }


}