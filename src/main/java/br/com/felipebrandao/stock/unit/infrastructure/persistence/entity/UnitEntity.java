package br.com.felipebrandao.stock.unit.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "unit")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "abbreviation", nullable = false, unique = true, length = 20)
    private String abbreviation;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}

