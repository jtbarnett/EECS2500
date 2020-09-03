import java.util.Iterator;

//A word counter whose underlying data type is sorted alphabetically
public class SortedWordCounter extends WordCounter implements PerformanceTraceable
{
    //All encountered words are collected in this list. They are sorted alphabetically
    SortedLinkedList<Word> words = new SortedLinkedList<>();

    @Override
    public void encounter(String word)
    {
        //Add the word, or if it already exists, increment it
        //Doing it this way saves a few comparisons by not having to search the list twice
        words.addOr(new Word(word), Word::increment);
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
        return words.getComparisonCount();
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
}
