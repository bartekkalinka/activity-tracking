package pl.tracking.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pl.tracking.androidapp.R;
import pl.tracking.androidapp.TrackingServiceActions;

public class MainActivity extends AppCompatActivity {
    private EditText restURL;

    private final View.OnClickListener mStartButtonAction = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final Intent intent = new Intent();
            intent.setAction(TrackingServiceActions.CONNECT_ACTION);
            intent.putExtra(TrackingServiceActions.EXTRA_DOMAIN, restURL.getText().toString());
            sendBroadcast(intent);
        }
    };

    private final View.OnClickListener mCollectButtonAction = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final Intent intent = new Intent();
            intent.setAction(TrackingServiceActions.TRIGGER_START_TEST);
            sendBroadcast(intent);
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
