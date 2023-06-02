package org.example;

import javax.persistence.*;
import javax.print.DocFlavor;

@Entity
@Table(name = "ASAMBLEISTA")
public class Asambleista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ASAMBLEISTA")
    private Long id;

    @Column(nullable = false, length = 1, columnDefinition = "CHECK (region IN ('N', 'E', 'P'))")
    private String region;

    public Asambleista() {
    }

}
