package com.garingalami.be.repository;

import com.garingalami.be.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    
    @Query(value = "SELECT CAST(timestamp AS DATE) as date, COUNT(*) as count FROM visits GROUP BY CAST(timestamp AS DATE) ORDER BY date DESC LIMIT 7", nativeQuery = true)
    List<Object[]> getDailyVisitCounts();

    @Query("SELECT COUNT(DISTINCT v.ipHash) FROM Visit v WHERE v.timestamp > :after")
    long countUniqueVisitorsAfter(java.time.LocalDateTime after);

    @Query("SELECT COUNT(DISTINCT v.ipHash) FROM Visit v")
    long countTotalUniqueVisitors();
}
