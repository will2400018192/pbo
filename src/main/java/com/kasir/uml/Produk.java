package com.kasir.uml;

import javax.swing.*;

public class Produk {
    private String id;
    private String nama;
    private double harga;
    private ImageIcon gambar;

    public Produk(String id, String nama, double harga, ImageIcon gambar) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.gambar = gambar;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public ImageIcon getGambar() { return gambar; }
}
