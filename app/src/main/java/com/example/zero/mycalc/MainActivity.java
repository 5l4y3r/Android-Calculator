package com.example.zero.mycalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.w3c.dom.Text;

import static com.example.zero.mycalc.R.id.clear;

public class MainActivity extends AppCompatActivity {

    //variables

    static double xp = 0;
    static String buttonText="";
    static TextView t;
    char[] dataSet= new char[1000];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClick(View v){
        View Vclear = findViewById(R.id.clear);
        View Vsqr = findViewById(R.id.square);
        View Vroot = findViewById(R.id.root);
        View Vdelete = findViewById(R.id.delete);
        View Vplus= findViewById(R.id.plus);
        View Vminus = findViewById(R.id.minus);
        View Vmultiply = findViewById(R.id.multiply);
        View Vdiv = findViewById(R.id.divide);
        View Vequal = findViewById(R.id.buttonEqual);
        try {


            if (Vclear.isPressed()) {

                // String x="0";
                buttonText = "";
                t.setText("0");

            } else if (Vequal.isPressed()) {
                double x=0;
                x=eval(buttonText);
                //String temp = new String();
                //temp = String.valueOf(x);
                if(x-(int)x == 0){

                    t.setText(String.valueOf((int)x));

                }else{

                t.setText(String.valueOf(x));

                }
            } else if(Vdelete.isPressed()){

                buttonText = buttonText.substring(0,buttonText.length()-1);
                t.setText(buttonText);

            }
        else {

                buttonText += ((Button) v).getText().toString();


                t = (TextView) findViewById(R.id.textDisplay);
                t.setText(buttonText);
            }
       }catch (Exception e){

        }
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

}
