import java.util.Arrays;

/**
 * Created by alexandretadros on 06/09/2016.
 */
public class MostLikelySequenceOfStates extends Exercice {

    @Override
    public void resolve() {

        int observationValue = (int) data.observations.matrix[0][0];
/*
        System.out.println("Transition Matrix");
        Matrix.printMatrix(data.transitionMatrix.matrix);

        System.out.println("Emission Matrix");
        Matrix.printMatrix(data.emissionMatrix.matrix);
*/
        // Initialize delta
        double[] delta = Matrix.vectorialProduct(data.emissionMatrix.getCol(observationValue), data.pi.matrix[0]);
        for(int i=0; i<delta.length; i++) {
            delta[i] = Math.log(delta[i]);
        }
        int[][] deltaIndex = new int[data.nbStates][data.nbObservations];

        // Initialize final result
        int[] finalStateSequence = new int[data.nbObservations];

        int finalState = deltaAlgorithm(1, delta, deltaIndex);
        // System.out.println("final State : "+finalState);
        // System.out.println("example deltaIndex line : "+Arrays.toString(deltaIndex[1]));
/*
        double[][] test = new double[deltaIndex.length][deltaIndex[0].length];
        for (int i=0; i<test.length; i++) {
            for(int j=0; j<test[0].length; j++) {
                test[i][j] = (double) deltaIndex[i][j];
            }
        }
        Matrix.printMatrix(test);
*/
        finalStateSequence[data.nbObservations - 1] = finalState;

        for (int t=data.nbObservations-2; t>=0; t--) {
            // System.out.println("t : "+t+", finalStateSeq[t+1] : "+finalStateSequence[t+1]);
            // System.out.println("result : "+deltaIndex [finalStateSequence[t+1]] [t+1]);
            finalStateSequence[t] = deltaIndex [finalStateSequence[t+1]] [t+1];
        }

        for (int i=0; i<finalStateSequence.length; i++) {
            System.out.print(finalStateSequence[i]+" ");
        }
        System.out.println();
    }

    public int deltaAlgorithm(int step, double[] delta, int[][] deltaIndex) {

        // System.out.println("step : "+step);

        // We stop when we have computed all observations
        if (step >= data.nbObservations) {
            // System.out.println("in final condition");
            // System.out.println(maxIndex(delta));
            return maxIndex(delta);
        }

        int currentObservation = (int) data.observations.matrix[step][0];
        // System.out.println("current observation : "+currentObservation);
        // System.out.println("delta : "+Arrays.toString(delta));

        double[] deltaTmp = new double[data.nbStates];
        for(int i=0; i<data.nbStates; i++) {

            double[] m1 = data.transitionMatrix.getCol(i);

            double[] tmp = new double[m1.length];
            for (int k = 0; k < m1.length; k++) {
                tmp[k] = Math.log(m1[k]) + delta[k] + Math.log(data.emissionMatrix.getCol(currentObservation)[i]);
            }


            // double[] tmp = Matrix.vectorialProduct(data.transitionMatrix.getCol(i), delta);
            /*for(int j=0; j<tmp.length; j++) {
                tmp[j] += Math.log(data.emissionMatrix.getCol(currentObservation)[i]);
            }*/
            // deltaTmp[i] = data.emissionMatrix.getCol(currentObservation)[i] * max(tmp);
            deltaTmp[i] = max(tmp);
            // System.out.println("tmp : "+Arrays.toString(tmp));
            // System.out.println("maxIndex at step "+step+" ["+i+"] : "+maxIndex(tmp));
            deltaIndex[i][step] = maxIndex(tmp);
        }
        /*
        for (int i=0; i<data.nbStates; i++) {
            delta[i] = deltaTmp[i];
        }
        */
        delta = deltaTmp;
        // System.out.println("delta after processing : "+Arrays.toString(delta));

/*
        for (int i=0; i<data.nbStates; i++) {
            deltaIndex[i][step] = maxIndex(delta);
        }
*/
        // System.out.println("delta : "+Arrays.toString(delta));
/*
        double[][] test = new double[deltaIndex.length][deltaIndex[0].length];
        for (int i=0; i<test.length; i++) {
            for(int j=0; j<test[0].length; j++) {
                test[i][j] = (double) deltaIndex[i][j];
            }
        }
        Matrix.printMatrix(test);
*/
        return deltaAlgorithm(step+1, delta, deltaIndex);
    }

    public double max(double[] vector) {
        double max = vector[0];
        for(int i=1; i<vector.length; i++) {
            if(vector[i] > max) {
                max = vector[i];
            }
        }
        return max;
    }

    public int maxIndex(double[] vector) {
        int argmax = 0;
        for(int i = 1; i<vector.length; i++) {
            if(vector[i]>vector[argmax]) {
                argmax = i;
            }
        }
        return argmax;
    }

}
