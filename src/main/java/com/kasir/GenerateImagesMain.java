package com.kasir;

import com.kasir.repository.ProductRepository;

public class GenerateImagesMain {
    public static void main(String[] args) {
        ProductRepository repo = new ProductRepository();
        repo.getAll();
        System.out.println("Generated images run complete");
    }
}
