package com.example.elasticsearchusers.models.indexs;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.suggest.Completion;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Document(indexName = "users")
public class UserIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String location;

    @CompletionField
    private Completion locationCompletion;


    public UserIndex(Long id, String username, String password, String email,
                     String firstName, String lastName, String phoneNumber,
                     String location) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.locationCompletion = new Completion(new String[]{location});
    }
}
