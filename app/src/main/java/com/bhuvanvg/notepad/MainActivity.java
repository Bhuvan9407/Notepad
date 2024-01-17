package com.bhuvanvg.notepad;


import static com.bhuvanvg.notepad.Settings.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    LinearLayout layout;
    FloatingActionButton new_file_btn;
    SharedPreferences preferences;
    SharedPreferences.Editor prefseditor;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.conatiner);
        new_file_btn = findViewById(R.id.floatingActionButton);
        listing();
        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefseditor = preferences.edit();
        String dpmode = preferences.getString("Display","not defined");
        String listingmode = preferences.getString("listingmode","list");
        if (listingmode.equals("refresh2")){
            prefseditor.putString("listingmode","list");
        } else if (listingmode.equals("refresh")) {
            prefseditor.putString("listingmode","list");
        }
        prefseditor.commit();
        switch (dpmode) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "system":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
        new_file_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.new_file_popup);
                EditText file_name = dialog.findViewById(R.id.name_of_file);
                Spinner file_extension = dialog.findViewById(R.id.extension_of_file);
                Button cancel_btn = dialog.findViewById(R.id.cancel_new_file_btn);
                Button ok_btn = dialog.findViewById(R.id.ok_new_file_btn);
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String directory_path  = getFilesDir().getAbsolutePath() + "/savedfiles";
                        String filepath = directory_path +"/"+ file_name.getText() + file_extension.getSelectedItem();
                        File file = new File(filepath);
                        String filename = file_name.getText() + String.valueOf(file_extension.getSelectedItem());
                        Log.e("path",filepath);
                        try {
                            if(!file.exists()) {
                                file.createNewFile();
                                dialog.dismiss();
                                Intent open = new Intent(MainActivity.this,Openfile.class);
                                open.putExtra("path",filepath);
                                open.putExtra("file_name",filename);
                                SharedPreferences.Editor prefseditor = preferences.edit();
                                preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                                prefseditor.putString("listingmode","refresh");
                                prefseditor.commit();
                                listing();
                                startActivity(open);
                            }else{
                                Toast.makeText(MainActivity.this, "A file with the given name already exists!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("error", String.valueOf(e));
                        }
                    }
                });
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
    public void listing(){
        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefseditor = preferences.edit();
        layout = findViewById(R.id.conatiner);
        String listingmode = preferences.getString("listingmode","list");
        Log.d("listingmode", listingmode);
        if (listingmode.equals("list")) {
            File directory = new File(getFilesDir().getAbsolutePath() + "/savedfiles");
            if (directory.exists()) {
                File[] files = directory.listFiles();
                Log.e("data", Arrays.toString(files));
                if (Arrays.toString(files).equals("[]")) {
                    String nothing;
                } else {
                    String files_str = Arrays.toString(files);
                    int number_files = files.length;
                    int x = 0;
                    Log.e("data", files_str);
                    int last_num = files_str.length();
                    String array_files = files_str.replace("[", "");
                    String array_files2 = array_files.replace("]", "");
                    String[] array_files3 = (array_files2).split(",");
                    for (String a : array_files3) {
                        onclick(a);
                    }
                }
            } else {
                directory.mkdir();
            }
        } else if (listingmode.equals("refresh")) {

            layout.removeAllViews();
            prefseditor.putString("listingmode","list");
            prefseditor.commit();
            listing();
        } else if (listingmode.equals("refresh2")) {
            layout.removeAllViews();
            prefseditor.putString("listingmode","list");
            prefseditor.commit();
            listing();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.file_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent settings = new Intent(MainActivity.this, Settings.class);
            startActivity(settings);
            return true;
        }
        return true;
    }
    @SuppressLint("ResourceType")
    public void onclick(String name) {
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String name2 = name.trim();
        String file_name = null,extension = null;
        File path = new File(name2);
        try {

            Log.e("data", name2);
            file_name = path.getName();
            String[] extension1 = file_name.split("\\.");
            extension = "." + extension1[1];
        }catch (Exception e){
            Log.d("extension error", String.valueOf(e));
            extension = "";
        }
        View view = getLayoutInflater().inflate(R.layout.file_card,null);
        TextView file_nameview = view.findViewById(R.id.file_name);
        ImageView icon = view.findViewById(R.id.imageView);
        ImageView options = view.findViewById(R.id.optionsicon);
        file_nameview.setText(file_name);
        Log.d("extension",extension);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this, R.style.Theme_Notepad);
        TypedArray typedArray;
        int resourceId;
        switch (extension){
            case ".txt":
                typedArray = contextThemeWrapper.obtainStyledAttributes(R.style.Theme_Notepad, new int[]{R.attr.text});
                resourceId = typedArray.getResourceId(0, 0);
                typedArray.recycle();
                icon.setImageResource(resourceId);
                break;
            case ".html":
            case ".css":
            case ".js":
            case ".xml":
                typedArray = contextThemeWrapper.obtainStyledAttributes(R.style.Theme_Notepad,new int[]{R.attr.codeicon});
                resourceId = typedArray.getResourceId(0, 0);
                typedArray.recycle();
                icon.setImageResource(resourceId);
                break;
            case ".java":
            case ".class":
                typedArray = contextThemeWrapper.obtainStyledAttributes(R.style.Theme_Notepad,new int[]{R.attr.java});
                resourceId = typedArray.getResourceId(0, 0);
                typedArray.recycle();
                icon.setImageResource(resourceId);
                break;
            case ".py":
                typedArray = contextThemeWrapper.obtainStyledAttributes(R.style.Theme_Notepad,new int[]{R.attr.python});
                resourceId = typedArray.getResourceId(0, 0);
                typedArray.recycle();
                icon.setImageResource(resourceId);
                break;
            case "":
                String nothing;
        }
        layout.addView(view);
        String finalFile_name = file_name;
        String finalExtension = extension;
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
                popupMenu.getMenuInflater().inflate(R.menu.edit_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.openfile:
                                Intent open = new Intent(MainActivity.this, Openfile.class);
                                open.putExtra("path", name);
                                Log.e("data", name);
                                open.putExtra("file_name", finalFile_name);
                                startActivity(open);
                                return true;

                            case R.id.delete:
                                path.delete();
                                Toast.makeText(MainActivity.this, "File deleted!!", Toast.LENGTH_SHORT).show();
                                layout.removeView(view);
                                return true;

                            case R.id.rename:
                                Dialog dialog = new Dialog(MainActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.rename_popup);
                                EditText newname = dialog.findViewById(R.id.new_file_name);
                                Button cancel = dialog.findViewById(R.id.cancel_rename);
                                Button ok = dialog.findViewById(R.id.set_rename);
                                cancel.setOnClickListener(v -> dialog.dismiss());
                                ok.setOnClickListener(v -> {
                                    String newfilename = String.valueOf(newname.getText());
                                    if (!newfilename.equals("")) {
                                        String newfilename2 = name2.replace(path.getName(), newfilename + finalExtension);
                                        String newfilename3 = newfilename2.trim();
                                        File newnamefile = new File(newfilename3);
                                        if (!newnamefile.exists()) {
                                            dialog.dismiss();
                                            path.renameTo(newnamefile);
                                            prefseditor.putString("listingmode","refresh");
                                            prefseditor.commit();
                                            listing();
                                        } else {
                                            Toast.makeText(MainActivity.this, "A file with the given name already exists!!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Please type a name first!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.show();
                                return true;
                            case R.id.share:
                                String path2 = name.trim();
                                File test = new File(path2);
                                Log.e("data", path2);
                                Uri contentUri = FileProvider.getUriForFile(MainActivity.this, "com.bhuvanvg.notepad.fileprovider", test);

                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                startActivity(Intent.createChooser(shareIntent, "Share"+ finalFile_name));
                                return true;
                            case R.id.file_info:
                                Dialog dialog1 = new Dialog(MainActivity.this);
                                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog1.setCancelable(true);
                                dialog1.setContentView(R.layout.file_info_popup);
                                TextView filename = dialog1.findViewById(R.id.file_name);
                                TextView filetype = dialog1.findViewById(R.id.file_type);
                                TextView filesize = dialog1.findViewById(R.id.file_size);
                                TextView filemodified = dialog1.findViewById(R.id.file_modified);
                                Button okbtn = dialog1.findViewById((R.id.file_info_ok_btn));
                                filename.setText(finalFile_name);
                                String path = name.trim();
                                File file = new File(path);
                                Double file_size = (double) file.length();
                                String Finalfile_size = null ;
                                  if (file_size <= 600){
                                    Finalfile_size = String.valueOf(file_size) + " Bytes";
                                } else if (file_size > 600) {
                                    Finalfile_size = String.valueOf(file_size/1024) + " KB";
                                } else if (file_size/1024 >600) {
                                    Finalfile_size = String.valueOf(file_size/1024/1024) + " MB";
                                  }
                                int file_size2 = Integer.parseInt(String.valueOf(file.length()/1024));
                                filesize.setText(String.valueOf(file_size2) + " KB");
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                String datemodified = formatter.format(file.lastModified());
                                filemodified.setText(datemodified);
                                String Extension = finalExtension.trim();
                                Log.d("extension1", Extension);
                                switch(Extension){
                                    case ".txt":
                                        filetype.setText("Plain Text File");
                                        break;

                                    case ".java":
                                        filetype.setText("Java File");
                                        break;

                                    case ".html":
                                        filetype.setText("HTML Script");
                                        break;

                                    case ".css":
                                        filetype.setText("CSS Script");
                                        break;

                                    case ".xml":
                                        filetype.setText("XML Script");
                                        break;

                                    case ".js":
                                        filetype.setText("JavaScript file");
                                        break;

                                    case ".py":
                                        filetype.setText("Python File");
                                        break;

                                    case "":
                                        String nothing;
                                        break;
                                }
                                okbtn.setOnClickListener(v -> dialog1.dismiss());
                                dialog1.show();
                                return true;
                        }
                        return false;
                    }

                });
                popupMenu.show();
            }
        });
        String finalFile_name1 = file_name;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(MainActivity.this,Openfile.class);
                open.putExtra("path",name);
                open.putExtra("file_name", finalFile_name1);
                startActivity(open);
            }
        });
    }
}