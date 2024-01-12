package com.bhuvanvg.notepad;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class Settings extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefs";
    SharedPreferences preferences;
    RadioButton System_radio_btn,Light_radio_btn,Dark_radio_btn,autosave_off,autosave_on;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String dpmode = preferences.getString("Display","not defined");
        System_radio_btn = findViewById(R.id.radio_system);
        Light_radio_btn = findViewById(R.id.radio_light);
        Dark_radio_btn = findViewById(R.id.radio_dark);
        switch (dpmode) {
            case "light":
                Light_radio_btn.setChecked(true);
                break;
            case "dark":
                Dark_radio_btn.setChecked(true);
                break;
            case "system":
                System_radio_btn.setChecked(true);
                break;
        }
        String autosavemode = preferences.getString("Autosave","off");
        autosave_off = findViewById(R.id.autosave_off);
        autosave_on = findViewById(R.id.autosave_on);
        switch(autosavemode){
            case "off":
                autosave_off.setChecked(true);
                break;
            case "on":
                autosave_on.setChecked(true);
                break;
        }

    }
    public void radiobtnclicked(View view) {
        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefseditor = preferences.edit();
        boolean checked = ((RadioButton) view).isChecked();
        try {
            switch (view.getId()) {
                case R.id.radio_light:
                    if (checked)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    prefseditor.putString("Display","light");
                    break;
                case R.id.radio_dark:
                    if (checked)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    prefseditor.putString("Display","dark");
                    break;
                case R.id.radio_system:
                    if (checked)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    prefseditor.putString("Display","system");
                    break;
            }
        }catch (Exception e){
            String exception = String.valueOf(e);
            Toast.makeText(this, exception, Toast.LENGTH_SHORT).show();
        }
        prefseditor.commit();
    }
    public void autosave_btn_clicked(View view) {
        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefseditor = preferences.edit();
        boolean checked = ((RadioButton) view).isChecked();
        try {
            switch (view.getId()) {
                case R.id.autosave_off:
                    if (checked)
                    prefseditor.putString("Autosave","off");
                    break;
                case R.id.autosave_on:
                    if (checked)
                    prefseditor.putString("Autosave","on");
                    break;
            }
        }catch (Exception e){
            String exception = String.valueOf(e);
            Toast.makeText(this, exception, Toast.LENGTH_SHORT).show();
        }
        prefseditor.commit();
    }
}