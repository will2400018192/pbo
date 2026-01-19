package com.kasir;

import com.kasir.repository.ProductRepository;
import com.kasir.uml.Produk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class MapImagesMain {
    public static void main(String[] args) throws Exception {
        ProductRepository repo = new ProductRepository();
        List<Produk> products = repo.getAll();

        // candidate dirs where user placed images
        String[] dirs = new String[]{"resources/img", "src/main/resources/img", "img"};
        List<File> candidates = new ArrayList<>();
        for (String d : dirs) {
            File dir = new File(d);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles((f)->{
                    String n = f.getName().toLowerCase();
                    return n.endsWith(".png") || n.endsWith(".jpg") || n.endsWith(".jpeg");
                });
                if (files != null) for (File f: files) candidates.add(f);
            }
        }

        File outDir = new File("img");
        if (!outDir.exists()) outDir.mkdirs();

        for (Produk p : products) {
            String pid = p.getId();
            String name = p.getNama();
            String pnorm = normalize(name);
            File best = null;
            int bestScore = Integer.MAX_VALUE;
            for (File f : candidates) {
                String base = f.getName();
                String fn = base.substring(0, base.lastIndexOf('.'));
                String fnorm = normalize(fn);
                if (fnorm.equalsIgnoreCase(pnorm) || fnorm.contains(pnorm) || pnorm.contains(fnorm)) {
                    best = f; break;
                }
                // fallback: compute simple distance by length difference
                int score = Math.abs(fnorm.length() - pnorm.length());
                if (score < bestScore) { bestScore = score; best = f; }
            }

            if (best != null) {
                File dest = new File(outDir, pid + getExt(best.getName()));
                try {
                    Files.copy(best.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Mapped " + best.getName() + " -> " + dest.getPath());
                } catch (IOException ex) {
                    System.err.println("Failed to copy " + best.getName() + ": " + ex.getMessage());
                }
            } else {
                System.out.println("No image found for " + pid + " - " + name);
            }
        }

        System.out.println("Mapping complete. Output in img/");
    }

    private static String normalize(String s) {
        return s.toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    private static String getExt(String name) {
        int i = name.lastIndexOf('.');
        return (i >= 0) ? name.substring(i) : ".png";
    }
}
