import java.util.Iterator;

//A word counter in which more frequently encountered words "bubble-up" to the
//top of the list one node at a time. New words are added to the front of the list
public class BubbleSelfAdjustingWordCounter extends WordCounter implements PerformanceTraceable
{
    //The first element in the word list
    private Node<Word> head;

    //The number of comparisons made during the lifetime of the word counter
    private long comparisons = 0;
    //The number of reference changes made to the internal data structure of the word counter
    private long referenceChanges = 0;

    @Override
    public void encounter(String word)
    {
    	//this method is used to add the encountered word to the list
    	
        if (head == null)
        {
            //There are no words in the list. Start the list now
            head = new Node<>(new Word(word));
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
            Node<Word> parentOfParent = null;
            Node<Word> parent = head;
            do
            {
                Node<Word> target = parent.next();

                if (target == null)
                {
                    //The word is not in the list. Add it
                    Node<Word> added = new Node<>(new Word(word));

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

                    //Move the node up one
                    //First, link the parent to the following node
                    parent.linkTo(target.next());
                    target.linkTo(parent);

                    //Now, re-insert in-between the parent's parent and the parent
                    if (parentOfParent == null)
                    {
                        head = target;
                    }
                    else
                    {
                        parentOfParent.linkTo(target);
                    }
                    
                    referenceChanges += 3;
                    return;
                }

                parentOfParent = parent;
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

                    //we need to delte the node
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

        for(Word w : this){
            count += w.getValue();
        }

        return count;
    }

    @Override
    public long getDistinctWordCount()
    {
        long count = 0;

        for(@SuppressWarnings("unused") Word w : this){
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

    @Override
    public long getReferenceAssignmentCount()
    {
        return referenceChanges;
    }

	@Override
	public long setComparisonCount()
	{
		return comparisons = 0;
	}

	@Override
	public long setReferenceAssignmentCount()
	{
		return referenceChanges = 0;
	}
}
