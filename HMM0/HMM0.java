import java.util.*;
import java.io.*;

public class HMM0 {

		private double [][] a;
		private List<Integer> dimA;
		
		private double [][] b;
		private List<Integer> dimB;

		private double [] pi;
		private List<Integer> dimPI;

		private List<Integer> finalDim;

		private double [] testPI = {0.0 , 0.0 , 0.0 , 1.0};

		public List<Integer> getdimA(){
			return this.dimA;
		}
		public List<Integer> getdimB(){
			return this.dimB;
		}
		
		public List<Integer> getdimPI(){
			return this.dimPI;
		}
		public List<Integer> getfinalDim(){
			return this.finalDim;
		}
		public double[][] getA(){
			return this.a;
		}
		public double[][] getB(){
			return this.b;
		}
		public double[] getPI(){
			return this.pi;
		}
		public void setA(List<Double> val, List<Integer> dim) {

			this.a = new double[dim.get(0)][dim.get(1)];
			this.dimA = new ArrayList<Integer> (dim);
			int k = 0;

		for(int i = 0; i < dim.get(0); i++) {
    		for(int j = 0; j < dim.get(1); j++) {
        // read information from somewhere
    				//System.out.print(val.get(k));
    				this.a[i][j] = val.get(k);
    				k++;
    			}
    		}
    	}
    

   		public void setB(List<Double> val, List<Integer> dim) {
   			this.b = new  double[dim.get(0)][dim.get(1)];
   			this.dimB = new ArrayList<Integer> (dim);
			int k = 0;

		for(int i = 0; i < dim.get(0); i++) {
    		for(int j = 0; j < dim.get(1); j++) {
        // read information from somewhere
    				//System.out.print(val.get(k));
    				this.b[i][j] = val.get(k);
    				k++;
    			}
    		}

   		}

   		public void setPI(List<Double> val, List<Integer> dim) {
   			this.dimPI = new ArrayList<Integer> (dim);
			this.pi = new double[dim.get(1)];
			int k = 0;

		for(int i = 0; i < dim.get(1); i++) {
    				this.pi[i] = val.get(k);
    				k++;
    			}
    		

    	}
    
 

		public void readFile(){
			List<Double> array = new ArrayList<Double>();
			List<Integer> dimensions = new ArrayList<Integer>();

			int x = 0; 
			int n = 0;
	

			try {

			//Scanner numFile = new Scanner(new File("sample_00.in"));
			Scanner numFile = new Scanner(System.in);
			while (numFile.hasNextLine()){

    			String line = numFile.nextLine();
    			Scanner sc = new Scanner(line);
    			sc.useDelimiter(" ");
    			//System.out.println(line);

    			while(sc.hasNext()) {
    				 if (n < 2 ) {
            			int i = Integer.parseInt(sc.next());
    					dimensions.add(i);
    					n++;
    				
       			 	}

       			 	else {

       			 	Double d = Double.parseDouble(sc.next());
    				//System.out.println(d);
    				array.add(d);
    				n++;

        			}

        			
    				
    			}
 				

 				switch (x) {            
                case 0 : setA(array, dimensions); 
                		 array.clear();
                		 dimensions.clear();
                		 n=0;
                		 break;
                case 1 : setB(array, dimensions);
                		 array.clear();
                		 dimensions.clear();
                		 n=0;
                		 break;
                case 2 : setPI(array, dimensions);
                		 array.clear();
                		 dimensions.clear();
                		 n=0;  
                		 break;
                
                
     
            	}
            x++;
            
            //System.out.print(array);
    		
    		
    		sc.close();
			}
			}

		catch (Exception e) {
    		
		}


		
    	}


		public double[] calculate(List<Integer> dimA, List<Integer> dimB, double [] a, double [][] b){
		 double c = 0;
		 double rowsum = 0;

		 double prod[]  = new double[dimB.get(1)];

		for (int k = 0; k < dimB.get(1); k++){
		
			for (int r =0; r < dimB.get(0); r++){
				
				c = a[r] * b[r][k];
				//System.out.println(c);
				
				prod[k] = prod[k] + c;

			}

			c = 0;
		
		}

		List<Integer> newDim = new ArrayList<Integer>();
		newDim.add(dimA.get(0));
		newDim.add(dimB.get(1));
		this.finalDim = new ArrayList<Integer> (newDim);

		return prod;


	}

		public static void main(String args[]) {



				HMM0 h = new HMM0();
				h.readFile();


				double[] x = h.calculate(h.getdimPI(), h.getdimA() ,h.getPI(),h.getA());

				//double[] y = h.calculate(h.getdimPI(), h.getdimB(), h.getPI(), h.getB());

				double[] ans = h.calculate(h.getfinalDim(),h.getdimB(),x, h.getB());

				String finalanswerString = "";

				List<Integer> dimensions = h.getfinalDim();

				finalanswerString = finalanswerString + String.valueOf(dimensions.get(0)) + " " + String.valueOf(dimensions.get(1)) + " "; 

				for (int z = 0; z < ans.length; z++){
					finalanswerString = finalanswerString + String.valueOf(ans[z]) + " ";
				}
				System.out.println(finalanswerString);
			



		}
}
