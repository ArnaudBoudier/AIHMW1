
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexandretadros on 05/09/2016.
 */
public class InputParser {

    public int nbStates = 0;
    public Matrix transitionMatrix;

    public int nbTypeObservations = 0;
    public Matrix emissionMatrix;

    public Matrix pi;
    
    public int nbObservations = 0;
    public Matrix observations;
    
    private String transitionMatrixString = "";

    public InputParser(Scanner input) {

        Pattern pattern = Pattern.compile("(\\d+) (\\d+) (.*)");

        /*
         * Process first line : transition matrix
         */
        String observationLine = input.nextLine();
        Matcher transitionMatcher = pattern.matcher(observationLine);

        if (transitionMatcher.matches()) {
            this.nbStates = Integer.parseInt(transitionMatcher.group(1));
            String transitionMatrixString = transitionMatcher.group(3);
            this.transitionMatrix = new Matrix(this.nbStates, transitionMatrixString);
        }

        /*
         * Process second line : emission matrix
         */
        String emissionLine = input.nextLine();
        Matcher emissionMatcher = pattern.matcher(emissionLine);

        if (emissionMatcher.matches()) {
            this.nbTypeObservations = Integer.parseInt(emissionMatcher.group(2));
            String emissionMatrixString = emissionMatcher.group(3);
            this.emissionMatrix = new Matrix(this.nbTypeObservations, emissionMatrixString);
        }

        /*
         * Process third line : pi vector
         */
        String piLine = input.nextLine();
        Matcher piMatcher = pattern.matcher(piLine);

        if (piMatcher.matches()) {
            String piString = piMatcher.group(3);
            this.pi = new Matrix(nbStates, piString);
        }

        /*
         * Process fourth line if exists : observation sequence
         */
 
        observationLine = input.nextLine();
        
        Pattern patternObs = Pattern.compile("(\\d+) (.*)");
        Matcher observationMatcher = patternObs.matcher(observationLine);

        
        if (observationMatcher.matches()) {
            this.nbObservations = Integer.parseInt(observationMatcher.group(1));
            String observationsMatrixString = observationMatcher.group(2);
            this.observations = new Matrix(1, observationsMatrixString);
        }
         
    }

}
