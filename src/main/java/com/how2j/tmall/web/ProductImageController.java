package com.how2j.tmall.web;

import com.how2j.tmall.dao.ProductImageDAO;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.ProductImage;
import com.how2j.tmall.service.ProductImageService;
import com.how2j.tmall.service.ProductService;
import com.how2j.tmall.util.ImageUtil;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.StyleContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductService productService;

    @GetMapping("products/{pid}/productImages")
    public List<ProductImage> list(@PathVariable("pid") int pid,@RequestParam("type") String type){
        Product product = productService.get(pid);
        if(productImageService.type_single.equals(type)){
            return productImageService.listSingleProductImages(product);
        }else if(productImageService.type_detail.equals(type)){
            return productImageService.listDetailProductImages(product);
        }else{
            return new ArrayList<>();
        }
    }

    @PostMapping("productImages")
    public Object add(@RequestParam int pid,@RequestParam String type,MultipartFile image,HttpServletRequest request){
        Product product = productService.get(pid);
        ProductImage bean = new ProductImage();
        bean.setProduct(product);
        bean.setType(type);
        productImageService.add(bean);

        String folder = "img/";
        if(productImageService.type_single.equals(type)){
            folder += "productSingle";
        }else{
            folder += "productDetail";
        }
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File imagefolder = new File(request.getServletContext().getRealPath(folder));

        File file = new File(imageFolder,bean.getId() + ".jpg");
        String fileName = file.getName();

        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img,"jpg",file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(productImageService.type_single.equals(type)){
            String imageFolder_small = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getServletContext().getRealPath("img/productSingle_middle");
            File file_small = new File(imageFolder_small,fileName);
            File file_middle = new File(imageFolder_middle,fileName);
            if(!file_small.getParentFile().exists()){
                file_small.getParentFile().mkdirs();
            }
            if(!file_middle.getParentFile().exists()){
                file_middle.getParentFile().mkdirs();
            }
            ImageUtil.resizeImage(file,56,56,file_small);
            ImageUtil.resizeImage(file,217,190,file_middle);
        }
        return bean;
    }

    @DeleteMapping("productImages/{id}")
    public Object delete(@PathVariable("id") int id,HttpServletRequest request){
        // ProductImage bean = productImageService.get(id);
        // productImageService.delete(id);
        ProductImage bean = productImageService.get(id);
        productImageService.delete(bean.getId());

        String folder = "img/";
        if(productImageService.type_single.equals(bean.getType())){
            folder += "productSingle";
        }else{
            folder += "productDetail";
        }

        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,bean.getId() + ".jpg");
        String fileName = file.getName();
        file.delete();

        if(productImageService.type_single.equals(bean.getType())){
            String image_small = request.getServletContext().getRealPath("img/productSingle_small");
            String image_middle = request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(image_small,fileName);
            File f_middle = new File(image_middle,fileName);
            f_small.delete();
            f_middle.delete();
        }
        return null;
    }


}
