package com.kasir.controller;

import com.kasir.uml.Produk;
import com.kasir.uml.ItemTransaksi;
import com.kasir.uml.Transaksi;
import com.kasir.uml.Pembayaran;
import com.kasir.uml.PembayaranTunai;
import com.kasir.uml.PembayaranDigital;
import com.kasir.repository.ProductRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class KasirController {
    private static java.util.List<ItemTransaksi> cart = new ArrayList<>();
    private static DefaultTableModel cartModel;

    public static void showGui() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Kasir Sederhana");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLayout(new BorderLayout());

            JPanel productPanel = new JPanel();
            productPanel.setLayout(new GridLayout(0, 4, 8, 8));
            productPanel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

            ProductRepository repo = new ProductRepository();
            for (Produk p : repo.getAll()) {
                productPanel.add(productCard(p));
            }

            JScrollPane leftScroll = new JScrollPane(productPanel);
            leftScroll.setPreferredSize(new Dimension(560, 0));

            // Cart panel
            JPanel rightPanel = new JPanel(new BorderLayout());
            cartModel = new DefaultTableModel(new Object[]{"Nama","Qty","Harga","Subtotal"}, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            JTable cartTable = new JTable(cartModel);
            rightPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

            JPanel checkout = new JPanel(new GridLayout(0,1));
            JTextField cashField = new JTextField();
            JRadioButton rbTunai = new JRadioButton("Tunai", true);
            JRadioButton rbDigital = new JRadioButton("Digital");
            ButtonGroup bg = new ButtonGroup(); bg.add(rbTunai); bg.add(rbDigital);
            JButton payBtn = new JButton("Bayar");

            checkout.add(new JLabel("Metode Pembayaran:"));
            checkout.add(rbTunai);
            checkout.add(rbDigital);
            checkout.add(new JLabel("Nominal Tunai (jika pakai tunai):"));
            checkout.add(cashField);
            checkout.add(payBtn);

            rightPanel.add(checkout, BorderLayout.SOUTH);

            payBtn.addActionListener((ActionEvent e) -> {
                Transaksi trx = new Transaksi();
                for (ItemTransaksi it : cart) trx.addItem(it);
                Pembayaran bayar;
                if (rbTunai.isSelected()) {
                    double uang = 0;
                    try { uang = Double.parseDouble(cashField.getText()); } catch (Exception ex) {}
                    bayar = new PembayaranTunai(uang);
                } else {
                    bayar = new PembayaranDigital("DigiPay");
                }
                String receipt = bayar.process(trx);
                JOptionPane.showMessageDialog(frame, receipt, "Struk", JOptionPane.INFORMATION_MESSAGE);
                cart.clear();
                refreshCart();
            });

            frame.add(leftScroll, BorderLayout.CENTER);
            frame.add(rightPanel, BorderLayout.EAST);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static JPanel productCard(Produk p) {
        JPanel c = new JPanel(new BorderLayout());
        ImageIcon icon = p.getGambar();
        JLabel img = new JLabel(scaleIcon(icon, 180, 140));
        img.setHorizontalAlignment(SwingConstants.CENTER);
        img.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        img.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showPreview(p);
            }
        });
        c.add(img, BorderLayout.CENTER);
        JPanel info = new JPanel(new GridLayout(0,1));
        JLabel nameLabel = new JLabel(p.getNama());
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        info.add(nameLabel);
        JLabel priceLabel = new JLabel(String.format("Rp %.0f", p.getHarga()));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        info.add(priceLabel);
        JButton add = new JButton("Tambah");
        add.addActionListener(e -> addToCart(p));
        info.add(add);
        c.add(info, BorderLayout.SOUTH);
        c.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        return c;
    }

    private static ImageIcon scaleIcon(ImageIcon srcIcon, int targetW, int targetH) {
        if (srcIcon == null) return null;
        Image img = srcIcon.getImage();
        if (img == null) return srcIcon;
        int sw = srcIcon.getIconWidth();
        int sh = srcIcon.getIconHeight();
        if (sw <= 0 || sh <= 0) return srcIcon;
        double scale = Math.min((double) targetW / sw, (double) targetH / sh);
        int nw = Math.max(1, (int) Math.round(sw * scale));
        int nh = Math.max(1, (int) Math.round(sh * scale));
        Image scaled = img.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
        BufferedImage out = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,targetW,targetH);
        int x = (targetW - nw) / 2;
        int y = (targetH - nh) / 2;
        g.drawImage(scaled, x, y, null);
        g.dispose();
        return new ImageIcon(out);
    }

    private static void showPreview(Produk p) {
        // attempt to load larger image from img/ by id or name
        String id = p.getId();
        String name = p.getNama();
        String[] exts = new String[]{"png","jpg","jpeg"};
        java.io.File found = null;
        for (String ext : exts) {
            java.io.File f = new java.io.File("img/" + id + "." + ext);
            if (f.exists()) { found = f; break; }
        }
        if (found == null) {
            for (String ext : exts) {
                java.io.File f = new java.io.File("img/" + name + "." + ext);
                if (f.exists()) { found = f; break; }
            }
        }
        ImageIcon icon;
        if (found != null) {
            icon = new ImageIcon(found.getAbsolutePath());
        } else {
            icon = p.getGambar();
        }

        JDialog dlg = new JDialog();
        dlg.setTitle(p.getNama());
        dlg.setModal(true);
        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setIcon(scaleIcon(icon, 420, 320));
        dlg.getContentPane().add(lbl);
        dlg.pack();
        dlg.setSize(460,360);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

    private static void addToCart(Produk p) {
        for (ItemTransaksi it : cart) {
            if (it.getProduk().getId().equals(p.getId())) {
                it.setQty(it.getQty() + 1);
                refreshCart();
                return;
            }
        }
        cart.add(new ItemTransaksi(p, 1));
        refreshCart();
    }

    private static void refreshCart() {
        SwingUtilities.invokeLater(() -> {
            cartModel.setRowCount(0);
            for (ItemTransaksi it : cart) {
                cartModel.addRow(new Object[]{it.getProduk().getNama(), it.getQty(), it.getProduk().getHarga(), it.getSubtotal()});
            }
        });
    }
}
