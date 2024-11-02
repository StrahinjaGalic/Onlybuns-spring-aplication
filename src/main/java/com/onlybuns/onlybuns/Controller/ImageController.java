package com.onlybuns.onlybuns.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import com.onlybuns.onlybuns.Model.Image;
import com.onlybuns.onlybuns.Repository.ImageRepository;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @GetMapping()
    public List<Image> getImages(){
        return imageRepository.findAll();
    }

}
