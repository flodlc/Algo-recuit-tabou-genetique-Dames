/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.scene.control.Tab;

import java.util.Scanner;

/**
 * @author Florian
 */
public class Dame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int algo = 0;
        while (algo != 1 && algo!=2 && algo!=3) {
            System.out.println("Choisissez l'algorithme :");
            System.out.println("1 : Tabou     2 : Recuit Simulé     3 : Algo génétique");
            algo = sc.nextInt();
        }

        int taille = 0;
        while (taille < 1) {
            System.out.print("Entrez la taille du plateau à résoudre:");
            taille = sc.nextInt();
        }

        long start = System.currentTimeMillis();
        Plateau solution = null;
        switch (algo) {
            case 1 :
                Tabou tabou = new Tabou(taille);
                solution = tabou.start();
                break;
            case 2 :
                Recuit recuit = new Recuit(taille);
                solution = recuit.start();
                break;
            case 3 :
                AlgoGen algoGen = new AlgoGen(taille);
                solution = algoGen.start();
                break;
        }
        System.out.println(solution.getNbConflits());
        System.out.println("Run time : " + (System.currentTimeMillis() - start));
    }

}
