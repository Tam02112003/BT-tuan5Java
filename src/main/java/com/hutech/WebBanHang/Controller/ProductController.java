package com.hutech.WebBanHang.Controller;


import com.hutech.WebBanHang.model.Product;
import com.hutech.WebBanHang.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("product",new Product());
        return "products/create";
    }
    @PostMapping("/create")
    public String create(@Valid Product newProduct, BindingResult result, @RequestParam MultipartFile imageProduct, Model model ) {
        if (result.hasErrors()) {
            model.addAttribute("product",newProduct);
            return "products/create";
        }
        if(imageProduct !=null && imageProduct.getSize() >0)
        {
            try{
                File saveFile = new ClassPathResource("static/images").getFile();
                String newImageFile = UUID.randomUUID() + ".png";
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
                Files.copy(imageProduct.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImage(newImageFile);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        productService.add(newProduct);
        return "redirect:/products";
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("listproduct",productService.GetAll());
        return "products/index";
    }
    @GetMapping("/edit/{id}")
    public String getEditForm(@PathVariable("id") int id, Model model) {
        Product product = productService.get(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "products/edit";
        } else {
            // Xử lý trường hợp không tìm thấy sản phẩm
            return "redirect:/products";
        }
    }

    @PostMapping("/edit")
    public String edit(@Valid Product editProduct, BindingResult result, @RequestParam MultipartFile imageProduct, Model model ) {
        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            return "products/edit";
        }
        if (imageProduct != null && imageProduct.getSize()> 0) {
            try {
                File saveFile = new ClassPathResource("static/images").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + editProduct.getImage());
                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        productService.edit(editProduct);
        return "redirect:/products";
    }
}