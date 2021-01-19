package com.example.drinkwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnnotify;
    private EditText ediMinutos;
    private TimePicker timePicker;

    private int hour;
    private int minute;
    private int interval;
    private Build.VERSION_CODES android;
    private boolean activated = false;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnnotify = findViewById(R.id.btn_notify);
        ediMinutos = findViewById(R.id.txt_Hora);
        timePicker = findViewById(R.id.time_Picker);

        timePicker.setIs24HourView(true);

        //Chama o banco de dados
        preferences = getSharedPreferences("db", Context.MODE_PRIVATE);
        activated = preferences.getBoolean("activated", false);

        if ( activated ) {
            btnnotify.setText(R.string.pause);
            int color = ContextCompat.getColor(this, R.color.black);
            btnnotify.setBackgroundTintList(ColorStateList.valueOf(color));

            int interval = preferences.getInt("inteval", 0);
            int hour = preferences.getInt("hour", timePicker.getCurrentHour());
            int minute = preferences.getInt("minute", timePicker.getCurrentMinute());

            ediMinutos.setText(String.valueOf(interval));
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
    }

    public void notifyClick(View view) {
        String sInterval = ediMinutos.getText().toString();
        if ( sInterval.isEmpty() ) {
            Toast.makeText(this, R.string.error_msg, Toast.LENGTH_LONG).show();
            return;
        }

        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        interval = Integer.parseInt(sInterval);

        if ( !activated ) {
            btnnotify.setText(R.string.pause);
            int color = ContextCompat.getColor(this, R.color.black);
            btnnotify.setBackgroundTintList(ColorStateList.valueOf(color));
            activated = true;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", true);
            editor.putInt("interval", interval);
            editor.putInt("hour", hour);
            editor.putInt("minute", minute);
            editor.apply();
        } else {
            btnnotify.setText(R.string.notify);
            int color = ContextCompat.getColor(this, R.color.colorAccent);
            btnnotify.setBackgroundTintList(ColorStateList.valueOf(color));
            activated = false;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", false);
            editor.remove("interval");
            editor.remove("hour");
            editor.remove("minute");
            editor.apply();

        }


        Log.d("teste ", "Hora: " + hour + " Minuto: " + minute + " Intervalo: " + interval);
    }
}