package com.example.organizze.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
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

public class DespesasActivity extends AppCompatActivity {
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private FloatingActionButton fabSalvar;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguraFirebase.getDatabaseReference();
    private FirebaseAuth firebaseAuth = ConfiguraFirebase.getFirebaseAuth();
    private double despesaTotal;
    private double despesaGerada;
    private double despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.editValor);
        campoCategoria = findViewById(R.id.editCategoria);
        campoData = findViewById(R.id.editData);
        campoDescricao = findViewById(R.id.editDescricao);

        campoData.setText(DateCustom.dataAtual());

        fabSalvar = findViewById(R.id.fabSalvar);

        getDespesasTotal();

        fabSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDespesa();
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

    public void salvarDespesa(){
        if(validarCamposDespesas()){
            movimentacao = new Movimentacao();

            long valorLong = Util.getAmmount(campoValor.getText().toString());


            movimentacao.setValor(valorLong);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(campoData.getText().toString());
            movimentacao.setTipo("D");

            despesaAtualizada = despesaTotal + valorLong;
            updateDespesa( despesaAtualizada );


            movimentacao.salvar(campoData.getText().toString());

            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.sucesso_add,"Despesa"),
                    Toast.LENGTH_SHORT).show();

            campoValor.setText("");
            campoDescricao.setText("");
            campoCategoria.setText("");
            campoData.setText("");

            finish();
        }
    }

    public boolean validarCamposDespesas(){
        if(!campoValor.getText().toString().isEmpty()){
            if(!campoData.getText().toString().isEmpty()){
                if(!campoDescricao.getText().toString().isEmpty()){
                    if(!campoCategoria.getText().toString().isEmpty()){
                        return true;
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
                        getResources().getString(R.string.msg_error,"data"),
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

    public void getDespesasTotal(){
        String emailUsr = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(emailUsr);
        DatabaseReference usrRef = firebaseRef.child("usuarios")
                .child(idUser);

        usrRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateDespesa(double despesa){
        String emailUsr = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(emailUsr);
        DatabaseReference usrRef = firebaseRef.child("usuarios")
                .child(idUser);

        usrRef.child("despesaTotal").setValue(despesa);


    }
}
