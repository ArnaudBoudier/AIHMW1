
import Duckhunt.HMMOfBirdSpecies;
import java.util.Random;
import java.util.Scanner;

class Player {

    HMMOfBirdSpecies HMMPigeon;
    HMMOfBirdSpecies HMMRaven;
    HMMOfBirdSpecies HMMSkylark;
    HMMOfBirdSpecies HMMSwallow;
    HMMOfBirdSpecies HMMSnipe;
    HMMOfBirdSpecies HMMBlackStork;

    HMMOfBirdSpecies[] listHMM;
    int[] guess;

    // Parameters Initialization
    int nbSpecies = 6;
    int nbStates = 5;
    int nbTypesObservations = 9;

    public Player() {

        // HMM Initialization
        listHMM = new HMMOfBirdSpecies[nbSpecies];

        double[][] emmissionMatrix = emissionMatrixInit(nbStates, nbTypesObservations);
        double[][] transitionMatrix = transitionMatrixInit(nbStates);
        double[] piMatrix = piMatrixInit(nbStates);
        HMMPigeon = new HMMOfBirdSpecies(transitionMatrix, emmissionMatrix, piMatrix);
        listHMM[0] = HMMPigeon;

        emmissionMatrix = emissionMatrixInit(nbStates, nbTypesObservations);
        transitionMatrix = transitionMatrixInit(nbStates);
        piMatrix = piMatrixInit(nbStates);
        HMMRaven = new HMMOfBirdSpecies(transitionMatrix, emmissionMatrix, piMatrix);
        listHMM[1] = HMMRaven;

        emmissionMatrix = emissionMatrixInit(nbStates, nbTypesObservations);
        transitionMatrix = transitionMatrixInit(nbStates);
        piMatrix = piMatrixInit(nbStates);
        HMMSkylark = new HMMOfBirdSpecies(transitionMatrix, emmissionMatrix, piMatrix);
        listHMM[2] = HMMSkylark;

        emmissionMatrix = emissionMatrixInit(nbStates, nbTypesObservations);
        transitionMatrix = transitionMatrixInit(nbStates);
        piMatrix = piMatrixInit(nbStates);
        HMMSwallow = new HMMOfBirdSpecies(transitionMatrix, emmissionMatrix, piMatrix);
        listHMM[3] = HMMSwallow;

        emmissionMatrix = emissionMatrixInit(nbStates, nbTypesObservations);
        transitionMatrix = transitionMatrixInit(nbStates);
        piMatrix = piMatrixInit(nbStates);
        HMMSnipe = new HMMOfBirdSpecies(transitionMatrix, emmissionMatrix, piMatrix);
        listHMM[4] = HMMSnipe;

        emmissionMatrix = emissionMatrixInit(nbStates, nbTypesObservations);
        transitionMatrix = transitionMatrixInit(nbStates);
        piMatrix = piMatrixInit(nbStates);
        HMMBlackStork = new HMMOfBirdSpecies(transitionMatrix, emmissionMatrix, piMatrix);
        listHMM[5] = HMMBlackStork;
    }

    /**
     * Shoot!
     *
     * This is the function where you start your work.
     *
     * You will receive a variable pState, which contains information about all
     * birds, both dead and alive. Each bird contains all past moves.
     *
     * The state also contains the scores for all players and the number of time
     * steps elapsed since the last time this function was called.
     *
     * @param pState the GameState object with observations etc
     * @param pDue time before which we must have returned
     * @return the prediction of a bird we want to shoot at, or cDontShoot to
     * pass
     */
    public Action shoot(GameState pState, Deadline pDue) {
        guess = new int[pState.getNumBirds()];

        for (int numBird = 0; numBird < pState.getNumBirds(); numBird++) {
            Bird bird = pState.getBird(numBird);
            if (bird.getSeqLength() < 90) {
                break;
            }

            if (bird.isAlive()) {
                // we get the information about the bird
                double[][] observations = new double[bird.getSeqLength()][1];
                for (int idObs = 0; idObs < bird.getSeqLength(); idObs++) {
                    observations[idObs][0] = bird.getObservation(idObs);
                }

                // Search for best model wich best fit with the Informations
                double logProb = Double.NEGATIVE_INFINITY;
                int species = 0;
                for (int j = 0; j < nbSpecies; j++) {
                    double newLogProb = listHMM[j].SequenceLikelihood(observations);
                    if (newLogProb > logProb) {
                        logProb = newLogProb;
                        species = j;
                    }
                }
                // If species is a black Stork we do not shoot
                if (species == Constants.SPECIES_BLACK_STORK) {
                    break;
                }

                // We search for the mostLikely state sequence;
                // int[] stateSeq = listHMM[species].MostLikelySequenceOfStates(observations);
                // int currentState = stateSeq[stateSeq.length - 1];
                // When we have the best species we want to know the current Pi_t
                double[] currentPi_t = listHMM[species].pi;//listHMM[species].transitionMatrix[currentState];
                double[][] currentPi_t_Matrix = new double[1][currentPi_t.length];

                currentPi_t_Matrix[0] = currentPi_t.clone();
                for (int i = 0; i < bird.getSeqLength() - 1; i++) {
                    currentPi_t_Matrix = listHMM[species].multiplyByMatrix(currentPi_t_Matrix, listHMM[species].transitionMatrix);
                    System.err.println("Result t " + i);
                    Matrix.printVector(currentPi_t_Matrix[0]);
                }
             
                // We compute a vector which represent the probability of each observation to apperat at t
                double[] nextObsDist = listHMM[species].NextObsDistribution(currentPi_t_Matrix[0]);

                // We search the max of this probability
                int index = 0;
                double maxProb = 0;
                int maxIndex = 0;
                while (index < nbTypesObservations) {
                    if (nextObsDist[index] > maxProb) {
                        maxProb = nextObsDist[index];
                        maxIndex = index;
                    }
                    index++;
                }
                //   System.err.println("Max prob " + maxProb);
                // If prob > 0.60 we choose to shoot
                if (maxProb >= 0.45) {
                    guess[numBird] = species;
                    System.err.println("shoot bird " + numBird + " action : " + maxIndex + " Prob " + maxProb);
                    return new Action(numBird, maxIndex);

                }
            }
        }

        return cDontShoot;

        // This line would predict that bird 0 will move right and shoot at it.
        // return new Action(0, Constants.MOVE_RIGHT);
    }

    /**
     * Guess the species! This function will be called at the end of each round,
     * to give you a chance to identify the species of the birds for extra
     * points.
     *
     * Fill the vector with guesses for the all birds. Use SPECIES_UNKNOWN to
     * avoid guessing.
     *
     * @param pState the GameState object with observations etc
     * @param pDue time before which we must have returned
     * @return a vector with guesses for all the birds
     */
    public int[] guess(GameState pState, Deadline pDue) {
        /*
         * Here you should write your clever algorithms to guess the species of
         * each bird. This skeleton makes no guesses, better safe than sorry!
         */

        double logProb;
        int[] lguess = new int[pState.getNumBirds()];
        int species;
        for (int i = 0; i < pState.getNumBirds(); ++i) {
            species = 0;
            logProb = Double.NEGATIVE_INFINITY;
            Bird currentBird = pState.getBird(i);
            if (currentBird.isAlive()) {
                double[][] observations = new double[currentBird.getSeqLength()][1];
                for (int j = 0; j < currentBird.getSeqLength(); j++) {
                    observations[j][0] = currentBird.getObservation(j);
                }

                for (int j = 0; j < 6; j++) {
                    double newLogProb = listHMM[j].SequenceLikelihood(observations);
                    if (newLogProb > logProb) {
                        logProb = newLogProb;
                        species = j;
                    }
                }
                System.err.println("Estimation for Bird number " + i + " " + speciesName(species) + " with p :" + logProb);
                lguess[i] = species;
                guess[i] = lguess[i];
            } else {
                lguess[i] = guess[i];
            }
        }

        return guess;
    }

    /**
     * If you hit the bird you were trying to shoot, you will be notified
     * through this function.
     *
     * @param pState the GameState object with observations etc
     * @param pBird the bird you hit
     * @param pDue time before which we must have returned
     */
    public void hit(GameState pState, int pBird, Deadline pDue) {
        System.err.println("Bird num + " + pBird + " was hit, species hypothese : " + speciesName(guess[pBird]));

    }

    /**
     * If you made any guesses, you will find out the true species of those
     * birds through this function.
     *
     * @param pState the GameState object with observations etc
     * @param pSpecies the vector with species
     * @param pDue time before which we must have returned
     */
    public void reveal(GameState pState, int[] pSpecies, Deadline pDue) {
        System.err.println("--REVELATION TIME---");
        int score = 0;
        for (int i = 0; i < pSpecies.length; i++) {

            int currentSpecies = pSpecies[i];
            if (currentSpecies == guess[i]) {
                score++;
            }
            if (currentSpecies < 0) {
                break;
            }
            Bird currentBird = pState.getBird(i);
            System.err.println("Bird num " + i + " : " + speciesName(currentSpecies));
            if (currentBird.isAlive()) {

                double[][] observations = new double[currentBird.getSeqLength()][1];
                for (int j = 0; j < currentBird.getSeqLength(); j++) {
                    observations[j][0] = currentBird.getObservation(j);
                }
                if (!listHMM[currentSpecies].isTrained()) {
                    System.err.println("Train model for " + speciesName(currentSpecies) + " nb " + i);
                    listHMM[currentSpecies].BaumWelchAlgorithm(observations, 50);
                }
            }
        }

        System.err.println("Result : " + score + "/" + pSpecies.length);
    }

    public static double randRange(double min, double max) {
        Random r = new Random();
        return min + r.nextFloat() * (max - min);
    }

    public double[][] transitionMatrixInit(int nbStates) {
        double[][] transitionMatrix = new double[nbStates][nbStates];
        for (int i = 0; i < nbStates; i++) {
            for (int j = 0; j < nbStates; j++) {
                transitionMatrix[i][j] = 0.2;
                if (j % 2 == 0 && j < nbStates - 1) {
                    transitionMatrix[i][j] = transitionMatrix[i][j] - 0.05;
                } else if (j % 2 != 0) {
                    transitionMatrix[i][j] = transitionMatrix[i][j] + 0.05;
                }

            }
        }
        return transitionMatrix;
    }

    public double[][] emissionMatrixInit(int nbStates, int nbTypesObservations) {
        double[][] emissionMatrix = new double[nbStates][nbTypesObservations];
        for (int i = 0; i < nbStates; i++) {
            for (int j = 0; j < nbTypesObservations; j++) {
                emissionMatrix[i][j] = 0.11111111;
                if (j % 2 == 0 && j < nbTypesObservations - 1) {
                    emissionMatrix[i][j] = emissionMatrix[i][j] - 0.05;
                } else if (j % 2 != 0) {
                    emissionMatrix[i][j] = emissionMatrix[i][j] + 0.05;
                }

            }
        }
        return emissionMatrix;
    }

    public double[] piMatrixInit(int nbStates) {
        double[] piMatrix = new double[nbStates];
        for (int i = 0; i < nbStates; i++) {
            piMatrix[i] = 0.2;
            if (i % 2 == 0 && i < nbStates - 1) {
                piMatrix[i] = piMatrix[i] - 0.05;
            } else if (i % 2 != 0) {
                piMatrix[i] = piMatrix[i] + 0.05;
            }
        }
        return piMatrix;
    }

    public String speciesName(int i) {
        switch (i) {
            case 0:
                return "Pigeon";
            case 1:
                return "Raven";
            case 2:
                return "Skylark";
            case 3:
                return "Swallow";
            case 4:
                return "Snipe";
            case 5:
                return "Black Stork";
            case 6:
                break;
            default:
                return "unknown";
        }
        return " ";
    }

    public static final Action cDontShoot = new Action(-1, -1);
}
