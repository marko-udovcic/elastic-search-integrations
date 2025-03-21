package com.example.elasticsearchusers.controllers;

import com.example.elasticsearchusers.models.UserEntity;
import com.example.elasticsearchusers.models.indexs.UserIndex;
import com.example.elasticsearchusers.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        UserEntity savedUser = userService.saveUser(userEntity);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/searchByUsername")
    public ResponseEntity<?> searchByUsername(@RequestParam String username) {
        SearchHits<UserIndex> searchHits = userService.searchByUsername(username);

        List<UserIndex> userList = userService.convertSearchHitsToList(searchHits);

        return ResponseEntity.ok(userList);
    }

    @GetMapping("/searchByLocation")
    public ResponseEntity<?> searchByLocation(@RequestParam String location) {
        SearchHits<UserIndex> searchHits = userService.searchByLocation(location);
        List<UserIndex> userList = userService.convertSearchHitsToList(searchHits);
        return ResponseEntity.ok(userList);

    }

    @GetMapping("/searchByLocation&Username")
    public ResponseEntity<?> searchByLocationAndUsername(@RequestParam String searchTerm) {
        SearchHits<UserIndex> searchHits = userService.searchByLocationAndUsername(searchTerm);
        List<UserIndex> userList = userService.convertSearchHitsToList(searchHits);
        return ResponseEntity.ok(userList);

    }


    @GetMapping("/suggest-location")
    public Set<String> suggestUsers(@RequestParam String prefix) {
        return userService.suggestLocations(prefix);
    }
}
