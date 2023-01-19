package com.example.code.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activation_code")
public class ActivationCode
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long activation_code_id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm['Z']")
    @CreationTimestamp
    @Column(name = "create_datetime")
    private LocalDateTime createDateTime;

    @Column(name = "tariff_id")
    private long tariffId;

    @Column(name = "is_active")
    private Boolean isActive;
}

