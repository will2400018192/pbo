package com.kasir.uml;

import java.util.UUID;

public class PembayaranDigital extends Pembayaran {
    private String provider;

    public PembayaranDigital(String provider) { this.provider = provider; }

    @Override
    public String process(Transaksi trx) {
        double total = trx.getTotal();
        String id = UUID.randomUUID().toString().substring(0,8).toUpperCase();
        StringBuilder sb = new StringBuilder();
        sb.append("--- STRUK PEMBAYARAN (DIGITAL) ---\n");
        trx.getItems().forEach(it -> sb.append(String.format("%s x%d = Rp %.0f\n", it.getProduk().getNama(), it.getQty(), it.getSubtotal())));
        sb.append(String.format("Total: Rp %.0f\n", total));
        sb.append(String.format("Provider: %s\n", provider));
        sb.append(String.format("Transaksi ID: %s\n", id));
        return sb.toString();
    }
}
