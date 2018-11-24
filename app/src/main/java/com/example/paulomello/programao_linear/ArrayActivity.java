package com.example.paulomello.programao_linear;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class ArrayActivity extends AppCompatActivity {


    private List<EditText> rows;
    private List<String> rowsText;
    private float[][] items;
    private int tam_i;
    private int tam_j;

    @BindView(R.id.calcButton)
    Button button;

    @BindView(R.id.table)
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array);
        ButterKnife.bind(this);
        buildArray();
    }

    private void buildArray() {
        try
        {
            Intent intent = getIntent();
            int var = intent.getIntExtra("VAR", 0);
            int rest = intent.getIntExtra("REST", 0);
            tam_i = rest + 1;
            tam_j = var + rest + 2;
            items = new float[tam_i][tam_j];
            rows = new ArrayList<>(tam_i);
            for(int i=0;i<tam_i;i++)
            {
                rows.add(new EditText(this));
                if(i==0)
                {
                    rows.get(i).setHint(getString(R.string.fo_max));
                }
                else
                {
                    rows.get(i).setHint(getString(R.string.rest)+i);
                }
                rows.get(i).setMaxLines(1);
                linearLayout.addView(rows.get(i));
            }
        }
        catch(Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(""+e);
        }
    }

    private boolean parseArray() {
        boolean check;
        try
        {
            String line;
            StringTokenizer parts;
            for(int i=0 ; i<tam_i ; i++)
            {
                line = rows.get(i).getText().toString();
                parts = new StringTokenizer(line," ");
                for(int j=0 ; j<tam_j ; j++)
                {
                    items[i][j] = Float.parseFloat(parts.nextToken());
                }
            }
            check=true;
        }
        catch (Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error_parse_matrix);
            builder.setMessage(""+e);
            check=false;
        }
        return check;
    }

    private boolean verifyArray() {
        boolean check=true;
        try
        {
            rowsText = new ArrayList<>();
            String line;
            StringTokenizer parts;
            for(int i=0;i<tam_i;i++)
            {
                line = String.valueOf(rows.get(i).getText());
                rowsText.add(line);
                parts = new StringTokenizer(line," ");
                if(parts.countTokens()!=tam_j)
                {
                    check = false;
                    rows.get(i).setError(getString(R.string.verify_string));
                }
            }
        }
        catch (Exception e)
        {
            AlertDialog builder = new AlertDialog.Builder(this).setMessage(""+e).show();
            check=false;
        }
        return check;
    }

    public void goToResult(View view) {
        if(verifyArray() && parseArray())
        {
           Intent intent = new Intent(ArrayActivity.this,ResultActivity.class);
           intent.putExtra("MATRIZ",(ArrayList<String>) rowsText);
           intent.putExtra("TAM_I",tam_i);
           intent.putExtra("TAM_J",tam_j);
           startActivity(intent);
        }
    }
}