package com.ufop.guilherme.appfinanas;

import java.io.Serializable;

public class Rendas implements Serializable {
    private int codigo;
    private String valor;
    private String data;
    private String info;
    private String id;
    private String email;

    public Rendas() {
    }

    public Rendas(int codigo, String valor, String data, String info, String id, String email) {
        this.codigo = codigo;
        this.valor = valor;
        this.data = data;
        this.info = info;
        this.id = id;
        this.email = email;
    }

    public void salvar()
    {
        ConfigFirebase.getFirebase().child("Rendas").child(getId()).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getData() {
        return data;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
