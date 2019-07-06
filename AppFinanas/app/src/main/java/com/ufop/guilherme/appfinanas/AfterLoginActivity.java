package com.ufop.guilherme.appfinanas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AfterLoginActivity extends Activity {

    private Button exibeDespesas;
    private Button exibeRenda;
    private TextView balanco;
    private TextView balanco2;
    private TextView balanco3;
    private static float balancoTotal = 0;
    private static float totalDesp = 0;
    private static float totalRend = 0;
    boolean cadastradoDesp = false;
    boolean cadastradoRend = false;
    private FirebaseAuth mAuth;
    private TextView tvUSD;
    private TextView tvEUR;
    private TextView tvGBP;
    private TextView tvJPY;
    private ArrayList<Float> conversoes;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Rendas> arrayListRend;
    private ArrayList<Despesas> arrayListDesp;
    private FirebaseUser firebaseUser;
    String email;
    private static List<Rendas> listaRendas = new ArrayList<Rendas>();
    private static List<Despesas> listaDesp = new ArrayList<Despesas>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        balanco = (TextView) findViewById(R.id.textViewBalanco);
        balanco2 = (TextView) findViewById(R.id.textViewBalanco2);
        balanco3 = (TextView) findViewById(R.id.textViewBalanco3);
        balanco.setText("Balanço R$0.00");
        balanco2.setText("0");
        balanco3.setText("0");
        balanco2.setVisibility(View.GONE);
        balanco3.setVisibility(View.GONE);
        inicializarFirebase();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
        }else{
            email = "erro";
        }
        listaDesp=listaTodasDesps();
        listaRendas=listaTodasRendas();
            //atualizaBDRendas();
            //atualizaBDDespesas();
            List<PieEntry> entries = new ArrayList<PieEntry>();
            List<PieEntry> entries2 = new ArrayList<PieEntry>();


        tvUSD = (TextView) findViewById(R.id.textViewUSD);
        tvEUR = (TextView) findViewById(R.id.textViewEUR);
        tvGBP = (TextView) findViewById(R.id.textViewGBP);
        tvJPY = (TextView) findViewById(R.id.textViewJPY);
        buscarConversoes();



        mAuth = FirebaseAuth.getInstance();
        ExibeRendaActivity rendaActivity = new ExibeRendaActivity();
        ExibeDespActivity despActivity = new ExibeDespActivity();




        exibeDespesas = (Button) findViewById(R.id.exibeDespesas);
        exibeDespesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AfterLoginActivity.this, ExibeDespActivity.class);
                startActivity(it);
            }
        });
        exibeRenda = (Button) findViewById(R.id.exibeRenda);
        exibeRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AfterLoginActivity.this, ExibeRendaActivity.class);
                startActivity(it);
            }
        });


    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(AfterLoginActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }




    public void logout(View view) {
        Toast.makeText(AfterLoginActivity.this, "Deslogou!!", Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        Intent it = new Intent(this,
                MainActivity.class);
        startActivity(it);
    }

    public List<Rendas> listaTodasRendas(){
        try {

            if(listaRendas.size()!=0){
                listaRendas.clear();

            }
            databaseReference.child("Rendas").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    totalRend=0;
                    totalDesp=0;
                    balancoTotal=0;
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Rendas r = objSnapshot.getValue(Rendas.class);
                        Rendas red = new Rendas();
                        if (r.getEmail() != null) {
                            if (r.getEmail().equals(email)) {
                                totalRend = totalRend + Float.parseFloat(r.getValor());
                                red.setEmail(r.getEmail());
                                red.setValor(r.getValor());
                                red.setInfo(r.getInfo());
                                red.setData(r.getData());
                                red.setId(r.getId());
                                listaRendas.add(red);
                                balanco2.setText(totalRend+"");
                                totalDesp=Float.parseFloat((String) balanco3.getText());
                                balancoTotal=totalRend-totalDesp;
                                balanco.setText("Balanço R$"+balancoTotal);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }
        return listaRendas;
    }

    public List<Despesas> listaTodasDesps(){
        try {

            if(listaDesp.size()!=0){
                listaDesp.clear();
            }
            databaseReference.child("Despesas").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    totalDesp=0;
                    totalRend=0;
                    balancoTotal=0;
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Despesas d = objSnapshot.getValue(Despesas.class);
                        Despesas desp = new Despesas();
                        if (d.getEmail() != null) {
                            if (d.getEmail().equals(email)) {
                                totalDesp = totalDesp + Float.parseFloat(d.getValor());
                                desp.setEmail(d.getEmail());
                                desp.setValor(d.getValor());
                                desp.setInfo(d.getInfo());
                                desp.setData(d.getData());
                                desp.setId(d.getId());
                                listaDesp.add(desp);
                                balanco3.setText(totalDesp+"");
                                totalRend=Float.parseFloat((String) balanco2.getText());
                                balancoTotal=totalRend-totalDesp;
                                balanco.setText("Balanço R$"+balancoTotal);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }
        return listaDesp;
    }


    public void buscarConversoes() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            //validação simples da entrada
            new DownloadConversaoTask().execute();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Sem Internet!")
                    .setMessage("Não tem nenhuma conexão de rede disponível!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // nada
                        }
                    })
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private class DownloadConversaoTask extends AsyncTask<String, Void, WebService> {
        @Override
        protected WebService doInBackground(String... strings) {
            WebService webService = null;

            try {
                webService = new WebService(AfterLoginActivity.this);
            } finally {
                return webService;
            }
        }


        @Override
        protected void onPostExecute(WebService result) {
            if (result != null && result.isEncontrou()) {
                conversoes = result.getConvesoes();
                DecimalFormat df = new DecimalFormat("#,###.00");
                tvUSD.setText("Valor do USD: R$"+(df.format(1/conversoes.get(0))));
                tvEUR.setText("Valor do EUR: R$"+(df.format(1/conversoes.get(1))));
                tvGBP.setText("Valor do GBP: R$"+(df.format(1/conversoes.get(2))));
                tvJPY.setText("Valor do JPY: R$0"+(df.format(1/conversoes.get(3))));

            }
            else
                Toast.makeText(AfterLoginActivity.this, "Os dados de cambio não foram encontrados!", Toast.LENGTH_SHORT).show();
        }

    }

}
