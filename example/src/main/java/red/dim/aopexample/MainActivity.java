package red.dim.aopexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import me.ele.aopexample.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: --8989 +++-0-0-0-0-");
        final AOPClick aopClick = new AOPClick();

        findViewById(R.id.android_lib_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aopClick.androidLibClick();
            }
        });
        findViewById(R.id.java_lib_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aopClick.javaClick();

            }
        });
        findViewById(R.id.project_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aopClick.projectClick();
            }
        });
    }
}
