import java.util.Iterator;

//A word counter whose underlying data type is sorted alphabetically
//will use the linked list
public class SortedWordCounter extends WordCounter implements PerformanceTraceable
{	
    //All encountered words are collected in this list. They are sorted alphabetically
    LinkedList<Word> words = new LinkedList<>();
    
    @Override
    public void encounter(String word)
    {
        //Add the word, or if it already exists, increment it
        //Doing it this way saves a few comparisons by not having to search the list twice
    	Word w = new Word(word);
    	words.addOr(w, Word::increment);
    }
    
	@Override
	public void remove(String word)
	{
		//for this method, we want to decrease the word count if the word matches the word of a node
		//we also will delete the node if the word count drops to 0

		int count = 0;
        for (Word w : words)
        {
            words.comparisonCount++;

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
        return words.comparisonCount;
    }

    @Override
    public long getReferenceAssignmentCount()
    {
        return words.referenceChangeCount;
    }

    @Override
    public Iterator<Word> iterator()
    {
        return new NodeIterator<>(words.head);
    }

	@Override
	public long setComparisonCount()
	{
		return words.comparisonCount = 0;
	}

	@Override
	public long setReferenceAssignmentCount()
	{
		return words.referenceChangeCount = 0;
	}
}
