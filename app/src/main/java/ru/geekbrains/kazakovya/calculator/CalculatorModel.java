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
    private int trueCapasity = MainActivity.CAPASITY;
    private double mFirstNum = 0;
    private double mSndNum = 0;
    private boolean mIsInteger = true;
    private boolean mIsPositive = true;
    private String mLastAction = null;
    private boolean mLastKeyIsAction = true;
    private double mMemory = 0;

    View.OnClickListener buttonsNumClickListener = v -> {
        Log.e(VALUE, "001. press num");
        if (lastKeyIsEq(mExpression)) {
            Log.e(VALUE, "002. last key is = : " + mExpression);
            reset();
        }
        if (mLastKeyIsAction) {
            readyToEnterNewNumber();
            mSndNum = 0;
            Log.e(VALUE, "003. del sndnum: " + mSndNum);
        }
        mLastKeyIsAction = false;
        if (mInputStr.length() == 0 || mInputStr.toString().equals("0")) {
            Log.e(VALUE, "004. empty str");
            switch (v.getId()) {
                case R.id.buttonPt:
                    mInputStr = new StringBuilder("0.");
                    trueCapasity++;
                    mIsInteger = false;
                    mainScreen.setText(mInputStr);
                    Log.e(VALUE, "005. str = 0.");
                    return;
                case R.id.buttonPosNeg:
                    break;
                case R.id.buttonBack:
                    break;
                default:
                    mInputStr = new StringBuilder(((Button) v).getText());
                    mainScreen.setText(mInputStr);
                    Log.e(VALUE, "006. setText: " + mInputStr);
            }
        } else if (v.getId() == R.id.buttonBack) {
            if (!mIsPositive && mInputStr.length() == 2) {
                mInputStr.deleteCharAt(0);
                trueCapasity--;
                mIsPositive = true;
                mainScreen.setText(mInputStr);
                Log.e(VALUE, "007. set positive num " + mInputStr);
                return;
            } else  if (mInputStr.charAt(mInputStr.length()-1) == '.') {
                mIsInteger = true;
                trueCapasity--;
                Log.e(VALUE, "008. set int num");
            }
            if (mInputStr.length() == 1) {
                mInputStr = new StringBuilder("0");
                mainScreen.setText(mInputStr);
                Log.e(VALUE, "009. str = 0");
            } else {
                mInputStr.deleteCharAt(mInputStr.length()-1);
                mainScreen.setText(mInputStr);
                Log.e(VALUE, "010. del last char: " + mInputStr);
            }
        } else if (v.getId() == R.id.buttonPosNeg) {
            if (sbToNum(mInputStr) > 0) {
                mIsPositive = false;
                trueCapasity ++;
                Log.e(VALUE, "011. num < 0: " + mInputStr);
            } else {
                mIsPositive = true;
                trueCapasity --;
                Log.e(VALUE, "012. num > 0: " + mInputStr);
            }
            mInputStr = new StringBuilder(delExtraZero(sbToNum(mInputStr) * (-1)));
            mainScreen.setText(mInputStr);
            Log.e(VALUE, "013. set str: " + mInputStr);
        } else if (mInputStr.length() < trueCapasity) {
            Log.e(VALUE, "014. str is not full: " + mInputStr);

            if (v.getId() == R.id.buttonPt) {
                Log.e(VALUE, "015. press Pt: " + mInputStr);
                if (mIsInteger) {
                    trueCapasity++;
                    mIsInteger = false;
                    Log.e(VALUE, "016. add Pt: " + mInputStr);
                }
            }
            mInputStr.append(((Button) v).getText());
            mainScreen.setText(mInputStr);
            Log.e(VALUE, "017. add num: " + mInputStr);
        }
    };

    View.OnClickListener buttonsMainActClickListener = v -> {
        String buttonTxt = (String) ((Button) v).getText();
        boolean buttonIsAct = buttonTxt.equals("+") || buttonTxt.equals("-") ||
                buttonTxt.equals("x") || buttonTxt.equals("÷");
        if (buttonIsAct){
            Log.e(VALUE, "mFirstNum: " + mFirstNum);
            Log.e(VALUE, "mInputStr: " + mInputStr);
            Log.e(VALUE, "lastKeyIsEq: " + lastKeyIsEq(mExpression));
            Log.e(VALUE, "018. set action: " + ((Button) v).getText()
                    + "\n" + "mFirstNum == 0: " + (mFirstNum == 0)
                    + "\n lastKeyIsAction: " + mLastKeyIsAction
                    + "\n" + "lastKeyIsEq: " + lastKeyIsEq(mExpression));
            if (mFirstNum == 0 || mLastKeyIsAction || lastKeyIsEq(mExpression)) {
                Log.e(VALUE, "019 set action: " + (String) ((Button) v).getText());
                mFirstNum = sbToNum(new StringBuilder(mainScreen.getText()));
                mainScreen.setText(delExtraZero(mFirstNum));
                Log.e(VALUE, "020. set firstNum: " + mFirstNum);
                mSndNum = 0;
                Log.e(VALUE, "021 set sndNum = 0: " + mSndNum);
                mExpression = new StringBuilder(delExtraZero(mFirstNum).toString() + " " + ((Button) v).getText() + " ");
                mLastAction = lastAction(mExpression);
                Log.e(VALUE, "022. set expr str: " + mExpression);

                Log.e(VALUE, "023. save action: " + mLastAction);
                readyToEnterNewNumber();
                Log.e(VALUE, "024. del str: " + mInputStr);
            } else {
                if (mInputStr.length() > 0) mSndNum = sbToNum(mInputStr);
                Log.e(VALUE, "025. set sndNum: " + mSndNum);
                try {
                    mFirstNum = computation(mFirstNum, mSndNum, mExpression);
                    Log.e(VALUE, "026. computation: " + mFirstNum);
                } catch (ArithmeticException e) {
                    mainScreen.setText("Деление на 0!");
                    Log.e(VALUE, "027. exception");
                    return;
                }
                mExpression = new StringBuilder(delExtraZero(mFirstNum).toString() + " " + (String) ((Button) v).getText() + " ");
                mLastAction = lastAction(mExpression);
                Log.e(VALUE, "028. set expr str: " + mExpression);
                mainScreen.setText(delExtraZero(mFirstNum));
                Log.e(VALUE, "029.1 set str: " + mFirstNum);
                readyToEnterNewNumber();
                Log.e(VALUE, "030. del str: " + mInputStr);
            }
            expressionScreen.setText(mExpression);
            mLastKeyIsAction = true;
            Log.e(VALUE, "031. set expr str: " + mExpression);
        } else {
            if (buttonTxt.equals("MC")) {
                memoryScreen.setText("");
                mMemory = 0;
                Log.e(VALUE, "031.1 mMemory: " + mMemory);
            } else if (buttonTxt.equals("MR")) {
                mInputStr = delExtraZero(mMemory);
                mainScreen.setText(mInputStr);
                mLastKeyIsAction = false;
                Log.e(VALUE, "031.2 mMemory: " + mMemory);
                Log.e(VALUE, "031.3 mInputStr: " + mInputStr);
                if (lastKeyIsEq(mExpression)) {
                    mExpression.setLength(0);
                    expressionScreen.setText("");
                } else if (mLastKeyIsAction) {

                }
            } else if (buttonTxt.equals("M+")) {
                mMemory += sbToNum(new StringBuilder(mainScreen.getText()));
                if (mMemory != 0) memoryScreen.setText("M");
                Log.e(VALUE, "031.4 mMemory: " + mMemory);
                readyToEnterNewNumber();
            } else if (buttonTxt.equals("M-")) {
                mMemory -= sbToNum(new StringBuilder(mainScreen.getText()));
                if (mMemory != 0) memoryScreen.setText("M");
                readyToEnterNewNumber();
                Log.e(VALUE, "031.4 mMemory: " + mMemory);
            }
        }
    };


    View.OnClickListener buttonCClickListener = v -> {
        Log.e(VALUE, "032. reset");
        reset();
    };

    View.OnClickListener buttonEqClickListener = v -> {
        Log.e(VALUE, "033. press ="
                    + "\n mLastKeyIsAction: " + mLastKeyIsAction);

        if (mLastKeyIsAction) {
            mSndNum = mFirstNum;
            Log.e(VALUE, "034. sndNum = frstNum: " + mSndNum);
        } else if (mExpression.length() == 0) {
            Log.e(VALUE, "035. num without any actions: " + mExpression);
            return;
        } else {
            if (mInputStr.length() > 0) mSndNum = sbToNum(mInputStr);
            Log.e(VALUE, "036. set sndnum: " + mSndNum);
        }
        mExpression = new StringBuilder(delExtraZero(mFirstNum) + " " + mLastAction + " " + delExtraZero(mSndNum) + " =");
        expressionScreen.setText(mExpression);
        try {
            mFirstNum = computation(mFirstNum, mSndNum, mExpression);
            Log.e(VALUE, "037. computation: " + mFirstNum);
        } catch (ArithmeticException e) {
            mainScreen.setText("Деление на 0!");
            mInputStr.setLength(0);
            Log.e(VALUE, "038. exception: ");
            return;
        }
        Log.e(VALUE, "039. set expr str: " + mExpression);
        mInputStr = new StringBuilder(delExtraZero(mFirstNum));
        mainScreen.setText(mInputStr);
        Log.e(VALUE, "040. set str: " + mInputStr);
        readyToEnterNewNumber();
        mLastKeyIsAction = false;
        Log.e(VALUE, "041. del str: " + mInputStr);
    };

    private boolean lastKeyIsEq(StringBuilder mExpression) {
        boolean lastKeyIsEq = false;
        if (mExpression.length() != 0 && mExpression.charAt(mExpression.length()-1) == '=') {
            Log.e(VALUE, "042. lastKeyIsEq: " + lastKeyIsEq);
            lastKeyIsEq = true;
        }
        Log.e(VALUE, "043. lastKeyIsEq: " + lastKeyIsEq);
        return lastKeyIsEq;
    }

    private String lastAction (StringBuilder expression) {
        if (!expression.toString().contains(" ")) {
            Log.e(VALUE, " " + expression.toString().indexOf(" "));
            Log.e(VALUE, "044. lastAction: null");
            return null;
        } else {
            int firstSpace = expression.toString().indexOf(" ");
            String actionChar = expression.toString().substring(firstSpace + 1, firstSpace + 2);
            Log.e(VALUE, "045. lastAction: " + actionChar);
            return actionChar;
        }
    }

    private void readyToEnterNewNumber (){
        mInputStr.setLength(0);
        Log.e(VALUE, "046. readyToEnterNewNumber: " + mInputStr.length());
        trueCapasity = MainActivity.CAPASITY;
        mIsInteger = true;
        mIsPositive = true;
    }

    private double sbToNum (StringBuilder sbNum) {
        if (sbNum.length() == 0) {
            Log.e(VALUE, "047. sbToNum = 0: " + sbNum);
            return  0;
        }
        Log.e(VALUE, "048. sbToNum: " + Double.valueOf(sbNum.toString())
                + "\n sbNum: " + sbNum);
        return Double.valueOf(sbNum.toString());
    }

    private double computation (double mFirstArg, double mSndArg, StringBuilder expression) throws ArithmeticException {
        mLastAction = lastAction(expression);
        Log.e(VALUE, "049. mLastAction: " + mLastAction);
        if (mLastAction.equals("+")) {
            Log.e(VALUE, "050. mLastAction: +");
            return mFirstArg + mSndArg;
        } else if (mLastAction.equals("-")) {
            Log.e(VALUE, "051. mLastAction: -");
            return mFirstArg - mSndArg;
        } else if (mLastAction.equals("×")) {
            Log.e(VALUE, "052. mLastAction: x");
            return mFirstArg * mSndArg;
        } else if (mLastAction.equals("÷")) {
            Log.e(VALUE, "053. mLastAction: /");
            return mFirstArg / mSndArg;
        }
        Log.e(VALUE, "054. result: " + 0);
        return 0;
    }

    private  StringBuilder delExtraZero (double d) {
        StringBuilder sb = new StringBuilder(String.valueOf(d));
        Log.e(VALUE, "055. sb: " + sb);
        for (int i = 0; i < sb.length()-2; i++) {
            Log.e(VALUE, "056. sb.length(): " + (sb.length()-1-i)
                    + "\n i: " + i
                    + "\n sb.length()-2-i: " + (sb.length()-2-i));
            if (!(sb.charAt(sb.length()-1-i) == '0') && !(sb.charAt(sb.length()-1-i) == '.')) {
                Log.e(VALUE, "057. sb: " + sb);
                return sb;
            }
            if (sb.charAt(sb.length()-1-i) == '0') {
                Log.e(VALUE, "058. sb: " + sb);
                sb.deleteCharAt(sb.length()-1-i);
            }
            if (sb.charAt(sb.length()-1-i) == '.') {
                sb.deleteCharAt(sb.length()-1-i);
                Log.e(VALUE, "059. sb: " + sb);
                return sb;
            }
        }
        Log.e(VALUE, "060. sb: " + sb);
        return sb;
    }

    private void reset() {
        Log.e(VALUE, "061. reset");
        mExpression.setLength(0);
        mInputStr.setLength(0);
        trueCapasity = MainActivity.CAPASITY;
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
        Log.e(VALUE, "62. expression: " + mExpression);
        if (inputStr.toString().contains(".")) {
            trueCapasity++;
            mIsInteger = false;
        }
        if (mInputStr.charAt(0) == '-') {
            trueCapasity ++;
            mIsPositive = true;
        }
        if (mExpression.length() == 0) {
            Log.e(VALUE, "63. expression: null" + mExpression);
            mFirstNum = 0;
            mLastAction = null;
            mSndNum = 0;
        } else {
        Log.e(VALUE, "064. (expression.toString().split(\" \") [0]): " + (mExpression.toString().split(" ") [0])
                + "\n mFirstNum: " + Integer.parseInt(mExpression.toString().split(" ") [0]));
            mFirstNum = Integer.parseInt(mExpression.toString().split(" ") [0]);
            if (mExpression.toString().indexOf(" ") < 0) {
                Log.e(VALUE, "65. mLastAction: null" + mLastAction);
                mLastAction = null;
                mLastKeyIsAction = false;
                mSndNum = 0;
            } else {
                mLastAction = lastAction(mExpression);
                Log.e(VALUE, "66. mLastAction: " + mLastAction);
                mLastKeyIsAction = true;
                int firstSpace = mExpression.toString().indexOf(" ");
                Log.e(VALUE, "67. firstSpace: " + firstSpace);
                int n = 1;
                if (expressionStr.contains("=")) n = 2;
                Log.e(VALUE, "68. n: " + n);

                String substr = mExpression.toString().substring(firstSpace + 2, (mExpression.toString().length() - n));
                Log.e(VALUE, "69. substr: " + substr
                                + "\n substr.length():" + substr.length() + "!");
                if ((substr.length()) == 0) {
                    if (Math.abs(mFirstNum - sbToNum(mInputStr)) < 0.0000001) {
                        readyToEnterNewNumber();
                        mSndNum = 0;
                    } else {
                        mLastKeyIsAction = false;
                        if (mInputStr.charAt(0) == '-') {
                            trueCapasity ++;
                            mIsPositive = true;
                        }
                        if (mInputStr.toString().contains(".")) {
                            trueCapasity++;
                            mIsInteger = false;
                        }
                    }
                } else {
                    mSndNum = Double.parseDouble(substr);
                    mFirstNum = sbToNum(mInputStr);
                    Log.e(VALUE, "70. mSndNum: " + mSndNum);
                    mLastKeyIsAction = false;
                    readyToEnterNewNumber();
                    }
            }
        }

    }

    public String getMemory () {
        return String.valueOf(mMemory);
    }

    public void setMemory(String memoryStr) {
        mMemory = Double.parseDouble(memoryStr);
        if (mMemory != 0) memoryScreen.setText("M");
    }
}

