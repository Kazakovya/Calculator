package ru.geekbrains.kazakovya.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int CAPASITY = 8;
    public static final String MY_TAG = "Lifecicle";
    public static final String VALUE = "Val";
    public static final String  KEY_MAIN_SCREEN = "MainScreen";
    public static final String  KEY_EQUATION = "Equation";
//    public static final String  KEY_FIRST_ARG = "FirstArg";
//    public static final String  KEY_SND_ARG = "FirstArg";
//    public static final String  KEY_LASTACTION_ARG = "FirstArg";

    static TextView mTextView;
    private TextView mExpressionView;
    private TextView mMemMark;
    private CalculatorModel calculatorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(MY_TAG, "onCreate(): " + (savedInstanceState == null ? "first" : "next"));

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
        mTextView = findViewById(R.id.inputStr);
        mExpressionView = findViewById(R.id.phrase);
        mMemMark = findViewById(R.id.memMark);

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
//                buttonMR,
//                buttonMC,
//                buttonMPlus,
//                buttonMMinus
        };
//        Button [] buttonsMemoryAct = new Button[] {
//                buttonMR,
//                buttonMC,
//                buttonMPlus,
//                buttonMMinus
//        };

        calculatorModel = new CalculatorModel(mTextView, mExpressionView, mMemMark);

        for (int i = 0; i < buttonsNum.length; i++) {
            buttonsNum[i].setOnClickListener(calculatorModel.buttonsNumClickListener);
        }

        for (int i = 0; i < buttonsMainAct.length; i++) {
            buttonsMainAct[i].setOnClickListener(calculatorModel.buttonsMainActClickListener);
        }

//        for (int i = 0; i < buttonsMemoryAct.length; i++) {
//            buttonsMemoryAct[i].setOnClickListener(calculatorModel.buttonsMemoryActClickListener);
//        }

        buttonC.setOnClickListener(calculatorModel.buttonCClickListener);

        buttonEq.setOnClickListener(calculatorModel.buttonEqClickListener);

//        for (int i = 0; i < buttonsMemoryAct.length; i++) {
//            buttonsMainAct[i].setOnClickListener(calculatorModel.mButtonsMemoryActionClickListener);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(MY_TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(MY_TAG, "onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(MY_TAG, "onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(MY_TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(MY_TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(MY_TAG, "onDestroy()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(KEY_MAIN_SCREEN, mTextView.getText().toString());
        state.putString(KEY_EQUATION, mExpressionView.getText().toString());
        Log.e(VALUE, "111. mInputStr: " + mTextView.getText()
            + "\nmExpression: " + mExpressionView.getText());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calculatorModel.setState(savedInstanceState.getString(KEY_EQUATION), savedInstanceState.getString(KEY_MAIN_SCREEN));
        Log.e(VALUE, "112. mInputStr: " + savedInstanceState.getString(KEY_MAIN_SCREEN)
                + "\nmExpression: " + savedInstanceState.getString(KEY_EQUATION));
    }

    @Override
    public void onClick(View v) {
    }

}