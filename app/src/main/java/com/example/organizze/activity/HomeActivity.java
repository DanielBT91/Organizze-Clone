package com.example.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguraFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.MovimentosAdapter;
import organizze.Util;

public class HomeActivity extends AppCompatActivity {
    private MaterialCalendarView materialCalendarView;
    private TextView textSaudacao, textSaldo;
    private RecyclerView recyclerView;

    private FirebaseAuth auth = ConfiguraFirebase.getFirebaseAuth();
    private DatabaseReference firebaseRef = ConfiguraFirebase.getDatabaseReference();
    private DatabaseReference usrRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacao;
    private DatabaseReference movimentacaoRef;

    private long despesaTotal = 0;
    private long receitaTotal = 0;
    private long resumoUsuario = 0;
    private String mounthYearSelected;

    private List<Movimentacao> lstMovimentos = new ArrayList<>();
    private MovimentosAdapter movimentosAdapter;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        materialCalendarView = findViewById(R.id.calendarView);
        textSaudacao = findViewById(R.id.textSaudacao);
        textSaldo = findViewById(R.id.textSaldo);

        recyclerView = findViewById(R.id.recyclerMovimentos);

        movimentosAdapter = new MovimentosAdapter(lstMovimentos, this);
        RecyclerView.LayoutManager lmrv = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lmrv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(movimentosAdapter);

        configCalendarView();

        swaipe();
    }

    public void swaipe() {
        ItemTouchHelper.Callback itemTouchHelper = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
    }

    public void removeMovimentacao(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setMessage("Você tem certeza que deseja realmente excluir essa movimentação?");
        alert.setTitle("Excluir Movimentação da conta");
        alert.setCancelable(false);

        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int itemPosition = viewHolder.getAdapterPosition();
                movimentacao = lstMovimentos.get(itemPosition);

                String emailUser = auth.getCurrentUser().getEmail();
                String idUser = Base64Custom.encodeBase64(emailUser);
                movimentacaoRef = firebaseRef.child("movimentacoes")
                        .child(idUser)
                        .child(mounthYearSelected);

                movimentacaoRef.child(movimentacao.getKey()).removeValue();
                movimentosAdapter.notifyItemRemoved(itemPosition);

                updateSaldo();
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                movimentosAdapter.notifyDataSetChanged();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    public void updateSaldo(){
        String emailUsr = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(emailUsr);
        usrRef = firebaseRef.child("usuarios")
                .child(idUser);

        if(movimentacao.getTipo().equals("R")){
            receitaTotal = receitaTotal - movimentacao.getValor();
            usrRef.child("receitaTotal").setValue(receitaTotal);
        }

        if(movimentacao.getTipo().equals("D")){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usrRef.child("despesaTotal").setValue(despesaTotal);
        }
    }

    public void getResumo() {
        String emailUsr = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(emailUsr);
        usrRef = firebaseRef.child("usuarios")
                .child(idUser);

        valueEventListenerUsuario = usrRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;
                textSaudacao.setText("Olá, " + usuario.getNome());
                textSaldo.setText("R$ " + Util.addCommaPointer(String.valueOf(resumoUsuario)));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void getMovimentacao() {
        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(emailUser);

        movimentacaoRef = firebaseRef.child("movimentacoes")
                .child(idUser)
                .child(mounthYearSelected);

        valueEventListenerMovimentacao = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstMovimentos.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Movimentacao movimentacao = data.getValue(Movimentacao.class);
                    movimentacao.setKey(data.getKey());
                    lstMovimentos.add(movimentacao);
                }

                movimentosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void adicionarDespesa(View view) {
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void adicionarReceita(View view) {
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void configCalendarView() {
        CharSequence[] meses = {"Janeiro",
                "Fevereiro",
                "Março",
                "Abril",
                "Maio",
                "Junho",
                "Julho",
                "Agosto",
                "Setembro",
                "Outubro",
                "Novembro",
                "Dezembro"};

        materialCalendarView.setTitleMonths(meses);

        CalendarDay dataAtual = materialCalendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1));
        mounthYearSelected = String.valueOf(mesSelecionado + "" + dataAtual.getYear());

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth() + 1));
                mounthYearSelected = String.valueOf(mesSelecionado + "" + date.getYear());

                movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
                getMovimentacao();

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        usrRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getResumo();
        getMovimentacao();
    }
}
