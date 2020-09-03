import java.io.*;

//Written by Jacob Barnett on 12/7/2019

//A class to benchmark each of the WordCounters implementations
//As well as tear down each list once the benchmark is complete
//We are testing against multiple different text files
public class MainClass implements PerformanceTraceable
{
	//!!!!!
	//THIS IS WHERE YOU SELECT THE TEXT FILE YOU WANT TO TEST
	//All you have to do is copy and paste the files path to the location it is stored in on your C drive
	//An example is listed below between the parantheses
    private final static String FILE_NAME = "C:\\Users\\Jake\\Documents\\Eclipse\\text files\\Text-20M.txt";

    public static void main(String[] args) throws FileNotFoundException
    {
        //The main entry point of the program
        //the only parameter is the arguments passed on the command line
    	
    	
    	File textFile = new File(FILE_NAME);
    	
        System.out.println("\n");
        System.out.println("Test commencing...");
        System.out.println("\n");
    	
    	MainClass b = new MainClass(textFile);
    	b.getResourceInputStream(textFile);
    	
    	//initialize the benchmark procedure
        b.runAllBenchmarks(textFile);

        //Print results for normal benchmarks
        System.out.println("\n");
        System.out.println("Tests Complete.\n\n\nResults:\n");
        for (int i = 0; i < BENCHMARK_NAMES.length; i++)
        {
            System.out.print(BENCHMARK_NAMES[i] + " --- Duration:" + (double) b.results[i] / 1000d + " seconds");

            if (i != OVERHEAD)
            {
                WordCounter counter = b.counters[i - 1];

                System.out.print(",  Word Count:" + counter.getWordCount() +
                        ",  Distinct:" + counter.getDistinctWordCount() +
                        ",  Comparisons:" + counter.getComparisonCount() +
                        ",  Reference Changes:" + counter.getReferenceAssignmentCount());
            }
            System.out.println();
        }
        
        System.out.println();
        System.out.println("\n-----------------------------------------------------\n\n");
        
        System.out.println("The height of the Skip List is: " + b.counters[0].getSLHeight());
        System.out.println("The height of the Binary Tree is: " + b.counters[2].getBSTHeight(BinarySearchTreeWordCounter.root));
        
        System.out.println();
        System.out.println("\n-----------------------------------------------------\n\n");
        
        //printing out the first 100 words of the binary tree
        b.counters[2].displayFirst100();
    }
    
    //The names of all benchmarks
    public static final String[] BENCHMARK_NAMES = {
            "Overhead",
            "Skip List",
            "Hash Table 3",
            "Binary Search Tree",
            //"Front Self-Adjusting"
    };

    //All word counters to benchmark
    public final WordCounter[] counters = new WordCounter[]{
            new SkipListWordCounter(),
            new HashTableList3(),
            new BinarySearchTreeWordCounter(),
            //new FrontSelfAdjustingWordCounter()
    };

    
    //these are the value we will use for all of the lists to run their tests
    public static final int OVERHEAD = 0;
    
    //The path to the file to read. If left null, the file will be loaded from the classpath
    private final File TEST_FILE;
    
    //The token that separates words (Any whitespace)
    public static final String WORD_SEPARATOR = "[\\s]";
    
    //Time results are stored here
    private long[] results = new long[BENCHMARK_NAMES.length];
    
    public MainClass(File inputFile)
    {
    	//the constructor we will use to set the inputed text file
    	//to our TEST_FILE varibale
    	
        this.TEST_FILE = inputFile;
    }

    public void runBenchmark(InputStream in, WordCounter counter) throws IOException
    {
        //This is the benchmark method we will use to read the selcted text file line by line
    	//and then reading each line word for word where the words are separated by spaces
    	
    	//All leading and trailing punctuation will be cut, if any punctuation is left between the first and last
    	//character, then we keep it
    	
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            //Read the file line by line
            String line;
            while ((line = reader.readLine()) != null)
            {
            	//Iterate thorugh each line and optimize the line to be able to better read each word
                for (String word : line.split(WORD_SEPARATOR))
                {
                    String sanitized = Word.sanitize(word);

                    //If, after sanitizing the word, it is not empty, submit it to the word counter
                    if (!sanitized.isEmpty() && counter != null)
                    {
                        counter.encounter(sanitized);
                    }
                }
            }
        }
    }
    
    public InputStream getResourceInputStream(File text) throws FileNotFoundException
    {
        //The project spec requires a constant to appear towards the top of the benchmark class that points to the
        //path to the input file. In order to maintain compatibility on different workstations, default to getting
        //the input stream from the class path instead if the constant is null
        //@return an InputStream for the resource to benchmark against
        //@throws FileNotFoundException
    	
        return new FileInputStream(TEST_FILE);
    }

    public boolean runAllBenchmarks(File text)
    {
        //Runs all benchmarks
        //@return false iff there was an exception thrown while the benchmarks were running
    	
        try
        {
            //The first pass is just to calculate overhead
            System.out.print("Benchmarking " + BENCHMARK_NAMES[OVERHEAD] + "...");
            long start = System.currentTimeMillis();
            runBenchmark(getResourceInputStream(text), null);
            results[OVERHEAD] = System.currentTimeMillis() - start;
            System.out.println((double)results[OVERHEAD] / 1000d + " seconds");

            //Run the rest of the benchmarks
            for (int i = 1; i < BENCHMARK_NAMES.length; i++)
            {
                System.out.print("Benchmarking " + BENCHMARK_NAMES[i] + "...");
                start = System.currentTimeMillis();
                runBenchmark(getResourceInputStream(text), counters[i - 1]);
                results[i] = System.currentTimeMillis() - start;
                System.out.println((double)results[i] / 1000d + " seconds");
            }
            
            return true;
        }
        catch(IOException e)
        {
            System.err.println("Encountered an error while running benchmarks");
            e.printStackTrace();
            return false;
        }
    }

	@Override
	public long getComparisonCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getReferenceAssignmentCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
