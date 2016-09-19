
import Duckhunt.HMMOfBirdSpecies;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Player {

    ArrayList<ArrayList<HMMOfBirdSpecies>> listHMMGuess;
    ArrayList<ArrayList<Integer>> listObs;
    
    int[] guess;
    // Parameters Initialization
    int nbSpecies = Constants.COUNT_SPECIES;
    int nbTypesObservations = Constants.COUNT_MOVE;
    
    
    double[][] matTrans2 = {{0.47, 0.53}, {0.52, 0.48}};
    double[][] matEmi2 = {{0.12, 0.10, 0.10, 0.10, 0.15, 0.8, 0.13, 0.12, 0.10}, {0.10, 0.15, 0.13, 0.12, 0.10, 0.8, 0.12, 0.10, 0.10}};
    double[] matPi2 = {0.53, 0.47};

    double[][] matTrans3 = {{0.37, 0.31, 0.33}, {0.32, 0.35, 0.33}, {0.35, 0.32, 0.33}};
    double[][] matEmi3 = {{0.12, 0.10, 0.10, 0.10, 0.13, 0.10, 0.13, 0.12, 0.10}, {0.10, 0.13, 0.13, 0.12, 0.10, 0.10, 0.12, 0.10, 0.10}, {0.13, 0.12, 0.11, 0.12, 0.10, 0.10, 0.12, 0.10, 0.10}};
    double[] matPi3 = {32, 33, 35};

    double[][] matTrans4 = {{0.24, 0.26, 0.25, 0.25}, {0.25, 0.26, 0.24, 0.25}, {0.23, 0.24, 0.26, 0.27}, {0.25, 0.24, 0.25, 0.26}};
    double[][] matEmi4 = {{0.12, 0.10, 0.10, 0.10, 0.13, 0.10, 0.13, 0.12, 0.10}, {0.10, 0.13, 0.13, 0.12, 0.10, 0.10, 0.12, 0.10, 0.10}, {0.13, 0.12, 0.11, 0.12, 0.10, 0.10, 0.12, 0.10, 0.10}, {0.13, 0.13, 0.11, 0.11, 0.10, 0.11, 0.11, 0.10, 0.10}};
    double[] matPi4 = {0.24, 0.22, 0.26, 0.28};

    public Player() {

        // HMM Initialization
        listHMMGuess = new ArrayList<>();
        for (int i = 0; i < nbSpecies; i++) {
            listHMMGuess.add(new ArrayList<HMMOfBirdSpecies>());
        }
        listObs = new ArrayList<>();
        for (int i = 0; i < nbSpecies; i++) {
            listObs.add(new ArrayList<Integer>());
        } 
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
        int firstStepShoot = 80;
        double ThresholdShootProb = 0.60;
        // If we don't have seen enough datas to make a shoot with confidence we don't shoot
        if (pState.getBird(0).getSeqLength() < firstStepShoot) {
            return new Action(-1, -1);
        }
        // If this this the first round
        if (pState.getRound() == 0) {

            double bestShootProb = Double.NEGATIVE_INFINITY;
            int bestShoot = -1;
            int idBirdToShoot = -1;

            // We are looking for the best shoot probability
            for (int idBird = 0; idBird < pState.getNumBirds(); idBird++) {

                Bird currentBird = pState.getBird(idBird);

                if (currentBird.isAlive()) {
                    // we get the information about the bird
                    double[][] observations = new double[currentBird.getSeqLength()][1];
                    for (int idObs = 0; idObs < currentBird.getSeqLength(); idObs++) {
                        observations[idObs][0] = currentBird.getObservation(idObs);
                    }
                    // We create a new HMM 
                    HMMOfBirdSpecies newHMMOfBirdSpecies = new HMMOfBirdSpecies(transitionMatrixInit(2), emissionMatrixInit(2, nbTypesObservations), piMatrixInit(2));
                    newHMMOfBirdSpecies.BaumWelchAlgorithm(observations, 100);
                    // We search for the mostLikely state sequence;
                    int[] statesSeq = newHMMOfBirdSpecies.MostLikelySequenceOfStates(observations);
                    // We get our current State
                    int currentState = statesSeq[statesSeq.length - 1];
                    double[] currentPi_t = newHMMOfBirdSpecies.transitionMatrix[currentState];
                    // We compute a vector which represent the probability of each observation to apperat at t+1
                    double[] nextObsDist = newHMMOfBirdSpecies.NextObsDistribution(currentPi_t);

                    // We search for the best shoot to do by maximizing the probability
                    double bestLocalShootProb = Double.NEGATIVE_INFINITY;
                    int bestLocalShoot = -1;
                    for (int idObs = 0; idObs < Constants.COUNT_MOVE; idObs++) {
                        if (nextObsDist[idObs] > bestLocalShootProb) {
                            bestLocalShootProb = nextObsDist[idObs];
                            bestLocalShoot = idObs;
                        }
                    }
                    // At this moment we have the best move with the best probability for the current bird
                    // We compare with the best shoot probability of the other birds
                    // We don't shoot if the bird will move righ or left because it might be a black stork
                    if (bestLocalShootProb > bestShootProb && bestLocalShoot != Constants.MOVE_RIGHT && bestLocalShoot != Constants.MOVE_LEFT) {
                        if (bestLocalShootProb >= ThresholdShootProb) {
                            bestShootProb = bestLocalShootProb;
                            bestShoot = bestLocalShoot;
                            idBirdToShoot = idBird;
                        }
                    }
                }

            }
            if (idBirdToShoot != -1) {
                System.err.println("shoot bird " + idBirdToShoot + "action " + bestShoot + " Prob " + bestShootProb);
            }
            return new Action(idBirdToShoot, bestShoot);
        }// If it's not the first round
        else {
            double bestShootProb = Double.NEGATIVE_INFINITY;
            int bestShoot = -1;
            int idBirdToShoot = -1;
            // Same thing we are looking for the best probability to shoot
            for (int idBird = 0; idBird < pState.getNumBirds(); idBird++) {
                Bird currentBird = pState.getBird(idBird);

                if (currentBird.isAlive()) {
                    double[][] observations = new double[currentBird.getSeqLength()][1];
                    for (int idObs = 0; idObs < currentBird.getSeqLength(); idObs++) {
                        observations[idObs][0] = currentBird.getObservation(idObs);
                    }

                    // Now we want to find its species and its best HMM associated
                    // To do that we just have to find the HMM which best maximize the likelihood of the observations
                    int currentSpecies = Constants.SPECIES_UNKNOWN;
                    double bestLikelihood = Double.NEGATIVE_INFINITY;
                    HMMOfBirdSpecies bestHMM = null;
                    for (int idSpecies = 0; idSpecies < Constants.COUNT_SPECIES; idSpecies++) {
                        // We have various HMM for a same species so we look at all this HMM
                        int nbHMM = listHMMGuess.get(idSpecies).size();
                        for (int idHMM = 0; idHMM < nbHMM; idHMM++) {
                            HMMOfBirdSpecies currentHMM = listHMMGuess.get(idSpecies).get(idHMM);
                            double localLikehood = currentHMM.SequenceLikelihood(observations);
                            if (localLikehood > bestLikelihood) {
                                bestLikelihood = localLikehood;
                                bestHMM = currentHMM;
                                currentSpecies = idSpecies;
                            }
                        }
                    }
                    // At this moment we have the bestHMM which maximize the likelihood of our observations
                    // We have also the species associated to this guess
                    // We don't want to shoot at a black stork
                    if (!(currentSpecies == Constants.SPECIES_BLACK_STORK) && bestLikelihood > -200) {
                        // Now we want to find the next observation which has the best probability to appear
                        // We search for the mostLikely state sequence;
                        int[] statesSeq = bestHMM.MostLikelySequenceOfStates(observations);
                        // We get our current State
                        int currentState = statesSeq[statesSeq.length - 1];
                        double[] currentPi_t = bestHMM.transitionMatrix[currentState];
                        // We compute a vector which represent the probability of each observation to apperat at t+1
                        double[] nextObsDist = bestHMM.NextObsDistribution(currentPi_t);

                        // We search for the best shoot to do by maximizing the probability
                        double bestLocalShootProb = Double.NEGATIVE_INFINITY;
                        int bestLocalShoot = -1;
                        for (int idObs = 0; idObs < Constants.COUNT_MOVE; idObs++) {
                            if (nextObsDist[idObs] > bestLocalShootProb) {
                                bestLocalShootProb = nextObsDist[idObs];
                                bestLocalShoot = idObs;
                            }
                        }

                        if (bestLocalShootProb > bestShootProb) {
                            if (bestLocalShootProb >= ThresholdShootProb) {
                                bestShootProb = bestLocalShootProb;
                                bestShoot = bestLocalShoot;
                                idBirdToShoot = idBird;
                            }
                        }

                    }
                }
            }
            if (idBirdToShoot != -1) {
                System.err.println("shoot bird " + idBirdToShoot + "action " + bestShoot + " Prob " + bestShootProb);
            }
            return new Action(idBirdToShoot, bestShoot);
        }

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

            double[][] observations = new double[currentBird.getSeqLength()][1];
            for (int j = 0; j < currentBird.getSeqLength(); j++) {
                if (currentBird.wasAlive(j)) {
                    observations[j][0] = currentBird.getObservation(j);
                }
            }

            for (int j = 0; j < nbSpecies; j++) {
                int nbHmm = listHMMGuess.get(j).size();
                for (int z = 0; z < nbHmm; z++) {
                    HMMOfBirdSpecies currentHMM = listHMMGuess.get(j).get(z);
                    double newLogProb = currentHMM.SequenceLikelihood(observations);
                    if (newLogProb > logProb && currentHMM.isTrained()) {
                        logProb = newLogProb;
                        species = j;
                    }
                }
            }
            System.err.println("Estimation for Bird number " + i + " " + speciesName(species) + " with p :" + logProb);
            lguess[i] = species;

        }
        guess = lguess.clone();
        return lguess;
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
        System.err.println("Bird num + " + pBird + " was hit");

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
        //("--REVELATION TIME---");
        int score = 0;
        ArrayList<ArrayList<Integer>> listObs = new ArrayList<>();
        for (int i = 0; i < nbSpecies; i++) {
            listObs.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < pSpecies.length; i++) {

            int currentSpecies = pSpecies[i];
            if (currentSpecies == guess[i]) {
                score++;
            }
            System.err.println("Bird num " + i + " : " + speciesName(currentSpecies));
            Bird currentBird = pState.getBird(i);

            for (int j = 0; j < currentBird.getSeqLength(); j++) {
                if (currentBird.wasAlive(j)) {
                    listObs.get(currentSpecies).add(currentBird.getObservation(j));
                }
            }

        }
        for (int i = 0; i < nbSpecies; i++) {
            int size = listObs.get(i).size();
            if (size >= 70) {

                double[][] observationsMatrix = new double[size][1];
                for (int z = 0; z < size; z++) {
                    observationsMatrix[z][0] = listObs.get(i).get(z);
                }
                int nbStates;
                int nbIterations = 300;

                nbStates = 3;

                HMMOfBirdSpecies newHMMOfBirdSpecies = new HMMOfBirdSpecies(transitionMatrixInit(nbStates), emissionMatrixInit(nbStates, nbTypesObservations), piMatrixInit(nbStates));
                newHMMOfBirdSpecies.BaumWelchAlgorithm(observationsMatrix, nbIterations);
                newHMMOfBirdSpecies.setTrained(true);
                listHMMGuess.get(i).add(newHMMOfBirdSpecies);

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
        switch (nbStates) {

            case 2:
                transitionMatrix = matTrans2.clone();
                for (int i = 0; i < nbStates; i++) {
                    transitionMatrix[i] = matTrans2[i].clone();
                }
                break;
            case 3:
                transitionMatrix = matTrans3.clone();
                for (int i = 0; i < nbStates; i++) {
                    transitionMatrix[i] = matTrans3[i].clone();
                }
                break;
            case 4:
                transitionMatrix = matTrans4.clone();
                for (int i = 0; i < nbStates; i++) {
                    transitionMatrix[i] = matTrans4[i].clone();
                }
                break;

        }
        return transitionMatrix;
    }

    public double[][] emissionMatrixInit(int nbStates, int nbTypesObservations) {
        double[][] emissionMatrix = new double[nbStates][nbTypesObservations];

        switch (nbStates) {

            case 2:
                emissionMatrix = matEmi2.clone();
                for (int i = 0; i < nbStates; i++) {
                    emissionMatrix[i] = matEmi2[i].clone();
                }
                break;
            case 3:
                emissionMatrix = matEmi3.clone();
                for (int i = 0; i < nbStates; i++) {
                    emissionMatrix[i] = matEmi3[i].clone();
                }
                break;
            case 4:
                emissionMatrix = matEmi4.clone();
                for (int i = 0; i < nbStates; i++) {
                    emissionMatrix[i] = matEmi4[i].clone();
                }
                break;

        }

        return emissionMatrix;
    }

    public double[] piMatrixInit(int nbStates) {
        double[] piMatrix = new double[nbStates];

        switch (nbStates) {

            case 2:
                piMatrix = matPi2.clone();
                break;
            case 3:
                piMatrix = matPi3.clone();
                break;
            case 4:
                piMatrix = matPi4.clone();
                break;

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
