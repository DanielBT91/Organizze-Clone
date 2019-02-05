package com.example.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguraFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class MainActivity extends IntroActivity {
    private Button btnCadastro;
    private FirebaseAuth auth;
    private MaterialCalendarView calendarViwe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);


        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
            .fragment(R.layout.intro_1)
                .background(android.R.color.white)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.intro_2)
                .background(android.R.color.white)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.intro_3)
                .background(android.R.color.white)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.intro_4)
                .background(android.R.color.white)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.intro_cadastro)
                .background(android.R.color.white)
                .canGoForward(false)
                .build()
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        verificaUsuário();
    }

    public void onClickCadastro(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void onClickLogin(View vew){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void verificaUsuário(){
        auth = ConfiguraFirebase.getFirebaseAuth();
//        auth.signOut();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}
