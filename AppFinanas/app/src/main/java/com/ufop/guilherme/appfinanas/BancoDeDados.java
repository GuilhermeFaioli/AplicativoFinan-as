package com.ufop.guilherme.appfinanas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BancoDeDados extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private static final String NomeBanco = "bdFinancas";

    private static final String tabelaDespesas = "despesas";
    private static final String tabelaRendas = "rendas";
    private static final String ID = "id";
    private static final String Data = "data";
    private static final String Valor = "valor";
    private static final String Info = "descricao";

    public BancoDeDados(Context context) {
        super(context, NomeBanco, null, VERSAO);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + tabelaDespesas + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Data + " DATE, " + Valor + " FLOAT, " + Info + " TEXT)";
        db.execSQL(query);
        String query2 = "CREATE TABLE " + tabelaRendas + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Data + " DATE, " + Valor + " FLOAT, " + Info + " TEXT)";
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addDespesas(Despesas d) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Data, d.getData());
        values.put(Valor, d.getValor());
        values.put(Info, d.getInfo());
        db.insert(tabelaDespesas, null, values);
        db.close();
    }

    public void addRendas(Rendas r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Data, r.getData());
        values.put(Valor, r.getValor());
        values.put(Info, r.getInfo());
        db.insert(tabelaRendas, null, values);
        db.close();
    }

    //Para apagar use o comando:
    // despesa.setCodigo(id da despesa a ser apagada);
    //apagarDespesas(despesa);
    public void apagarDespesas(Despesas d) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tabelaDespesas, ID + " = ?", new String[]{String.valueOf(d.getCodigo())});
        db.close();
    }

    public void apagarRendas(Rendas r) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tabelaRendas, ID + " = ?", new String[]{String.valueOf(r.getCodigo())});
        db.close();
    }

    //Para buscar use o comando:
    //buscarDespesa(codigo da despesa);
    // e faça um cliente receber a função, para depois usar os dados desse cliente




    //Para atualizar use o comando:
    // despesa.setCodigo(id da despesa a ser atualizada);
    //e altere os dados da despesa, que serão atualizados
    //atualizaDespesa(despesa);
    public void atualizaDespesa(Despesas d){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Data, d.getData());
        values.put(Valor, d.getValor());
        values.put(Info, d.getInfo());
        db.update(tabelaDespesas, values, ID+" = ?", new String[]{String.valueOf(d.getCodigo())});
        db.close();
    }

    public void atualizaRenda(Rendas r){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Data, r.getData());
        values.put(Valor, r.getValor());
        values.put(Info, r.getInfo());
        db.update(tabelaRendas, values, ID+" = ?", new String[]{String.valueOf(r.getCodigo())});
        db.close();
    }

    public List<Despesas> listaTodasDespesas(){
        List<Despesas> listaDespesas = new ArrayList<Despesas>();
        String query = "SELECT * FROM "+tabelaDespesas;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()){
            do {
                Despesas d = new Despesas();
                d.setCodigo(Integer.parseInt(c.getString(0)));
                d.setData(c.getString(1));
                d.setValor(c.getString(2));
                d.setInfo(c.getString(3));
                listaDespesas.add(d);
            }while(c.moveToNext());
        }
        return listaDespesas;
    }

    public List<Rendas> listaTodasRendas(){
        List<Rendas> listaRendas = new ArrayList<Rendas>();
        String query = "SELECT * FROM "+tabelaRendas;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()){
            do {
                Rendas r = new Rendas();
                r.setCodigo(Integer.parseInt(c.getString(0)));
                r.setData(c.getString(1));
                r.setValor(c.getString(2));
                r.setInfo(c.getString(3));
                listaRendas.add(r);
            }while(c.moveToNext());
        }
        return listaRendas;
    }

    public List<Despesas> filtrarDespesas(String month){
        List<Despesas> listaDespesas = new ArrayList<Despesas>();
        String query;
        if (month.equals("Todos")){
            query = "SELECT * FROM "+tabelaDespesas;
        } else{
            query = "SELECT * FROM "+tabelaDespesas+ " WHERE "+Data+" LIKE '%/"+month+"/%'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()){
            do {
                Despesas d = new Despesas();
                d.setCodigo(Integer.parseInt(c.getString(0)));
                d.setData(c.getString(1));
                d.setValor(c.getString(2));
                d.setInfo(c.getString(3));
                listaDespesas.add(d);
            }while(c.moveToNext());
        }
        return listaDespesas;
    }

    public List<Rendas> filtrarRendas(String month){
        List<Rendas> listaRendas = new ArrayList<Rendas>();
        String query;
        if (month.equals("Todos")){
            query = "SELECT * FROM "+tabelaRendas;
        } else {
            query =  "SELECT * FROM "+tabelaRendas+ " WHERE "+Data+" LIKE '%/"+month+"/%'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()){
            do {
                Rendas r = new Rendas();
                r.setCodigo(Integer.parseInt(c.getString(0)));
                r.setData(c.getString(1));
                r.setValor(c.getString(2));
                r.setInfo(c.getString(3));
                listaRendas.add(r);
            }while(c.moveToNext());
        }
        return listaRendas;
    }
}
