package org.example;

import javax.persistence.*;
@Entity
@Table(name = "VOTO")
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VOTO")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ID_ASAMBLEISTA", referencedColumnName = "ID_ASAMBLEISTA")
    private Asambleista asambleista;

    @Column(name = "TIPO")
    private String tipoVoto;


    public Voto() {
    }

    public void setTipoVoto(String tipoVoto) {
        this.tipoVoto = tipoVoto;
    }

    public void setAsambleista(Asambleista asambleista) {
        this.asambleista = asambleista;
    }
}
