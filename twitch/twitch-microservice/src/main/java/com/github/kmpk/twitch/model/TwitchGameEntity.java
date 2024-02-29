package com.github.kmpk.twitch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "game", indexes = {@Index(columnList = "name", name = "idx_game__name")})
@NoArgsConstructor
@AllArgsConstructor
public class TwitchGameEntity {
    @Id
    @NaturalId
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "art_url", nullable = false)
    private String artUrl;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;
}
