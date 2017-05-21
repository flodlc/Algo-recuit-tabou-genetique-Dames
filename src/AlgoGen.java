import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Florian on 21/05/2017.
 */
public class AlgoGen {

    private Random random;
    private int nbIndividuSelectionne;
    private double probaMutation;
    private int nbIterations;
    private int sizePlateau;

    private Plateau bestPlateau;

    public AlgoGen() {
        random = new Random();
        sizePlateau = 100;
        nbIndividuSelectionne = 30;
        probaMutation = 0.2;
        nbIterations = 10000;
        bestPlateau = new Plateau(Plateau.getInitialPlateau(sizePlateau));
    }

    private List<Plateau> createInitialPopulation(int size) {
        List<Plateau> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int[] plateauArray = new int[sizePlateau];
            for (int j = 0; j < sizePlateau; j++) {
                plateauArray[j] = (j + i) % sizePlateau;
            }
            Plateau plateau = new Plateau(plateauArray);
            if (plateau.getNbConflits() < bestPlateau.getNbConflits()) {
                bestPlateau = plateau;
            }
            population.add(plateau);
        }
        return population;
    }

    public Plateau start() {
        List<Plateau> population = createInitialPopulation(100);
        for (int i = 0; i < nbIterations; i++) {
            population = nextGeneration(population);
            if (bestPlateau.getNbConflits() == 0) return bestPlateau;
        }
        return bestPlateau;
    }

    private List<Plateau> selectPopulation(List<Plateau> population, int nb) {
        List<Plateau> selectedPopulation = new ArrayList<>();
        Random random = new Random();
        Double finesseInverseTotale = 0.0;
        for (Plateau plateau : population) {
            finesseInverseTotale += 1.0 / plateau.getNbConflits();
        }
        double randomDouble = (random.nextDouble() * finesseInverseTotale) / nb;

        for (int i = 0; i < nb; i++) {
            double randomDoubleMoved = randomDouble + (finesseInverseTotale / nb) * i;
            Plateau selectedPlateau = null;

            int j = 0;
            double somme = 0.0;
            while (somme < randomDoubleMoved) {
                selectedPlateau = population.get(j);
                somme += 1.0 / selectedPlateau.getNbConflits();
                j++;
            }
            selectedPopulation.add(selectedPlateau);
        }
        for (Plateau plateau : selectedPopulation) {
            population.remove(plateau);
        }
        return selectedPopulation;
    }


    private List<Plateau> nextGeneration(List<Plateau> population) {
        List<Plateau> newGeneration = new ArrayList<>();
        List<Plateau> selectedPopulation = selectPopulation(population, nbIndividuSelectionne);

        for (int i = 0; i < selectedPopulation.size(); i += 2) {
            Plateau plateau1 = selectedPopulation.get(i);
            Plateau plateau2 = selectedPopulation.get(i + 1);
            Plateau[] enfants = croisePlateaux(plateau1, plateau2);
            for (Plateau enfant : enfants) {
                if (enfant.getNbConflits() < bestPlateau.getNbConflits()) {
                    bestPlateau = enfant;
                    System.out.println(bestPlateau.getNbConflits());
                }
                newGeneration.add(enfant);
            }
            newGeneration.add(plateau1);
            newGeneration.add(plateau2);
        }

        for (Plateau plateau : population) {
            if (newGeneration.size() >= 100) break;

            //Mutation d'un individu selon la probabilité choisie, si mutation on choisit un voisin
            double randomDouble = random.nextDouble();
            if (randomDouble < probaMutation) {
                int v = plateau.getNbConflits();
                plateau = Plateau.getVoisin(plateau.getArrayPosition());
                if (plateau.getNbConflits() < bestPlateau.getNbConflits()) {
                    bestPlateau = plateau;
                    System.out.println(bestPlateau.getNbConflits());
                }
            }

            newGeneration.add(plateau);
        }
        return newGeneration;
    }


    private Plateau[] croisePlateaux(Plateau plateau1, Plateau plateau2) {
        Plateau[] enfants = new Plateau[2];
        Plateau enfant1, enfant2;
        Random random = new Random();


        int rnd = random.nextInt(sizePlateau - 1);
        int[] enfant1Array = new int[sizePlateau];
        int[] enfant2Array = new int[sizePlateau];

        //Découpe des parents
        int[] plateau1part1 = Arrays.copyOfRange(plateau1.getArrayPosition(), 0, rnd + 1);
        int[] plateau1part2 = Arrays.copyOfRange(plateau1.getArrayPosition(), rnd + 1, sizePlateau);
        int[] plateau2part1 = Arrays.copyOfRange(plateau2.getArrayPosition(), 0, rnd + 1);
        int[] plateau2part2 = Arrays.copyOfRange(plateau2.getArrayPosition(), rnd + 1, sizePlateau);

        //Fusion des 2 parties pour le premier enfant
        System.arraycopy(plateau1part1, 0, enfant1Array, 0, rnd + 1);
        System.arraycopy(plateau2part2, 0, enfant1Array, rnd + 1, sizePlateau - rnd - 1);

        //Fusion des 2 parties pour le deuxième enfant
        System.arraycopy(plateau2part1, 0, enfant2Array, 0, rnd + 1);
        System.arraycopy(plateau1part2, 0, enfant2Array, rnd + 1, sizePlateau - rnd - 1);

        enfant1 = new Plateau(enfant1Array);
        enfant2 = new Plateau(enfant2Array);
        enfants[0] = enfant1;
        enfants[1] = enfant2;

        return enfants;
    }
}
