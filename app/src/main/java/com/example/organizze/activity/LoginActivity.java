package com.example.organizze.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguraFirebase;
import com.example.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    private Button btnEntrar;
    private EditText campoEmail, campoSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnEntrar = findViewById(R.id.btnEntrar);
        campoEmail = findViewById(R.id.etEmailEntrar);
        campoSenha =findViewById(R.id.etPasswordEntrar);


        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if(!textoEmail.isEmpty()){
                    if(!textoSenha.isEmpty()){
                        Usuario usuario = new Usuario();
                        usuario.setSenha(textoSenha);
                        usuario.setEmail(textoEmail);

                        logarUsuario(usuario.getEmail(), usuario.getSenha());

                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Preencha o campo de senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Preencha o campo de email!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void logarUsuario(String email, String senha){
        auth = ConfiguraFirebase.getFirebaseAuth();

        auth.signInWithEmailAndPassword(
                email,senha
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful()){
                abrirActivity();
             }else{

                 String exception;

                 try{
                     throw task.getException();
                 }catch (FirebaseAuthInvalidUserException e) {
                     exception = "usuário não está cadastrado";
                 }catch (FirebaseAuthInvalidCredentialsException e){
                     exception = "Email e senha não correspondem a um usuário cadastrado";
                 }catch (Exception e){
                     exception = "Erro ao fazer login! "+e.getMessage();
                    e.printStackTrace();
                 }

                 Toast.makeText(getApplicationContext(),
                         exception,
                         Toast.LENGTH_SHORT).show();
             }
            }
        });
    }

    private void abrirActivity(){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
