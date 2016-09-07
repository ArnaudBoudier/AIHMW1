
/**
 * Created by alexandretadros on 04/09/2016.
 */
public class Main {

    public static void main(String[] args) {
        Exercice ex = null;
        // Exercice answer for the Next Observation Distribution
        // ex = new NextObsDisExercice();

        // Exercice answer for the Probability of the observation sequence
         ex = new ProbObsSeqExercice();
        // Exercice EstiModeParamService
        // ex = new EstiModParamExerice();
        /* double c,d,e,f = 0.00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001;
        d = f;
        e = f;
        c = f;
        double a = c*d + e*f;
        double b = Math.exp(Math.log(c)+Math.log(d)) + Math.exp(Math.log(e)+Math.log(f));
        System.out.println("valeur a " + a);
         System.out.println("valeur b " + b);
         */
       
        ex.resolve();
    }
}
