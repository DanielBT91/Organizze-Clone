package com.example.organizze.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguraFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.CurrencyTextMask;
import com.example.organizze.helper.DateCustom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import organizze.Util;

public class ReceitasActivity extends AppCompatActivity {
    private EditText campoValor;
    private EditText campoDescricao,campoData,campoCategoria;
    private FloatingActionButton fabSalvar;
    private double receitaTotal;
    private double receitaAtualizada;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguraFirebase.getDatabaseReference();
    private FirebaseAuth firebaseAuth = ConfiguraFirebase.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoValor = findViewById(R.id.campoValor);
        campoDescricao = findViewById(R.id.editDescricao);
        campoCategoria = findViewById(R.id.editCategoria);
        campoData = findViewById(R.id.editData);

        campoData.setText(DateCustom.dataAtual());

        getReceitaTotal();

        fabSalvar = findViewById(R.id.fabSalvar);

        fabSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaReceita();
            }
        });

        CurrencyTextMask masckDate = new CurrencyTextMask(campoValor);
        campoValor.setTag(masckDate);

        CurrencyTextMask oldMask = (CurrencyTextMask) campoValor.getTag();
        if(oldMask != null){
            campoValor.removeTextChangedListener(oldMask);
        }
        CurrencyTextMask currencyTextMask = new CurrencyTextMask(campoValor);
        campoValor.addTextChangedListener(currencyTextMask);


    }

    public void salvaReceita(){
        if(checkFields()){
            String textoValor = campoValor.getText().toString();
            String textoDescricao = campoDescricao.getText().toString();
            String textoCategoria = campoCategoria.getText().toString();
            String textoData = campoData.getText().toString();

            long valorLong = Util.getAmmount(textoValor.trim());

            movimentacao = new Movimentacao();
            movimentacao.setTipo("R");
            movimentacao.setData(textoData);
            movimentacao.setDescricao(textoDescricao);
            movimentacao.setCategoria(textoCategoria);
            movimentacao.setValor(valorLong);
            receitaAtualizada= receitaTotal + valorLong;
            updateReceitas(receitaAtualizada);


            movimentacao.salvar(textoData);

            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.sucesso_add,"Receita"),
                    Toast.LENGTH_SHORT).show();

            campoValor.setText("");
            campoDescricao.setText("");
            campoCategoria.setText("");
            campoData.setText("");

            finish();
        }
    }


    public boolean checkFields(){
        String textoValor = campoValor.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoData = campoData.getText().toString();

        if(!textoValor.isEmpty()){
            if(!textoDescricao.isEmpty()){
                if(!textoCategoria.isEmpty()){
                    if(!textoData.isEmpty()){
                        return true;
                    }else {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.msg_error,"data"),
                                Toast.LENGTH_SHORT).show();

                        return false;
                    }
                }else{
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.msg_error,"categoria"),
                            Toast.LENGTH_SHORT).show();

                    return false;
                }
            }else{
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.msg_error,"descrição"),
                        Toast.LENGTH_SHORT).show();

                return false;
            }
        }else{
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.msg_error,"valor"),
                    Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    public void getReceitaTotal(){
        String emailUsr = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(emailUsr);
        DatabaseReference usrRef = firebaseRef.child("usuarios")
                .child(idUser);

        usrRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateReceitas(double receita){
        String emailUsr = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(emailUsr);
        DatabaseReference usrRef = firebaseRef.child("usuarios")
                .child(idUser);

        usrRef.child("receitaTotal").setValue(receita);
    }
}
