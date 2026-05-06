package com.eazybytes.backend.UserController;

import com.eazybytes.backend.dto.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping(path = {"/{userId}/posts/{postId}", "/user/{userId}"})
    public ResponseEntity<String> searchUserPost(@PathVariable Long userId, @PathVariable(required = false) Long postId) {
        String response;
        if (postId == null) {
            response = "Fetched user id with " + userId;
        } else {
            response = "Fetched user id with " + userId + " and  post id with " + postId;
        }
        // return response;
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public String searchUserOrder(@PathVariable(name = "userId") Long customerId, @PathVariable Long orderId) {
           return  "Fetched user id with " + customerId + " and  order id with " + orderId;
    }

    @GetMapping("/{userId}/address/{addressId}")
    public String searchUserDetails(@PathVariable Map<String, String> pathVariablesMap) {
        return  "Fetched user id with: " + pathVariablesMap.get("userId") + " and  address id with: " + pathVariablesMap.get("addressId");
    }

    @GetMapping("/search")
    public String searchUser(@RequestParam(required = false, defaultValue = "Suman") String name, @RequestParam String gender) {
        return "Fetched user " + name + " with gender: " + gender;
    }

    @GetMapping("/search/map")
    public String searchUserWithMapQueryParams(@RequestParam Map<String, String> requestParamsMap) {
        return "Fetched user " + requestParamsMap.get("name") + " with gender: " + requestParamsMap.get("gender");
    }

    @GetMapping("headers")
    public String readRequestHeader(@RequestHeader("user-Agent") String userAgent, @RequestHeader("user-Location") String userLocation){
        return "User-Agent: " + userAgent + " and User-Location: " + userLocation;
    }

    @PostMapping
    public String createUser(@RequestBody UserDto userDto) {
        return "Created with data" + userDto.toString();
    }

    @PostMapping(path = "/requestEntity")
    public ResponseEntity<String> createUserWithRequestEntity(RequestEntity<UserDto> requestEntity){
        HttpHeaders httpHeaders = requestEntity.getHeaders();
        UserDto userDto = requestEntity.getBody();
        String queryParam = requestEntity.getUrl().getQuery();
        String pathVariable = requestEntity.getUrl().getPath();

        // return "Created with data" + userDto.toString();
        return ResponseEntity.status(HttpStatus.CREATED).body("Created with data" + userDto.toString());
    }

}
