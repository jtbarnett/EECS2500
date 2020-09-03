
//Written by Jacob Barnett on 11/6/2019

//A Word Counter is a variant of a list that records the number of times
//a word is encountered. Each implementation should keep track of the
//number of comparisons made and references changed during the lifetime
//of the Word Counter.
public abstract class WordCounter implements Iterable<Word>
{
    //Records an encounter of the specified word
    public abstract void encounter(String word);
    
    //decreases the word count or removes the node if the word has a count of 1
    public abstract void remove(String word);

    //return the total number of words encountered during the lifetime of this Word Counter
    public abstract long getWordCount();

    //return the number of unique words encountered during the lifetime of this Word Counter
    public abstract long getDistinctWordCount();

    //return the number of comaprisons made
	public abstract long getComparisonCount();
	
	//set the comparion count to 0
	public abstract long setComparisonCount();

	//return the number of reference changes made
	public abstract long getReferenceAssignmentCount();
	
	//set the reference count to 0
	public abstract long setReferenceAssignmentCount();
}
