package com.ufop.guilherme.appfinanas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vicmikhailau.maskededittext.MaskedEditText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class RendaActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private EditText valorRenda;
    private EditText dataRenda;
    private Button cadastrarRenda;
    private MaskedEditText maskedEditText;
    private Spinner spinner;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private String email;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renda);

        spinner = (Spinner) findViewById(R.id.spinnerInfo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerRends, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        valorRenda = (EditText) findViewById(R.id.valorRend);
        maskedEditText = (MaskedEditText) findViewById(R.id.dataRend);
        cadastrarRenda = (Button) findViewById(R.id.CadastrarRend);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
        }else{
            email = "erro";
        }
        inicializarFirebase();

        cadastrarRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!valorRenda.getText().toString().isEmpty() && !maskedEditText.getText().toString().isEmpty()) {
                    Cadastra();
                    Toast.makeText(RendaActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(RendaActivity.this, AfterLoginActivity.class);
                    startActivity(it);
                } else {
                    Toast.makeText(RendaActivity.this, "Preencha os campos de valor e data!", Toast.LENGTH_LONG).show();
                }

            }

        });
    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(RendaActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void Cadastra() {
        Rendas r = new Rendas();
        r.setId(UUID.randomUUID().toString());
        r.setData(maskedEditText.getText().toString());
        r.setInfo(spinner.getSelectedItem().toString());
        r.setValor(valorRenda.getText().toString());
        r.setEmail(email);
        databaseReference.child("Rendas").child(r.getId()).setValue(r);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
