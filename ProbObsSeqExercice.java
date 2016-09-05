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
        int observationValue = (int) data.observations.matrix[0][0];
        double[] alpha = Matrix.vectorialProduct(data.emissionMatrix.getCol(observationValue), data.pi.matrix[0]);
      
        // ForwardAlgorithm Computation
        double[] alphaFinal = forwardAlgorithm(1, alpha);
        
        double res = 0;
        for (int i = 0; i < alphaFinal.length; i++) {
            res += alphaFinal[i];
        }
        // Print of result
        System.out.println(res);
    }

    public double[] forwardAlgorithm(int step, double[] alphaVector) {
        // We stop when we have compute all observations
        if (step >= data.nbObservations) {
            return alphaVector;
        }
        // Else we take our current observation
        int currentObservation = (int) data.observations.matrix[step][0];
        // Conversion of our alpha Vector in alpha Matrix for computation purposes
        Matrix alphaMatrix = new Matrix(1, alphaVector.length, alphaVector);

        double[][] temp = Matrix.multiplyByMatrix(alphaMatrix.matrix, data.transitionMatrix.matrix);
        
        alphaVector = Matrix.vectorialProduct(temp[0], data.emissionMatrix.getCol(currentObservation));
        // Recursive calcul
        return forwardAlgorithm(step + 1, alphaVector);
    }

}
