package com.onlybuns.onlybuns.Repository;
import com.onlybuns.onlybuns.Model.Location;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location,Long> {
    Location findById(long id);
}
