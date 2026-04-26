package com.garingalami.be.controller;

import com.garingalami.be.model.AdminLog;
import com.garingalami.be.service.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/logs")
public class AdminLogController {

    @Autowired
    private AdminLogService adminLogService;

    @GetMapping
    public List<AdminLog> getLogs() {
        return adminLogService.getAllLogs();
    }
}
