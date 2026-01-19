package com.kasir.uml;

public class PembayaranTunai extends Pembayaran {
    private double uang;

    public PembayaranTunai(double uang) { this.uang = uang; }

    @Override
    public String process(Transaksi trx) {
        double total = trx.getTotal();
        StringBuilder sb = new StringBuilder();
        sb.append("--- STRUK PEMBAYARAN (TUNAI) ---\n");
        trx.getItems().forEach(it -> sb.append(String.format("%s x%d = Rp %.0f\n", it.getProduk().getNama(), it.getQty(), it.getSubtotal())));
        sb.append(String.format("Total: Rp %.0f\n", total));
        sb.append(String.format("Tunai: Rp %.0f\n", uang));
        sb.append(String.format("Kembali: Rp %.0f\n", uang - total));
        return sb.toString();
    }
}
