package com.onlybuns.onlybuns.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.Image;
import com.onlybuns.onlybuns.Repository.ImageRepository;

import io.jsonwebtoken.io.IOException;
import jakarta.annotation.PostConstruct;

@Service
public class ImageService {
    
    @Autowired
    public ImageRepository imageRepository;

        @PostConstruct
    public void init() throws java.io.IOException {
        try {
            compressImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compressImages() throws IOException, java.io.IOException {
        List<Image> images = imageRepository.findAll();
        
        for (Image image : images) {
            
            if (!image.getCompressed()) {
                
        
                    image = compressImage(image);
                    imageRepository.save(image);
                }
            }
        
    }
    
    
    private Image compressImage(Image image) throws IOException, java.io.IOException {
        byte[] imageBytes = Base64.getDecoder().decode(image.getData());
    
        
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(bais);
    
        if (bufferedImage == null) {
            throw new IOException("Unsupported image format.");
        }
    
        
        String formatName;
        String fileName = image.getName().toLowerCase(); 
        if (fileName.endsWith(".png")) {
            formatName = "png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            formatName = "jpg";
        } else {
            throw new IOException("Unsupported image format. Only PNG and JPG are supported.");
        }
    
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, formatName, baos);
        byte[] compressedBytes = baos.toByteArray();
    
        // Convert compressed bytes back to Base64
        String compressedData = Base64.getEncoder().encodeToString(compressedBytes);
        image.setData(compressedData);
        image.setCompressed(true);
    
        return image;
    }
    
}
