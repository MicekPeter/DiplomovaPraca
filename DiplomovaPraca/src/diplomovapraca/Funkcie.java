package diplomovapraca;
//@author Peter Micek

import java.util.Arrays;

public class Funkcie {

    public static void GenerateItems(int k, int l, int max_w, int min_w, int max_p, int min_p, int[] weights, int[] points) {
        for (int i = 0; i < k * l; i++) {

            int x = (int) (Math.random() * (max_w - min_w + 1) + min_w);
            weights[i] = x;

            int y = (int) (Math.random() * (max_p - min_p + 1) + min_p);
            points[i] = y;
        }
    }

    public static void SaleCount(int k, int l, int[] weights, int[] points, int sale_weight, float sale_percentage){
        for (int i = 0; i < k * l; i++) {
            if (weights[i] == sale_weight) {
                points[i] = ((points[i]) * Math.round((100 + sale_percentage) / 100));
            }        
        }
    }
    
    public static void GenerateBuckets(int gen, int k, int l, int[] bucket, int[] bucket_weight, int[] weights, int[] bucket_points, int[] points, int[][] solutions) {
        for (int j = 0; j < gen; j++) {
            for (int i = 0; i < k * l; i++) {

                int z = (int) (Math.random() * (l + 1));
                bucket[i] = z;

                    for (int m = 1; m < l + 1; m++){
                        if (bucket[i] == m){
                            bucket_weight[j * l + (m - 1)] = (int) (bucket_weight[j * l + (m - 1)] + weights[i]);
                            bucket_points[j * l + (m - 1)] = (int) (bucket_points[j * l + (m - 1)] + points[i]);
                        }
                    }
                    
                solutions[j][i] = bucket[i];
            }
            System.out.println("bucket: " + Arrays.toString(bucket));
        }
    System.out.println("solutions: " + Arrays.deepToString(solutions));
    System.out.println("bucket_weight: " + Arrays.toString(bucket_weight));
    }

    public static void ErrorCount(int gen, int l, int max_b, int[] error, int[] bucket_weight) {
        for (int j = 0; j < gen; j++) {
            for (int i = 0; i < l; i++) {
                error[j] = error[j] + Math.abs(bucket_weight[l * j + i] - max_b);
            }
        }
//        System.out.println("error: " + Arrays.toString(error));
    }

    public static void Sort(int gen, int[] error, int[] temp_arr, int[] bucket_weight, int[] bucket_points, int[][] solutions) {
        for (int m = 0; m < gen; m++) {
            for (int n = 1; n < (gen - m); n++) {
                if (error[n - 1] > error[n]) {
                    int temp = error[n - 1];
                    error[n - 1] = error[n];
                    error[n] = temp;

                    temp_arr = solutions[n - 1];
                    solutions[n - 1] = solutions[n];
                    solutions[n] = temp_arr;

                    temp = bucket_weight[n - 1];
                    bucket_weight[n - 1] = bucket_weight[n];
                    bucket_weight[n] = temp;

                    temp = bucket_points[n - 1];
                    bucket_points[n - 1] = bucket_points[n];
                    bucket_points[n] = temp;
                }
            }
        }
    }

    public static void Output(int[] bucket_weight, int best_error, int[] error, int[] bucket_points, int[] best_solution) {
        System.out.println("najlepsie riesenie: " + Arrays.toString(best_solution));
        System.out.println("vaha: " + bucket_weight[0]);
        System.out.println("best_error: " + best_error);
        System.out.println("chyba : " + error[0]);
        System.out.println("skore : " + bucket_points[0]);
    }

    public static void Top50(int gen, int[][] solutions, int[][] solutions_50, int k) {
        for (int i = 0; i < gen / 2; i++) {
            System.arraycopy(solutions[i], 0, solutions_50[i], 0, k);
        }
    }

    public static void MemErase(int[] pamat) {
        for (int i = 0; i < pamat.length; i++) {
            pamat[i] = 0;
        }
    }

    public static void StringConvert(int gen, int k, int[][] solutions_50, String[] spojeny_solutions) {
        for (int i = 0; i < (gen / 2); i++) {
            for (int j = 0; j < k; j++) {
                if (j == 0) {
                    spojeny_solutions[i] = Integer.toString(solutions_50[i][j]);
                } else {
                    spojeny_solutions[i] = spojeny_solutions[i] + Integer.toString(solutions_50[i][j]);
                }
            }
        }
    }

    public static void FirstPairChoose(int gen, int[] pamat) {
        //vyber prvych dvojic
        pamat[0] = (int) (Math.random() * (gen / 2) + 1);
        pamat[1] = (int) (Math.random() * (gen / 2) + 1);

        while (pamat[1] == pamat[0]) {
            pamat[1] = (int) (Math.random() * (gen / 2) + 1);
        }
    }

    public static void SinglePointCross(int gen, int rnd, int k, int[] pamat, String[] spojeny_solutions, String[] final_solutions, int i) {
        //krizenie
        final_solutions[2 * i] = spojeny_solutions[pamat[2 * i] - 1].substring(0, rnd) + spojeny_solutions[pamat[2 * i + 1] - 1].substring(rnd, k);
        final_solutions[2 * i + 1] = spojeny_solutions[pamat[2 * i + 1] - 1].substring(0, rnd) + spojeny_solutions[pamat[2 * i] - 1].substring(rnd, k);
    }

    public static void OtherPairChoose(int gen, int[] pamat, int i, int rnd, int k, String[] spojeny_solutions, String[] final_solutions) {
        //generovanie prveho z dvojice
        int rnd1 = (int) (Math.random() * (gen / 2) + 1);
        int opak1 = 1;

        while (opak1 == 1) {
            for (int j = 0; j < pamat.length; j++) {
                if (rnd1 == pamat[j] || rnd1 == pamat[0]) {
                    rnd1 = (int) (Math.random() * (gen / 2) + 1);
                    j = 0;
                } else {
                    opak1 = 0;
                }
            }
        }
        pamat[2 * i] = rnd1;

        //vyber druheho z dvojice
        int rnd2 = (int) (Math.random() * (gen / 2) + 1);
        int opak2 = 1;

        while (opak2 == 1) {
            for (int j = 0; j < pamat.length; j++) {
                if (rnd2 == pamat[j] || rnd2 == pamat[0]) {
                    rnd2 = (int) (Math.random() * (gen / 2) + 1);
                    j = 0;
                } else {
                    opak2 = 0;
                }
            }
        }
        pamat[2 * i + 1] = rnd2;
    }

    public static void TwoPointCross(int gen, int rnd, int k, int[] pamat, String[] spojeny_solutions, String[] final_solutions, int i, int rnd2kriz) {
        //krizenie
        final_solutions[2 * i] = spojeny_solutions[pamat[2 * i] - 1].substring(0, rnd) + spojeny_solutions[pamat[2 * i + 1] - 1].substring(rnd, rnd2kriz) + spojeny_solutions[pamat[2 * i] - 1].substring(rnd2kriz, k);
        final_solutions[2 * i + 1] = spojeny_solutions[pamat[2 * i + 1] - 1].substring(0, rnd) + spojeny_solutions[pamat[2 * i] - 1].substring(rnd, rnd2kriz) + spojeny_solutions[pamat[2 * i + 1] - 1].substring(rnd2kriz, k);
    }

    public static void Mutation(int gen, int k, int rmut, int prah, char[] final_solutions_char) {
        for (int i = 0; i < gen * k; i++) {
            int rndmut = (int) (Math.random() * (rmut + 1));
            if (rndmut > prah) {
                if (final_solutions_char[i] == '1') {
                    final_solutions_char[i] = '0';
                } else {
                    final_solutions_char[i] = '1';
                }
            }
        }
    }

    public static void StrtoBucket(int gen, int k, String[] final_solutions_strings, String final_solutions_str) {
        for (int i = 0; i < gen; i++) {
            final_solutions_strings[i] = final_solutions_str.substring(i * (k), i * (k) + (k));
        }
    }

    public static void ChartoSolutions(int gen, int k, char[] final_solutions_char, int[][] solutions) {
        for (int i = 0; i < gen; i++) {
            for (int j = 0; j < k; j++) {
                solutions[i][j] = Character.getNumericValue(final_solutions_char[i * k + j]);
            }
        }
    }

    public static void CountValues(int gen, int[] bucket_weight, int[] bucket_points, int k, char[] final_solutions_char, int[] weights, int[] points) {
        for (int i = 0; i < gen; i++) {
            bucket_weight[i] = 0;
            bucket_points[i] = 0;

            for (int j = 0; j < k; j++) {
                if (final_solutions_char[i * k + j] == '1') {
                    bucket_weight[i] = (int) (bucket_weight[i] + weights[j]);
                    bucket_points[i] = (int) (bucket_points[i] + points[j]);
                }
            }
        }
    }

    public static void FindBest(int[] error, int best_error, int k, int [] best_solution, int [][] solutions, int best_points, int best_weight, int[] bucket_points, int[] bucket_weight){
        if (error[0] < best_error) {
            System.arraycopy(solutions[0], 0, best_solution, 0, k);

            best_error = error[0];
            best_points = bucket_points[0];
            best_weight = bucket_weight[0];
        }
    }
    public static void OutputCrossNumber(int kriz) {
        if (kriz == 1) {
            System.out.println("zvolene " + kriz + "-bodove krizenie");
        } else {
            System.out.println("zvolene " + kriz + "-bodove krizenie");
        }
    }
}