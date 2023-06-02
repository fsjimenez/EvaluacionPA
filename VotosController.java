package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class VotosController {

    private static EntityManagerFactory emf = null;

    public VotosController() {
        getEmf("pu-pa");
    }

    public void getEmf(String puName) {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(puName);
        }
    }

    public synchronized boolean addData(Asambleista asambleista, String tipo){
        EntityManager em = emf.createEntityManager();

        Voto voto = new Voto();
        voto.setAsambleista(asambleista);
        voto.setTipoVoto(tipo);
        em.getTransaction().begin();
        em.persist(voto);
        em.getTransaction().commit();

        return true;

    }
}
