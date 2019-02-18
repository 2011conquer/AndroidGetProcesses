package com.testprocesses.org.getprocess;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.List;
import com.testprocesses.org.getprocess.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private class ProcessInfo{
        private String strAppName;
        private String strPackageName;

        public ProcessInfo(String strAppName,String strPackageName){
            super();
            this.strAppName = strAppName;
            this.strPackageName = strPackageName;
        }
        public String GetAppName(){
            return this.strAppName;
        }
        public String GetPackageName(){
            return this.strPackageName;
        }
        @Override
        public String toString(){
            return "ProcessInfo: AppName = "+this.strAppName+" PackageName = "+this.strPackageName;
        }
    }

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private Activity activity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();

                ArrayList<ProcessInfo> infos = new ArrayList<>();
                for (AndroidAppProcess process:processes) {
                    String name = process.name;
                    String packageName = process.getPackageName();
                    Boolean bForeground = process.foreground;
                    String appName = Utils.getName(activity,process);
                    Log.d("===AppName : ",""+appName);
                    Log.d("Name : ",""+name);
                    Log.d("PackageName : ",""+packageName);
                    Log.d("Foreground : ",""+bForeground);
                    infos.add(new ProcessInfo(appName,packageName));
                }

                String strInfo = passProcessInfos(infos);

                TextView tv = (TextView) findViewById(R.id.sample_text);
                tv.setText(strInfo);
            }
        });

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String passProcessInfos(ArrayList<ProcessInfo> procList);
}
