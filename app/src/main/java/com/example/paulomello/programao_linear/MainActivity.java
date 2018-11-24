package com.example.paulomello.programao_linear;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.qnt_var)
    EditText variaveis;

    @BindView(R.id.qnt_rest)
    EditText restricoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    public void goToTable(View view) {
        try
        {
            if(validadeFields())
            {
                changeActivity();
            }
        }
        catch(Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(""+e);
        }
    }

    private void changeActivity(){
        Intent intent = new Intent(MainActivity.this,ArrayActivity.class);
        String qnt_varStr = variaveis.getText().toString();
        String qnt_restStr = restricoes.getText().toString();
        Integer var = Integer.parseInt(qnt_varStr);
        Integer rest = Integer.parseInt(qnt_restStr);
        intent.putExtra("VAR",var);
        intent.putExtra("REST",rest);
        startActivity(intent);
    }

    private boolean validadeFields()
    {
        boolean check=true;
        if(restricoes.getText().toString().isEmpty())
        {
            restricoes.setError("Insira corretamente o número de restrições");
            check=false;
        }
        if(variaveis.getText().toString().isEmpty())
        {
            variaveis.setError("Insira corretamente o número de variáveis");
            check=false;
        }
        return  check;
    }
}
