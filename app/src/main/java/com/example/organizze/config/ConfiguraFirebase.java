package com.example.organizze.config;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguraFirebase {
    private static FirebaseAuth auth;
    //Retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAuth(){

        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }
}
