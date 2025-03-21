package com.example.elasticsearchusers.repositories;

import com.example.elasticsearchusers.models.indexs.UserIndex;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserIndexRepository extends ElasticsearchRepository<UserIndex, Long> {

//    SearchHits<UserIndex> findByUsername(String username);


    @Query("""
            {
                "multi_match": {
                    "query": "?0",
                    "fields": ["username"],
                    "operator": "and",
                    "fuzziness": "AUTO",
                    "type": "best_fields",
                    "prefix_length": 1,
                    "tie_breaker": 0.3
                }
            }
            """)
    SearchHits<UserIndex> findByUsername(String query);


    SearchHits<UserIndex> findByEmail(String email);

    SearchHits<UserIndex> findByLocation(String location);

    SearchHits<UserIndex> findByFirstName(String firstName);

    SearchHits<UserIndex> findByLastName(String lastName);

    SearchHits<UserIndex> findByUsernameAndLocation(String username, String location);


    @Query("""
            {
            "multi_match":{
                "query": "?0",
                "fields": [ "location^3", "username" ],
                "operator": "and",
                "fuzziness": "AUTO",
                "type": "best_fields",
                "tie_breaker": 0.7
                }
            }
            """)
    SearchHits<UserIndex> search(String searchTerm);
}
