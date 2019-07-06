package com.how2j.tmall.web;

import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.service.CategoryService;
import com.how2j.tmall.util.ImageUtil;
import com.how2j.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("categories")
    public Page4Navigator<Category> list(@RequestParam(value = "start",defaultValue = "0") int start, @RequestParam(value = "size",defaultValue = "5") int size){
        start = start < 0 ? 0 : start;
        Page4Navigator<Category> page = categoryService.list(start,size,5);
        return page;
    }

    @PostMapping("categories")
    public Object add(Category bean,MultipartFile image,HttpServletRequest request) throws IOException {
        categoryService.add(bean);
        saveOrUpdateImageFile(bean,image,request);
        return bean;
    }

    public void saveOrUpdateImageFile(Category bean,MultipartFile image,HttpServletRequest request) throws IOException {
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,bean.getId() + ".jpg");
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img,".jpg",file);
    }

    @DeleteMapping("categories/{id}")
    public String delete(@PathVariable("id") int id,HttpServletRequest request){
        categoryService.delete(id);
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id + ".jpg");
        file.delete();
        return null;
    }

    @GetMapping("categories/{id}")
    public Category get(@PathVariable("id") int id){
        Category category = categoryService.get(id);
        return category;
    }

    @PutMapping("categories/{id}")
    public Object update(Category bean,MultipartFile image,HttpServletRequest request) throws IOException {
        String name = request.getParameter("name");
        bean.setName(name);
        System.out.println(bean);
        categoryService.update(bean);
        if (image!=null){
            saveOrUpdateImageFile(bean,image,request);
        }
        return bean;
    }
}
