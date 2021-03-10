package ru.geekbrains.kazakovya.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static int CAPASITY = 9;
    public static final String MY_TAG = "Lifecicle";
    public static final String VALUE = "Val";


    private TextView mTextView;
    private TextView mPhraseView;
    private StringBuilder mExpression = new StringBuilder();
    private StringBuilder mInputStr = new StringBuilder();
    private String result;
    private int trueCapasity = CAPASITY;
    private boolean mEmptyTextView = true;
    private boolean mIsInteger = true;
    private boolean lastKeyIsNum;
    private boolean mMustEdit;

    private View.OnClickListener mButtonsNumClickListener = new View.OnClickListener() {
                @Override
        public void onClick(View v) {
            Log.e(VALUE, "EmptyTextView: " + mEmptyTextView);
            Log.e(VALUE, "(mInputStr.length() < CAPASITY): " + String.valueOf((mInputStr.length() < CAPASITY)));
            Log.e(VALUE, "!((Button) v).getText().equals(\".\"):  " + String.valueOf(!((Button) v).getText().equals(".")));
            Log.e(VALUE, "!((Button) v).getText().equals(\"0\"):  " + String.valueOf(!((Button) v).getText().equals("0")));

            if (!((Button) v).getText().equals("0") && !((Button) v).getText().equals(".")) mEmptyTextView = false;

            if ((mInputStr.length() < trueCapasity) && !mEmptyTextView && !((Button) v).getText().equals(".")) {
                lastKeyIsNum = true;
                mInputStr.append(((String) ((Button) v).getText()));
                mTextView.setText(mInputStr);
            } else if ((((Button) v).getText()).equals(".")) {
                if (!mEmptyTextView && mIsInteger) {
                    mInputStr.append(".");
                    trueCapasity++;
                } else if (mEmptyTextView) {
                    mInputStr.append("0.");
                    mEmptyTextView = false;
                    trueCapasity++;
                }
                mIsInteger = false;
                lastKeyIsNum = true;
                mTextView.setText(mInputStr);
            } else if (mEmptyTextView) {
                mTextView.setText("0");
            }
            Log.e(VALUE, "mInputStr: " + mInputStr);
            Log.e(VALUE, "mPhrase " + mExpression);
            Log.e(VALUE, "EmptyTextView: " + mEmptyTextView);
            Log.e(VALUE, "char: " + (((String) ((Button) v).getText()).charAt(0)));
            Log.e(VALUE, "lastKeyIsNum: " + lastKeyIsNum);
        }
    };
    private View.OnClickListener mButtonsMainActClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMustEdit = false;
            mExpression = mExpression.append(mInputStr.toString() + " ");
            readyToEnterNewNumber();
            if (!((Button) v).getText().equals("=")) {
                if (!lastKeyIsNum) {
                    mExpression.delete(mExpression.length()-3, mExpression.length());
                    mPhraseView.setText(mExpression);                                                   /*добавить проверку на (0 и .) на конце дроби*/
                    Log.e(VALUE, "mPhrase:      " + mExpression);
                }
                mExpression = mExpression.append(((String) ((Button) v).getText()) + " ");
                lastKeyIsNum = false;
                mPhraseView.setText(mExpression);
            } else
            mInputStr.delete(0, mInputStr.length());
            Log.e(VALUE, "mInputStr: " + mInputStr);
            Log.e(VALUE, "mPhrase " + mExpression);
            Log.e(VALUE, "EmptyTextView: " + mEmptyTextView);
            Log.e(VALUE, "lastKey: " + ((String) ((Button) v).getText()));
            Log.e(VALUE, "lastKeyIsNum: " + lastKeyIsNum);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(MY_TAG, "onCreate(): " + (savedInstanceState == null ? "first" : "next"));

        final Button button0 = findViewById(R.id.button0);
        final Button button1 = findViewById(R.id.button1);
        final Button button2 = findViewById(R.id.button2);
        final Button button3 = findViewById(R.id.button3);
        final Button button4 = findViewById(R.id.button4);
        final Button button5 = findViewById(R.id.button5);
        final Button button6 = findViewById(R.id.button6);
        final Button button7 = findViewById(R.id.button7);
        final Button button8 = findViewById(R.id.button8);
        final Button button9 = findViewById(R.id.button9);
        final Button buttonPt = findViewById(R.id.buttonPt);
        final Button buttonPlus = findViewById(R.id.buttonPlus);
        final Button buttonMinus = findViewById(R.id.buttonMinus);
        final Button buttonX = findViewById(R.id.buttonX);
        final Button buttonDiv = findViewById(R.id.buttonDiv);
        final Button buttonEq = findViewById(R.id.buttonEq);
        final Button buttonMR = findViewById(R.id.buttonMR);
        final Button buttonMC = findViewById(R.id.buttonMC);
        final Button buttonMPlus = findViewById(R.id.buttonMPlus);
        final Button buttonMMinus = findViewById(R.id.buttonMMinus);
        final Button buttonC = findViewById(R.id.buttonC);
        final Button buttonBack = findViewById(R.id.buttonBack);
        final Button buttonPosNeg = findViewById(R.id.buttonPosNeg);

        final String mActiveNumber = "0";
        final Button [] buttonsNum = new Button[] {
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
                buttonPt
        };
        final Button [] buttonsMainAct = new Button[] {
                buttonPlus,
                buttonMinus,
                buttonX,
                buttonDiv,
                buttonEq,
        };
        final Button [] buttonsMemoryAct = new Button[] {
                buttonMR,
                buttonMC,
                buttonMPlus,
                buttonMMinus,
        };


        mTextView = findViewById(R.id.inputStr);
        mPhraseView = findViewById(R.id.phrase);

        for (int i = 0; i < buttonsNum.length; i++) {
            buttonsNum[i].setOnClickListener(mButtonsNumClickListener);
        }
        for (int i = 0; i < buttonsMainAct.length; i++) {
            buttonsMainAct[i].setOnClickListener(mButtonsMainActClickListener);
        }

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
    public void onClick(View v) {

    }

    void readyToEnterNewNumber (){
        mInputStr = new StringBuilder();
        mEmptyTextView = true;
        trueCapasity = CAPASITY;
        boolean mIsInteger = true;
        boolean lastKeyIsNum = true;
        boolean mMustEdit = true;
    }

}