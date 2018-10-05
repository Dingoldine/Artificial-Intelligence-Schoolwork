		public double[][] betaPass(int[] o) {
	    int T = o.length;
	    double[][] bwd = new double[numStates][T];
	        
	    /* initialization (time 0) */
	    for (int i = 0; i < numStates; i++){
	      bwd[i][T-1] = 1;
	    }


	    /* induction */
	    for (int t = T - 2; t >= 0; t--) {
	      for (int i = 0; i < numStates; i++) {
			bwd[i][t] = 0;
			for (int j = 0; j < numStates; j++)
		  	bwd[i][t] += (bwd[j][t+1] * a[i][j] * b[j][o[t+1]]);
	      	}
	    	}


	   // Prints all betas at time t
	     for(int j=0; j< o.length; j++){
	     System.out.print("\n");
	     for (int i = 0; i < numStates; i++){
	  	  System.out.println(bwd[i][j]);
	      }
	      }


	    	return bwd;
	  	}
