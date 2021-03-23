package ru.geekbrains.kazakovya.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int CAPACITY = 8;
    public static final String MY_TAG = "Lifecicle";
    public static final String VALUE = "Val";
    public static final String  KEY_MAIN_SCREEN = "MainScreen";
    public static final String  KEY_EQUATION = "Equation";
    public static final String  KEY_MEMORY = "Memory";
    public static final String MY_PREFERENCES = "nightModePreferences";
    public static final String KEY_NIGHT_MODE = "nightMode";
    SharedPreferences sharedPreferences;

    private TextView mTextView;
    private TextView mExpressionView;
    private CalculatorModel calculatorModel;
    SwitchCompat changeTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        Button buttonPt = findViewById(R.id.buttonPt);
        Button buttonPlus = findViewById(R.id.buttonPlus);
        Button buttonMinus = findViewById(R.id.buttonMinus);
        Button buttonX = findViewById(R.id.buttonX);
        Button buttonDiv = findViewById(R.id.buttonDiv);
        Button buttonEq = findViewById(R.id.buttonEq);
        Button buttonMR = findViewById(R.id.buttonMR);
        Button buttonMC = findViewById(R.id.buttonMC);
        Button buttonMPlus = findViewById(R.id.buttonMPlus);
        Button buttonMMinus = findViewById(R.id.buttonMMinus);
        Button buttonC = findViewById(R.id.buttonC);
        Button buttonBack = findViewById(R.id.buttonBack);
        Button buttonPosNeg = findViewById(R.id.buttonPosNeg);
        Button settings = findViewById(R.id.settings);
        mTextView = findViewById(R.id.inputStr);
        mExpressionView = findViewById(R.id.phrase);
        TextView mMemMark = findViewById(R.id.memMark);
        changeTheme = findViewById(R.id.change_theme);


        Button [] buttonsNum = new Button[] {
                button0,
                button1,
                button2,
                button3,
                button4,
                button5,
                button6,
                button7,
                button8,
                button9,
                buttonPt,
                buttonBack,
                buttonPosNeg,
        };

        Button [] buttonsMainAct = new Button[] {
                buttonPlus,
                buttonMinus,
                buttonX,
                buttonDiv,
                buttonMR,
                buttonMC,
                buttonMPlus,
                buttonMMinus
        };

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);


        Log.e(MY_TAG, "onCreate(): " + (savedInstanceState == null ? "first" : "next"));

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

        calculatorModel = new CalculatorModel(mTextView, mExpressionView, mMemMark);

        for (Button button : buttonsNum) {
            button.setOnClickListener(calculatorModel.buttonsNumClickListener);
        }

        for (Button button : buttonsMainAct) {
            button.setOnClickListener(calculatorModel.buttonsMainActClickListener);
        }

        buttonC.setOnClickListener(calculatorModel.buttonCClickListener);

        buttonEq.setOnClickListener(calculatorModel.buttonEqClickListener);

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, RESULT_OK);
        });
        checkNightModeActivated();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(KEY_MAIN_SCREEN, mTextView.getText().toString());
        state.putString(KEY_EQUATION, mExpressionView.getText().toString());
        state.putFloat(KEY_MEMORY, (float) calculatorModel.getMemory());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calculatorModel.setState(savedInstanceState.getString(KEY_EQUATION), savedInstanceState.getString(KEY_MAIN_SCREEN));
        calculatorModel.setMemory(savedInstanceState.getFloat(KEY_MEMORY));
    }

    @Override
    public void onClick(View v) {
    }

    public void checkNightModeActivated() {
        if (sharedPreferences.getBoolean(KEY_NIGHT_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RESULT_CANCELED) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (resultCode == RESULT_OK) {
            saveNightModeState(data.getExtras().getBoolean(KEY_NIGHT_MODE));
            recreate();
        }
    }

    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NIGHT_MODE, nightMode).apply();
    }
}