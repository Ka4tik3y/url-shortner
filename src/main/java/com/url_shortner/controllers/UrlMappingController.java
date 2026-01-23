package com.url_shortner.controllers;

import com.url_shortner.dtos.ClickEventDTO;
import com.url_shortner.dtos.UrlMappingDTO;
import com.url_shortner.models.User;
import com.url_shortner.services.UrlMappingService;
import com.url_shortner.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {
    private UrlMappingService urlMappingService;
    private UserService userService;
    @PostMapping("/shorten")
    @PreAuthorize("hasAuthority('ROLE_USER')")

    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String,String> map, Principal principal){

        String originalUrl = map.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl,user);
        return ResponseEntity.ok(urlMappingDTO);
    }


    @GetMapping("/myurls")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal){
        User user = userService.findByUsername(principal.getName());
       List<UrlMappingDTO> urls =  urlMappingService.getUrlsByUser(user);
       return ResponseEntity.ok(urls);
    }


    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@RequestBody String shortUrl ,
                                                               @RequestParam("startDate") String startDate,
                                                               @RequestParam("endDate") String endDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDateTime = LocalDateTime.parse(startDate, dateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, dateTimeFormatter);
       List<ClickEventDTO> clickEventDTOS =  urlMappingService.getClickEventsByDateAndTime(shortUrl,startDateTime,endDateTime);
       return ResponseEntity.ok(clickEventDTOS);
    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Map<LocalDate,Long>> getTotalClicksByDate (Principal principal,@RequestParam("startDate") String startDate,
                                                               @RequestParam("endDate") String endDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        User user =  userService.findByUsername(principal.getName());
        LocalDate startDateTime = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate  endDateTime = LocalDate.parse(endDate, dateTimeFormatter);
        Map<LocalDate, Long> totalClicks = urlMappingService.getTotalClicksByUserAndDate(user, startDateTime,endDateTime);
        return  ResponseEntity.ok(totalClicks);
    }
}
