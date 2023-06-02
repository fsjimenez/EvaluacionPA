package org.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class VotosRunnable implements Runnable{

    private final Asambleista asambleista;
    private final VotosController insercion = new VotosController();

    private static final int TOTAL_ASAMBLEISTAS = 5;
    private volatile static AtomicInteger contador = new AtomicInteger(0);

    public VotosRunnable(Asambleista asambleistas) {
        this.asambleista = asambleistas;
    }


    public void run() {

        //long delay = (long) (Math.random() * MAX_DELAY);
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.getStackTrace();
        }

        boolean resultado = insercion.addData(asambleista, getTipo());

        if (resultado) {
            System.out.println("El voto se ha añadido correctamente.");
            contador.incrementAndGet();
        } else {
            System.out.println("No se pudo añadir el voto.");
        }

    }

    public String getTipo(){
        Random random = new Random();
        String tipoVoto;
        int randomNumber = random.nextInt(100) + 1;
        if (randomNumber >= 1 && randomNumber <= 24) {
            tipoVoto = "S";
        } else if (randomNumber >= 25 && randomNumber <= 49) {
            tipoVoto = "N";
        } else {
            tipoVoto = "A";
        }
        return tipoVoto;
    }

}
