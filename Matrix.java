
import sun.rmi.runtime.Log;

/**
 * Created by alexandretadros on 05/09/2016.
 */
public class Matrix {

    public double[][] matrix;
    public int nbCol = 0;
    public int nbRows = 0;

    // Matrix creation with a vector of values and its number of columns and rows
    public Matrix(int nbRows, int nbCol, double[] matrixValues) {
        this.nbRows = nbRows;
        this.nbCol = nbCol;
        this.matrix = new double[nbRows][nbCol];
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCol; j++) {
                this.matrix[i][j] = matrixValues[i * nbCol + j];
            }
        }
    }

    // Matrix creation with String 
    public Matrix(int nbRows, String matrixString) {
        this.matrix = stringToDoubleMatrix(transform(matrixString.split(" "), nbRows));
        this.nbCol = matrix[0].length;
        this.nbRows = matrix.length;
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

    public static double[][] stringToDoubleMatrix(String[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        double[][] result = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i][j] = Double.parseDouble(matrix[i][j]);
            }
        }
        return result;
    }

    // Display Fonction for matrix
    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Display Fonction for matrix
    public static void printVector(double[] vector) {
        for (int i = 0; i < vector.length; i++) {
            System.out.println(vector[i] + " ");
        }
    }

    // Get a column of a matrix has a table
    public double[] getCol(int colValue) {
        double[] col = new double[nbRows];
        for (int i = 0; i < nbRows; i++) {
            col[i] = matrix[i][colValue];
        }
        return col;
    }

    public static double[] vectorialProduct(double[] m1, double[] m2) {
        if (m1.length != m2.length) {
            System.err.println("m1 and m2 have a different dimension");
            System.exit(0);
        }
        double[] res = new double[m1.length];
        for (int i = 0; i < m1.length; i++) {
            res[i] = m1[i] * m2[i];
        }
        return res;
    }

    public static double[][] multiplyByMatrix(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if (m1ColLength != m2RowLength) {
            System.err.println("Matrix multiplication is not possible");
            System.exit(0); // matrix multiplication is not possible
        }
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length

        double[][] mResult = new double[mRRowLength][];

        for (int i = 0; i < mRRowLength; i++) {
            mResult[i] = new double[mRColLength];
            for (int j = 0; j < mRColLength; j++) {
                mResult[i][j] = new Double(0);
            }
        }

        for (int i = 0; i < mRRowLength; i++) {         // rows from m1
            for (int j = 0; j < mRColLength; j++) {     // columns from m2
                for (int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += ((m1[i][k]) * (m2[k][j]));
                }
            }
        }
        return mResult;
    }

}
