package com.myexpensetracker.royan096.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myexpensetracker.royan096.R;
import com.myexpensetracker.royan096.model.Transaction;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.VH> {

    public interface Listener {
        void onEdit(Transaction tx);
        void onDelete(Transaction tx);
    }

    private ArrayList<Transaction> list;
    private Listener listener;

    public TransactionAdapter(ArrayList<Transaction> list, Listener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        Transaction tx = list.get(i);
        h.tvName.setText(tx.getName());
        h.tvCategory.setText(tx.getCategory());
        h.tvDate.setText(tx.getDate());

        // Format rupiah (tanpa desimal)
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedAmount = nf.format(tx.getAmount()).replace(",00", "");
        h.tvAmount.setText(formattedAmount);

        // Klik item → edit transaksi
        h.itemView.setOnClickListener(v -> listener.onEdit(tx));

        // Klik tombol hapus → hapus transaksi
        h.btnDelete.setOnClickListener(v -> listener.onDelete(tx));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvAmount, tvCategory, tvDate;
        ImageButton btnDelete;

        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvAmount = v.findViewById(R.id.tvAmount);
            tvCategory = v.findViewById(R.id.tvCategory);
            tvDate = v.findViewById(R.id.tvDate);
            btnDelete = v.findViewById(R.id.btnDelete); // ✅ tombol hapus
        }
    }
}
