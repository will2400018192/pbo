package com.kasir.uml;

public class ItemTransaksi {
    private Produk produk;
    private int qty;

    public ItemTransaksi(Produk produk, int qty) {
        this.produk = produk;
        this.qty = qty;
    }

    public Produk getProduk() { return produk; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public double getSubtotal() { return produk.getHarga() * qty; }
}
