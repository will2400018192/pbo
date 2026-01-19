package com.kasir.uml;

// A small helper class demonstrating UML classes usage
public class KasirUML {
    public static void main(String[] args) {
        Produk p = new Produk("X01","Contoh",10000,null);
        ItemTransaksi it = new ItemTransaksi(p,2);
        Transaksi trx = new Transaksi();
        trx.addItem(it);
        Pembayaran bayar = new PembayaranDigital("DemoPay");
        System.out.println(bayar.process(trx));
    }
}
