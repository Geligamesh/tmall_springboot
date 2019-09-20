package com.gxb.tmall.web;

import com.gxb.tmall.pojo.Property;
import com.gxb.tmall.service.PropertyService;
import com.gxb.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("categories/{cid}/properties")
    public Page4Navigator<Property> list(@PathVariable("cid") int cid,@RequestParam(value = "start",defaultValue = "0") int start,@RequestParam(value = "size",defaultValue = "5") int size){
        start = start < 0 ? 0 : start;
        Page4Navigator<Property> page = propertyService.list(cid,start,size,5);
        return page;
    }

    @PostMapping("properties")
    public Object add(@RequestBody Property bean){
        propertyService.add(bean);
        return bean;
    }

    @DeleteMapping("properties/{id}")
    public Object delete(@PathVariable("id") int id){
        propertyService.delete(id);
        return null;
    }

    @GetMapping("properties/{id}")
    public Property get(@PathVariable("id") int id){
        Property bean = propertyService.get(id);
        return bean;
    }

    @PutMapping("properties")
    public Object update(@RequestBody Property bean){
        propertyService.update(bean);
        return bean;
    }

}
