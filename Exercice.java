
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Abstract classe use for exercice resolution, when you begin a new exercice,
 * you juste have to create a new classe which extends this classe. Then you
 * have acces to the data parsed easily.
 *
 * @author Arnaud
 */
public abstract class Exercice {

    public InputParser data;

    // We parse the data first
    public Exercice() {
        Scanner input = new Scanner(System.in);
        this.data = new InputParser(input);
    }

    // Init phase for forward Algorithme return alphaVector
    public double[] initForwardAlgorithme() {
        int observationValue = (int) data.observations.matrix[0][0];
        return Matrix.vectorialProductUsingLog(data.emissionMatrix.getCol(observationValue), data.pi.matrix[0]);
    }

    public double[] forwardAlgorithm(int step, double[] alphaVector,ArrayList<double[]> listAlpha) {
        // We stop when we have compute all observations
        listAlpha.add(step-1, alphaVector);
        if (step >= data.nbObservations) {
            return alphaVector;
        }
        // Else we take our current observation
        int currentObservation = (int) data.observations.matrix[step][0];
        // Conversion of our alpha Vector in alpha Matrix for computation purposes
        Matrix alphaMatrix = new Matrix(1, alphaVector.length, alphaVector);

        double[][] temp = Matrix.multiplyByMatrixUsingLog(alphaMatrix.matrix, data.transitionMatrix.matrix);
        alphaVector = Matrix.vectorialProductUsingLog(temp[0], data.emissionMatrix.getCol(currentObservation));
        // Recursive calcul
        return forwardAlgorithm(step + 1, alphaVector,listAlpha);
    }
    
    // Init phase for the backward algorithm return betaVector
    public double[] initBackwardAlgorithme() {
        double[] initialBeta = new double[data.nbStates];
        for (int i = 0; i < data.nbStates; i++) {
            initialBeta[i] = 1;
        }
        
        return initialBeta;
    }

    public double[] backwardAlgorithm(int step, double[] betaVector, ArrayList<double[]> listBeta) {
        // We stop when we have compute all observations
        listBeta.add(0,betaVector);
        if (step < 0) {
            return betaVector;
        }
        // Else we take our current observation
        int currentObservation = (int) data.observations.matrix[step+1][0];
     
        double[]tempVector = Matrix.vectorialProductUsingLog(betaVector, data.emissionMatrix.getCol(currentObservation));
        Matrix tempMatrix = new Matrix(1, tempVector.length, tempVector);
        double[][] betaMatrice = Matrix.multiplyByMatrixUsingLog(tempMatrix.matrix, data.transitionMatrix.matrix);
       
        return backwardAlgorithm(step-1, betaMatrice[0],listBeta);
    }

    

    // Abstract Function which resolve the exercice
    public abstract void resolve();

}
