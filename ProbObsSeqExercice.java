
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
        double[] alpha = initForwardAlgorithme();
        ArrayList<double[]> listAlpha = new ArrayList<>();
        // ForwardAlgorithm Computation
        double[] alphaFinal = forwardAlgorithm(1, alpha, listAlpha);
       
        double res = 0;
        for (int i = 0; i < alphaFinal.length; i++) {
            res += alphaFinal[i];
        }
        // Print of result
        System.out.println(res);
        printComputationValues(listAlpha, "ALPHA");
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
