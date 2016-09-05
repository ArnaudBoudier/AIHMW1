/**
 * Created by alexandretadros on 05/09/2016.
 */
public class Matrix {
    
    public double[][] matrix;
    
    public Matrix(int nbRows, String matrixString) {
        this.matrix = stringToDoubleMatrix(transform(matrixString.split(" "), nbRows));
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
        for (int i=0; i<n; i++) {
            for (int j=0; j<m; j++) {
                result[i][j] = Double.parseDouble(matrix[i][j]);
            }
        }
        return result;
    }
    
}
