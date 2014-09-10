package testApp.com.snowplowanalytics;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.snowplowanalytics.snowplow.tracker.android.Subject;
import com.snowplowanalytics.snowplow.tracker.android.Tracker;
import com.snowplowanalytics.snowplow.tracker.android.emitter.Emitter;
import com.snowplowanalytics.snowplow.tracker.core.emitter.BufferOption;
import com.snowplowanalytics.snowplow.tracker.core.emitter.HttpMethod;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity {

    private enum TrackerEvents {
        trackScreenView,
        trackPageView,
        trackEcommItem,
        trackEcommTransaction,
        trackStructured,
        trackUnStructured
    }

    Emitter emitter = null;
    Subject subject = null;
    Tracker tracker = null;

    EditText editText;

    HttpMethod method = HttpMethod.POST;
    BufferOption bufferOption = BufferOption.Default;

    final String TAG = MyActivity.class.getName();
    TrackerEvents chosenEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        editText = (EditText) findViewById(R.id.editTextEndpoint);
        editText.setText("segfault.ngrok.com");


        subject = new Subject(MyActivity.this.getApplicationContext());
        emitter = new Emitter(editText.getText().toString(), this, HttpMethod.POST);
        tracker = new Tracker(emitter, subject, "someNamespace", "appId", false);

        final Button button = (Button) findViewById(R.id.buttonFireEvent);
        final Button buttonUpdate = (Button) findViewById(R.id.buttonUpdateTracker);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emitter = new Emitter(editText.getText().toString(),
                        MyActivity.this.getApplicationContext(), method);
                emitter.setBufferOption(bufferOption);
                tracker = new Tracker(emitter, subject, "someNamespace", "appId", false);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "button_event1 clicked");
                tracker.trackScreenView("Screen 1", "screen1");
            }
        });

        addSpinnerBufferOption();
        addSpinnerHttpMethod();
    }

    private void addSpinnerBufferOption() {
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerBufferOption);

        List<String> list = new ArrayList<String>();
        list.add("BufferOption.Default");
        list.add("BufferOption.Instant");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int)l) {
                    case 0:
                        bufferOption = BufferOption.Default;
                        break;
                    case 1:
                        bufferOption = BufferOption.Instant;
                        break;
                    default:
                        bufferOption = BufferOption.Default;
                }
                Log.v(TAG, "Set bufferOption to: " + bufferOption.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addSpinnerHttpMethod() {
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerHttpMethod);

        List<String> list = new ArrayList<String>();
        list.add("HttpMethod.POST");
        list.add("HttpMethod.GET");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) l) {
                    case 0:
                        method = HttpMethod.POST;
                        break;
                    case 1:
                        method = HttpMethod.GET;
                        break;
                    default:
                        method = HttpMethod.POST;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addSpinnerTracker() {
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerTracker);

        List<String> list = new ArrayList<String>();
        list.add("trackScreenView");
        list.add("trackPageView");
        list.add("trackEcommItem");
        list.add("trackEcommTransaction");
        list.add("trackStructured");
        list.add("trackUnStructured");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) l) {
                    case 0:
                        // Choose trackScreenView
                        chosenEvent = TrackerEvents.trackScreenView;
                        break;
                    case 1:
                        // Choose trackPageView
                        chosenEvent = TrackerEvents.trackPageView;
                        break;
                    case 2:
                        // Choose trackEcommItem
                        chosenEvent = TrackerEvents.trackEcommItem;
                        break;
                    case 3:
                        // Choose trackEcommTransaction
                        chosenEvent = TrackerEvents.trackEcommTransaction;
                    case 4:
                        // Choose trackStructured
                        chosenEvent = TrackerEvents.trackStructured;
                    case 5:
                        // Choose trackStructured
                        chosenEvent = TrackerEvents.trackUnStructured;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
