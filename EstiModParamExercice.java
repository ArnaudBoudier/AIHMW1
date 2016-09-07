
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

    @Override
    public void resolve() {
        int iters = 0;
        int itersMax = 1005;
        double oldLogProb = Double.NEGATIVE_INFINITY;
        double logProb = 0;
        boolean isFirstIteration = true;
        while (iters < itersMax) {
            if (!isFirstIteration) {
                oldLogProb = logProb;
            }
            isFirstIteration = false;
            ///////// FORWARD ALGORITHM //////////////
            double[] alphas = new double[data.nbStates];
            ArrayList<double[]> listAlpha = new ArrayList<>();
            //double oldProb = Math.

            // Compute alpha 0;
            double c0 = 0;
            for (int i = 0; i < data.nbStates; i++) {
                alphas[i] = data.pi.matrix[0][i] * data.emissionMatrix.matrix[i][(int) data.observations.matrix[0][0]];
                c0 += alphas[i];
            }

            // Scale alpha0
            c0 = 1 / c0;
            for (int i = 0; i < data.nbStates; i++) {
                alphas[i] *= c0;
            }

            listAlpha.add(alphas);
            // Compute of alpha_t(i)
            // Initialisation part
            double[] alphas_tm1 = new double[data.nbStates];
            for (int i = 0; i < data.nbStates; i++) {
                alphas_tm1[i] = alphas[i];
            }
            double[] alphas_t = new double[data.nbStates];
            double[] ctValues = new double[data.nbObservations];
            ctValues[0] = c0;
            // computation parts
            for (int t = 1; t < data.nbObservations; t++) {
                double ct = 0;
                double observation_T = data.observations.matrix[t][0];
                for (int i = 0; i < data.nbStates; i++) {
                    alphas_t[i] = 0;
                    for (int j = 0; j < data.nbStates; j++) {
                        alphas_t[i] += (alphas_tm1[j] * data.transitionMatrix.matrix[j][i]);
                    }
                    alphas_t[i] *= data.emissionMatrix.matrix[i][(int) observation_T];
                    ct += alphas_t[i];
                }
                ct = 1 / ct;
                ctValues[t] = ct;
                for (int i = 0; i < data.nbStates; i++) {
                    alphas_t[i] *= ct;
                   
                }
                // t_m1 = t for next iteration
                for (int i = 0; i < data.nbStates; i++) {
                    alphas_tm1[i] = alphas_t[i];
                    
                }
                listAlpha.add(alphas);
            }

            ///////// END FORWARD ALGORITHM //////////////
            ///////// BEGIN BACKWARD ALGORITHM //////////////
            ArrayList<double[]> listBetas = new ArrayList<>();

            double[] betas_tP1 = new double[data.nbStates];
            double[] betas_t = new double[data.nbStates];

            // Initialization
            for (int i = 0; i < data.nbStates; i++) {
                betas_tP1[i] = ctValues[data.nbObservations - 1];
            }
            listBetas.add(0, betas_tP1);
            // Beta Pass
            for (int t = data.nbObservations - 2; t >= 0; t--) {
                double observation_TP1 = data.observations.matrix[t + 1][0];
                for (int i = 0; i < data.nbStates; i++) {
                    betas_t[i] = 0;
                    for (int j = 0; j < data.nbStates; j++) {
                        betas_t[i] +=(data.transitionMatrix.matrix[i][j] * data.emissionMatrix.matrix[j][(int) observation_TP1] * betas_tP1[j]);
                    }
                    betas_t[i] *= ctValues[t];
                }
                // t_P1 = t for next iteration
                for (int i = 0; i < data.nbStates; i++) {
                    betas_tP1[i] = betas_t[i];
                }
                listBetas.add(0, betas_t);
            }

            ///////// END BACKWARD ALGORITHM //////////////
            ///////// BEGIN COMPUTATION OF DIGAMA  AND GAMA ALGORITHM //////////////
            ArrayList<double[]> listGama = new ArrayList<>();
            ArrayList<double[][]> listDiGama = new ArrayList<>();

            double[] gama_t = new double[data.nbStates];
            double[][] digama_t = new double[data.nbStates][data.nbStates];

            for (int t = 0; t < data.nbObservations - 1; t++) {
                double denom = 0;
                double ob_TP1 = data.observations.matrix[t + 1][0];
                for (int i = 0; i < data.nbStates;i++){
                    denom+=listAlpha.get(data.nbObservations-1)[i];
                }
                /*
                for (int i = 0; i < data.nbStates; i++) {
                    for (int j = 0; j < data.nbStates; j++) {
                        denom += (listAlpha.get(t)[i] * data.transitionMatrix.matrix[i][j] * data.emissionMatrix.matrix[j][(int) ob_TP1] * listBetas.get(t + 1)[j]);
                    }
                }
                */
                for (int i = 0; i < data.nbStates; i++) {
                    gama_t[i] = 0;
                    for (int j = 0; j < data.nbStates; j++) {
                        digama_t[i][j] = (listAlpha.get(t)[i] * data.transitionMatrix.matrix[i][j] * data.emissionMatrix.matrix[j][(int) ob_TP1] * listBetas.get(t + 1)[j]) / denom;
                        gama_t[i] += digama_t[i][j];
                    }
                }

                listDiGama.add(digama_t);
                listGama.add(gama_t);
            }

            // RE estimation de PI
            for (int i = 0; i < data.nbStates; i++) {
                data.pi.matrix[0][i] = listGama.get(0)[i];
            }
            // Re estimation de A   
            double numer = 0;
            double denom = 0;
            for (int i = 0; i < data.nbStates; i++) {
                for (int j = 0; j < data.nbStates; j++) {
                    numer = 0;
                    denom = 0;
                    for (int t = 0; t < data.nbObservations - 1; t++) {
                        numer += listDiGama.get(t)[i][j];
                        denom += listGama.get(t)[i];
                    }
                    data.transitionMatrix.matrix[i][j] = numer / denom;
                }
            }

            // Re estimation de B   
            for (int i = 0; i < data.nbStates; i++) {
                for (int j = 0; j < data.nbTypeObservations; j++) {
                    numer = 0;
                    denom = 0;
                    for (int t = 0; t < data.nbObservations - 1; t++) {
                        double curr_obs = data.observations.matrix[t][0];
                        if ((int) curr_obs == j) {
                            numer += listGama.get(t)[i];
                        }
                        denom += listGama.get(t)[i];
                    }
                    data.emissionMatrix.matrix[i][j] = numer / denom;
                }
            }

            /////// Calcul of P(O\lambda)
            logProb = 0;
            for (int t = 0; t < data.nbObservations; t++) {
                logProb += Math.log(ctValues[t]);
            }
            logProb = -logProb;

            System.out.println("MATRICE TRANS ESTIMATED AT T = " + iters);
            Matrix.printMatrix(data.transitionMatrix.matrix);
            System.out.println("MATRICE EMI ESTIMATED AT T = " + iters);
            Matrix.printMatrix(data.emissionMatrix.matrix);
            iters++;
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
