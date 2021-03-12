package ru.geekbrains.kazakovya.calculator;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static ru.geekbrains.kazakovya.calculator.MainActivity.VALUE;

public class CalculatorModel {


    private StringBuilder mExpression = new StringBuilder();
    private StringBuilder mInputStr = new StringBuilder();
    private int trueCapasity = MainActivity.CAPASITY;
    private double mNum;
    private StringBuilder mtvNum;
    private double mFirstNum;
    private StringBuilder mtvFirstNum;
    private double mSndNum;
    private double mResult;
    private StringBuilder mtvResult;
    private int mActionButtonsId;
    private boolean mIsInteger = true;
    private boolean mLastKeyIsAction = false;
    private boolean mIsPositive = true;


    View.OnClickListener buttonsNumClickListener = v -> {
        if (mLastKeyIsAction) readyToEnterNewNumber();
        if (mInputStr.length() == 0 || mInputStr.toString().equals("0")) {
            switch (v.getId()) {
                case R.id.button0:
                    mInputStr = new StringBuilder("0");
                    MainActivity.mTextView.setText(mInputStr);
                    break;
                case R.id.buttonPt:
                    mInputStr = new StringBuilder("0.");
                    trueCapasity++;
                    mIsInteger = false;
                    MainActivity.mTextView.setText(mInputStr);
                    mLastKeyIsAction = false;
                    break;
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
                mInputStr.append('0');
            } else if (mInputStr.charAt(mInputStr.length()-1) == '.') {
                mIsInteger = true;
                trueCapasity--;
            }
            mInputStr.deleteCharAt(mInputStr.length()-1);
            if (mInputStr.length() == 1) {
                mInputStr = new StringBuilder("0");
                MainActivity.mTextView.setText(mInputStr);
            } else MainActivity.mTextView.setText(mInputStr);
        } else if (mInputStr.length() < trueCapasity) {
            switch (v.getId()) {
                case R.id.buttonPt:
                    if (mIsInteger) {
                        mLastKeyIsAction = false;
                        mInputStr.append(((Button) v).getText());
                        trueCapasity++;
                        MainActivity.mTextView.setText(mInputStr);
                        mIsInteger = false;
                    }
                    break;
                case R.id.buttonPosNeg:
                    if (mIsPositive) {
                        mInputStr.insert(0, '-');
                        trueCapasity++;
                        MainActivity.mTextView.setText(mInputStr);
                        mIsPositive = false;
                    } else {
                        mInputStr.deleteCharAt(0);
                        trueCapasity--;
                        MainActivity.mTextView.setText(mInputStr);
                        mIsPositive = true;
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
        if (mLastKeyIsAction) {
            mExpression.delete(mExpression.length()-3, mExpression.length()-1);
        } else {
            mLastKeyIsAction = true;
            sbToNum(mInputStr);
            if (mFirstNum == 0) {
                mFirstNum = mNum;
                mtvFirstNum = mtvNum;
                mExpression.setLength(0);
                mExpression.append(mtvFirstNum.toString() + " " + ((String) ((Button) v).getText()) + " ");
                readyToEnterNewNumber();
                mActionButtonsId = v.getId();
            } else if (mSndNum == 0) {
                mSndNum = mNum;
                computation(mFirstNum, mSndNum, mActionButtonsId);
                mActionButtonsId = v.getId();
                sbToNum(new StringBuilder(String.valueOf(mResult)));
                mFirstNum = mNum;
                mtvResult = mtvNum;
                mExpression.setLength(0);
                mExpression.append(mtvResult.toString() + " " + ((String) ((Button) v).getText()) + " ");
                MainActivity.mTextView.setText(mtvResult);
            }
        }
        MainActivity.mExpressionView.setText(mExpression);
    };

    View.OnClickListener buttonCClickListener = v -> {
        reset();
    };

    View.OnClickListener buttonsMemoryActClickListener = v -> {
        switch (v.getId()) {
            case R.id.buttonMC:
                break;
            case R.id.buttonMR:
                break;
            case R.id.buttonMPlus:
                break;
            case R.id.buttonMMinus:
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

    private void sbToNum (StringBuilder sbNum) {
        if (sbNum.length() == 0) {
            mNum = 0;
            mtvNum = sbNum.append("0");
            return;
        }
        mNum = Double.valueOf(sbNum.toString());
        mtvNum = delExtraZero(new StringBuilder(String.valueOf(mNum)));
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

    private  StringBuilder delExtraZero (StringBuilder sb) {
        for (int i = 0; i < sb.length()-2; i++) {
            if ((sb.charAt(sb.length()-1-i) == '0')) {
                sb.deleteCharAt(sb.length()-1-i);
            } else if (sb.charAt(sb.length()-1-i) == '.') {
                sb.deleteCharAt(sb.length()-1-i);
                break;
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
