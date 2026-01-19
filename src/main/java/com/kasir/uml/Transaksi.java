package com.kasir.uml;

import java.util.ArrayList;
import java.util.List;

public class Transaksi {
    private List<ItemTransaksi> items = new ArrayList<>();

    public void addItem(ItemTransaksi it) { items.add(it); }
    public List<ItemTransaksi> getItems() { return items; }
    public double getTotal() {
        return items.stream().mapToDouble(ItemTransaksi::getSubtotal).sum();
    }
}
