/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Duckhunt;

import java.util.ArrayList;

/**
 *
 * @author Arnaud
 */
public class HMMOfBirdSpecies {

    public double[][] transitionMatrix;
    public double[][] emissionMatrix;
    public double[] pi;

    int nbStates;
    // This refers to the 9 possible moves we can observe
    // Up, down, left, right, up-left, up-right, down-left, down-right + move stoped
    int nbTypesObservations;

    public boolean trained = false;

    public HMMOfBirdSpecies(double[][] transitionMatrix, double[][] emmissionMatrix, double[] pi) {
        this.transitionMatrix = transitionMatrix;
        this.emissionMatrix = emmissionMatrix;
        this.pi = pi;
        this.nbStates = emmissionMatrix.length;
        this.nbTypesObservations = emmissionMatrix[0].length;

    }

    public void setTrained(boolean trained) {
        this.trained = trained;
    }

    // Return a vector contening the next observation distribution 
    public double[] NextObsDistribution(double[] pi_T) {

        // Conversion of PI in matrix for computation purposes
        double[][] piMatrix = new double[1][nbStates];
        for (int i = 0; i < nbStates; i++) {
            piMatrix[0][i] = pi_T[i];
        }
        double[][] abProduct = multiplyByMatrix(transitionMatrix, emissionMatrix);
        double[][] emissionProb = multiplyByMatrix(piMatrix, abProduct);

        return emissionProb[0];

    }

    public boolean isTrained() {
        return trained;
    }

    // Return the probability of observe a sequence given our HMM object
    public double SequenceLikelihood(double[][] observations) {

        int nbObservations = observations.length;
        ArrayList<double[]> listAlpha = new ArrayList<>();
        double[] alphas = new double[nbStates];

        //double oldProb = Math.
        // Compute alpha 0;
        double c0 = 0;
        for (int i = 0; i < nbStates; i++) {
            alphas[i] = pi[i] * emissionMatrix[i][(int) observations[0][0]];
            c0 += alphas[i];
        }

        // Scale alpha0
        c0 = 1 / c0;
        for (int i = 0; i < nbStates; i++) {
            alphas[i] = alphas[i] * c0;
        }

        listAlpha.add(alphas);
        // Compute of alpha_t(i)
        // Initialisation part
        double[] alphas_tm1 = new double[nbStates];
        for (int i = 0; i < nbStates; i++) {
            alphas_tm1[i] = alphas[i];
        }
        double[] alphas_t = new double[nbStates];
        double[] ctValues = new double[nbObservations];
        ctValues[0] = c0;
        // Calcul parts
        for (int t = 1; t < nbObservations; t++) {
            double ct = 0;
            double observation_T = observations[t][0];
            for (int i = 0; i < nbStates; i++) {
                alphas_t[i] = 0;
                for (int j = 0; j < nbStates; j++) {
                    alphas_t[i] += (alphas_tm1[j] * transitionMatrix[j][i]);
                }
                alphas_t[i] *= emissionMatrix[i][(int) observation_T];
                ct += alphas_t[i];
            }
            ct = 1 / ct;
            ctValues[t] = ct;
            for (int i = 0; i < nbStates; i++) {
                alphas_t[i] = alphas_t[i] * ct;
            }
            // t_m1 = t for next iteration
            for (int i = 0; i < nbStates; i++) {
                alphas_tm1[i] = alphas_t[i];
            }
            listAlpha.add(alphas);
        }

        // Calcul of P(O\lambda)
        double logProb = 0;
        for (int t = 0; t < nbObservations; t++) {
            logProb += Math.log(ctValues[t]);
        }
        logProb = -logProb;
        return logProb;
        // double resultat = Math.exp(logProb);
        // return resultat;

    }

    // BaumWelchAlgorithm 
    public boolean BaumWelchAlgorithm(double[][] observations, int iterMax) {

        // Initialization of parameters
        int nbObservations = observations.length;
        int nbTypeObservations = emissionMatrix[0].length;

        // Begin of Baum Welch Algorithm
        int iters = 0;

        double oldLogProb = Double.NEGATIVE_INFINITY;
        double logProb = 0;

        while (iters < iterMax) {

            ///////// FORWARD ALGORITHM //////////////
            double[] alphas = new double[nbStates];
            ArrayList<double[]> listAlpha = new ArrayList<>();

            // Compute alpha 0;
            double c0 = 0;
            for (int i = 0; i < nbStates; i++) {
                alphas[i] = pi[i] * emissionMatrix[i][(int) observations[0][0]];
                c0 += alphas[i];
            }

            // Scale alpha0
            c0 = 1 / c0;
            for (int i = 0; i < nbStates; i++) {
                alphas[i] *= c0;
            }

            listAlpha.add(alphas.clone());
            // Compute of alpha_t(i)
            // Initialisation part
            double[] alphas_tm1 = new double[nbStates];
            for (int i = 0; i < nbStates; i++) {
                alphas_tm1[i] = alphas[i];
            }
            double[] alphas_t = new double[nbStates];
            double[] ctValues = new double[nbObservations];
            ctValues[0] = c0;
            // computation parts

            for (int t = 1; t < nbObservations; t++) {
                double ct = 0;
                double observation_T = observations[t][0];
                for (int i = 0; i < nbStates; i++) {
                    alphas_t[i] = 0;
                    for (int j = 0; j < nbStates; j++) {
                        alphas_t[i] += (alphas_tm1[j] * transitionMatrix[j][i]);
                    }

                    alphas_t[i] *= emissionMatrix[i][(int) observation_T];
                    ct += alphas_t[i];
                }
                ct = 1 / ct;
                ctValues[t] = ct;
                for (int i = 0; i < nbStates; i++) {
                    alphas_t[i] *= ct;

                }
                // t_m1 = t for next iteration
                for (int i = 0; i < nbStates; i++) {
                    alphas_tm1[i] = alphas_t[i];

                }
                listAlpha.add(alphas_t.clone());
            }

            ///////// END FORWARD ALGORITHM //////////////
            ///////// BEGIN BACKWARD ALGORITHM //////////////
            ArrayList<double[]> listBetas = new ArrayList<>();

            double[] betas_tP1 = new double[nbStates];
            double[] betas_t = new double[nbStates];

            // Initialization
            for (int i = 0; i < nbStates; i++) {
                betas_tP1[i] = ctValues[nbObservations - 1];
            }
            listBetas.add(0, betas_tP1.clone());
            // Beta Pass
            for (int t = nbObservations - 2; t >= 0; t--) {
                double observation_TP1 = observations[t + 1][0];
                for (int i = 0; i < nbStates; i++) {
                    betas_t[i] = 0;
                    for (int j = 0; j < nbStates; j++) {
                        betas_t[i] += (transitionMatrix[i][j] * emissionMatrix[j][(int) observation_TP1] * betas_tP1[j]);
                    }
                    betas_t[i] *= ctValues[t];
                }
                // t_P1 = t for next iteration
                for (int i = 0; i < nbStates; i++) {
                    betas_tP1[i] = betas_t[i];
                }
                listBetas.add(0, betas_t.clone());
            }

            ///////// END BACKWARD ALGORITHM //////////////
            ///////// BEGIN COMPUTATION OF DIGAMA  AND GAMA ALGORITHM //////////////
            ArrayList<double[]> listGama = new ArrayList<>();
            ArrayList<double[][]> listDiGama = new ArrayList<>();

            double[] gama_t = new double[nbStates];
            double[][] digama_t = new double[nbStates][nbStates];

            for (int t = 0; t < nbObservations - 1; t++) {
                double denom = 0;
                double ob_TP1 = observations[t + 1][0];
                for (int i = 0; i < nbStates; i++) {
                    for (int j = 0; j < nbStates; j++) {
                        denom += (listAlpha.get(t)[i] * transitionMatrix[i][j] * emissionMatrix[j][(int) ob_TP1] * listBetas.get(t + 1)[j]);
                    }
                }

                for (int i = 0; i < nbStates; i++) {
                    gama_t[i] = 0;
                    for (int j = 0; j < nbStates; j++) {
                        digama_t[i][j] = (listAlpha.get(t)[i] * transitionMatrix[i][j] * emissionMatrix[j][(int) ob_TP1] * listBetas.get(t + 1)[j]) / denom;
                        gama_t[i] += digama_t[i][j];
                    }
                }

                double[][] diGamaCopy = new double[digama_t.length][];
                for (int i = 0; i < digama_t.length; i++) {
                    diGamaCopy[i] = digama_t[i].clone();
                }
                listDiGama.add(diGamaCopy);
                listGama.add(gama_t.clone());
            }

            // Special case for gama T-1
            double denomGama = 0;
            for (int i = 0; i < nbStates; i++) {
                denomGama += listAlpha.get(nbObservations - 1)[i];
            }
            for (int i = 0; i < nbStates; i++) {
                gama_t[i] = listAlpha.get(nbObservations - 1)[i] / denomGama;
            }
            listGama.add(gama_t.clone());

            /////// Calcul of P(O\lambda)
            logProb = 0;
            for (int t = 0; t < nbObservations; t++) {
                logProb += Math.log(ctValues[t]);
            }
            logProb = -logProb;
            // If we have reached the convergence point or iter == iterMAx
            iters++;
            if (oldLogProb >= logProb || iters == iterMax) {
                if (iters == iterMax) {
                    System.err.println("No convergence, step value " + iters);
                    return false;
                } else {
                    System.err.println(" Convergence, step value " + iters);
                    return true;
                }

            }
            oldLogProb = logProb;

            // RE estimation de PI
            for (int i = 0; i < nbStates; i++) {
                pi[i] = listGama.get(0)[i];
            }
            // Re estimation de A   
            double numer = 0;
            double denom = 0;
            for (int i = 0; i < nbStates; i++) {
                for (int j = 0; j < nbStates; j++) {
                    numer = 0;
                    denom = 0;
                    for (int t = 0; t < nbObservations - 1; t++) {
                        numer += listDiGama.get(t)[i][j];
                        denom += listGama.get(t)[i];
                    }
                    transitionMatrix[i][j] = numer / denom;
                }
            }

            // Re estimation de B   
            for (int i = 0; i < nbStates; i++) {
                for (int j = 0; j < nbTypeObservations; j++) {
                    numer = 0;
                    denom = 0;
                    for (int t = 0; t < nbObservations; t++) {
                        double curr_obs = observations[t][0];
                        if ((int) curr_obs == j) {
                            numer += listGama.get(t)[i];
                        }
                        denom += listGama.get(t)[i];
                    }
                    emissionMatrix[i][j] = numer / denom;
                }
            }

        }
        return false;
    }

    public int[] MostLikelySequenceOfStates(double[][] observations) {
        int observationValue = (int) observations[0][0];
        int nbObservations = observations.length;
        // Initialize delta
        double[] delta = vectorialProduct(getCol(emissionMatrix, observationValue), pi);
        for (int i = 0; i < delta.length; i++) {
            delta[i] = Math.log(delta[i]);
        }
        int[][] deltaIndex = new int[nbStates][nbObservations];

        // Initialize final result
        int[] finalStateSequence = new int[nbObservations];

        int finalState = deltaAlgorithm(1, delta, deltaIndex, observations);

        finalStateSequence[nbObservations - 1] = finalState;

        for (int t = nbObservations - 2; t >= 0; t--) {
            finalStateSequence[t] = deltaIndex[finalStateSequence[t + 1]][t + 1];
        }
        /*
        for (int i = 0; i < finalStateSequence.length; i++) {
            System.out.print(finalStateSequence[i] + " ");
        }
         */
        return finalStateSequence;
    }

    public int deltaAlgorithm(int step, double[] delta, int[][] deltaIndex, double[][] observations) {

        // System.out.println("step : "+step);
        // We stop when we have computed all observations
        int nbObservations = observations.length;
        if (step >= nbObservations) {
            return maxIndex(delta);
        }

        int currentObservation = (int) observations[step][0];
        // System.out.println("current observation : "+currentObservation);
        // System.out.println("delta : "+Arrays.toString(delta));

        double[] deltaTmp = new double[nbStates];
        for (int i = 0; i < nbStates; i++) {

            double[] m1 = getCol(transitionMatrix, i);

            double[] tmp = new double[m1.length];
            for (int k = 0; k < m1.length; k++) {
                tmp[k] = Math.log(m1[k]) + delta[k] + Math.log(getCol(emissionMatrix, currentObservation)[i]);
            };
            deltaTmp[i] = max(tmp);

            deltaIndex[i][step] = maxIndex(tmp);
        }

        delta = deltaTmp;

        return deltaAlgorithm(step + 1, delta, deltaIndex, observations);
    }

    // Get a column of a matrix has a table
    public double[] getCol(double[][] matrix, int colValue) {
        double[] col = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            col[i] = matrix[i][colValue];
        }
        return col;
    }

    public static double[] vectorialProduct(double[] m1, double[] m2) {
        if (m1.length != m2.length) {
            //("m1 and m2 have a different dimension");
            System.exit(0);
        }
        double[] res = new double[m1.length];
        for (int i = 0; i < m1.length; i++) {
            res[i] = m1[i] * m2[i];
        }
        return res;
    }

    public double max(double[] vector) {
        double max = vector[0];
        for (int i = 1; i < vector.length; i++) {
            if (vector[i] > max) {
                max = vector[i];
            }
        }
        return max;
    }

    public int maxIndex(double[] vector) {
        int argmax = 0;
        for (int i = 1; i < vector.length; i++) {
            if (vector[i] > vector[argmax]) {
                argmax = i;
            }
        }
        return argmax;
    }

    public static double[][] multiplyByMatrix(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if (m1ColLength != m2RowLength) {
            //("matrix multiplication is not possible");
            System.exit(0); // matrix multiplication is not possible
        }
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length

        double[][] mResult = new double[mRRowLength][];

        for (int i = 0; i < mRRowLength; i++) {
            mResult[i] = new double[mRColLength];
            for (int j = 0; j < mRColLength; j++) {
                mResult[i][j] = new Double(0);
            }
        }

        for (int i = 0; i < mRRowLength; i++) {         // rows from m1
            for (int j = 0; j < mRColLength; j++) {     // columns from m2
                for (int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += ((m1[i][k]) * (m2[k][j]));
                }
            }
        }
        return mResult;
    }

}
