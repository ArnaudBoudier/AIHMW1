

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
        EstiModParamExercice.BaumWelchAlgorithm(cloneMatrix(data.transitionMatrix.matrix), cloneMatrix(data.emissionMatrix.matrix.clone()), cloneMatrix(data.pi.matrix.clone()), cloneMatrix(data.observations.matrix.clone()), 5000, 1000);
        
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

}
