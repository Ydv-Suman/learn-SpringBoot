package com.eazybytes.backend.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/legacy/versions")
public class LegacyVersionController {

    @GetMapping(path = {"", "/", "/v1"})
    public ResponseEntity<String> getVersionOne() {
        return ResponseEntity.ok("Response from Version 1");
    }

    @GetMapping("/v2")
    public ResponseEntity<String> getVersionTwo() {
        return ResponseEntity.ok("Response from Version 2");
    }

    @GetMapping(params = "version=1")
    public ResponseEntity<String> v1ReqParamVersion() {
        return ResponseEntity.ok("Response from v1ReqParamVersion 1");
    }

    @GetMapping(params = "version=2")
    public ResponseEntity<String> v2ReqParamVersion() {
        return ResponseEntity.ok("Response from v2ReqParamVersion 2");
    }

    @GetMapping(headers = "X-API-VERSION=1")
    public ResponseEntity<String> v1ReqHeaderVersion() {
        return ResponseEntity.ok("Response from v1ReqHeaderVersion 1");
    }

    @GetMapping(headers = "X-API-VERSION=2")
    public ResponseEntity<String> v2ReqHeaderVersion() {
        return ResponseEntity.ok("Response from v2ReqHeaderVersion 2");
    }

    @GetMapping(produces = "applicatioon/vnd.eazyapp.v1+json1")
    public ResponseEntity<String> v1ReqMediaVersion() {
        return ResponseEntity.ok("Response from v1ReqMediaVersion 1");
    }

    @GetMapping(produces = "applicatioon/vnd.eazyapp.v2+json1")
    public ResponseEntity<String> v2ReqMediaVersion() {
        return ResponseEntity.ok("Response from v2ReqMediaVersion 2");
    }
}
