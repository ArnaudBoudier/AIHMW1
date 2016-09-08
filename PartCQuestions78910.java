

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

    @Override
    public void resolve() {
        EstiModParamExercice.BaumWelchAlgorithm(data.transitionMatrix.matrix, data.emissionMatrix.matrix, data.pi.matrix, data.observations.matrix, 1000);

        Matrix.printMatrix(data.pi.matrix);
    }

}
