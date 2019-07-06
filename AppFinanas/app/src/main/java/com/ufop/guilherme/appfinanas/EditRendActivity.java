package com.ufop.guilherme.appfinanas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vicmikhailau.maskededittext.MaskedEditText;

public class EditRendActivity extends Activity {

    private Button buttonAtualizar, buttonExcluir, buttonLimpar;
    private EditText editValor;
    private MaskedEditText editData;
    private BancoDeDados bd = new BancoDeDados(this);
    private Rendas r;
    Spinner spinner;
    String id = "";
    String email ="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rend);

        Intent it = getIntent();
        r = (Rendas) it.getSerializableExtra("RendEnviada");
        spinner = (Spinner) findViewById(R.id.spinnerInfo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerRends, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        buttonAtualizar = (Button)findViewById(R.id.buttonAtualizar);
        buttonExcluir = (Button)findViewById(R.id.buttonExcluir);
        buttonLimpar = (Button)findViewById(R.id.buttonLimpar);
        editData = (MaskedEditText)findViewById(R.id.editData);
        editValor = (EditText)findViewById(R.id.editValor);
        inicializarFirebase();
        if(r != null) {
            //Fill text fields
            if(r.getInfo().equals("Salário")){
                spinner.setSelection(0);
            } else if (r.getInfo().equals("Ganhos fixos")){
                spinner.setSelection(1);
            }else if (r.getInfo().equals("Recebimento de aluguel")){
                spinner.setSelection(2);
            }else if (r.getInfo().equals("Rentabilidade de rendimentos")){
                spinner.setSelection(3);
            }
            editData.setText(String.valueOf(r.getData()));
            editValor.setText(String.valueOf(r.getValor()));
            id = r.getId();
            email = r.getEmail();
        }

        buttonLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpar();
            }
        });
        buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rendas r = new Rendas();


                String data = editData.getText().toString();
                String valor = editValor.getText().toString();
                String info = spinner.getSelectedItem().toString();

                if(id.isEmpty()){
                    Toast.makeText(EditRendActivity.this, "Confira se os campos, data, id e valor foram prenchidos", Toast.LENGTH_LONG).show();
                } else if(data.isEmpty()){
                    Toast.makeText(EditRendActivity.this, "Confira se os campos, data, id e valor foram prenchidos", Toast.LENGTH_LONG).show();
                } else if(valor.isEmpty()){
                    Toast.makeText(EditRendActivity.this, "Confira se os campos, data, id e valor foram prenchidos", Toast.LENGTH_LONG).show();
                }
                else{

                    r.setData(data);
                    r.setValor(valor);
                    r.setInfo(info);
                    r.setId(id);
                    r.setEmail(email);
                    databaseReference.child("Rendas").child(id).setValue(r);
                    Toast.makeText(EditRendActivity.this, "Renda atualizada", Toast.LENGTH_LONG).show();
                    limpar();
                    Intent it = new Intent(EditRendActivity.this, AfterLoginActivity.class);
                    startActivity(it);
                }
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.isEmpty() ){
                    Toast.makeText(EditRendActivity.this, "ID invalido", Toast.LENGTH_LONG).show();
                } else{
                    Rendas r = new Rendas();

                    r.setId(id);
                    databaseReference.child("Rendas").child(id).removeValue();
                    Toast.makeText(EditRendActivity.this, "Renda apagada", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(EditRendActivity.this, AfterLoginActivity.class);
                    startActivity(it);
                }
            }
        });
    }

    void limpar(){
        editData.setText("");
        editValor.setText("");
        editData.requestFocus();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(EditRendActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
