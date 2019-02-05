package com.example.organizze.model;

import com.example.organizze.config.ConfiguraFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {
    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private long valor;
    private String key;

    private static final String MOVIMENTACAO = "movimentacoes";
    public Movimentacao() {
    }

    public void salvar(String dataEscolhida){
        FirebaseAuth auth = ConfiguraFirebase.getFirebaseAuth();
        DatabaseReference reference = ConfiguraFirebase.getDatabaseReference();

        String idUsuario = Base64Custom.encodeBase64(auth.getCurrentUser().getEmail());

        reference.child(MOVIMENTACAO)
                .child(idUsuario)
                .child(DateCustom.mounthYearDateSelected(dataEscolhida))
                .push()
                .setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }
}
