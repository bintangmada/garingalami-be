package com.garingalami.be.controller;

import com.garingalami.be.model.Visit;
import com.garingalami.be.repository.VisitRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private VisitRepository visitRepository;

    @PostMapping("/hit")
    public void recordHit(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String path = body.get("path");
        String ip = request.getRemoteAddr();
        if (ip == null) ip = "unknown";
        
        // Normalize localhost IPv6 to IPv4 to prevent double counting in development
        if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("::1")) {
            ip = "127.0.0.1";
        }
        
        String ipHash = String.valueOf(ip.hashCode());
        System.out.println("Analytics Hit | Path: " + path + " | IP: " + ip + " | Hash: " + ipHash);
        
        Visit visit = Visit.builder()
                .path(path)
                .ipHash(ipHash)
                .timestamp(LocalDateTime.now())
                .build();
        
        visitRepository.save(visit);
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        List<Object[]> rawCounts = visitRepository.getDailyVisitCounts();
        List<Map<String, Object>> chartData = new ArrayList<>();
        
        for (Object[] row : rawCounts) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("date", row[0].toString());
            dataPoint.put("count", row[1]);
            chartData.add(dataPoint);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("dailyVisits", chartData);
        response.put("totalToday", visitRepository.countUniqueVisitorsAfter(LocalDateTime.now().withHour(0).withMinute(0)));
        response.put("totalLifetime", visitRepository.countTotalUniqueVisitors());
        
        return response;
    }
}
