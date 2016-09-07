
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
public class ProbObsSeqExercice extends Exercice {

    @Override
    public void resolve() {

        // Init phase
        //  double[] alpha = initForwardAlgorithme();
        ArrayList<double[]> listAlpha = new ArrayList<>();
        // ForwardAlgorithm Computation
        /*
        double[] alphaFinal = forwardAlgorithm(1, alpha, listAlpha);

        double res = 0;
        for (int i = 0; i < alphaFinal.length; i++) {
            res += alphaFinal[i];
        }
         */
        // Print of result
        System.out.println(ForwardAlgorithmWithScaling(listAlpha));
        // printComputationValues(listAlpha, "ALPHA");
    }

    public double ForwardAlgorithmWithScaling(ArrayList<double[]> listAlpha) {
        double[] alphas = new double[data.nbStates];

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
            alphas[i] = alphas[i] * c0;
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
        // Calcul parts
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
                alphas_t[i] = alphas_t[i] * ct;
            }
            // t_m1 = t for next iteration
            for (int i = 0; i < data.nbStates; i++) {
                alphas_tm1[i] = alphas_t[i];
            }
            listAlpha.add(alphas);
        }

        // Calcul of P(O\lambda)
        double logProb = 0;
        for (int t = 0; t < data.nbObservations; t++) {
            logProb += Math.log(ctValues[t]);
        }
        logProb = -logProb;
        double resultat = Math.exp(logProb);
        return resultat;

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
    /*
    public double ForwardAlgorithmWithScaling(ArrayList<double[]> listAlpha) {

        double[] alphas = new double[data.nbStates];

        // Compute alpha 0;
        double observation = data.observations.matrix[0][0];
        double c0 = 0;
        for (int i = 0; i < data.nbStates; i++) {
            alphas[i] = Math.log(data.pi.matrix[0][i]) + Math.log(data.emissionMatrix.matrix[i][(int) observation]);
            c0 += Math.exp(alphas[i]);
        }

        // Scale alpha0
        c0 = 1 / c0;
        for (int i = 0; i < data.nbStates; i++) {
            alphas[i] = alphas[i] + Math.log(c0);
        }

        listAlpha.add(alphas);
        // Compute of alpha_t(i)
        // Initialisation part
        double[] alphas_tm1 = new double[data.nbStates];
        for (int i = 0; i < data.nbStates; i++) {
            alphas_tm1[i] = alphas[i];
        }
        double[] alphas_t = new double[data.nbStates];
        double[] alphas_show = new double[data.nbStates];
        double[] ctValues = new double[data.nbObservations];
        ctValues[0] = c0;
        // Calcul parts
        for (int t = 1; t < data.nbObservations; t++) {
            double ct = 0;
            observation = data.observations.matrix[t][0];
            for (int i = 0; i < data.nbStates; i++) {
                alphas_t[i] = 0;
                double temp = 0;
                for (int j = 0; j < data.nbStates; j++) {
                    temp += (Math.exp(alphas_tm1[j] + Math.log(data.transitionMatrix.matrix[j][i])));
                }
                alphas_t[i] = Math.log((temp));
                alphas_t[i] += Math.log(data.emissionMatrix.matrix[i][(int) observation]);
                ct += Math.exp(alphas_t[i]);
            }
            ct = 1 / ct;
            ctValues[t] = ct;
            for (int i = 0; i < data.nbStates; i++) {
                alphas_t[i] = alphas_t[i] + Math.log(ct);
            }
            // t_m1 = t for next iteration
            for (int i = 0; i < data.nbStates; i++) {
                alphas_tm1[i] = alphas_t[i];
                alphas_show[i] = Math.exp(alphas_t[i]);
            }
            listAlpha.add(alphas_show);
        }

        // Calcul of P(O\lambda)
        double logProb = 0;
        for (int t = 0; t < data.nbObservations; t++) {
            logProb += Math.log(ctValues[t]);
        }
        logProb = -logProb;

        return logProb;

    }
     */

}
