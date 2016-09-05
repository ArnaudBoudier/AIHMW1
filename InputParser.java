import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexandretadros on 05/09/2016.
 */
public class InputParser {

    public int nbState = 0;
    public Matrix transitionMatrix;

    public int nbObservation = 0;
    public Matrix emissionMatrix;

    public Matrix pi;


    private String transitionMatrixString = "";

    public InputParser(Scanner input) {

        Pattern pattern = Pattern.compile("(\\d+) (\\d+) (.*)");

        /*
         * Process first line : transition matrix
         */
        String transitionLine = input.nextLine();
        Matcher transitionMatcher = pattern.matcher(transitionLine);

        if (transitionMatcher.matches()) {
            this.nbState = Integer.parseInt(transitionMatcher.group(1));
            String transitionMatrixString = transitionMatcher.group(3);
            this.transitionMatrix = new Matrix(this.nbState, transitionMatrixString);
        }

        /*
         * Process second line : emission matrix
         */
        String emissionLine = input.nextLine();
        Matcher emissionMatcher = pattern.matcher(emissionLine);

        if (emissionMatcher.matches()) {
            this.nbObservation = Integer.parseInt(emissionMatcher.group(2));
            String emissionMatrixString = emissionMatcher.group(3);
            this.emissionMatrix = new Matrix(this.nbObservation, emissionMatrixString);
        }

        /*
         * Process third line : pi vector
         */
        String piLine = input.nextLine();
        Matcher piMatcher = pattern.matcher(piLine);

        if (piMatcher.matches()) {
            String piString = piMatcher.group(3);
            for (int i=0; i<nbState; i++) {
                this.pi = new Matrix(1, piString);
            }
        }

        /*
         * Process fourth line if exists : observation sequence
         */
        /*
        String transitionLine = input.nextLine();
        Matcher transitionMatcher = pattern.matcher(transitionLine);

        if (transitionMatcher.matches()) {
            this.nbStates = Integer.parseInt(transitionMatcher.group(1));
            String transitionMatrixString = transitionMatcher.group(3);
            this.transitionMatrix = new Matrix(this.nbStates, transitionMatrixString);
        }
        */

    }

}
