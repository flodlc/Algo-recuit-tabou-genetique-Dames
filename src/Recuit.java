/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Random;

/**
 * @author Florian
 */
public class Recuit {

    private Double temperature;
    private int n1;
    private int n2;
    private Plateau currentPlateau;
    private Plateau bestPlateau;

    public Recuit() {
        currentPlateau = new Plateau(Plateau.getInitialPlateau(200));
        bestPlateau = new Plateau(currentPlateau.getArrayPosition().clone(), currentPlateau.getNbConflits());
        temperature = 5.0;
        n1 = 2500;
        n2 = 50;
    }

    public Plateau start() {
        int delta;
        Random rnd = new Random();
        Double p;

        for (int k = 0; k < n1; k++) {
            for (int l = 0; l < n2; l++) {
                Plateau plateauVoisin = Plateau.getVoisin(currentPlateau.getArrayPosition());
                delta = plateauVoisin.getNbConflits() - currentPlateau.getNbConflits();
                if (delta <= 0) {
                    currentPlateau = plateauVoisin;
                    if (plateauVoisin.getNbConflits() < bestPlateau.getNbConflits()) {
                        bestPlateau = new Plateau(plateauVoisin.getArrayPosition());
                        if (bestPlateau.getNbConflits() == 0) {
                            return bestPlateau;
                        }
                    }
                } else {
                    p = rnd.nextDouble();
                    if (p <= Math.exp(-delta / temperature)) {
                        currentPlateau = plateauVoisin;
                    }
                }
            }
            System.out.println(bestPlateau.getNbConflits());
            temperature = temperature * 0.98;
        }
        return bestPlateau;
    }
}
