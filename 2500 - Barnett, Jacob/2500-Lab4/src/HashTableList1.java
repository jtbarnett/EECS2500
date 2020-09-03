import java.util.Iterator;
import java.util.ArrayList;

//Written by Jacob Barnett on 11/25/2019

//this is the first hash table word counter. In this counter we will be hashing the
//the input string (i.e. each word of each line of the text file). We will be hashing the input
//string wiht the hash function that takes the sum of the numeric values of each of the characters
//in the string mod 256
public class HashTableList1 extends WordCounter implements PerformanceTraceable
{
	//used to keep track of how many nodes are present in our array
	public int n = 0;
	
    //The number of comparisons made during the lifetime of the word counter
    private long comparisons = 0;
    //The number of reference changes made to the internal data structure of the word counter
    private long referenceChanges = 0;
	
    //creating the array and the size of the array
    public int arraySize = 256;
    ArrayList<Node<Word>> hashArray = new ArrayList<>();
    
    Node<Word> nullRef = null;
    
    //creating the array list of 256 slots
    public HashTableList1()
    {	
    	for(int i = 0; i < arraySize; i++)
    	{
    		hashArray.add(nullRef);
    	}
    }

	@Override
	public void encounter(String word)
	{
		//this method will first try to find the string we are dealing with in the hash array. If it is not
		//there, then we will have to add it
		
		//calls the method that tries to find the word and increment the count if found. If not, it will
		//add the new node in its correct place
		findEncounter(word);
	}

	@Override
	public void remove(String word)
	{
		//the method that tries to find the word and decrement the count of the word. If the
		//count is equal to zero, the it deletes the node
		
		int index = hashFunction(word);
		
		Node<Word> head = hashArray.get(index);
		
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
				
	        	hashArray.set(index, head.next());
				referenceChanges++;
				n--;
				
	            oldHead.linkTo(null);
			}
		}
		//the item is not the first node in the list, we need to search for it
		else
		{
            Node<Word> parent = hashArray.get(index);
            
            do
            {
                Node<Word> target = parent.next();

                comparisons++;
                if (target.getValue().getKey().equals(word))
                {
                    //We found the word. decrement the count
                    target.getValue().decrement();

                    //the node is empty, delete it
                    if(target.getValue().getValue() == 0)
                    {
                    	Node<Word> oldTarget = target;
                    	
                    	parent.linkTo(target.next());
                        referenceChanges++;
                        n--;
                        
                    	oldTarget.linkTo(null);
                    }
                    //move the node to the front of the list
                    else
                    {
                        parent.linkTo(target.next());
                        target.linkTo(head);
                    	hashArray.set(index, target);

                        referenceChanges +=3;
                    }
                    
                    return;
                }

                parent = parent.next();
            } while (parent != null);
		}
	}
	
	public void findEncounter(String word)
	{
		//a method used to find if the word is already in the hash table for the encounter method.
		//If it is, then we only need to
		//increment its count, but if not then we need to add it to its respective linked list in its
		//hash value index of the hash array
		
		int index = hashFunction(word);
		
		Word w = new Word(word);
		Node<Word> current = new Node<>(w);
		Node<Word> head = hashArray.get(index);
		
        if (head == null)
        {
            //There are no words in the list. Start the list now
        	hashArray.set(index, current);
            referenceChanges++;
            n++;
            
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
            Node<Word> parent = hashArray.get(index);
            do
            {
                Node<Word> target = parent.next();

                if (target == null)
                {
                    //The word is not in the list. Add it

                    current.linkTo(head);
                	hashArray.set(index, current);
                    referenceChanges +=2;
                    n++;
                    
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
                	hashArray.set(index, target);

                    referenceChanges +=2;
                    return;
                }

                parent = parent.next();
            } while (parent != null);
        }
	}
	
	public int hashFunction(String word)
	{
		//a method used to convert the input string to the hash value that will be used to determine
		//which index of the array the word should be added to. This hash takes the sum of all of the characters
		//and then mod 256 the sum
		
		int hash = 0;
		
		for(int i = 0; i < word.length(); i++)
			hash += word.charAt(i);
		
		return hash % 256;
	}

	@Override
	public long getWordCount()
	{
    	//returning the total number of words in the text file
		
		long count = 0;
		
		for(int i = 0; i < 256; i++)
		{
			Node<Word> ref = hashArray.get(i);
			
			if(ref != null)
			{
				while(ref != null)
				{
					count += ref.getValue().getValue();
					ref = ref.next();
				}
			}
		}
		
		return count;
	}

	@Override
	public long getDistinctWordCount()
	{
    	//returning the total number of distinct words in the text file
		
		return n;
	}

	@Override
	public long getComparisonCount()
	{
		return comparisons;
	}

	@Override
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
	
	@Override
	public Iterator<Word> iterator()
	{
		return null;
	}
}
