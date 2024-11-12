package com.onlybuns.onlybuns.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.onlybuns.onlybuns.Model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
