package com.example.elasticsearchusers;

import com.example.elasticsearchusers.models.UserEntity;
import com.example.elasticsearchusers.models.indexs.UserIndex;
import com.example.elasticsearchusers.repositories.UserIndexRepository;
import com.example.elasticsearchusers.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataReindexService {

    private final UserRepository userRepository;
    private final UserIndexRepository userIndexRepository;

    @PostConstruct
    public void reindexUsersOnStartup() {
        reindexUsers();
    }

    public void reindexUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserIndex> userIndexes = users.stream()
                .map(user -> new UserIndex(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoneNumber(),
                        user.getLocation(),
                        new Completion(new String[]{user.getLocation()})
                ))
                .collect(Collectors.toList());

        userIndexRepository.saveAll(userIndexes);
    }
}
