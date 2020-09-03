import java.util.Iterator;

//A Word Counter implementation that simply inserts new words
//at the start of a list
public class UnsortedWordCounter extends WordCounter implements PerformanceTraceable
{
    //All encountered words are collected in this list
    private LinkedList<Word> words = new LinkedList<>();

    //The number of comparisons made during the lifetime of this class
    private long comparisons = 0;

    @Override
    public void encounter(String word)
    {
    	//this method is used during the benchmarks to put each word that is
    	//encountered into a the list
    	
        for (Word w : words)
        {
            comparisons++;

            if (w.getKey().equals(word))
            {
                //We found the word. Increment the word count and exit early
                w.increment();
                return;
            }
        }

        //The word was not found, add it to the list
        words.add(0, new Word(word));
    }
    
	@Override
	public void remove(String word)
	{
		//this method is used during tear down after the lists are constructed
		
		int count = 0;
        for (Word w : words)
        {
            comparisons++;

            if (w.getKey().equals(word))
            {
                //We found the word. decrement the word count and exit early
                w.decrement();
                
                //if the number of occurrences of the word is 0, then delete the node for the word
                if(w.value == 0)
                {
                	words.remove(count);
                }
                return;
            }
            count++;
        }
	}

    @Override
    public long getWordCount()
    {
        long count = 0;

        for (Word w : words)
        {
            count += w.getValue();
        }

        return count;
    }

    @Override
    public long getDistinctWordCount()
    {
        long count = 0;

        //For this project we're not allowed to keep track of the distinct count as elements are being added
        //We have to traverse the list to get the word count
        for(@SuppressWarnings("unused") Word w : words)
        {
            count++;
        }

        return count;
    }

    @Override
    public long getComparisonCount()
    {
        return comparisons;
    }

    @Override
    public long getReferenceAssignmentCount()
    {
        return words.getReferenceAssignmentCount();
    }

    @Override
    public Iterator<Word> iterator()
    {
        return words.iterator();
    }

	@Override
	public long setComparisonCount()
	{
		return comparisons = 0;
	}

	@Override
	public long setReferenceAssignmentCount()
	{
		return words.referenceChangeCount = 0;
	}
}
