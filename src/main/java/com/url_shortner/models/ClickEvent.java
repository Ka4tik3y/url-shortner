package com.url_shortner.models;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class ClickEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate clickDate;
    @ManyToOne
    @JoinColumn(name = "url_mapping_id")
    private UrlMapping urlMapping;
}
