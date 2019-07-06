package com.ufop.guilherme.appfinanas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vicmikhailau.maskededittext.MaskedEditText;

import java.util.UUID;

public class DespesasActivity extends Activity {

    private EditText valorDespesa;
    private MaskedEditText dataDespesa;
    private Button cadastrarDespesa;
    private Spinner spinner;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String email;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        spinner = (Spinner) findViewById(R.id.spinnerInfo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerDesp, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        valorDespesa = (EditText) findViewById(R.id.valorDesp);
        dataDespesa = (MaskedEditText) findViewById(R.id.dataDesp);
        cadastrarDespesa = (Button) findViewById(R.id.CadastrarDesp);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
        }else{
            email = "erro";
        }
        inicializarFirebase();

        cadastrarDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!valorDespesa.getText().toString().isEmpty() && !dataDespesa.getText().toString().isEmpty()){

                        Cadastra();
                        Toast.makeText(DespesasActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                        Intent it = new Intent(DespesasActivity.this, AfterLoginActivity.class);
                        startActivity(it);
                    } else{
                        Toast.makeText(DespesasActivity.this, "Preencha os campos de valor e data!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    Log.d("errolog", "Erro: "+e);
                }


            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(DespesasActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void Cadastra() {
        Despesas d = new Despesas();
        d.setId(UUID.randomUUID().toString());
        d.setData(dataDespesa.getText().toString());
        d.setInfo(spinner.getSelectedItem().toString());
        d.setValor(valorDespesa.getText().toString());
        d.setEmail(email);
        databaseReference.child("Despesas").child(d.getId()).setValue(d);
    }
}
