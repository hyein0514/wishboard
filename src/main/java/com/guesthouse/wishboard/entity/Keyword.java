package com.guesthouse.wishboard.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "keyword")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id", nullable = false)
    private Long keywordId;

    @Column(name = "keyword", nullable = false)
    private String keyword;

    @Column(name = "notification", nullable = false)
    private boolean notification;

    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
