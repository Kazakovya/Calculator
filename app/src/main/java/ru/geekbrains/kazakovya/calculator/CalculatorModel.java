package ru.geekbrains.kazakovya.calculator;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import static ru.geekbrains.kazakovya.calculator.MainActivity.VALUE;

public class CalculatorModel {


    private StringBuilder mExpression = new StringBuilder();
    private StringBuilder mInputStr = new StringBuilder("0");
    private int trueCapasity = MainActivity.CAPASITY;
    private static double mMemory;
    private double mNum;
    private double mFirstNum = 0;
    private StringBuilder mtvFirstNum;
    private double mSndNum;
    private StringBuilder mtvSndNum;
    private double mResult;
    private int mActionButtonsId;
    private String mLastAction;
    private boolean mLastKeyIsEq = false;
    private boolean mIsInteger = true;
    private boolean mLastKeyIsAction = false;
    private boolean mIsPositive = true;
    private boolean mCanEdit = true;


    View.OnClickListener buttonsNumClickListener = v -> {
        if (mLastKeyIsEq) {
            mFirstNum = 0;
            mSndNum = 0;
        }
        mLastKeyIsEq = false;
        if (mLastKeyIsAction) readyToEnterNewNumber();
        if (mInputStr.length() == 0 || mInputStr.toString().equals("0")) {
            switch (v.getId()) {
                case R.id.button0:
                    mLastKeyIsAction = false;
                    break;
                case R.id.buttonPt:
                    mInputStr = new StringBuilder("0.");
                    trueCapasity++;
                    mIsInteger = false;
                    mLastKeyIsAction = false;
                    MainActivity.mTextView.setText(mInputStr);
                    return;
                case R.id.buttonPosNeg:
                    break;
                case R.id.buttonBack:
                    break;
                default:
                    mInputStr = new StringBuilder(((Button) v).getText());
                    MainActivity.mTextView.setText(mInputStr);
                    mLastKeyIsAction = false;
            }
        } else if (v.getId() == R.id.buttonBack) {
            if (!mIsPositive && mInputStr.length() == 2) {
                mInputStr.deleteCharAt(0);
                trueCapasity--;
                mIsPositive = true;
                MainActivity.mTextView.setText(mInputStr);
                return;
            } else  if (mInputStr.charAt(mInputStr.length()-1) == '.') {
                mIsInteger = true;
                trueCapasity--;
            }
            if (mInputStr.length() == 1) {
                mInputStr = new StringBuilder("0");
                MainActivity.mTextView.setText(mInputStr);
            } else {
                mInputStr.deleteCharAt(mInputStr.length()-1);
                MainActivity.mTextView.setText(mInputStr);
            }
        } else if (v.getId() == R.id.buttonPosNeg) {
            if (sbToNum(mInputStr) > 0) {
                mIsPositive = false;
                trueCapasity ++;
            } else {
                mIsPositive = true;
                trueCapasity --;
            }
            mInputStr = new StringBuilder(delExtraZero(sbToNum(mInputStr) * (-1)));
            MainActivity.mTextView.setText(mInputStr);

        } else if (mInputStr.length() < trueCapasity) {
            switch (v.getId()) {
                case R.id.buttonPt:
                    if (mIsInteger) {
                        mInputStr.append(((Button) v).getText());
                        trueCapasity++;
                        MainActivity.mTextView.setText(mInputStr);
                        mIsInteger = false;
                    }
                    break;
                default:
                    mInputStr.append(((Button) v).getText());
                    MainActivity.mTextView.setText(mInputStr);
                    mLastKeyIsAction = false;
            }
        }
    };

    View.OnClickListener buttonsMainActClickListener = v -> {
        Log.e(VALUE, "mLastKeyIsAction: " + mLastKeyIsAction);
        Log.e(VALUE, "mExpression: " + mExpression);


        if (mFirstNum == 0 || mLastKeyIsEq) {
            mFirstNum = sbToNum(mInputStr);
            mSndNum = 0;
            mtvFirstNum = delExtraZero(mFirstNum);
            MainActivity.mTextView.setText(mtvFirstNum);
            mExpression = new StringBuilder(mtvFirstNum.toString() + " " + ((Button) v).getText() + " ");
            readyToEnterNewNumber();
            mActionButtonsId = v.getId();
            mLastAction = (String) ((Button) v).getText();
            Log.e(VALUE, "buttonPlus: " + (v.getId() == R.id.buttonPlus));
            Log.e(VALUE, "buttonMinus: " + (v.getId() == R.id.buttonMinus));
            Log.e(VALUE, "buttonX: " + (v.getId() == R.id.buttonX));
            Log.e(VALUE, "buttonDiv: " + (v.getId() == R.id.buttonDiv));
            Log.e(VALUE, "mFirstNum: " + mFirstNum);
            Log.e(VALUE, "mtvFirstNum: " + mtvFirstNum);
            Log.e(VALUE, "lastKey: " + ((Button) v).getText());
            Log.e(VALUE, "mExpression: " + mExpression);
        } else if (mLastKeyIsAction) {
            mExpression.delete(mExpression.length()-3, mExpression.length()-1);
            mExpression = new StringBuilder(mtvFirstNum.toString() + " " + ((Button) v).getText() + " ");
            mActionButtonsId = v.getId();
            mLastAction = (String) ((Button) v).getText();
        } else {
            mSndNum = sbToNum(mInputStr);
            mtvSndNum = delExtraZero(mSndNum);
            computation(mFirstNum, mSndNum, mActionButtonsId);
            mActionButtonsId = v.getId();
            mLastAction = (String) ((Button) v).getText();
            mtvFirstNum = delExtraZero(mResult);
            mExpression = new StringBuilder(mtvFirstNum.toString() + " " + ((Button) v).getText() + " ");
            mFirstNum = mResult;

            MainActivity.mTextView.setText(mtvFirstNum);
            mInputStr = new StringBuilder(mtvFirstNum);
        }
        mLastKeyIsAction = true;
        MainActivity.mExpressionView.setText(mExpression);
        Log.e(VALUE, "mExpression: " + mExpression);
    };

    View.OnClickListener buttonCClickListener = v -> {
        reset();
    };

    View.OnClickListener buttonEqClickListener = v -> {
        if (mLastKeyIsAction) {
            mSndNum = mFirstNum;
            mtvSndNum = delExtraZero(mSndNum);
        } else if (mLastAction == null) {
            return;
        } else if (mLastKeyIsEq) {
        } else {
            mSndNum = sbToNum(mInputStr);
            mtvSndNum = delExtraZero(mSndNum);
        }
        computation(mFirstNum, mSndNum, mActionButtonsId);
        mExpression = new StringBuilder(mtvFirstNum.toString() +
                " " + mLastAction + " " + mtvSndNum + " = ");
        mFirstNum = mResult;
        mtvFirstNum = delExtraZero(mResult);
        mInputStr = new StringBuilder(mtvFirstNum);
        MainActivity.mTextView.setText(mtvFirstNum);
        MainActivity.mExpressionView.setText(mExpression);
        readyToEnterNewNumber();
        mLastKeyIsEq = true;
    };

//    View.OnClickListener buttonsMemoryActClickListener = v -> {
//        switch (v.getId()) {
//            case R.id.buttonMC:
//                mMemory = 0;
//                break;
//            case R.id.buttonMR:
//                mInputStr = delExtraZero(mMemory);
//                MainActivity.mTextView.setText(mInputStr);
//                mCanEdit = false;
//                break;
//            case R.id.buttonMPlus:
//                mMemory += sbToNum(mInputStr);
//                break;
//            case R.id.buttonMMinus:
//                mMemory -= sbToNum(mInputStr);
//                break;
//        }
//    };

    public void buttonsMemoryActClicked (View v) {
        switch (v.getId()) {
            case R.id.buttonMC:
                mMemory = 0;
                MainActivity.mMemMark.setText("");
                break;
            case R.id.buttonMR:
                mInputStr = delExtraZero(mMemory);
                MainActivity.mTextView.setText(mInputStr);
                mCanEdit = false;
                break;
            case R.id.buttonMPlus:
                mMemory += sbToNum(mInputStr);
                if (mMemory != 0) MainActivity.mMemMark.setText("M");
                break;
            case R.id.buttonMMinus:
                mMemory -= sbToNum(mInputStr);
                if (mMemory != 0) MainActivity.mMemMark.setText("M");
                break;
        }
    };

    private void readyToEnterNewNumber (){
        mInputStr.setLength(0);
        trueCapasity = MainActivity.CAPASITY;
        mIsInteger = true;
        mIsPositive = true;
        mLastKeyIsEq = false;
    }

    private double sbToNum (StringBuilder sbNum) {
        if (sbNum.length() == 0) {
            mNum = 0;
        }
        return mNum = Double.valueOf(sbNum.toString());
    }

    private void computation (double mFirstArg, double mSndArg, int buttonId) {
        switch (buttonId) {
            case R.id.buttonPlus:
                mResult = mFirstArg + mSndArg;
                break;
            case R.id.buttonMinus:
                mResult = mFirstArg - mSndArg;
                break;
            case R.id.buttonX:
                mResult = mFirstArg * mSndArg;
                break;
            case R.id.buttonDiv:
                mResult = mFirstArg / mSndArg;
                break;
        }
    }

    private  StringBuilder delExtraZero (double d) {
        StringBuilder sb = new StringBuilder(String.valueOf(d));
        for (int i = 0; i < sb.length()-2; i++) {
            if (!(sb.charAt(sb.length()-1-i) == '0') && !(sb.charAt(sb.length()-1-i) == '.')) {
                return sb;
            }
            if (sb.charAt(sb.length()-1-i) == '0') {
                sb.deleteCharAt(sb.length()-1-i);
            }
            if (sb.charAt(sb.length()-1-i) == '.') {
                sb.deleteCharAt(sb.length()-1-i);
                return sb;
            }
        }
        return sb;
    }

    private void reset() {
        mExpression.setLength(0);
        mInputStr.setLength(0);
        trueCapasity = MainActivity.CAPASITY;
        mIsInteger = true;
        mLastKeyIsAction = false;
        mIsPositive = true;
        MainActivity.mExpressionView.setText(mExpression);
        MainActivity.mTextView.setText("0");
        mFirstNum = 0;
        mLastAction = null;
        mLastKeyIsEq = false;
    }
}