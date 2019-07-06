package com.ufop.guilherme.appfinanas;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ExibeRendaActivity extends Activity {

    private BancoDeDados bd = new BancoDeDados(this);
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private ListView rend;
    private ArrayList<Rendas> arrayListRend;
    private ArrayAdapter<Rendas> adapterRend;
    private ImageButton buttonFiltro;
    private Spinner spinner;
    private List<String> list;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String email;


    TextView rendTotal;
    private float totalRend = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_renda);

        spinner = (Spinner) findViewById(R.id.spinnerMonth);
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this, R.array.stringsSpinnerMonth, android.R.layout.simple_spinner_item);
        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterS);
        rend = (ListView) findViewById(R.id.ListaRend);
        rendTotal = (TextView)findViewById(R.id.textViewRend);
        buttonFiltro = (ImageButton)  findViewById(R.id.buttonFiltro);

        list = new ArrayList<String>();
        arrayListRend = new ArrayList<Rendas>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
        }else{
            email = "erro";
        }
        inicializarFirebase();
        eventoDatabase();


        rend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rendas rendasEnviadas = (Rendas) adapterRend.getItem(position);
                Intent it = new Intent(ExibeRendaActivity.this, EditRendActivity.class);
                it.putExtra("RendEnviada", rendasEnviadas);
                startActivity(it);
            }
        });

        buttonFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!spinner.getSelectedItem().toString().isEmpty()){
                    List<Rendas> rendas = bd.filtrarRendas(spinner.getSelectedItem().toString());
                    arrayList = new ArrayList<String>();
                    arrayListRend = new ArrayList<Rendas>();
                    adapter = new ArrayAdapter<String>(ExibeRendaActivity.this, android.R.layout.simple_list_item_1, arrayList);
                    adapterRend = new ArrayAdapter<Rendas>(ExibeRendaActivity.this, android.R.layout.simple_list_item_1, arrayListRend);
                    rend.setAdapter(adapter);
                    int i = 1;
                    for (Rendas r : rendas){
                        //Log.d("Lista","\n ID: "+d.getCodigo()+", data: "+d.getData()+", valor: "+ d.getValor()+", info: "+d.getInfo());
                        arrayList.add("Renda "+i+"\n  ID: "+r.getCodigo()+"\n  Data: "+r.getData()+"\n  Valor: "+r.getValor()+"\n  Info: "+r.getInfo());
                        arrayListRend.add(r);
                        adapter.notifyDataSetChanged();
                        adapterRend.notifyDataSetChanged();
                        i++;
                    }
                }else{
                    Toast.makeText(ExibeRendaActivity.this, "Preencha os campos de valor e data!", Toast.LENGTH_LONG).show();
                }
            }
        });

        bd.close();

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(ExibeRendaActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public  void eventoDatabase() {
        databaseReference.child("Rendas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalRend=0;
                list.clear();
                arrayListRend.clear();
                int i = 1;
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Rendas r = objSnapshot.getValue(Rendas.class);
                    if(r.getEmail()!=null){
                        if (r.getEmail().equals(email)){
                            list.add("Renda " + i + "\n  ID: " + i + "\n  Data: " + r.getData() + "\n  Valor: " + r.getValor() + "\n  Info: " + r.getInfo());
                            totalRend = totalRend + Float.parseFloat(r.getValor());
                            arrayListRend.add(r);
                            i++;
                        }
                    }
                }
                arrayAdapter = new ArrayAdapter<String>(ExibeRendaActivity.this, android.R.layout.simple_list_item_1, list);
                adapterRend = new ArrayAdapter<Rendas>(ExibeRendaActivity.this, android.R.layout.simple_list_item_1, arrayListRend);
                rend.setAdapter(arrayAdapter);
                rendTotal.setText("Despesa Total: R$" + totalRend);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public float retornatotalRend(){
        eventoDatabase();
        return totalRend;
    }

    public void addRend(View view) {
        Intent it = new Intent(this,
                RendaActivity.class);
        startActivity(it);
    }

}
