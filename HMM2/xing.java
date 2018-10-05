/**
 *
 * @author erlunds n Xing
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author erlunds n Xing
 */ 
public class xing {
    static List<Float[]> input = new ArrayList<>();
    public xing(){
    
    }
    public static Float[][] create_matrix(Float[] value) {
        int row = Math.round(value[0]);
        int col = Math.round(value[1]);
        Float[][] matrix = new Float[row][col];
        int counter = 2;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = value[counter];
                counter++;
            }
        }  
        return matrix;
    }
    public static List<Float[][]> create_all_matrix(List<Float[]> matrix_info) {
        Float total_row, total_col;
        List<Float[]> matrix_v = new ArrayList<>();
        List<Float[][]> all_matrix = new ArrayList<>();
        for (int i = 0; i < matrix_info.size()-1; i++) {
            Float[][] matrix = create_matrix(matrix_info.get(i));
            all_matrix.add(matrix);
        }
        return all_matrix;
    }
    
    
    public Float[][] calcMax(Float[][] a,Float[][] b,Float[][] pi, Float[] seq){
        int aCol = a[0].length;
        int aRow = a.length;
        int bCol = b[0].length;
        int bRow = b.length;
        int piCol = pi[0].length;
        int piRow = pi.length;

        Float[][] gamma = new Float[seq.length -1][aRow]; 
        int[][] argMax = new int[seq.length -1][aRow]; 
        int start_index = Math.round(seq[1]);
        float initMax = 0;
        int maxIndex = 0;
        int index = 0;



        for (int i = 0; i<bRow; i++){
            float gammaValue = pi[0][i]*b[i][start_index];
            gamma[0][i] = gammaValue;
            if(gammaValue>initMax){
                initMax = gammaValue; //ev lägg till vilken argmax det är här.
                index = i;
            }


        }
        //argMax[0][index] = index;
        for(int j = 1; j<seq.length -1;j++){ //for each time step t
            int observation = Math.round(seq[j+1]); //seq[j] kan ev. vara av typen float
            for(int i = 0; i<bRow;i++){ //for each state i
                float bValue = b[i][observation]; //får ut värdet av b givet en observationn
                System.out.print(bValue);
                float max = 0;
                int argIndex = 0;
                for (int k = 0; k<aCol; k++){
                    float value = bValue*a[k][i]*gamma[j-1][k];
                    if(value>max){
                        max = value; //ev lägg till vilken argmax det är här.
                        argIndex = k;

                    }
                }
                
                argMax[j][i] = argIndex;
                gamma[j][i] = max;
            }
        
        }

       calcStateSeq(gamma,argMax);
       return gamma;
    }
    
    public void calcStateSeq (Float[][] gamma, int[][] argMax) {
        int index = 0;
        int[] indexList = new int[argMax.length];
        for(int i = argMax.length-1; i>=0;i--){
            
            if(i == argMax.length-1){
                float max = 0;
                
                for(int k = 0; k<gamma[0].length; k++){
                    float val = gamma[i][k];
                    if(max<val){
                        index = k;
                        max = val;

                    }
                    
                }
            }
            //System.out.println(index + " här");
            indexList[i] = index;
            index = argMax[i][index];
        }
    
    for(int val:indexList){
        System.out.println(val);
    }
    }


    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileReader("hmm2.in"));
        //Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] elements = line.split(" ");
            Float[] value = new Float[elements.length];
            int i = 0;
            for (String s : elements) {
                float res = Float.parseFloat(s);
                value[i] = res;
                i++;
            }
        input.add(value);

        }
        xing test = new xing();
        List<Float[][]> all_matrix = xing.create_all_matrix(input);
        Float[][] a = all_matrix.get(0);
        Float[][] b= all_matrix.get(1);
        Float[][] pi= all_matrix.get(2);
        Float[] eSequence = input.get(3);
        Float[][] prob_result = test.calcMax (a,b,pi,eSequence);
        /*for(Float[] prob1:prob_result){
            System.out.println(Arrays.toString(prob1));
        }*/

        
    }
}