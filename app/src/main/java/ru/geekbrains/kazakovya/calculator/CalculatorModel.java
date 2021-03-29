package ru.geekbrains.kazakovya.calculator;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static ru.geekbrains.kazakovya.calculator.MainActivity.VALUE;

public class CalculatorModel {

    private TextView mainScreen;
    private TextView expressionScreen;
    private TextView memoryScreen;


    public CalculatorModel (TextView mainScreen, TextView expressionScreen, TextView memoryScreen) {
        this.mainScreen = mainScreen;
        this.expressionScreen = expressionScreen;
        this.memoryScreen = memoryScreen;
    }

    private StringBuilder mExpression = new StringBuilder();
    private StringBuilder mInputStr = new StringBuilder("0");
    private int trueCapacity = MainActivity.CAPACITY;
    private float mFirstNum = 0;
    private float mSndNum = 0;
    private boolean mIsInteger = true;
    private boolean mIsPositive = true;
    private String mLastAction = null;
    private boolean mLastKeyIsAction = true;
    private float mMemory = 0;

    View.OnClickListener buttonsNumClickListener = v -> {
        if (lastKeyIsEq(mExpression)) {
            reset();
        }
        if (mLastKeyIsAction) {
            readyToEnterNewNumber();
            mSndNum = 0;
        }
        mLastKeyIsAction = false;
        if (mInputStr.length() == 0 || mInputStr.toString().equals("0")) {
            switch (v.getId()) {
                case R.id.buttonPt:
                    mInputStr = new StringBuilder("0.");
                    trueCapacity++;
                    mIsInteger = false;
                    mainScreen.setText(mInputStr);
                    return;
                case R.id.buttonPosNeg:
                    break;
                case R.id.buttonBack:
                    break;
                default:
                    mInputStr = new StringBuilder(((Button) v).getText());
                    mainScreen.setText(mInputStr);
            }
        } else if (v.getId() == R.id.buttonBack) {
            if (!mIsPositive && mInputStr.length() == 2) {
                mInputStr.deleteCharAt(0);
                trueCapacity--;
                mIsPositive = true;
                mainScreen.setText(mInputStr);
                return;
            } else  if (mInputStr.charAt(mInputStr.length()-1) == '.') {
                mIsInteger = true;
                trueCapacity--;
            }
            if (mInputStr.length() == 1) {
                mInputStr = new StringBuilder("0");
                mainScreen.setText(mInputStr);
            } else {
                mInputStr.deleteCharAt(mInputStr.length()-1);
                mainScreen.setText(mInputStr);
            }
        } else if (v.getId() == R.id.buttonPosNeg) {
            if (sbToNum(mInputStr) > 0) {
                mIsPositive = false;
                trueCapacity++;
            } else {
                mIsPositive = true;
                trueCapacity--;
            }
            mInputStr = new StringBuilder(delExtraZero(sbToNum(mInputStr) * (-1)));
            mainScreen.setText(mInputStr);
        } else if (mInputStr.length() < trueCapacity) {
            if (v.getId() == R.id.buttonPt) {
                if (mIsInteger) {
                    trueCapacity++;
                    mIsInteger = false;
                }
            }
            mInputStr.append(((Button) v).getText());
            mainScreen.setText(mInputStr);
        }
    };

    View.OnClickListener buttonsMainActClickListener = v -> {
        String buttonTxt = (String) ((Button) v).getText();
        boolean buttonIsAct = buttonTxt.equals("+") || buttonTxt.equals("-") ||
                buttonTxt.equals("×") || buttonTxt.equals("÷");
        if (buttonIsAct){
            if (mFirstNum == 0 || mLastKeyIsAction || lastKeyIsEq(mExpression)) {
                mFirstNum = sbToNum(new StringBuilder(mainScreen.getText()));
                mainScreen.setText(delExtraZero(mFirstNum));
                mSndNum = 0;
                mExpression = new StringBuilder(delExtraZero(mFirstNum).toString() + " " + ((Button) v).getText() + " ");
                mLastAction = lastAction(mExpression);
                readyToEnterNewNumber();
            } else {
                if (mInputStr.length() > 0) mSndNum = sbToNum(mInputStr);
                if (lastAction(mExpression).equals("÷") && mSndNum == 0) {
                    mInputStr.setLength(0);
                    mainScreen.setText("Деление на 0!");
                    return;
                } else mFirstNum = computation(mFirstNum, mSndNum, mExpression);
                if (Float.isInfinite(mFirstNum)) {
                    mainScreen.setText("Переполнение!");
                    return;
                }
                mExpression = new StringBuilder(delExtraZero(mFirstNum).toString() + " " + (String) ((Button) v).getText() + " ");
                mLastAction = lastAction(mExpression);
                mainScreen.setText(delExtraZero(mFirstNum));
                readyToEnterNewNumber();
            }
            expressionScreen.setText(mExpression);
            mLastKeyIsAction = true;
        } else {
            switch (buttonTxt) {
                case "MC":
                    memoryScreen.setText("");
                    mMemory = 0;
                    break;
                case "MR":
                    mInputStr = delExtraZero(mMemory);
                    mainScreen.setText(mInputStr);
                    mLastKeyIsAction = false;
                    if (lastKeyIsEq(mExpression)) {
                        mExpression.setLength(0);
                        expressionScreen.setText("");
                    }
                    break;
                case "M+":
                    mMemory += sbToNum(new StringBuilder(mainScreen.getText()));
                    if (mMemory != 0) memoryScreen.setText("M");
                    readyToEnterNewNumber();
                    break;
                case "M-":
                    mMemory -= sbToNum(new StringBuilder(mainScreen.getText()));
                    if (mMemory != 0) memoryScreen.setText("M");
                    readyToEnterNewNumber();
                    break;
            }
        }
    };

    View.OnClickListener buttonCClickListener = v -> {
        reset();
    };

    View.OnClickListener buttonEqClickListener = v -> {
        if (mLastKeyIsAction) {
            mSndNum = mFirstNum;
        } else if (mExpression.length() == 0) {
            return;
        } else {
            if (mInputStr.length() > 0) mSndNum = sbToNum(mInputStr);
        }
        if (mSndNum < 0) {
            mExpression = new StringBuilder(delExtraZero(mFirstNum) + " " + mLastAction + " (" + delExtraZero(mSndNum) + ") =");
        } else mExpression = new StringBuilder(delExtraZero(mFirstNum) + " " + mLastAction + " " + delExtraZero(mSndNum) + " =");
        expressionScreen.setText(mExpression);
        if (lastAction(mExpression).equals("÷") && mSndNum == 0) {
            mInputStr.setLength(0);
            mainScreen.setText("Деление на 0!");
            mExpression = new StringBuilder(delExtraZero(mFirstNum) + " " + mLastAction + " ");
            expressionScreen.setText(mExpression);
            return;
        } else mFirstNum = computation(mFirstNum, mSndNum, mExpression);
        if (Float.isInfinite(mFirstNum)) {
            mainScreen.setText("Переполнение!");
            return;
        }
        mInputStr = new StringBuilder(delExtraZero(mFirstNum));
        mainScreen.setText(mInputStr);
        readyToEnterNewNumber();
        mLastKeyIsAction = false;
    };

    private boolean lastKeyIsEq(StringBuilder mExpression) {
        boolean lastKeyIsEq = false;
        if (mExpression.length() != 0 && mExpression.charAt(mExpression.length()-1) == '=') {
            lastKeyIsEq = true;
        }
        return lastKeyIsEq;
    }

    private String lastAction (StringBuilder expression) {
        if (!expression.toString().contains(" ")) {
            return null;
        } else {
            int firstSpace = expression.toString().indexOf(" ");
            String actionChar = expression.toString().substring(firstSpace + 1, firstSpace + 2);
            return actionChar;
        }
    }

    private void readyToEnterNewNumber (){
        mInputStr.setLength(0);
        trueCapacity = MainActivity.CAPACITY;
        mIsInteger = true;
        mIsPositive = true;
    }

    private float sbToNum (StringBuilder sbNum) {
        if (sbNum.length() == 0) {
            return  0;
        }
        return Float.parseFloat(sbNum.toString());
    }

    private float computation (float mFirstArg, float mSndArg, StringBuilder expression) {
        mLastAction = lastAction(expression);
        switch (mLastAction) {
            case "+":
                return mFirstArg + mSndArg;
            case "-":
                return mFirstArg - mSndArg;
            case "×":
                return mFirstArg * mSndArg;
            case "÷":
                return mFirstArg / mSndArg;
        }
        return 0;
    }

    private  StringBuilder delExtraZero (float f) {
        StringBuilder sb = new StringBuilder(String.valueOf(f));
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
        trueCapacity = MainActivity.CAPACITY;
        mIsInteger = true;
        mIsPositive = true;
        expressionScreen.setText("");
        mainScreen.setText("0");
        mFirstNum = 0;
    }

    public void setState (String expressionStr, String inputStr) {
        reset();
        mExpression = new StringBuilder(expressionStr);
        expressionScreen.setText(mExpression);
        mInputStr = new StringBuilder(inputStr);
        mainScreen.setText(mInputStr);
        trueCapacity = MainActivity.CAPACITY;
        if (inputStr.toString().contains(".")) {
            trueCapacity++;
            mIsInteger = false;
        }
        if (mInputStr.charAt(0) == '-') {
            trueCapacity++;
            mIsPositive = false;
        }
        if (mExpression.length() == 0) {
            mFirstNum = 0;
            mLastAction = null;
            mSndNum = 0;
        } else {
            mFirstNum = Integer.parseInt(mExpression.toString().split(" ") [0]);
            mLastAction = lastAction(mExpression);
            mLastKeyIsAction = true;
            int firstSpace = mExpression.toString().indexOf(" ");
            int n = 1;
            if (expressionStr.contains("=")) n = 2;

            String substr = mExpression.toString().substring(firstSpace + 2, (mExpression.toString().length() - n));
            if ((substr.length()) == 0) {
                if (Math.abs(mFirstNum - sbToNum(mInputStr)) < 0.0000001) {
                    readyToEnterNewNumber();
                    mSndNum = 0;
                } else {
                    mLastKeyIsAction = false;
                    if (mInputStr.charAt(0) == '-') {
                        trueCapacity++;
                        mIsPositive = true;
                    }
                    if (mInputStr.toString().contains(".")) {
                        trueCapacity++;
                        mIsInteger = false;
                    }
                }
            } else {
                mSndNum = Float.parseFloat(substr);
                mFirstNum = sbToNum(mInputStr);
                mLastKeyIsAction = false;
                readyToEnterNewNumber();
            }
        }

    }

    public float getMemory () {
        return mMemory;
    }

    public void setMemory(float memory) {
        mMemory = memory;
        if (mMemory != 0) memoryScreen.setText("M");
    }
}

