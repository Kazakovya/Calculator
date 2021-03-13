package ru.geekbrains.kazakovya.calculator;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static ru.geekbrains.kazakovya.calculator.MainActivity.CAPASITY;
import static ru.geekbrains.kazakovya.calculator.MainActivity.VALUE;

public class CalculatorModel {


    private StringBuilder mExpression = new StringBuilder();
    private StringBuilder mInputStr = new StringBuilder("0");
    private int trueCapasity = MainActivity.CAPASITY;
    private double mMemory;
    private double mNum;
    private double mFirstNum = 0;
    private StringBuilder mtvFirstNum;
    private double mSndNum;
    private StringBuilder mtvSndNum;
    private double mResult;
    private StringBuilder mtvResult;
    private int mActionButtonsId;
    private boolean mIsInteger = true;
    private boolean mLastKeyIsAction = false;
    private boolean mIsPositive = true;
    private boolean mCanEdit = true;


    View.OnClickListener buttonsNumClickListener = v -> {
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
                Log.e(VALUE, "mInputStr: " + mInputStr);
                mInputStr.deleteCharAt(0);
                trueCapasity--;
                Log.e(VALUE, "mInputStr: " + mInputStr);

//                mInputStr.append(" ");
                Log.e(VALUE, "mInputStr: " + mInputStr);
                mIsPositive = true;
                MainActivity.mTextView.setText(mInputStr);
                return;
            } else  if (mInputStr.charAt(mInputStr.length()-1) == '.') {
                mIsInteger = true;
                trueCapasity--;
            }
            Log.e(VALUE, "mInputStr: " + mInputStr);
            if (mInputStr.length() == 1) {
                Log.e(VALUE, "mInputStr: " + mInputStr);
                mInputStr = new StringBuilder("0");
                Log.e(VALUE, "mInputStr: " + mInputStr);
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
            mInputStr = new StringBuilder(delExtraZero((sbToNum(mInputStr) * (-1)), mInputStr));
            MainActivity.mTextView.setText(mInputStr);
//                    if (mIsPositive) {
//                        mInputStr.insert(0, '-');
//                        trueCapasity++;
//                        MainActivity.mTextView.setText(mInputStr);
//                        mIsPositive = false;
//                    } else {
//                        mInputStr.deleteCharAt(0);
//                        trueCapasity--;
//                        MainActivity.mTextView.setText(mInputStr);
//                        mIsPositive = true;
//                    }

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

//    View.OnClickListener buttonsMainActClickListener = v -> {
//        if (mLastKeyIsAction) {
//            mExpression.delete(mExpression.length()-3, mExpression.length()-1);
//        } else {
//            mLastKeyIsAction = true;
//            sbToNum(mInputStr);
//            if (mFirstNum == 0) {
//                mFirstNum = sbToNum(mInputStr);
//                mtvFirstNum = delExtraZero(mFirstNum);
//                mExpression.setLength(0);
//                mExpression.append(mtvFirstNum.toString() + " " + ((String) ((Button) v).getText()) + " ");
//                readyToEnterNewNumber();
//                mActionButtonsId = v.getId();
//            } else if (mSndNum == 0) {
//                mSndNum = mNum;
//                computation(mFirstNum, mSndNum, mActionButtonsId);
//                mActionButtonsId = v.getId();
//                sbToNum(new StringBuilder(String.valueOf(mResult)));
//                mFirstNum = sbToNum(mInputStr);
//                mtvResult = delExtraZero(mFirstNum);
//                mExpression.setLength(0);
//                mExpression.append(mtvResult.toString() + " " + ((String) ((Button) v).getText()) + " ");
//                MainActivity.mTextView.setText(mtvResult);
//            }
//        }
//        MainActivity.mExpressionView.setText(mExpression);
//        Log.e(VALUE, "mInputStr: " + mInputStr);
//        Log.e(VALUE, "mExpression " + mExpression);
//        Log.e(VALUE, "lastKey: " + ((String) ((Button) v).getText()));
//        Log.e(VALUE, "mLastKeyIsAction: " + mLastKeyIsAction);
//        Log.e(VALUE, "mNum: " + mNum);
//        Log.e(VALUE, "mFirstNum: " + mFirstNum);
//        Log.e(VALUE, "mtvFirstNum: " + mtvFirstNum);
//        Log.e(VALUE, "mSndNum: " + mSndNum);
//        Log.e(VALUE, "mResult: " + mResult);
//        Log.e(VALUE, "mtvResult: " + mtvResult);
//    };

    View.OnClickListener buttonsMainActClickListener = v -> {
        if (mLastKeyIsAction) {
            mExpression.delete(mExpression.length()-3, mExpression.length()-1);
        }
        mLastKeyIsAction = true;
        Log.e(VALUE, "mLastKeyIsAction: " + mLastKeyIsAction);

        if (mFirstNum == 0) {
            mFirstNum = sbToNum(mInputStr);
            mtvFirstNum = delExtraZero(mFirstNum, mInputStr);
            MainActivity.mTextView.setText(mInputStr);
            Log.e(VALUE, "mFirstNum: " + mFirstNum);
            Log.e(VALUE, "mtvFirstNum: " + mtvFirstNum);
            Log.e(VALUE, "lastKey: " + ((String) ((Button) v).getText()));

            mExpression = new StringBuilder(mtvFirstNum.toString() + " " + ((String) ((Button) v).getText()) + " ");
            readyToEnterNewNumber();
            mActionButtonsId = v.getId();
        } else {
            mSndNum = sbToNum(mInputStr);
            mtvSndNum = delExtraZero(mSndNum, mInputStr);
            computation(mFirstNum, mSndNum, mActionButtonsId);
            mActionButtonsId = v.getId();
            mFirstNum = sbToNum(new StringBuilder(String.valueOf(mResult)));
            mtvResult = delExtraZero(mFirstNum, mInputStr);
            mExpression = new StringBuilder(mtvResult.toString() + " " + ((String) ((Button) v).getText()) + " ");
            MainActivity.mTextView.setText(mtvResult);
        }
        MainActivity.mExpressionView.setText(mExpression);
    };

    View.OnClickListener buttonCClickListener = v -> {
        reset();
    };

    View.OnClickListener buttonsMemoryActClickListener = v -> {
        switch (v.getId()) {
            case R.id.buttonMC:
                mMemory = 0;
                break;
            case R.id.buttonMR:
                mInputStr = delExtraZero(mMemory, mInputStr);
                MainActivity.mTextView.setText(mInputStr);
                mCanEdit = false;
                break;
            case R.id.buttonMPlus:
                mMemory += sbToNum(mInputStr);
                break;
            case R.id.buttonMMinus:
                mMemory -= sbToNum(mInputStr);
                break;
        }
    };

    private void readyToEnterNewNumber (){
        mInputStr.setLength(0);
        trueCapasity = MainActivity.CAPASITY;
        mIsInteger = true;
        mLastKeyIsAction = false;
        mIsPositive = true;
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
            case R.id.buttonEq:
                if (mSndNum == 0) {
                    mSndNum = mFirstArg;
                }
        }
    }

    private  StringBuilder delExtraZero (double d, StringBuilder inputStr) {
        StringBuilder sb = new StringBuilder(String.valueOf(d));
        if (Math.abs(d) >= (1*10*CAPASITY)) {
            sb = inputStr;
            return sb;
        }
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
    }
}