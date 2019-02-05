package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.organizze.R;
import com.example.organizze.helper.CurrencyTextMask;
import com.example.organizze.model.Movimentacao;

import java.util.ArrayList;
import java.util.List;

import organizze.Util;

public class MovimentosAdapter extends RecyclerView.Adapter<MovimentosAdapter.MyViewHolder> {
    private List<Movimentacao> lstMovimentos = new ArrayList<>();
    private Context context;
    public MovimentosAdapter(List<Movimentacao> lstMovimentos, Context context) {
        this.lstMovimentos = lstMovimentos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
       Movimentacao movimentacao = lstMovimentos.get(i);
       String valorFormatado = Util.addCommaPointer(String.valueOf(movimentacao.getValor()));

        myViewHolder.textValor.setText("R$ "+valorFormatado);
        myViewHolder.textDescricao.setText(movimentacao.getDescricao());
        myViewHolder.textCategoria.setText(movimentacao.getCategoria());
        myViewHolder.textValor.setTextColor(context.getResources().getColor(R.color.colorAccentReceita));

        if(movimentacao.getTipo() == "D" || movimentacao.getTipo().equals("D")){
            myViewHolder.textValor.setText("R$ - "+valorFormatado);
            myViewHolder.textValor.setTextColor(context.getResources().getColor(R.color.colorAccentDespesa));
        }
    }

    @Override
    public int getItemCount() {
        return lstMovimentos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textDescricao, textCategoria,textValor;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoria = itemView.findViewById(R.id.textCategoria);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textValor = itemView.findViewById(R.id.textValor);

        }
    }
}