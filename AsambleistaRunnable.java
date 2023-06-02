package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

public class AsambleistaRunnable implements Runnable {
    private static final int NUM_INSERTS = 5; // Número de filas a insertar en cada hilo


    public void run() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/examenParcial1", "sa", "")) {
            String insertQuery = "INSERT INTO ASAMBLEISTA (REGION) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                Random random = new Random();
                for (int i = 0; i < NUM_INSERTS; i++) {
                    preparedStatement.setString(1, getRandomRegion(random));
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getRandomRegion(Random random) {
        String[] regions = { "N", "E", "P" };
        return regions[random.nextInt(regions.length)];
    }







}