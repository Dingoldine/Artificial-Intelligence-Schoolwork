		b[i][j] = Double.NEGATIVE_INFINITY;
	    }
	    // All neighbouring keys are assigned the probability 0.1
	    for ( int j=0; j<cs.length; j++ ) {
		b[i][RandomKey.charToIndex(cs[j])] = Math.log( 0.1 );
	    }
	    // The remainder of the probability mass is given to the correct key.
	    b[i][i] = Math.log( (10-cs.length)/10.0 );
	}
    }

    /**
     *  Performs the Viterbi decoding and returns the most likely
     *  string. 
     */
    String viterbi( String s ) {

	// First turn chars to integers, so that 'a' is represented by 0, 
	// 'b' by 1, and so on. 
	int[] index = new int[s.length()];
	for ( int i=0; i<s.length(); i++ ) {
	    index[i] = RandomKey.charToIndex( s.charAt( i ));
	}

	// The Viterbi matrices
	v = new double[index.length][RandomKey.NUMBER_OF_CHARS];
	backptr = new int[index.length+1][RandomKey.NUMBER_OF_CHARS];

	// Initialization
	for ( int i=0; i<RandomKey.NUMBER_OF_CHARS; i++ ) {
	    v[0][i] = a[RandomKey.START_END][i]+b[index[0]][i]; 
	    backptr[0][i] = RandomKey.START_END;
	}


	// Induction step
	
	//For each letter in the string
     for(int t = 1; t<index.length; t++) {
      v[t] = new double[RandomKey.NUMBER_OF_CHARS];

    //For each letter in the string
      for(int y = 0; y<RandomKey.NUMBER_OF_CHARS; y++) {
        double maxprob = Math.log(0); 			// - infinity
        int maxstate = 0;

    //For each transition probability, sannolikheten att man hoppar till nästa steg
        for(int x = 0; x<RandomKey.NUMBER_OF_CHARS; x++) {
          double prob = v[t-1][x] + a[x][y];
          if(prob > maxprob) {
            maxprob = prob;
            maxstate = x;
          }
        }

        v[t][y] = maxprob + b[y][index[t]]; 
        backptr[t][y] = maxstate;
      }
    }


	// Termination step
	double best_prob = Double.NEGATIVE_INFINITY;
	int last_backptr = RandomKey.START_END;
	for ( int j=0; j<RandomKey.NUMBER_OF_CHARS; j++ ) {
	    double m = v[index.length-1][j]+a[j][RandomKey.START_END];
	    if ( m > best_prob ) {
		best_prob = m;
		last_backptr = j;
	    } 
	}
	    
	// Finally return the result
	char[] c = new char[index.length];
	int current = last_backptr;
	for ( int t=index.length-1; t>=0; t-- ) {
	    if ( current >= RandomKey.key.length ) 
		c[t] = ' ';
	    else
		c[t] = RandomKey.key[current];
	    current = backptr[t][current];
	}
	print_trellis( c );
	return new String( c ).trim();  // Trim to remove the trailing space.
    }


    // ------------------------------------------------------
    // 
    // Some debugging methods
    //

    public void print_trellis( char[] c ) {
	for ( int t=0; t<c.length; t++ ) {
	    for ( int i=0; i<RandomKey.NUMBER_OF_CHARS; i++ ) {
		System.err.format( "%c ", RandomKey.indexToChar( backptr[t][i] ));
	    }
	    System.err.println();
	    if ( t<c.length-1 ) {
		print_connector( RandomKey.charToIndex(c[t]), RandomKey.charToIndex(c[t+1]) );
	    }
	    else {
		print_connector( RandomKey.charToIndex(c[t]), RandomKey.START_END );
	    }
	}
    }
    
    // Print the backpointers in an easy-to-read format
    public void print_connector( int start, int end ) {
	int i;
	for ( i=0; i<start; i++ ) {
	    System.err.print( "  " );
	}
	System.err.println( "|" );
	if ( start == end )
	    return;
	for ( i=0; i<Math.min(start,end); i++ ) {
	    System.err.print( "  " );
	}
	for ( int j=i; j<Math.max(start,end); j++ ) {
	    System.err.print( "--" );
	}
	System.err.println();
	for ( i=0; i<end; i++ ) {
	    System.err.print( "  " );
	}
	System.err.println( "|" );
    }

    // ------------------------------------------------------

    
    public Decoder( String filename ) {
	init_a( filename );
	init_b();
    }


    // ------------------------------------------------------
	

    public static void main( String[] args ) {
	Decoder d = new Decoder( args[0] );
	while ( true ) {
	    try {
		// The string we want to decode is input from the keyboard
		BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
		char[] c = in.readLine().toCharArray();
		// The following recoding is done because of the Windows command window
		// encoding.
		for ( int j=0; j<c.length; j++ ) {
		    int i = (int)c[j];
		    if ( i == 8221 )
			c[j] = 246;
		    if ( i == 8222 )
			c[j] = 228;
		    if ( i == 8224 )
			c[j] = 229;
		}
		String result = d.viterbi( new String( c ) + RandomKey.indexToChar( RandomKey.START_END ));
		System.out.println( result );
	    }
	    catch ( IOException e ) {
		e.printStackTrace();
	    }
	}
    }
}