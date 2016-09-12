
import java.util.Random;

/**
 * Created by alexandretadros on 04/09/2016.
 */
public class MainExercice {

    public static void main(String[] args) {
        Exercice ex = null;
        // GRADE E AND D
        // 1. Exercice answer for the Next Observation Distribution
        // ex = new NextObsDisExercice();

        // 2. Exercice answer for the Probability of the observation sequence
        // ex = new ProbObsSeqExercice();
        // 3. Exercice answer for the most likely sequence of states given the observations
        //ex = new MostLikelySequenceOfStates();
        // 4. Exercice EstiModeParamService
        // ex = new EstiModParamExercice();
        // GRADE C
        // C 1. QUESTION 7
         ex = new PartCQuestions78910();
        ex.resolve();
       
    }


}
