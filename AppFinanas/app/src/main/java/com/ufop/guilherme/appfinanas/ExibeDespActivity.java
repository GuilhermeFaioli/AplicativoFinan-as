package com.ufop.guilherme.appfinanas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExibeDespActivity extends Activity {

    private BancoDeDados bd = new BancoDeDados(this);
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private ListView desp;
    private ArrayList<Despesas> arrayListDesp;
    private ArrayAdapter<Despesas> adapterDesp;
    private ImageButton buttonFiltro;
    private Spinner spinner;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private List<String> list;
    private ArrayAdapter<String> arrayAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String email;


    TextView despTotal;
    private float totalDesp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_desp);

        buttonFiltro = (ImageButton) findViewById(R.id.buttonFiltro);
        spinner = (Spinner) findViewById(R.id.spinnerMonth);
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this, R.array.stringsSpinnerMonth, android.R.layout.simple_spinner_item);
        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterS);
        desp = (ListView) findViewById(R.id.ListaDesp);
        despTotal = (TextView) findViewById(R.id.textViewDesp);
        list = new ArrayList<String>();
        arrayListDesp = new ArrayList<Despesas>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
        }else{
            email = "erro";
        }
        inicializarFirebase();
        eventoDatabase();


        desp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Despesas despesasEnviadas = (Despesas) adapterDesp.getItem(position);
                Intent it = new Intent(ExibeDespActivity.this, EditDespActivity.class);
                it.putExtra("despEnviada", despesasEnviadas);
                startActivity(it);
            }
        });

        buttonFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Despesas> despesas = bd.filtrarDespesas(spinner.getSelectedItem().toString());
                arrayList = new ArrayList<String>();
                arrayListDesp = new ArrayList<Despesas>();
                adapter = new ArrayAdapter<String>(ExibeDespActivity.this, android.R.layout.simple_list_item_1, arrayList);
                adapterDesp = new ArrayAdapter<Despesas>(ExibeDespActivity.this, android.R.layout.simple_list_item_1, arrayListDesp);
                desp.setAdapter(adapter);
                int i = 1;
                for (Despesas d : despesas) {
                    //Log.d("Lista","\n ID: "+d.getCodigo()+", data: "+d.getData()+", valor: "+ d.getValor()+", info: "+d.getInfo());
                    arrayList.add("Despesa " + i + "\n  ID: " + d.getCodigo() + "\n  Data: " + d.getData() + "\n  Valor: " + d.getValor() + "\n  Info: " + d.getInfo());
                    arrayListDesp.add(d);
                    adapter.notifyDataSetChanged();
                    adapterDesp.notifyDataSetChanged();
                    i++;
                }
            }
        });
        bd.close();

    }


    public float retornatotalDesp() {
        eventoDatabase();
        return totalDesp;
    }

    public void addDesp(View view) {
        Intent it = new Intent(this,
                DespesasActivity.class);
        startActivity(it);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(ExibeDespActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void eventoDatabase() {
        databaseReference.child("Despesas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalDesp = 0;
                list.clear();
                arrayListDesp.clear();
                int i = 1;
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Despesas d = objSnapshot.getValue(Despesas.class);
                    if(d.getEmail()!=null){
                        if (d.getEmail().equals(email)){
                            list.add("Despesa " + i + "\n  ID: " + i + "\n  Data: " + d.getData() + "\n  Valor: " + d.getValor() + "\n  Info: " + d.getInfo());
                            totalDesp = totalDesp + Float.parseFloat(d.getValor());
                            arrayListDesp.add(d);
                            i++;
                        }
                    }
                }
                arrayAdapter = new ArrayAdapter<String>(ExibeDespActivity.this, android.R.layout.simple_list_item_1, list);
                adapterDesp = new ArrayAdapter<Despesas>(ExibeDespActivity.this, android.R.layout.simple_list_item_1, arrayListDesp);
                desp.setAdapter(arrayAdapter);
                despTotal.setText("Despesa Total: R$" + totalDesp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


