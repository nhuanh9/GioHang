package com.codegym.controller;

import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@PropertySource("classpath:upload_file.properties")
public class ProductController {
    @Autowired
    Environment env;

    @Autowired
    ProductService productService;

    List<Product> productOrder = new ArrayList<>();
    List<Integer> amountProducts = new ArrayList<>();

    @GetMapping("/products")
    public ModelAndView showProductList(@RequestParam("search") Optional<String> search) {
        List<Product> products;
        if (search.isPresent()) {
            products = productService.findByName(search.get());
        } else {
            products = productService.findAll();
        }
        ModelAndView modelAndView = new ModelAndView("/product/list");
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/create-product")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("productForm", new ProductForm());
        return modelAndView;
    }

    @PostMapping("/create-product")
    public ModelAndView saveProduct(@ModelAttribute("productForm") ProductForm productForm) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();

        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(),
                    new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Product product = new Product(productForm.getId(),
                productForm.getName(),
                productForm.getPrice(),
                productForm.getDescription(),
                fileName
        );
        productService.addElement(product);
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("productForm", new ProductForm());
        modelAndView.addObject("message", "Add Success");
        return modelAndView;
    }

    @GetMapping("/order-product/{id}")
    public ModelAndView showOrderForm(@PathVariable int id) {
        Product product = productService.findById(id);
        if (product != null) {
            ModelAndView modelAndView = new ModelAndView("/product/order");
            productOrder.add(product);
            modelAndView.addObject("products", productOrder);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error-404");
            return modelAndView;
        }
    }

    @GetMapping("/delete-product-order/{id}")
    public ModelAndView deleteProductFromCart(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("/product/order");
        try {
            for (int i = 0; i <= productOrder.size(); i++) {
                if (productOrder.get(i).getId() == id) {
                    productOrder.remove(i);
                }
            }
        } catch (Exception e){
            return modelAndView;
        }
        modelAndView.addObject("products", productOrder);
        return modelAndView;
    }
}
