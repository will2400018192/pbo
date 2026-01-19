package com.kasir.repository;

import com.kasir.uml.Produk;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    public List<Produk> getAll() {
        List<Produk> list = new ArrayList<>();
        // McDonald's menu items (example prices in IDR)
        list.add(new Produk("P001", "Big Mac", 45000, loadImageFor("P001", "BigMac", new Color(200,80,60))));
        list.add(new Produk("P002", "Quarter Pounder", 48000, loadImageFor("P002", "Quarter", new Color(190,90,60))));
        list.add(new Produk("P003", "McChicken", 30000, loadImageFor("P003", "McChicken", new Color(210,150,80))));
        list.add(new Produk("P004", "Cheeseburger", 18000, loadImageFor("P004", "Cheeseburger", new Color(220,140,80))));
        list.add(new Produk("P005", "Double Cheeseburger", 26000, loadImageFor("P005", "DoubleCheese", new Color(200,120,70))));
        list.add(new Produk("P006", "Chicken McNuggets (6pc)", 25000, loadImageFor("P006", "Nuggets6", new Color(210,170,100))));
        list.add(new Produk("P007", "Chicken McNuggets (9pc)", 35000, loadImageFor("P007", "Nuggets9", new Color(200,160,90))));
        list.add(new Produk("P008", "Filet-O-Fish", 29000, loadImageFor("P008", "FiletO", new Color(180,160,200))));
        list.add(new Produk("P009", "McFlurry", 22000, loadImageFor("P009", "McFlurry", new Color(220,220,240))));
        list.add(new Produk("P010", "Apple Pie", 10000, loadImageFor("P010", "ApplePie", new Color(230,180,120))));
        list.add(new Produk("P011", "French Fries (Small)", 12000, loadImageFor("P011", "FriesS", new Color(240,200,60))));
        list.add(new Produk("P012", "French Fries (Medium)", 15000, loadImageFor("P012", "FriesM", new Color(240,190,60))));
        list.add(new Produk("P013", "French Fries (Large)", 19000, loadImageFor("P013", "FriesL", new Color(240,170,60))));
        list.add(new Produk("P014", "Iced Tea", 9000, loadImageFor("P014", "IcedTea", new Color(120,180,220))));
        list.add(new Produk("P015", "Iced Coffee", 17000, loadImageFor("P015", "IcedCoffee", new Color(150,110,90))));
        list.add(new Produk("P016", "Soft Drink (Medium)", 12000, loadImageFor("P016", "SodaM", new Color(100,160,220))));
        list.add(new Produk("P017", "Egg McMuffin", 27000, loadImageFor("P017", "EggMuffin", new Color(240,220,140))));
        list.add(new Produk("P018", "Hotcakes", 32000, loadImageFor("P018", "Hotcakes", new Color(240,200,160))));
        list.add(new Produk("P019", "McSpicy", 35000, loadImageFor("P019", "McSpicy", new Color(220,80,60))));
        list.add(new Produk("P020", "Happy Meal", 40000, loadImageFor("P020", "HappyMeal", new Color(255,160,60))));
        return list;
    }

    /**
     * Try to load image from project resources folder `src/main/resources/images/{id}.png` or jpg,
     * then try classpath `/images/{id}.png`. If not found, generate a placeholder.
     */
    private ImageIcon loadImageFor(String id, String fallbackText, Color c) {
        String[] exts = new String[]{"png","jpg","jpeg"};
        // prefer user-uploaded locations first (resources/img, src/main/resources/img), then fall back to img/
        String[] dirs = new String[]{"resources/img", "src/main/resources/img", "img"};

        // search user-provided folders for id or fallback name (flexible matching)
        for (String dir : dirs) {
            java.io.File d = new java.io.File(dir);
            if (!d.exists() || !d.isDirectory()) continue;
            java.io.File[] files = d.listFiles();
            if (files == null) continue;
            for (java.io.File f : files) {
                String name = f.getName();
                int dot = name.lastIndexOf('.');
                if (dot < 0) continue;
                String base = name.substring(0, dot);
                String lb = normalize(base);
                String lid = normalize(id);
                String lfb = normalize(fallbackText);
                if (lb.equals(lid) || lb.equals(lfb) || lid.equals(lfb) || lb.contains(lfb) || lfb.contains(lb)) {
                    // if file came from resources folders, copy it into top-level img/ to override placeholder
                    try {
                        java.io.File imgDir = new java.io.File("img");
                        if (!imgDir.exists()) imgDir.mkdirs();
                        java.io.File dest = new java.io.File(imgDir, id + name.substring(dot));
                        if (!dest.exists() || !f.getAbsolutePath().equals(dest.getAbsolutePath())) {
                            java.nio.file.Files.copy(f.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        }
                        return loadImageFromFile(dest);
                    } catch (Exception ex) {
                        // fallback to direct load
                        return loadImageFromFile(f);
                    }
                }
            }
        }

        // not found — create placeholder into top-level img/
        return loadOrCreateInImg(id, fallbackText, c, exts);
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s.toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    private ImageIcon createImageIcon(String text, Color c) {
        int w = 140, h = 100;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,w,h);
        g.setColor(c);
        g.fillRoundRect(6,6,w-12,h-12,16,16);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();
        int tx = (w - fm.stringWidth(text)) / 2;
        int ty = (h + fm.getAscent()) / 2 - 6;
        g.drawString(text, tx, ty);
        g.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon loadOrCreateInImg(String id, String text, Color c, String[] exts) {
        // try id-based files in img/
        for (String ext : exts) {
            java.io.File f = new java.io.File("img/" + id + "." + ext);
            if (f.exists()) return new ImageIcon(f.getAbsolutePath());
        }
        // try fallbackText-based files in img/
        for (String ext : exts) {
            java.io.File f = new java.io.File("img/" + text + "." + ext);
            if (f.exists()) return loadImageFromFile(f);
        }

        // create placeholder and save into img/{id}.png
        int w = 140, h = 100;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,w,h);
        g.setColor(c);
        g.fillRoundRect(6,6,w-12,h-12,16,16);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();
        int tx = (w - fm.stringWidth(text)) / 2;
        int ty = (h + fm.getAscent()) / 2 - 6;
        g.drawString(text, tx, ty);
        g.dispose();

        try {
            File dir = new File("img");
            if (!dir.exists()) dir.mkdirs();
            File out = new File(dir, id + ".png");
            ImageIO.write(img, "png", out);
            System.out.println("Wrote placeholder image to img/: " + out.getAbsolutePath());
        } catch (IOException ex) {
            System.err.println("Failed to write placeholder to img/ for " + id + ": " + ex.getMessage());
        }

        return new ImageIcon(img);
    }

    private ImageIcon loadImageFromFile(File f) {
        try {
            BufferedImage src = ImageIO.read(f);
            if (src == null) return new ImageIcon(f.getAbsolutePath());
            int targetW = 140, targetH = 100;
            int sw = src.getWidth();
            int sh = src.getHeight();
            double scale = Math.min((double) targetW / sw, (double) targetH / sh);
            int nw = (int) Math.round(sw * scale);
            int nh = (int) Math.round(sh * scale);
            Image scaled = src.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
            BufferedImage out = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = out.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0,0,targetW,targetH);
            int x = (targetW - nw) / 2;
            int y = (targetH - nh) / 2;
            g.drawImage(scaled, x, y, null);
            g.dispose();
            return new ImageIcon(out);
        } catch (IOException e) {
            return new ImageIcon(f.getAbsolutePath());
        }
    }
}
