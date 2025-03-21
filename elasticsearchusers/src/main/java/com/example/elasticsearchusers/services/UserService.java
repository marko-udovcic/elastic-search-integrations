package com.example.elasticsearchusers.services;

import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import com.example.elasticsearchusers.models.UserEntity;
import com.example.elasticsearchusers.models.indexs.UserIndex;
import com.example.elasticsearchusers.repositories.UserIndexRepository;
import com.example.elasticsearchusers.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserIndexRepository userIndexRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public List<UserIndex> convertSearchHitsToList(SearchHits<UserIndex> searchHits) {
        return searchHits.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }

    public UserEntity saveUser(UserEntity userEntity) {
        UserEntity savedUser = userRepository.save(userEntity);

        Completion locationCompletionObj = new Completion(new String[]{savedUser.getLocation()});

        UserIndex userIndex = UserIndex.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .password(savedUser.getPassword())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .phoneNumber(savedUser.getPhoneNumber())
                .location(savedUser.getLocation())
                .locationCompletion(locationCompletionObj)

                .build();

        userIndexRepository.save(userIndex);

        return savedUser;
    }

    public SearchHits<UserIndex> searchByUsername(String username) {
        return userIndexRepository.findByUsername(username);
    }

    public SearchHits<UserIndex> searchByLocation(String location) {
        return userIndexRepository.findByLocation(location);
    }

    public SearchHits<UserIndex> searchByLocationAndUsername(String searchTerm) {
        return userIndexRepository.search(searchTerm);
    }

    public Set<String> suggestLocations(String prefix) {
        var fieldSuggester = FieldSuggester.of(b -> b.prefix(prefix)
                .completion(csb -> csb.field("locationCompletion")
                        .skipDuplicates(true)
                        .fuzzy(f -> f.fuzziness("AUTO"))
                        .size(10)));

        var suggester = Suggester.of(b -> b.suggesters("user-suggest", fieldSuggester));

        var query = NativeQuery.builder()
                .withSuggester(suggester)
                .withMaxResults(0)
                .withSourceFilter(FetchSourceFilter.of(b -> b.withExcludes("*")))
                .build();


        System.out.println("Query: " + query);
        var searchHits = elasticsearchOperations.search(query, UserIndex.class);


        if (searchHits.getSuggest() != null &&
                searchHits.getSuggest().getSuggestion("user-suggest") != null) {
            return searchHits.getSuggest().getSuggestion("user-suggest")
                    .getEntries()
                    .get(0)
                    .getOptions()
                    .stream()
                    .map(Suggest.Suggestion.Entry.Option::getText)
                    .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }

    }



}
