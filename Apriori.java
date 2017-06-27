import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * skeleton class for the project 1 of the LINGI2355 course.
 *
 * This class and the Dataset class are given to you as a skeleton for your implementation of the Apriori algorithm.
 * You are not obligated to use them and are free to write any class or method as long as the following requirements
 * are respected:
 *
 * Your apriori methods must take as parameters a string corresponding to the path to a valid dataset file and
 * a double corresponding to the minimum frequency.
 * You must write on the standard output (System.out) all the itemsets that are frequent in the dataset file according
 * to the minimum frequency given. Each itemset has to be printed on one line following
 * the format: [<item 1>, <item 2>, ... <item k>] (<frequency>).
 * Tip: you can use Arrays.toString(int[] a) to print the itemset.
 *
 * @author Syntyche Shimbi Amunaso
 */



public class Apriori {
	static Dataset dataset ;
	static Double minFrequency;
	static String filepath;
    static int number_of_items;
    static int number_of_transactions;
    static ArrayList<String> candidates = new ArrayList<String>();
    static ArrayList<String> frequent_candidates = new ArrayList<String>();
    static ArrayList<String> temp = new ArrayList<String>();
    
    public static void main(String[] args) {
        if(args.length != 2) {
        	System.out.println("Incorrect number of arguments! Aborting execution.");
        }

        String filepath = args[0];
        minFrequency = Double.parseDouble(args[1]);
        //apriori1(filepath, minFrequency);
        dataset = new Dataset(filepath);
        number_of_items = dataset.itemsNum();
        number_of_transactions = dataset.transNum();
        
        
   	 	// Returns the number of different items in the dataset.
   	    
        System.out.println("Nber of items in the input dataset");
   	 	System.out.println(dataset.itemsNum());
   	 
   	 	//return the nber of transactions in the dataset
   	 	System.out.println("the nber of transactions in the input dataset");
   	 	System.out.println(dataset.transNum());
   	 	
   	    apriori1(filepath, minFrequency);
   	    
   	    
    }

    static String s1, s2;
	static StringTokenizer str_token1, str_token2; 
	
    /**
     * Generate candidates for level 1 by including in the global variable( temporary candidates array ) every distinct item of the dataset 
     */
    
    public static void generate_level1(){
    	
    	for( int k = 0; k < number_of_items ; k++){
    		temp.add(Integer.toString(k+1));	
		}
    }
    
    /**
     * Generate level2 candidate set  by combining all the items candidates from the first level
     * these candidates are appended to the temp ArrayList
     */
    
    public static void generate_level2(){

    	for(int z = 0; z < candidates.size(); z++)
    	{
    		str_token1 = new StringTokenizer(candidates.get(z)); 
    		s1 = str_token1.nextToken();
    		for(int w = z+1; w < candidates.size(); w++)
    		{
    			str_token2 = new StringTokenizer(candidates.get(w));  
    			s2 = str_token2.nextToken();
    			temp.add(s1+ " " + s2);
    			
    		}
    	}
    }

    /**
     * Generate other levels of the apriori search other than the first and the second level
     * @param level is the level in the apriori search
     * */
    	
    public static void generate_leveli(int level){

    	for( int x = 0; x < candidates.size(); x++ )
    	{
    		for( int y = x+1; y < candidates.size(); y++)
    		{
    			s1 = new String();
    			str_token1 = new StringTokenizer(candidates.get(x));
    			s2 = new String();
    			str_token2 = new StringTokenizer(candidates.get(y));
    			
    			for(int k = 0; k < level-2; k++)
    			{
    			
    				s1 = s1 + " "+ str_token1.nextToken();
    				s2 = s2 + " "+ str_token2.nextToken();
    				
    			}
    			//Join them if they have the same level-2 tokens 
    			if(s2.compareToIgnoreCase(s1) == 0 )
    			{
    				temp.add( (s1 + " "+ str_token1.nextToken()+" "+ str_token2.nextToken()).trim());
    			}
    			
    		}
    		
    	}
    }
    	
    
    /**
     * Generate candidates Ci et level i from the dataset
     * @param level: the level of the Apriori Search
     * */
    public static void generate_candidates(int level){
    	
    	if (level == 1){
    		generate_level1();
    	}
    	else if (level == 2 ){
    		generate_level2();
    		
    	}
    	else{
    		generate_leveli(level);
    		
    	}
    	
    	candidates.clear();
    	candidates = new ArrayList<String>(temp);
    	temp.clear();
    	
    }
  
    
    /**
     * Converts a transaction into a boolean array
     * */
    
    /*Attention data-1 because transaction indices starts from 1 and not 0*/
    public static boolean[] convert_transaction_to_boolean_array(int id){
    	
    	boolean[] bool_trans = new boolean[number_of_items];
    	Arrays.fill(bool_trans, false);
    	
    	for(int x = 0; x < dataset.getTransaction(id).length; x++)
    	{
    		int data         = dataset.getTransaction(id)[x];
    		bool_trans[data - 1] = true;
    	}
    	
    	return bool_trans;
    	
    }
    
public static void get_frequent_itemset(){
    	
    	ArrayList<String> frequent_candidates = new ArrayList<String>(); //frequent candidates for the current itemset
    	StringTokenizer stringToken; //tokenizer for candidate and transaction
    	boolean correspond; //true if a transaction holds every item in the given itemset
    	boolean[] transaction_in_boolean = new boolean[number_of_items]; //matrix representatio of transaction 
    															    //which allows the transaction to be easily processed
    	int nber_of_matches[] = new int[candidates.size()]; //the number of successful matches
    	
    	
    	for(int j=0; j< number_of_transactions; j++)
        {
    		transaction_in_boolean = convert_transaction_to_boolean_array(j);

    		//process temp
    		for( int cand = 0; cand < candidates.size(); cand++ )
    		{
    			correspond = false;
    			stringToken = new StringTokenizer(candidates.get(cand));
    			//determine whether an itemset is included in a transaction j
    			while(stringToken.hasMoreTokens())
    			{
    				correspond = transaction_in_boolean[Integer.valueOf(stringToken.nextToken())-1];
    				if(!correspond)
    					break;
    			}
    			if(correspond)
    				nber_of_matches[cand]++;	
    		}
         }
    	
    	for(int x = 0; x < candidates.size(); x++){
    		
    		if(( nber_of_matches[x]/(double)number_of_transactions) >= minFrequency)
    	    {
    			String cand = candidates.get(x);
    			frequent_candidates.add(cand); 
    			System.out.println("[" + candidates.get(x) + "] " + "(" + nber_of_matches[x]/(double)number_of_transactions + ")" + "\n");
    	
    	    }
    	}
    	
    	candidates.clear();	
    	candidates = new ArrayList<String>(frequent_candidates);
    	frequent_candidates.clear();
    	
    }

    /**
     * 
     * @param filepath The path to a valid dataset file.
     * @param minFrequency the minimum frequency for an itemset to be considered as frequent.
     */
    public static void apriori1(String filepath, double minFrequency){
    	int level = 0;
        long start_time, end_time;
        //Starts counting the time
        start_time = System.currentTimeMillis();
        
        do{
        	level++;
        	
        	// generate candidate for level i
        	generate_candidates(level);
        	
        	//get_frequent_itemset(level, candidates);
        	get_frequent_itemset();
        	
         }
        while(candidates.size() > 0);
        
        end_time = System.currentTimeMillis();
        System.out.println("Time for execution: "+ (end_time - start_time));
     }

   
}
