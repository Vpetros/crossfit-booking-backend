package com.crossfit.booking.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "roles")
public class Role {

    // RoleInitializer constructor
    public Role(String name) {
        this.name = name;
    }

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;  // ROLE_USER, ROLE_ADMIN

    private String description;
}
