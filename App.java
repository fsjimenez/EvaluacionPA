package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception{

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu-pa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

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

        getDataRegion();
        getDataVoto();

    }

    public static void getDataVoto() throws SQLException {
        Connection con = getConnection();
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
                                "Número de 'ABSTENCION': %s\n",
                        rs.getString("COUNT_S"),
                        rs.getString("COUNT_N"),
                        rs.getString("COUNT_A"));
            }

        }
    }

    public static void getDataRegion() throws SQLException{
        Connection con = getConnection();
        String selectN = """ 
                        SELECT
                        REGION,
                        SUM(CASE WHEN TIPO = 'S' THEN 1 ELSE 0 END) AS SI,
                        SUM(CASE WHEN TIPO = 'N' THEN 1 ELSE 0 END) AS NO,
                        SUM(CASE WHEN TIPO = 'A' THEN 1 ELSE 0 END) AS ABSTENCIÓN
                        FROM ASAMBLEISTA
                        JOIN VOTO ON ASAMBLEISTA.ID_ASAMBLEISTA = VOTO.ID_ASAMBLEISTA
                        GROUP BY REGION;        
                         """;
        System.out.println("REGION\tSI\tNO\tABS");

        int totalSi = 0;
        int totalNo = 0;
        int totalAbstencion = 0;

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(selectN)
        ) {
            while (rs.next()) {
                String region = rs.getString("REGION");
                int si = rs.getInt("SI");
                int no = rs.getInt("NO");
                int abstencion = rs.getInt("ABSTENCIÓN");

                System.out.println(region + "\t\t" + si + "\t" + no + "\t" + abstencion);

                totalSi += si;
                totalNo += no;
                totalAbstencion += abstencion;
            }
        }
        System.out.println("TOTAL\t" + totalSi + "\t" + totalNo + "\t" + totalAbstencion + "\n");
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection("jdbc:h2:~/examenParcial1", "sa", "");
    }




}