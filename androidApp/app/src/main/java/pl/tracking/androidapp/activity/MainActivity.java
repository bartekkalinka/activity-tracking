package pl.tracking.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import pl.tracking.androidapp.R;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "restURL";
    private EditText restURL;

    private final View.OnClickListener mStartButtonAction = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final Intent intent = new Intent();
            intent.setClass(MainActivity.this, AccelerometerActivity.class);
            intent.putExtra(URL, restURL.getText().toString());
            startActivity(intent);
        }
    };

    private final View.OnClickListener mCollectButtonAction = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final Intent intent = new Intent();
            //intent.setClass(MainActivity.this, CollectDataActivity.class);
            intent.putExtra(URL, restURL.getText().toString());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restURL = (EditText) findViewById(R.id.editURL);

        final Button myStartButton = (Button) findViewById(R.id.button_start);
        myStartButton.setOnClickListener(mStartButtonAction);

        final Button myCollectButton = (Button) findViewById(R.id.button_collect);
        myCollectButton.setOnClickListener(mCollectButtonAction);
    }
}
