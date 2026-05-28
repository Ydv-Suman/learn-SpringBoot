package com.eazybytes.jobportal.company.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logging")
@Slf4j
public class LoggingController {

    @GetMapping(path = "/public", version = "1.0")
    public ResponseEntity<String> testLogging() {
        log.trace("TRACE: detailed trace log");
        log.debug("DEBUG: debug message");
        log.info("INFO: informational message");
        log.warn("WARN: warning message");
        log.error("ERROR: error message");
        return ResponseEntity.status(HttpStatus.OK)
                .body("Logging tested successfully");
    }
}
