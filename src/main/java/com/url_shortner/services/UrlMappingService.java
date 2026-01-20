package com.url_shortner.services;

import com.url_shortner.dtos.ClickEventDTO;
import com.url_shortner.dtos.UrlMappingDTO;
import com.url_shortner.models.ClickEvent;
import com.url_shortner.models.UrlMapping;
import com.url_shortner.models.User;
import com.url_shortner.repo.ClickEventRepository;
import com.url_shortner.repo.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMappingService {

    private ClickEventRepository clickEventRepository;
    private UrlMappingRepository urlMappingRepository;
    public UrlMappingDTO createShortUrl(String originalUrl, User user) {

        String shortUrl =  generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setUser(user);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setCreatedDate(LocalDateTime.now());
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertUrlMappingDto(savedUrlMapping);
    }

    private UrlMappingDTO convertUrlMappingDto(UrlMapping urlMapping) {
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());
        return urlMappingDTO;
    }



    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder shortUrl = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            shortUrl.append(characters.charAt(rnd.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }

    public List<UrlMappingDTO> getUrlsByUser(User user) {
        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertUrlMappingDto)
                .toList();
    }



    public List<ClickEventDTO> getClickEventsByDateAndTime(String shortUrl, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if(urlMapping != null) {
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, startDateTime, endDateTime).stream()
                    .collect(Collectors.groupingBy(click -> click.getClickDate().atStartOfDay().toLocalDate(), Collectors.counting()))
                    .entrySet().stream().map(
                            entry -> {
                                ClickEventDTO clickEventDTO = new ClickEventDTO();
                                clickEventDTO.setClickDate(entry.getKey().atStartOfDay());
                                clickEventDTO.setCount(entry.getValue());
                                return clickEventDTO;
                            })
                    .collect(Collectors.toList());
        }
        return null;
    }


    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate startDateTime, LocalDate endDateTime) {
       List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
       List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, startDateTime, endDateTime);
       return clickEvents.stream()
               .collect(Collectors.groupingBy(click -> click.getClickDate().atStartOfDay().toLocalDate(), Collectors.counting()));
    }
}
