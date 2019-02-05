package com.example.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {
    public static String dataAtual(){
        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = dateFormat.format(date);
        return dataString;
    }

    public static String mounthYearDateSelected(String data){
        String retornoData[] = data.split("/");

        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        String mesAno = mes + ano;

        return mesAno;
    }
}
