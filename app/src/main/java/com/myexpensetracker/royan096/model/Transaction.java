package com.myexpensetracker.royan096.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Model class untuk menyimpan data transaksi.
 * Mencakup nama transaksi, nominal, kategori, dan tanggal transaksi.
 */
public class Transaction {
    private String id;
    private String name;
    private long amount;
    private String category;
    private String date;

    /**
     * Constructor utama dengan tanggal otomatis (hari ini).
     */
    public Transaction(String name, long amount, String category) {
        this.id = UUID.randomUUID().toString(); // ✅ ID unik
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    /**
     * Constructor overload jika ingin menetapkan tanggal manual.
     */
    public Transaction(String name, long amount, String category, String date) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    /**
     * Constructor tambahan (dengan ID tertentu),
     * dipakai saat load data dari database atau restore dari storage.
     */
    public Transaction(String id, String name, long amount, String category, String date) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // ===== Getter =====
    public String getId() { return id; }
    public String getName() { return name; }
    public long getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }

    // ===== Setter =====
    public void setId(String id) { this.id = id; } // ✅ Tambahkan agar bisa ubah ID manual jika perlu
    public void setName(String name) { this.name = name; }
    public void setAmount(long amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(String date) { this.date = date; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
