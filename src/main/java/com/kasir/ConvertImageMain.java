package com.kasir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ConvertImageMain {
    public static void main(String[] args) throws Exception {
        File in = new File("img/P020.png");
        if (!in.exists()) {
            System.err.println("Source img/P020.png not found");
            return;
        }
        BufferedImage img = ImageIO.read(in);
        File out = new File("img/Happy Meal.jpg");
        ImageIO.write(img, "jpg", out);
        System.out.println("Wrote: " + out.getAbsolutePath());
    }
}
