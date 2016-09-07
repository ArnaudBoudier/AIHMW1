/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arnaud
 */
public class NextObsDisExercice extends Exercice {

    @Override
    public void resolve() {

        double[][] abProduct = Matrix.multiplyByMatrix(data.transitionMatrix.matrix, data.emissionMatrix.matrix);
        double[][] emissionProb = Matrix.multiplyByMatrix(data.pi.matrix, abProduct);

        System.out.print("1 " + data.nbTypeObservations + " ");
        for (double b : emissionProb[0]) {
            System.out.print(b + " ");
        }
        System.out.println();
    }

}
