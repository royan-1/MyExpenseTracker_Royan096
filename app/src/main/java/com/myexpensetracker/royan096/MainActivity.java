package com.myexpensetracker.royan096;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myexpensetracker.royan096.adapter.TransactionAdapter;
import com.myexpensetracker.royan096.model.Transaction;
import com.myexpensetracker.royan096.util.StorageManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TransactionAdapter.Listener {

    private TextView tvTotal;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private ArrayList<Transaction> transactions;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotal = findViewById(R.id.tvTotal);
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);

        // 🔹 Load data dari StorageManager
        transactions = StorageManager.loadList(this);
        adapter = new TransactionAdapter(transactions, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        updateTotal();

        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    // ==========================================================
    // 🟢 Dialog Tambah Transaksi
    // ==========================================================
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Transaksi");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding / 2, padding, padding / 2);

        // Input nama
        EditText inputName = new EditText(this);
        inputName.setHint("Nama Transaksi");
        layout.addView(inputName);

        // Input nominal
        EditText inputAmount = new EditText(this);
        inputAmount.setHint("Nominal (Rp)");
        inputAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(inputAmount);

        // Input kategori
        EditText inputCategory = new EditText(this);
        inputCategory.setHint("Kategori (Makanan, Transportasi, dll)");
        layout.addView(inputCategory);

        // Input tanggal
        EditText inputDate = new EditText(this);
        inputDate.setHint("Tanggal Transaksi (dd/MM/yyyy)");
        inputDate.setFocusable(false);
        inputDate.setClickable(true);
        layout.addView(inputDate);

        inputDate.setOnClickListener(v -> {
            final java.util.Calendar calendar = java.util.Calendar.getInstance();
            int year = calendar.get(java.util.Calendar.YEAR);
            int month = calendar.get(java.util.Calendar.MONTH);
            int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            new android.app.DatePickerDialog(this, (view, y, m, d) -> {
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m + 1, y);
                inputDate.setText(selectedDate);
            }, year, month, day).show();
        });

        builder.setView(layout);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String name = inputName.getText().toString().trim();
            String cat = inputCategory.getText().toString().trim();
            String amtStr = inputAmount.getText().toString().trim();
            String date = inputDate.getText().toString().trim();

            if (name.isEmpty() || cat.isEmpty() || amtStr.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            long amount;
            try {
                amount = Long.parseLong(amtStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Nominal tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }

            Transaction tx = new Transaction(name, amount, cat, date);
            transactions.add(tx);
            StorageManager.saveList(this, transactions);
            adapter.notifyDataSetChanged();
            updateTotal();
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    // ==========================================================
    // 🔢 Update total pengeluaran
    // ==========================================================
    private void updateTotal() {
        long total = 0;
        for (Transaction t : transactions) total += t.getAmount();

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        tvTotal.setText("Total Pengeluaran: " + nf.format(total));
    }

    // ==========================================================
    // ✏️ Edit Transaksi
    // ==========================================================
    @Override
    public void onEdit(Transaction tx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Transaksi");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int)(16 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding/2, padding, padding/2);

        EditText inputName = new EditText(this);
        inputName.setText(tx.getName());
        layout.addView(inputName);

        EditText inputAmount = new EditText(this);
        inputAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputAmount.setText(String.valueOf(tx.getAmount()));
        layout.addView(inputAmount);

        EditText inputCategory = new EditText(this);
        inputCategory.setText(tx.getCategory());
        layout.addView(inputCategory);

        EditText inputDate = new EditText(this);
        inputDate.setFocusable(false);
        inputDate.setClickable(true);
        inputDate.setText(tx.getDate());
        layout.addView(inputDate);

        inputDate.setOnClickListener(v -> {
            final java.util.Calendar calendar = java.util.Calendar.getInstance();
            new android.app.DatePickerDialog(this, (view, y, m, d) -> {
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m + 1, y);
                inputDate.setText(selectedDate);
            },
                    calendar.get(java.util.Calendar.YEAR),
                    calendar.get(java.util.Calendar.MONTH),
                    calendar.get(java.util.Calendar.DAY_OF_MONTH)).show();
        });

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            tx.setName(inputName.getText().toString().trim());
            try {
                tx.setAmount(Long.parseLong(inputAmount.getText().toString().trim()));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Nominal tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }
            tx.setCategory(inputCategory.getText().toString().trim());
            tx.setDate(inputDate.getText().toString().trim());
            StorageManager.saveList(this, transactions);
            adapter.notifyDataSetChanged();
            updateTotal();
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    // ==========================================================
    // 🗑️ Hapus Transaksi dengan Konfirmasi
    // ==========================================================
    @Override
    public void onDelete(Transaction tx) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Transaksi")
                .setMessage("Yakin ingin menghapus transaksi \"" + tx.getName() + "\"?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    transactions.remove(tx);
                    StorageManager.saveList(this, transactions);
                    adapter.notifyDataSetChanged();
                    updateTotal();
                    Toast.makeText(this, "Transaksi dihapus", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}
