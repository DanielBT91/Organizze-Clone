package com.example.organizze.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyTextMask implements TextWatcher {

    private EditText campo;
    private boolean isUpdating = false;

    private Locale ptBr = new Locale("pt", "BR");
    private NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);

    public CurrencyTextMask(EditText campo) {
        this.campo = campo;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int after) {
        // Evita que o método seja executado varias vezes.
        // Se tirar ele entre em loop
        if (isUpdating) {
            isUpdating = false;
            return;
        }

        isUpdating = true;
        String str = s.toString();


        // Verifica se já existe a máscara no texto.
        boolean hasMask = ((str.contains("R$") || str.contains("$")) ||
                (str.contains(".") || str.contains(",")));
        // Verificamos se existe máscara
        if (hasMask) {
            // Retiramos a máscara.
            str = str.replaceAll("[R$]", "").replaceAll("[,]", "")
                    .replaceAll("[.]", "").replaceAll("[$]", "")
                    .replaceAll("^\\s+", "");

        }
        try {
            // Transformamos o número que está escrito no EditText em
            // monetário.

            str = nf.format(Double.parseDouble(str) / 100).replaceAll("[R]", "")
                    .replaceAll("[$]","").trim();

            campo.setText(str);

            campo.setSelection(campo.getText().length());
        } catch (NumberFormatException e) {
            s = "";
        }
        boolean retorno;
        if (str.isEmpty()) {
            retorno =  false;
        } else {
            retorno =  true;
        }

    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // Não utilizado
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Não utilizado
    }


}
