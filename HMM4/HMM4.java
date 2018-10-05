import java.util.*;
import java.io.*;
import java.math.*;

public class HMM4 {

		private double [][] a;
		private List<Integer> dimA;
		private int numStates;
		
		private double [][] b;
		private List<Integer> dimB;

		private double [] pi;
		private List<Integer> dimPI;

		private List<Integer> finalDim;

		private int[] observations;
		private int numObs;

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
		public int[] getObs(){
			return this.observations;
		}
		public int getnumStates(){
			return this.numStates;
		}
		
		
		private void setA(List<Double> val, List<Integer> dim) {
			this.numStates = dim.get(0);
			this.a = new double[numStates][numStates];
			this.dimA = new ArrayList<Integer> (dim);
			int k = 0;

		for(int i = 0; i < numStates; i++) {
    		for(int j = 0; j < numStates; j++) {
        // read information from somewhere
    				//System.out.print(val.get(k));
    				this.a[i][j] = val.get(k);
    				k++;
    			}
    		}
    		//System.out.println(" A has been set");
    	}
    

   		private void setB(List<Double> val, List<Integer> dim) {
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
    		//System.out.println("B has been set");

   		}

   		private void setPI(List<Double> val, List<Integer> dim) {
   			this.dimPI = new ArrayList<Integer> (dim);
			this.pi = new double[dim.get(1)];


		for(int i = 0; i < dim.get(1); i++) {
    				this.pi[i] = val.get(i);
    			}
    		//System.out.println(" PI has been set");

    	}

    	private void setObs(int l, List<Integer> val){
    		this.observations = new int[l];
    		this.numObs = l;

			for(int i = 0; i < l; i++) {
    				this.observations[i] = val.get(i);
    			}
    		//System.out.println("Observation sequence set");

    	}
    
 

		public void readFile(){
			List<Double> array = new ArrayList<Double>();
			List<Integer> dimensions = new ArrayList<Integer>();
			List<Integer> observations = new ArrayList<Integer>();
			int numObs = 0;
			int numLine =0;
			int n = 0;

			try {

			Scanner numFile = new Scanner(new File("sample3.in"));
			//Scanner numFile = new Scanner(System.in);
			while (numFile.hasNextLine()){
				String line = numFile.nextLine();
    			Scanner sc = new Scanner(line);
    			sc.useDelimiter(" ");

				if (numLine<3){
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
    		
 				switch (numLine) {            
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
    			sc.close();
    			numLine++;
    			}
    			

    			else {

    				while(sc.hasNext()) {
    				 if (n < 1 ) {
            			numObs = Integer.parseInt(sc.next());
    					n++;
    				
       			 	}

       			 	else {

       			 	observations.add(Integer.parseInt(sc.next()));

        			}
    				
    			}

    			n=0;
    			numLine=0;
    			setObs(numObs, observations);
    				
    			}

    
            
            //System.out.print(array);
    		
    		

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


 
		public double[][] alphaPass(int[] o) {
	    int T = o.length;
	    double[][] alphas = new double[numStates][T];
	        
	    /* initialization (time 0) */
	    for (int i = 0; i < numStates; i++){
	      alphas[i][0] = pi[i] * b[i][o[0]];

	      }

	    /* induction */
	    for (int t = 0; t <= T-2; t++) {
	      for (int j = 0; j < numStates; j++) {
				alphas[j][t+1] = 0;
				
				for (int i = 0; i < numStates; i++){
		  		alphas[j][t+1] += (alphas[i][t] * a[i][j]);
		  		}
		  	alphas[j][t+1] *= b[j][o[t+1]];	
	      }
	    }

	    //Prints all alphas at time t
	     // for(int j=0; j<8; j++){
	     // System.out.print("\n");
	     // for (int i = 0; i < numStates; i++){
	  	  // System.out.println(alphas[i][j]);
	     //  }
	     //  }

	      return alphas;
		}

		public double[][] betaPass(int[] o) {
	    int T = o.length;
	    double[][] betas = new double[numStates][T];
	        
	    /* initialization (time 0) */
	    for (int i = 0; i < numStates; i++){
	      betas[i][T-1] = 1;
	    }


	    /* induction */
	    for (int t = T - 2; t >= 0; t--) {
	      for (int i = 0; i < numStates; i++) {
			betas[i][t] = 0;
			for (int j = 0; j < numStates; j++)
		  	betas[i][t] += (betas[j][t+1] * a[i][j] * b[j][o[t+1]]);
	      	}
	    	}


	   // Prints all betas at time t
/*	     for(int j=0; j< o.length; j++){
	     System.out.print("\n");
	     for (int i = 0; i < numStates; i++){
	  	  System.out.println(betas[i][j]);
	      }
	      }*/


	    	return betas;
	  	}


public double[][] estimateA(double[][]gamma, double[][]digamma){
	double[][] a = new double[numStates][numStates];
	int val = 0;
	int div = 0;
		for(int i = 0; i < numStates; i++) {
    		for(int j = 0; j < numStates; j++) {
    			val = 0;
    			div = 0;
    			for (int t=0; t < numObs;t++) {
    				val += digamma[i][t];
    				div += gamma[i][t];
    				
    			}
        // read information from somewhere
    				//System.out.print(val.get(k));
    			if (div == 0) {
    			a[i][j] = 0;
    				
    			}
    			else {
    			a[i][j] = val/div;
    			}
    			System.out.print("\n");
    			System.out.print("dehär är a: " + a[i][j]);
    			}
    		}


	return a;
}

  public double[][] gamma(double[][] digamma){
  	int T = numObs;
  	double[][] gamma = new double[numStates][T];
  	double val = 0; 

  	for (int t=0; t<numObs; t++){
  		for (int i = 0; i < numStates; i++) {
  			for (int j = 0; j < numStates ;j++ ){
  				val =+ digamma[i][j];

  			}

    			gamma[i][t] = val;

  		}

  	}
	     for(int j=0; j< T; j++){
	     System.out.print("\n");
	     for (int i = 0; i < numStates; i++){
	  	  System.out.println(gamma[i][j]);
	      }
	      }

  	return gamma;

  }

  public double[][] digamma(double[][] alpha, double[][] beta, int [] o){
  	int T = o.length;
  	double[][] digamma = new double[numStates][T];
  	double val = 0;
  	double div = 0;
  	for (int t=0; t<T; t++){
  		for (int i = 0; i < numStates; i++) {
  				val = 0;
  				div = 0;
  			for (int j = 0; j < numStates ;j++ ) {
  				if (t == T-1){
  					val = alpha[i][t] * a[i][j];
  					div =+ alpha[j][t] * beta[j][t];
  				}
  				else {
  				val = alpha[i][t] * a[i][j] * b[j][o[t+1]] * beta[j][t+1];
  				div =+ alpha[j][t] * beta[j][t];
  				}

  			}
  			if(div==0){
  				digamma[i][t] = 0;
  			}
  			else{
  				digamma[i][t] = val/div;
  			}
  			
  		}
  		
  	}

  		   // Prints all digamma at time t
  	System.out.print("digamma");
	     for(int j=0; j< T; j++){
	     System.out.print("\n");
	     for (int i = 0; i < numStates; i++){
	  	  System.out.println(digamma[i][j]);
	      }
	      }


  	return digamma;

  }




  public int[] Viterbi(int[] o) {
  // Initialization
  	 int T = o.length;
  	 double[][] delta = new double[numStates][T];
  	 int[][] backtracker = new int[numStates][T];

	  for (int i = 0; i < numStates; i++){
	  	delta[i][0] = pi[i] * b[i][o[0]];
	  	backtracker[i][0] = 0; 

	  }

	  /* induction */

	    for (int t = 1; t < T ; t++) {
	      for (int j = 0; j < numStates; j++) {

				int maxState = 0;
				double maxprod = 0; 

				for (int i = 0; i < numStates; i++){
		  		
		  		double prod = (delta[i][t-1] * a[i][j]*b[j][o[t]]);
	
		  		if (prod > maxprod) {
		  			maxState = i; 
		  			maxprod = prod;

		  			}	
		  		}

		  	delta[j][t] = maxprod;
		  	backtracker[j][t] = maxState;
		  	}
	      
	    }


	  // Termination step
	 double best_prob = 0;
	 int last_backptr = T-1;
	 for ( int j=0; j< numStates; j++ ) {
	     double m = delta[j][T-1]; //* a[T-1][j];
	     	if ( m > best_prob ) {
	 		best_prob = m;
	 		//System.out.print(m+ " " + j);
	 		last_backptr = j;
	     	} 
	 }

	 // Finally return the result
	 int[] c = new int[T];
	int current = last_backptr;
	for ( int t= T-1; t>=0; t-- ) {
		//c[t] = o[current];
	    c[t] = current;
	    current = backtracker[current][t];

	}

/*		   	for(int j=0; j<o.length; j++){
	     System.out.print("\n");
	     for (int i = 0; i < numStates; i++){
	  	  System.out.println(backtracker[i][j]);
	      }
	      }

	  	  // Prints all delta at time t
	     for(int j=0; j<o.length; j++){
	     System.out.print("\n");
	     for (int i = 0; i < numStates; i++){
	  	  System.out.println(delta[i][j]);
	      }
	      }*/

	return c;  
    


  }


		public static void main(String args[]) {

				double ans = 0;

				HMM4 h = new HMM4();
				h.readFile();
				double[][] alphaMatrix = h.alphaPass(h.getObs());
				double[][] betaMatrix = h.betaPass(h.getObs());
				double[][] digamma = h.digamma(alphaMatrix, betaMatrix, h.getObs());
				double[][] gamma = h.gamma(digamma);

				double[][] newA = h.estimateA(gamma, digamma);

	      		//double [][] y = h.betaPass(h.getObs());
				//System.out.println(x);


		}
}
