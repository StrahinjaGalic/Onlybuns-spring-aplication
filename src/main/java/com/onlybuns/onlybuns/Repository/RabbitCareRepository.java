package com.onlybuns.onlybuns.Repository;
import com.onlybuns.onlybuns.Model.RabbitCare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RabbitCareRepository extends JpaRepository<RabbitCare, Long> {
}

