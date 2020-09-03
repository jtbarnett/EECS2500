import java.io.*;
import java.util.*;

//A class to benchmark each of the WordCounters implementations
//against the entire text of Hamlet.
public class Benchmarks implements PerformanceTraceable
{
    //The path to the file to read. If left null, the file will be loaded from the classpath
    private final String TEST_FILE;

    //The token that separates words (Any whitespace)
    public static final String WORD_SEPARATOR = "\\s+";

    //The exit code returned when an error occurs while running the benchmarks
    public static final int EXIT_BENCHMARK_IO_ERROR = -1;

    public static final int OVERHEAD = 0;
    public static final int UNSORTED = 1;
    public static final int SORTED = 2;
    public static final int SELF_ADJUST_FRONT = 3;
    public static final int SELF_ADJUST_BUBBLE = 4;               

    //The names of all benchmarks
    public static final String[] BENCHMARK_NAMES = {
            "Overhead",
            "Unsorted",
            "Sorted (Alphabetically)",
            "Self-Adjusting (Front)",
            "Self-Adjusting (Bubble)",
    };

    //All word counters to benchmark
    public final WordCounter[] counters = new WordCounter[]{
            new UnsortedWordCounter(),
            new SortedWordCounter(),
            new FrontSelfAdjustingWordCounter(),
            new BubbleSelfAdjustingWordCounter()
    };

    //Time results are stored here
    private long[] results = new long[BENCHMARK_NAMES.length];

    
    public Benchmarks(String inputFile)
    {
        this.TEST_FILE = inputFile;
    }

    
    //Run the specified word counter using the specified input stream
    //@param in the stream to read words from
    //@param counter the word counter to benchmark
    //@throws IOException
    public void runBenchmark(InputStream in, WordCounter counter) throws IOException
    {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            //Read the file line by line
            String line;
            while ((line = reader.readLine()) != null)
            {
            	//Iterate thorugh each line and optimize the line to be able to better read each word
                for (String word : line.replaceAll("[^'a-zA-Z ]", " ").toLowerCase().split(WORD_SEPARATOR))
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

    //The project spec requires a constant to appear towards the top of the benchmark class that points to the
    //path to the input file. In order to maintain compatibility on different workstations, default to getting
    //the input stream from the class path instead if the constant is null
    //@return an InputStream for the resource to benchmark against
    //@throws FileNotFoundException
    public InputStream getResourceInputStream(String text) throws FileNotFoundException
    {
        return TEST_FILE == null || TEST_FILE.isEmpty() ?
                Benchmarks.class.getClassLoader().getResourceAsStream(text) :
                new FileInputStream(TEST_FILE);
    }

    //Runs all benchmarks
    //@return false iff there was an exception thrown while the benchmarks were running
    public boolean runAllBenchmarks(String text)
    {
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
        catch (IOException e)
        {
            System.err.println("Encountered an error while running benchmarks");
            e.printStackTrace();
            return false;
        }
    }

    //The main entry point of the program
    //@param args Any arguments passed on the command line
    public static void main(String[] args) throws FileNotFoundException
    {
    	//user input for slecting which text file they want to test
    	@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
    	String userIn = "";
    	String textFile ="";
    	
    	System.out.println("Please enter the name of the text file that you want to analyze.\nYour options include Green Eggs and Ham, Hamlet, Small Test, Moby Dick, Moby Dick Half, Moby Dick Concatenated, Large Test, King James Bible, and All Text.\nPlease enter the text exactly as it is described above or the program will not run.\n");
    	userIn = input.nextLine();
    	
    	//the different cases that the user can enter into the console
    	//made so that you dont have to change the code each time to run a differnt text file
    	//only have to enter the name and it will run for you
    	if(userIn.equalsIgnoreCase("Hamlet"))
    	{
    		textFile = "Hamlet.txt";
    	}
    	else if(userIn.equalsIgnoreCase("King James Bible"))
    	{
    		textFile = "KingJamesBible.txt";
    	}
    	else if(userIn.equalsIgnoreCase("Moby Dick Half"))
    	{
    		textFile = "MobyDickHalf.txt";
    	}
    	else if(userIn.equalsIgnoreCase("Moby Dick"))
    	{
    		textFile = "MobyDick.txt";
    	}
    	else if(userIn.equalsIgnoreCase("Moby Dick Concatenated"))
    	{
    		textFile = "MobyDickConcatenated.txt";
    	}
    	else if(userIn.equalsIgnoreCase("Green Eggs and Ham"))
    	{
    		textFile = "GreenEggsandHam.txt";
    	}
    	else if(userIn.equalsIgnoreCase("All Text"))
    	{
    		textFile = "ALL.txt";
    	}
    	else if(userIn.equalsIgnoreCase("Large Test"))
    	{
    		textFile = "LargeTest.txt";
    	}
    	else if(userIn.equalsIgnoreCase("Small Test"))
    	{
    		textFile = "SmallTest.txt";
    	}
    	else
    		System.exit(EXIT_BENCHMARK_IO_ERROR);
    	
    	
    	Benchmarks b = new Benchmarks(textFile);
    	b.getResourceInputStream(textFile);
    	
        if (!b.runAllBenchmarks(textFile))
        {
            System.exit(EXIT_BENCHMARK_IO_ERROR);
        }

        //Print Results
        System.out.println();
        System.out.println("Benchmarks Complete\nResults:\n\n");
        for (int i = 0; i < BENCHMARK_NAMES.length; i++)
        {
            System.out.print(BENCHMARK_NAMES[i] + ": Duration: " + (double) b.results[i] / 1000d + " seconds");

            if (i != OVERHEAD)
            {
                WordCounter counter = b.counters[i - 1];

                System.out.print("\tWord Count: " + counter.getWordCount() +
                        ", Distinct: " + counter.getDistinctWordCount() +
                        ", Comparisons: " + counter.getComparisonCount() +
                        ", Reference Changes: " + counter.getReferenceAssignmentCount());
            }

            System.out.println();
        }

        System.out.println("\n----------\n");

        //Print top 100 words for the latter two benchmarks
        int counter = 0;
        System.out.println("First 100 elements in Front Self-Adjusting:");
        for (Word w : b.counters[SELF_ADJUST_FRONT - 1])
        {
            System.out.println("\t" + w.getKey() + "\t\t" + w.getValue());
            if (++counter == 100) break;
        }

        counter = 0;
        System.out.println("First 100 elements in Bubble Self-Adjusting:");
        for (Word w : b.counters[SELF_ADJUST_BUBBLE - 1])
        {
            System.out.println("\t" + w.getKey() + "\t\t" + w.getValue());
            if (++counter == 100) break;
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
