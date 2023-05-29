package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

public class App {
    public static void main(String[] args) throws Exception{

        Connection conn =
                DriverManager.getConnection("jdbc:h2:~/examenParcial1", "sa", "");


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu-pa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<Asambleista> asambleistas = entityManager.createQuery("SELECT a FROM Asambleista a", Asambleista.class)
                .getResultList();

        Runnable runnable = new AsambleistaRunnable(asambleistas);

        for (int i = 0; i < asambleistas.size(); i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            thread.join();
        }

        getDataRegion(conn);
        getDataVoto(conn);

    }

    private static void getDataVoto(Connection con) throws SQLException {
        String select = """ 
                SELECT
                    (SELECT COUNT(*) FROM VOTO WHERE TIPO = 'N') AS count_n,
                    (SELECT COUNT(*) FROM VOTO WHERE TIPO = 'S') AS count_s,
                    (SELECT COUNT(*) FROM VOTO WHERE TIPO = 'A') AS count_a;
                         """;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(select)
        ) {
            while(rs.next()){
                System.out.printf("Número de 'SI': %s\n" +
                                "Número de 'NO': %s\n" +
                                "Número de 'ABSTENCION': %s\n\n",
                        rs.getString("COUNT_N"),
                        rs.getString("COUNT_S"),
                        rs.getString("COUNT_A"));
            }

        }
    }

    private static void getDataRegion(Connection con) throws SQLException{
        String selectN = """ 
                SELECT
                    (SELECT COUNT(*) FROM ASAMBLEISTA WHERE region = 'N') AS count_n,
                    (SELECT COUNT(*) FROM ASAMBLEISTA WHERE region = 'E') AS count_e,
                    (SELECT COUNT(*) FROM ASAMBLEISTA WHERE region = 'P') AS count_p;
                         """;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(selectN)
        ) {
            while(rs.next()){
                System.out.printf("Asambleista/s nacionales: %s\n" +
                                "Asambleista/s extranjeros: %s\n" +
                                "Asambleista/s provinciales: %s\n\n",
                        rs.getString("COUNT_N"),
                        rs.getString("COUNT_E"),
                        rs.getString("COUNT_P"));
            }

        }
    }

}