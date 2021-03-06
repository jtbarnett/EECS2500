import java.util.Iterator;

//Written by Jacob Barnett on 11/8/2019

//A word counter which moves words to the front of the list
//each time they are encountered
public class FrontSelfAdjustingWordCounter extends WordCounter implements PerformanceTraceable
{
    //A pointer to the start of the word linked-list
    private Node<Word> head = null;

    //The number of comparisons made during the lifetime of the word counter
    private long comparisons = 0;
    //The number of reference changes made to the internal data structure of the word counter
    private long referenceChanges = 0;

    @Override
    public void encounter(String word)
    {
    	Word w = new Word(word);
    	
        if (head == null)
        {
            //There are no words in the list. Start the list now
            head = new Node<>(w);
            referenceChanges++;
            return;
        }

        comparisons++;
        if (head.getValue().getKey().equals(word))
        {
            //The word is already at the front of the list
            head.getValue().increment();
        }
        else
        {
            //The word is somewhere else in the list, or not in the list at all
            Node<Word> parent = head;
            do
            {
                Node<Word> target = parent.next();

                if (target == null)
                {
                    //The word is not in the list. Add it
                    Node<Word> added = new Node<>(w);

                    added.linkTo(head);
                    head = added;
                    referenceChanges +=2;

                    return;
                }

                comparisons++;
                if (target.getValue().getKey().equals(word))
                {
                    //We found the word. Increment the count
                    target.getValue().increment();

                    //And move it to the front of the list
                    parent.linkTo(target.next());
                    target.linkTo(head);
                    head = target;

                    referenceChanges +=2;
                    return;
                }

                parent = parent.next();
            } while (parent != null);
        }
    }
    
	@Override
	public void remove(String word)
	{
		//this is a removed method takes the current word we are working with and searches for its
		//match in the data structure. if the match is found, we want to decrement the occurrences of the word
		//if the number of occurrences is 0, then delete the node. this will run until there is 0 nodes left in the list
		
		//the item we want to delete is the first node in the list
		if(head.getValue().getKey().equals(word))
		{
			comparisons++;
			head.getValue().decrement();
			
			//if the number of occurrences of the word for the
			//node is 0, then delete the node from the list
			if(head.getValue().getValue() == 0)
			{
	            Node<Word> oldHead = head;
				
				head = head.next();
				referenceChanges++;
				
	            oldHead.linkTo(null);
			}
		}
		//the item is not the first node in the list, we need to search for it
		else
		{
            Node<Word> parent = head;
            
            do
            {
                Node<Word> target = parent.next();

                comparisons++;
                if (target.getValue().getKey().equals(word))
                {
                    //We found the word. decrement the count
                    target.getValue().decrement();

                    //we need to delete the node
                    if(target.getValue().getValue() == 0)
                    {
                    	Node<Word> oldTarget = target;
                    	
                    	parent.linkTo(target.next());
        				referenceChanges++;
        				
                    	oldTarget.linkTo(null);
                    }
                    
                    return;
                }

                parent = parent.next();
            } while (parent != null);
		}
	}

    @Override
    public long getWordCount()
    {
        long count = 0;

        for(Word w : this)
        {
            count += w.getValue();
        }

        return count;
    }

    @Override
    public long getDistinctWordCount()
    {
        long count = 0;

        for(@SuppressWarnings("unused") Word w : this)
        {
            count++;
        }

        return count;
    }

    @Override
    public Iterator<Word> iterator()
    {
        return new NodeIterator<>(head);
    }

    @Override
    public long getComparisonCount()
    {
        return comparisons;
    }
    
    public long setComparisonCount()
    {
    	return comparisons = 0;
    }

    @Override
    public long getReferenceAssignmentCount()
    {
        return referenceChanges;
    }

	@Override
	public long setReferenceAssignmentCount()
	{
		return referenceChanges = 0;
	}
}
