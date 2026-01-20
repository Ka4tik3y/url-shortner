package com.url_shortner.repo;
import com.url_shortner.models.ClickEvent;
import com.url_shortner.models.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository  extends JpaRepository<ClickEvent, Long> {
    List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping urlMapping, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<ClickEvent> findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMapping, LocalDate startDateTime, LocalDate endDateTime);
}



