package com.hutech.WebBanHang.service;

import com.hutech.WebBanHang.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProductService {
    private List<Product> listProduct= new ArrayList<>();

    public ProductService(){
        this.listProduct.add(new Product(1, "Sản phẩm 1", "1.jpg",2912));
        this.listProduct.add(new Product(2, "Sản phẩm 2", "2.jpg",5112));
    }

    public void add(Product newProduct){
        listProduct.add(newProduct);
    }

    public List<Product> GetAll(){
        return listProduct;
    }
    public Product get(int id){
        return listProduct.stream().filter(p->p.getId()==id).findFirst().orElse(null);
    }
    public void edit(Product editProduct){
        Product find = listProduct.get(editProduct.getId());
        if(find!=null){
            find.setName(editProduct.getName());
            find.setImage(editProduct.getImage());
            find.setPrice(editProduct.getPrice());
        }
    }

}