package com.example.organizze.activity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private EditText campoNome, campoSenha,campoEmail;
    private Button btnCadastrar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.etNome);
        campoEmail = findViewById(R.id.etEmailCadastro);
        campoSenha = findViewById(R.id.etPasswordCadastro);
        btnCadastrar = findViewById(R.id.btnCadastro);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome = campoNome.getText().toString();
                String textoSenha = campoSenha.getText().toString();
                String textoEmail = campoEmail.getText().toString();

                //Valida se os campos foram preenchidos
                if(!textoNome.isEmpty()){
                    if(!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){

                            Usuario usuario = new Usuario();

                            usuario.setNome(textoNome);
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);

                            cadastrarUsuario(usuario.getEmail(), usuario.getSenha());
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "Preencha a senha",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Preencha o Email",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Preencha o nome",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public void cadastrarUsuario(String email, String senha){
        auth = ConfiguraFirebase.getFirebaseAuth();



        auth.createUserWithEmailAndPassword(
            email,senha
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                }else{
                    String exception = null;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        exception = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = "Por favor, digite um email válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        exception = "Essa conta já foi cadastrada";
                    }catch (Exception e){
                        exception = "Erro ao cadastrar usuário "+e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),
                            exception,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
