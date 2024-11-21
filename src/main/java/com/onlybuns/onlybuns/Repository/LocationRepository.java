package com.onlybuns.onlybuns.Repository;
import com.onlybuns.onlybuns.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location,Long> {
    Location findById(long id);
    
    @Query("SELECT p.location FROM Post p WHERE p.id = :postId")
    Location findByPostId(@Param("postId") Long postId);
    
    
}


