
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
public class PartCQuestions78910 extends Exercice {

    static double[][] transExpected = {{0.7, 0.05, 0.25}, {0.1, 0.8, 0.1}, {0.2, 0.3, 0.5}};
    static double[][] emmisExpected = {{0.7, 0.2, 0.1, 0}, {0.1, 0.4, 0.3, 0.2}, {0, 0.1, 0.2, 0.7}};
    static double[][] piExpected = {{1, 0, 0}};

    @Override
    public void resolve() {
        /*
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 1);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 3);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 5);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 6);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 7);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 8);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 9);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 10);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 20);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 30);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 40);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 50);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 100);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 500);
         */
        EstiModParamExercice.BaumWelchAlgorithm(data.transitionMatrix.matrix, data.emissionMatrix.matrix, data.pi.matrix, data.observations.matrix, 1000, 10000);
        System.out.println("Likelihood" + ForwardAlgorithmWithScaling());
        /*
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 2000);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 3000);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 4000);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 5000);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 6000);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 7000);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 8000);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 9000);
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 10000);
         */
    }

    public double[][] cloneMatrix(double[][] matrix) {
        double[][] matrixClonee = matrix.clone();
        for (int i = 0; i < matrix.length; i++) {
            matrixClonee[i] = matrix[i].clone();
        }
        return matrixClonee;
    }

    public double ForwardAlgorithmWithScaling() {
        ArrayList<double[]> listAlpha = new ArrayList<>();
        double[] alphas = new double[data.nbStates];

        //double oldProb = Math.
        // Compute alpha 0;
        double c0 = 0;
        for (int i = 0; i < data.nbStates; i++) {
            alphas[i] = data.pi.matrix[0][i] * data.emissionMatrix.matrix[i][(int) data.validationobservations.matrix[0][0]];
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
        double[] ctValues = new double[data.nbValidationObservations];
        ctValues[0] = c0;
        // Calcul parts
        for (int t = 1; t < data.nbValidationObservations; t++) {
            double ct = 0;
            double observation_T = data.validationobservations.matrix[t][0];
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
        for (int t = 0; t < data.nbValidationObservations; t++) {
            logProb += Math.log(ctValues[t]);
        }
        logProb = -logProb;
     //   double resultat = Math.exp(logProb);
        return logProb;

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
