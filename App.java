package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.SocketImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    public static void main(String[] args) throws Exception{

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu-pa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Simulacion sim = new Simulacion();
        sim.createTables();

        AsambleistaRunnable aRunnable = new AsambleistaRunnable();

        Thread hilo = new Thread(aRunnable);

        hilo.start();
        hilo.join();

        List<Asambleista> asambleistas = entityManager.createQuery("SELECT a FROM Asambleista a", Asambleista.class)
                .getResultList();

        List<Thread> threads = new ArrayList<>();

        for (Asambleista asambleista : asambleistas) {
            Thread thread = new Thread(new VotosRunnable(asambleista));
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        sim.getDataRegion();
        sim.getDataVoto();

    }



}