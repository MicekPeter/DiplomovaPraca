/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomovapraca;

/**
 *
 * @author Peter Micek
 */
public class DiplomovaPraca {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        //inicializacia
        //nastavitelne premenne
        int max_w = 2; //maximalna vaha predmetu (weight)
        int min_w = 1; //minimalna vaha predmetu (weight)
        int max_p = 40; //maximalne bodove ohodnotenie (points)
        int min_p = 1; //minimalne bodove ohodnotenie (points)
        int max_b = 50; //objem vedra (bucket)
        int k = 10; //pocet predmetov
        int l = 5; //pocet vedier
        int p = 500; //pocet iteracii (zacina od 0)
        int rmut = 100; //rozsah pre generovanie mutacie (interval 0 - rmut)
        int prah = 60; //prah mutacie
        int gen = 12; //pocet vygenerovani vektorov naplnenia (musi byt delit 4)
        int kriz = 1; //kolko bodove krizenie ma prebiehat ('1' = jednobodove krizenie | '2' = dvojbodove krizenie)
        int sale_weight = 1; //vyber vahy predmetu na akciu (ak 0 tak nieje ziadna akcia)
        float sale_percentage = 199; //velkost zvysenia bodov v percentach

        // TODO: Vlozit to do funckie

        //int
        int best_points = 0;
        int best_weight = 0;
        int best_error = k * max_w;

        //int[]
        int[] bucket_weight = new int[gen * l];
        int[] bucket_points = new int[gen * l];
        int[] temp_arr = new int[gen];
        int[] weights = new int[k * l];
        int[] points = new int[k * l];
        int[] bucket = new int[k * l];
        int[] error = new int[gen];
        int[] best_solution = new int[k];
        int[] pamat = new int[gen / 2];

        //String[]
        String[] spojeny_solutions = new String[(gen / 2)];
        String[] final_solutions = new String[gen];
        String[] final_solutions_strings = new String[gen];

        //int[][]
        int[][] solutions = new int[gen][k * l];
        int[][] solutions_50 = new int[gen / 2][k];

        //Zaciatok programu
        for (int t = 0; t < p; t++) {
            System.out.println("Iteracia: " + t + " ------------------------------------------------------------------------------------------------");

            if (t == 0) {
                //generovanie prvkov
                Funkcie.GenerateItems(k, l, max_w, min_w, max_p, min_p, weights, points);
                
                //pocitanie akcie
                if (sale_weight != 0){
                    Funkcie.SaleCount(k, l, weights, points, sale_weight, sale_percentage);
                }
                
                //generovanie vedier
                Funkcie.GenerateBuckets(gen, k, l, bucket, bucket_weight, weights, bucket_points, points, solutions);

                //pocitanie chyby
                Funkcie.ErrorCount(gen, max_b, error, bucket_weight);

                //triedenie
                Funkcie.Sort(gen, error, temp_arr, bucket_weight, bucket_points, solutions);

                //vyber najlepsieho
                System.arraycopy(solutions[0], 0, best_solution, 0, k);
                best_error = error[0];
                Funkcie.Output(bucket_weight, best_error, error, bucket_points, best_solution);
            }
            //vyber top 50 percent    
            Funkcie.Top50(gen, solutions, solutions_50, k);

            //CROSSOVER   
            //mazanie pamate
            Funkcie.MemErase(pamat);

            // vytvarame pole stringov
            Funkcie.StringConvert(gen, k, solutions_50, spojeny_solutions);

            //JEDNOBODOVE krizenie
            if (kriz == 1) {
                for (int i = 0; i < gen / 4; i++) {
                    //generovanie nahodneho cisla na krizenie
                    int rnd = (int) (Math.random() * (k - 1) + 1);

                    //vyber prvej dvojice a krizenie
                    if (i == 0) {
                        Funkcie.FirstPairChoose(gen, pamat);
                        Funkcie.SinglePointCross(gen, rnd, k, pamat, spojeny_solutions, final_solutions, i);
                    } //vyber ostavajucich dvojic a krizenie
                    else {
                        Funkcie.OtherPairChoose(gen, pamat, i, rnd, k, spojeny_solutions, final_solutions);
                        Funkcie.SinglePointCross(gen, rnd, k, pamat, spojeny_solutions, final_solutions, i);
                    }
                }
            }
            //DVOJBODOVE krizenie
            if (kriz == 2) {
                for (int i = 0; i < gen / 4; i++) {
                    //generovanie nahodnych cisel na krizenie       
                    int rnd = (int) (Math.random() * (k - 2) + 1);
                    int rnd2kriz = (int) (Math.random() * (k - 1) + 1);

                    while (rnd2kriz <= rnd) {
                        rnd2kriz = (int) (Math.random() * (k - 1) + 1);
                    }
                    //vyber prvej dvojice a krizenie
                    if (i == 0) {
                        Funkcie.FirstPairChoose(gen, pamat);
                        Funkcie.TwoPointCross(gen, rnd, k, pamat, spojeny_solutions, final_solutions, i, rnd2kriz);
                    } //vyber ostavajucich dvojic a krizenie
                    else {
                        Funkcie.OtherPairChoose(gen, pamat, i, rnd, k, spojeny_solutions, final_solutions);
                        Funkcie.TwoPointCross(gen, rnd, k, pamat, spojeny_solutions, final_solutions, i, rnd2kriz);
                    }
                }
            }
            //pridanie povodnych jedincov k potomkom
            System.arraycopy(spojeny_solutions, 0, final_solutions, gen / 2, gen / 2);

            //prevod stringu na char kvoli mutacii
            String string = "";
            for (String i : final_solutions) {
                string = string + i;
            }
            char[] final_solutions_char = string.toCharArray();

            //MUTACIA
            Funkcie.Mutation(gen, k, rmut, prah, final_solutions_char);

            //prevod char naspat na string
            String final_solutions_str = String.copyValueOf(final_solutions_char);

            //rozdelenie stringu na vedra
            Funkcie.StrtoBucket(gen, k, final_solutions_strings, final_solutions_str);

            //prevod z final_solutions_char [0,1,1,0,1,...] na solutions[[01101][11001]...]
            Funkcie.ChartoSolutions(gen, k, final_solutions_char, solutions);

            //vypocet vah a bodov      
            Funkcie.CountValues(gen, bucket_weight, bucket_points, k, final_solutions_char, weights, points);

            //pocitanie chyby
            Funkcie.CountError(gen, max_b, bucket_weight, error);

            //triedenie
            Funkcie.Sort(gen, error, temp_arr, bucket_weight, bucket_points, solutions);

            //najdenie najlepsieho
            Funkcie.FindBest(error, best_error, k, best_solution, solutions, best_points, best_weight, bucket_points, bucket_weight);
//            if (error[0] < best_error) {
//                System.arraycopy(solutions[0], 0, best_solution, 0, k);
//
//                best_error = error[0];
//                best_points = bucket_points[0];
//                best_weight = bucket_weight[0];
//            }
            Funkcie.Output(bucket_weight, best_error, error, bucket_points, best_solution);
        }
        Funkcie.OutputCrossNumber(kriz);
    }
    
}
