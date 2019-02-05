package com.example.organizze.model;

import com.example.organizze.config.ConfiguraFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String idUsuario;
    private long receitaTotal = 0;
    private long despesaTotal = 0;

    private static final String NODEUSER = "usuarios";

    public Usuario(){

    }

    public void salvar(){
        DatabaseReference reference = ConfiguraFirebase.getDatabaseReference();
        reference.child(NODEUSER)
                .child(this.idUsuario)
                .setValue(this);
    }

    public long getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(long receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public long getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(long despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
