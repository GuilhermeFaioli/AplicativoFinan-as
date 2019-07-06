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

public class EditDespActivity extends Activity {

    private EditText  editValor;
    private MaskedEditText editData;
    private Button buttonAtualizar, buttonExcluir, buttonLimpar;
    private BancoDeDados bd = new BancoDeDados(this);
    private Despesas d;
    Spinner spinner;
    String id = "";
    String email ="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_desp);


        Intent it = getIntent();
        d = (Despesas) it.getSerializableExtra("despEnviada");
        editData = (MaskedEditText)findViewById(R.id.editData);
        editValor = (EditText)findViewById(R.id.editValor);
        spinner = (Spinner) findViewById(R.id.spinnerInfo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerDesp, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        buttonAtualizar = (Button)findViewById(R.id.buttonAtualizar);
        buttonExcluir = (Button)findViewById(R.id.buttonExcluir);
        buttonLimpar = (Button)findViewById(R.id.buttonLimpar);
        inicializarFirebase();
        if(d != null) {
            //Fill text fields
            if(d.getInfo().equals("Supermercado")){
                spinner.setSelection(0);
            } else if (d.getInfo().equals("Contas")){
                spinner.setSelection(1);
            }else if (d.getInfo().equals("Posto de gasolina")){
                spinner.setSelection(2);
            }else if (d.getInfo().equals("Aluguel")){
                spinner.setSelection(3);
            }else if (d.getInfo().equals("Comida")){
                spinner.setSelection(4);
            }else if (d.getInfo().equals("Restaurante")){
                spinner.setSelection(5);
            }else if (d.getInfo().equals("Entretenimento")){
                spinner.setSelection(6);
            }else if (d.getInfo().equals("Compras")){
                spinner.setSelection(7);
            }
            editData.setText(String.valueOf(d.getData()));
            editValor.setText(String.valueOf(d.getValor()));
            id = d.getId();
            email = d.getEmail();
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
                Despesas d = new Despesas();

                String data = editData.getText().toString();
                String valor = editValor.getText().toString();
                String info = spinner.getSelectedItem().toString();

                if(id.isEmpty()){
                    Toast.makeText(EditDespActivity.this, "Confira se os campos, data, id e valor foram prenchidos", Toast.LENGTH_LONG).show();
                } else if(data.isEmpty()){
                    Toast.makeText(EditDespActivity.this, "Confira se os campos, data, id e valor foram prenchidos", Toast.LENGTH_LONG).show();
                } else if(valor.isEmpty()){
                    Toast.makeText(EditDespActivity.this, "Confira se os campos, data, id e valor foram prenchidos", Toast.LENGTH_LONG).show();
                }
                else{

                    d.setData(data);
                    d.setValor(valor);
                    d.setInfo(info);
                    d.setId(id);
                    d.setEmail(email);

                    databaseReference.child("Despesas").child(id).setValue(d);
                    Toast.makeText(EditDespActivity.this, "Despesa atualizada", Toast.LENGTH_LONG).show();
                    limpar();
                    Intent it = new Intent(EditDespActivity.this, AfterLoginActivity.class);
                    startActivity(it);
                }
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.isEmpty() ){
                    Toast.makeText(EditDespActivity.this, "ID invalido", Toast.LENGTH_LONG).show();
                } else{
                    Despesas d = new Despesas();
                    d.setId(id);
                    databaseReference.child("Despesas").child(id).removeValue();
                    Toast.makeText(EditDespActivity.this, "Despesa apagada", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(EditDespActivity.this, AfterLoginActivity.class);
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
        FirebaseApp.initializeApp(EditDespActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
