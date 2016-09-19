
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Arnaud
 */
public class EstiModParamExercice extends Exercice {

    public EstiModParamExercice() {
        super();
    }

    @Override
    public void resolve() {

        BaumWelchAlgorithm(data.transitionMatrix.matrix, data.emissionMatrix.matrix, data.pi.matrix, data.observations.matrix, 1, data.nbObservations);
    }

    public static void BaumWelchAlgorithm(double[][] transitionMatrix, double[][] emissionMatrix, double[][] pi, double[][] observations, int iterMax, int nbObservations) {

        // Initialization of parameters
        int nbStates = transitionMatrix.length;
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
                alphas[i] = pi[0][i] * emissionMatrix[i][(int) observations[0][0]];
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
            if (oldLogProb > logProb || iters == iterMax) {
                Matrix.printMatrix(transitionMatrix);
                Matrix.printMatrix(emissionMatrix);
                if (iters == iterMax) {
                    System.err.println("#Observations : " + nbObservations + " no convergence, step value " + iters);
                } else {
                    System.err.println("#Observations : " + nbObservations + " convergence, step value " + iters);
                }
                /*
                double distance = Matrix.distanceMatrixN1(pi, PartCQuestions78910.piExpected) + Matrix.distanceMatrixN1(transitionMatrix, PartCQuestions78910.transExpected) + Matrix.distanceMatrixN1(emissionMatrix, PartCQuestions78910.emmisExpected);
                System.err.println("Distance N1 " + distance);
                distance = Matrix.distanceMatrixN2(pi, PartCQuestions78910.piExpected) + Matrix.distanceMatrixN2(transitionMatrix, PartCQuestions78910.transExpected) + Matrix.distanceMatrixN2(emissionMatrix, PartCQuestions78910.emmisExpected);
                System.err.println("Distance N2 " + distance);
                distance = Matrix.distanceMatrixNInfini(pi, PartCQuestions78910.piExpected) + Matrix.distanceMatrixNInfini(transitionMatrix, PartCQuestions78910.transExpected) + Matrix.distanceMatrixNInfini(emissionMatrix, PartCQuestions78910.emmisExpected);
                System.err.println("Distance NInfini " + distance);
                System.err.println("");
                */
                break;
            }
            oldLogProb = logProb;

            // RE estimation de PI
            for (int i = 0; i < nbStates; i++) {
                pi[0][i] = listGama.get(0)[i];
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
    }

    public void printComputationValues(ArrayList<double[]> computationValues, String name) {
        int i = 1;
        for (double[] tab : computationValues) {
            System.out.println(name + " values at t = " + i);
            Matrix.printVector(tab);
            System.out.println("");
            i++;
        }
    }

}
