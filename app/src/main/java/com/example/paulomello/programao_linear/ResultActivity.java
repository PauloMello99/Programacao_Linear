package com.example.paulomello.programao_linear;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity {


    @BindView(R.id.text)
    LinearLayout linearLayout;

    private List<String> rowsText;
    private double[][] items;
    private int tam_i;
    private int tam_j;
    private int num_algorithm=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        startSimplex();
    }

    private void startSimplex(){
        getArray();
        parseArray();
        printFirstAlgorithm();
        calcSimplex();
    }

    private void getArray(){
        try
        {
            Intent intent = getIntent();
            rowsText = new ArrayList<>(tam_i);
            rowsText = intent.getStringArrayListExtra("MATRIZ");
            tam_i = intent.getIntExtra("TAM_I",0);
            tam_j = intent.getIntExtra("TAM_J",0);
        }
        catch (Exception e)
        {
            AlertDialog builder = new AlertDialog.Builder(this).setMessage(""+e).show();
        }
    }

    private void parseArray() {
        try
        {
            items = new double[tam_i][tam_j];
            String line;
            StringTokenizer parts;
            for(int i=0 ; i<tam_i ; i++)
            {
                line = rowsText.get(i);
                parts = new StringTokenizer(line," ");
                for(int j=0 ; j<tam_j ; j++)
                {
                    items[i][j] = Float.parseFloat(parts.nextToken());
                }
            }
        }
        catch (Exception e)
        {
            AlertDialog builder = new AlertDialog.Builder(this).setMessage(""+e).show();
        }
    }

    private void printFirstAlgorithm(){
        String alg = getString(R.string.alg_num)+String.valueOf(num_algorithm);
        TextView algorithm = new TextView(this);
        algorithm.setGravity(Gravity.CENTER);
        algorithm.setWidth(linearLayout.getWidth());
        algorithm.setText(alg);
        algorithm.setTextSize(28);
        algorithm.setTypeface(null,Typeface.BOLD);
        linearLayout.addView(algorithm);
        List<TextView> texts = new ArrayList<>(tam_i);
        for(int i=0 ; i < tam_i ; i++ )
        {
            texts.add(new TextView(this));
            texts.get(i).setText(rowsText.get(i));
            texts.get(i).setTextSize(18);
            texts.get(i).setGravity(Gravity.CENTER);
            linearLayout.addView(texts.get(i));
        }
    }

    private boolean checkArray(){
        boolean check = true;
        for(int j=0;j<tam_j;j++)
        {
            if(items[0][j] < 0.00)
            {
                check = false;
                num_algorithm++;
                TextView not_best_solution = new TextView(this);
                not_best_solution.setGravity(Gravity.CENTER);
                not_best_solution.setWidth(linearLayout.getWidth());
                not_best_solution.setText(getString(R.string.not_best_solution));
                not_best_solution.setPadding(0,15,0,10);
                not_best_solution.setTextSize(26);
                not_best_solution.setTypeface(null, Typeface.BOLD);
                linearLayout.addView(not_best_solution);
                break;
            }
        }
        return check;
    }

    private void calcSimplex(){
        while(!checkArray())
        {
            double menor = 99999.99;
            double[] pivotLine = new double[tam_j];
            double b=0,div=0,temp=0,pivotElement=0,a=0;
            int i=0,j=0,in=0,out=0;
            for(j=0;j<tam_j;j++)
            {
                if(items[0][j] < menor)
                {
                    menor = items[0][j];
                    in = j;
                }
            }
            menor = 99999.99;
            for(i=1;i<tam_i;i++)
            {
                b = items[i][tam_j-1];
                div = items[i][in];
                temp = b / div;
                if(temp < menor && temp > 0.00)
                {
                    menor = temp;
                    out = i;
                }
            }
            pivotElement = items[out][in];
            for(j=0;j<tam_j;j++)
            {
                items[out][j] /= pivotElement;
                pivotLine[j] = items[out][j];
            }
            for(i=0;i<tam_i;i++)
            {
                if(i!=out)
                {
                    a = items[i][in] * (-1);
                    for(j=0;j<tam_j;j++)
                    {
                        items[i][j] = (items[i][j] + (pivotLine[j]*a));
                    }
                }
            }
            printAlgorithm();
        }
        ImageView divider = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,20,5,20);
        divider.setLayoutParams(lp);
        divider.setBackgroundColor(Color.BLACK);
        linearLayout.addView(divider);
        TextView fo = new TextView(this);
        TextView rlt = new TextView(this);
        String result = "FO MAX = " + String.valueOf(items[0][tam_j-1]);
        rlt.setText(result);
        rlt.setTextSize(25);
        String txt = getString(R.string.calculated_algs)+String.valueOf(num_algorithm);
        fo.setText(txt);
        fo.setTextSize(25);
        fo.setPadding(0,30,0,30);
        linearLayout.addView(fo);
        linearLayout.addView(rlt);
    }

    private void printAlgorithm() {
        StringBuilder linha = new StringBuilder();
        String temp;
        List<TextView> texts = new ArrayList<>(tam_i);
        DecimalFormat df = new DecimalFormat("0.00");
        String alg = getString(R.string.alg_num)+String.valueOf(num_algorithm);
        TextView algorithm = new TextView(this);
        algorithm.setGravity(Gravity.CENTER);
        algorithm.setText(alg);
        algorithm.setPadding(0,30,0,30);
        algorithm.setTextSize(28);
        algorithm.setTypeface(null, Typeface.BOLD);
        linearLayout.addView(algorithm);
        for(int i=0 ; i<tam_i ; i++)
        {
            linha.setLength(0);
            texts.add(new TextView(this));
            for (int j=0 ; j<tam_j; j++)
            {
                temp = String.valueOf(df.format(items[i][j])) + " ";
                linha.append(temp);
            }
            texts.get(i).setText(linha);
            texts.get(i).setTextSize(18);
            texts.get(i).setGravity(Gravity.CENTER);
            linearLayout.addView(texts.get(i));
        }
    }

}
