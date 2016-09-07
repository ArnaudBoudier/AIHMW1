

/**
 * Created by alexandretadros on 04/09/2016.
 */
public class Main {

    public static void main(String[] args) {
        Exercice ex = null;
        // Exercice answer for the Next Observation Distribution
        // ex = new NextObsDisExercice();
      
        // Exercice answer for the Probability of the observation sequence
        // ex = new ProbObsSeqExercice();
        
        // Exercice answer for the most likely sequence of states given the observations
        ex = new MostLikelySequenceOfStates();

        ex.resolve();
    }
}
