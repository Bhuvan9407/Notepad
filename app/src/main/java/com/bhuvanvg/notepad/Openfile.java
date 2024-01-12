package com.bhuvanvg.notepad;

import static com.bhuvanvg.notepad.Settings.PREFS_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class Openfile extends AppCompatActivity {
    EditText textarea;
    String file_name,data,autosavemode;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openfile);
        textarea = findViewById(R.id.textarea);
        textarea.setBackgroundResource(android.R.color.transparent);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        file_name = intent.getStringExtra("file_name");
        getSupportActionBar().setTitle(file_name);
        String firstchar = String.valueOf(path.charAt(0));
        String path2 = path.trim();
        File file = new File(path2);
        data = text(file);
        textarea.setText(data);
        Log.e("path", String.valueOf(path));
        Log.e("name", file_name);
        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefseditor = preferences.edit();
        prefseditor.putString("listingmode","refresh2");
        autosavemode = preferences.getString("Autosave","off");
        if(autosavemode.equals("on")){
            autosavestep1();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.open_file_menu,menu);
        MenuItem save_item = menu.findItem(R.id.save);
        switch(autosavemode){
            case "off":
                String nothing;
                break;
            case "on":
                SpannableString s = new SpannableString("Autosave in On");
                s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
                save_item.setTitle(s);
                save_item.setEnabled(false);
                break;
        }
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                Save();
                return true;
            case R.id.selectall:
                textarea.requestFocus();
                textarea.selectAll();
        }
        return true;
    }
    public String text(File file){
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            reader.close();
        }catch (Exception e){
            Log.e("data", String.valueOf(e));
        }
        return builder.toString();
    }
    public void Save(){
        String text = String.valueOf(textarea.getText());
        String filepath = getFilesDir().getAbsolutePath() + "/savedfiles/" + file_name;
        try {
            FileWriter fileWriter = new FileWriter(filepath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(text);
            bufferedWriter.close();
        } catch (IOException e) {
            Log.e("error", String.valueOf(e));
        }
    }
    public void autosavestep1(){
        textarea = findViewById(R.id.textarea);
        textarea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String nothing;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nothing;
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("nothing","autosave");
                String text = String.valueOf(textarea.getText());
                String filepath = getFilesDir().getAbsolutePath() + "/savedfiles" + "/" + file_name;
                try {
                    FileWriter fileWriter = new FileWriter(filepath);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(text);
                    bufferedWriter.close();
                } catch (IOException e) {
                    Log.e("error", String.valueOf(e));
                }
            }
        });
    }
}