package com.garingalami.be.service;

import com.garingalami.be.model.AdminLog;
import com.garingalami.be.repository.AdminLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminLogService {

    @Autowired
    private AdminLogRepository adminLogRepository;

    public void log(String username, String action, String details) {
        AdminLog log = AdminLog.builder()
                .adminUsername(username)
                .action(action)
                .details(details)
                .build();
        adminLogRepository.save(log);
    }

    public List<AdminLog> getAllLogs() {
        return adminLogRepository.findAllByOrderByTimestampDesc();
    }
}
