package com.quizmaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;

import java.util.Date;
import javax.persistence.*;

@Data
@Builder
@Entity(name = "summaries")
@NoArgsConstructor
@AllArgsConstructor
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    private String date;

    private String title;

    @Column (columnDefinition = "text")
    private String summary;

    @Column (columnDefinition = "text")
    private String content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;


}
