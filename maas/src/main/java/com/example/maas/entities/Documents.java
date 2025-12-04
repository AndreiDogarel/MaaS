package com.example.maas.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@NoArgsConstructor
@Getter
@Setter
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private User parent;

    private String cdName;

    @Column
    private byte[] cd;


    private String pcName;

    @Column
    private byte[] pc;

    private LocalDateTime uploadDateCd;

    private LocalDateTime uploadDatePc;
}