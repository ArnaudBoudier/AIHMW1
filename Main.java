import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexandretadros on 04/09/2016.
 */
public class Main
{

    public static void main(String[] args) {
        
        Pattern pattern = Pattern.compile("(\\d+) (\\d+) (.*)");
        Scanner input = new Scanner(System.in);

        int nbStates = 0;
        int nbObservations = 0;

        String transitionMatrixString = "";
        String emissionMatrixString = "";

        String transitionLine = input.nextLine();

        Matcher transitionMatcher = pattern.matcher(transitionLine);
        if (transitionMatcher.matches()) {
            nbStates = Integer.parseInt(transitionMatcher.group(1));

            transitionMatrixString = transitionMatcher.group(3);
        }
        /*
        String[][] tmp = transform(transitionMatrixString.split(" "), nbStates);
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                System.out.print(tmp[i][j] + " ");
            }
            System.out.println();
        }
        */

        BigDecimal[][] transition = stringToBigDecimalMatrix(transform(transitionMatrixString.split(" "), nbStates));
        String emissionLine = input.nextLine();

        Matcher emissionMatcher = pattern.matcher(emissionLine);
        if (emissionMatcher.matches()) {
            emissionMatrixString = emissionMatcher.group(3);
            nbObservations = Integer.parseInt(emissionMatcher.group(2));
        }

        BigDecimal[][] emission = stringToBigDecimalMatrix(transform(emissionMatrixString.split(" "), nbObservations));


        BigDecimal[] pi = new BigDecimal[nbStates];
        String piLine = input.nextLine();

        Matcher piMatcher = pattern.matcher(piLine);
        if (piMatcher.matches()) {
            String[] piString = piMatcher.group(3).split(" ");
            for (int i=0; i<nbStates; i++) {
                pi[i] = new BigDecimal(piString[i]);
            }

        }

        BigDecimal[][] piNorm = new BigDecimal[1][nbStates];
        piNorm[0] = pi;
/*
        for (int i = 0; i < emission.length; i++) {
            for (int j = 0; j < emission[i].length; j++) {
                System.out.print(emission[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < transition.length; i++) {
            for (int j = 0; j < transition[i].length; j++) {
                System.out.print(transition[i][j] + " ");
            }
            System.out.println();
        }
*/
        BigDecimal[][] abProduct = multiplyByMatrix(transition, emission);
        BigDecimal[][] emissionProb = multiplyByMatrix(piNorm, abProduct);

        System.out.print("1 "+nbObservations+" ");
        for (BigDecimal b: emissionProb[0]) {
            System.out.print(b+" ");
        }
        System.out.println();
    }

    static String[][] transform(String[] arr, int N) {
        int M = (arr.length + N - 1) / N;
        String[][] mat = new String[M][];
        int start = 0;
        for (int r = 0; r < M; r++) {
            int L = Math.min(N, arr.length - start);
            mat[r] = java.util.Arrays.copyOfRange(arr, start, start + L);
            start += L;
        }
        return mat;
    }

    public static BigDecimal[][] multiplyByMatrix(BigDecimal[][] m1, BigDecimal[][] m2) {
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if(m1ColLength != m2RowLength) return null; // matrix multiplication is not possible
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length

        BigDecimal[][] mResult = new BigDecimal[mRRowLength][];

        for(int i=0; i<mRRowLength; i++) {
            mResult[i] = new BigDecimal[mRColLength];
            for(int j = 0; j<mRColLength; j++){
                mResult[i][j] = new BigDecimal(0);
            }
        }

        for(int i = 0; i < mRRowLength; i++) {         // rows from m1
            for(int j = 0; j < mRColLength; j++) {     // columns from m2
                for(int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] = mResult[i][j].add((m1[i][k]).multiply(m2[k][j]));
                }
            }
        }
        return mResult;
    }

    public static BigDecimal[][] stringToBigDecimalMatrix(String[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        BigDecimal[][] result = new BigDecimal[n][m];
        for (int i=0; i<n; i++) {
            for (int j=0; j<m; j++) {
               result[i][j] = new BigDecimal(matrix[i][j]);
            }
        }
        return result;
    }

}