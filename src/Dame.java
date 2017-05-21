/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Florian
 */
public class Dame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AlgoGen algo = new AlgoGen();
        Plateau solution = algo.start();
        System.out.println(solution.getNbConflits());
    }

}
