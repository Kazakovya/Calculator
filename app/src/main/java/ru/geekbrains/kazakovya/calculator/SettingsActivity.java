package ru.geekbrains.kazakovya.calculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private static final String MY_PREFERENCES = "nightModePreferences";
    private static final String KEY_NIGHT_MODE = "nightMode";
    String KEY_CHANGE_THEME = "changeTheme";
    private SharedPreferences sharedPreferences;
    private SwitchMaterial changeTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        changeTheme = findViewById(R.id.change_theme);

        checkNightModeActivated();

        changeTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveNightModeState(true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveNightModeState(false);
            }
            recreate();
        });
    }

    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NIGHT_MODE, nightMode).apply();
    }

    public void checkNightModeActivated() {
        if (sharedPreferences.getBoolean(KEY_NIGHT_MODE, false)) {
            changeTheme.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            changeTheme.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_CHANGE_THEME, changeTheme.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        changeTheme.setChecked(savedInstanceState.getBoolean(KEY_CHANGE_THEME));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(KEY_NIGHT_MODE, sharedPreferences.getBoolean(KEY_NIGHT_MODE, false));
        setResult(RESULT_OK, intent);
        finish();
    }
}