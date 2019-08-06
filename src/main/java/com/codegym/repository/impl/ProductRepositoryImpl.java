package com.codegym.repository.impl;

import com.codegym.model.Product;
import com.codegym.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {

    private static List<Product> productList;

    static {
        productList = new ArrayList<>();
        productList.add(new Product(1, "So 1", (long) 10000, "La so 1 thoi", "3.jpg"));
        productList.add(new Product(2, "So 2", (long) 30000, "La so 2 thoi", "2.jpg"));
        productList.add(new Product(3, "So 3", (long) 20000, "La so 3 thoi", "3.jpg"));
    }

    @Override
    public List<Product> findAll() {
        return productList;
    }

    @Override
    public Product findById(int id) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId() == id) {
                return productList.get(i);
            }
        }
        return null;
    }

    @Override
    public void addElement(Product element) {
        productList.add(element);
    }

    @Override
    public void updateElement(int id, Product element) {
        int index = -1;
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId() == id) {
                index = i;
                break;
            }
        }

    }

    @Override
    public void removeElement(int id) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId() == id) {
                productList.remove(id);
                return;
            }
        }
    }

    @Override
    public List<Product> findByName(String name) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getName().toLowerCase().contains(name)) {
                products.add(productList.get(i));
            }
        }
        return products;
    }
}
