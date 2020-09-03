import java.util.Iterator;
import java.util.Random;

//A skip list word counter implementation
//I am going to use the SLNode class to create nodes and Word class to save the value
//in the form of <Key, Value> where Key is the word and Value is the occurrences
public class SkipListWordCounter extends WordCounter implements PerformanceTraceable
{
	//height of the list
	public int h = 1;
	//number of nodes in the list
	public int n = 0;
	
	//Keeping track of the number of comparisons and reference changes
	private long comparisons = 0;
    private long referenceChanges = 0;
    
    //Negative and positive infinity used for head and tail
    Word negInf = new Word("-oo", 0);
    Word posInf = new Word("+oo", 0);
    Word nullBadPointer = new Word("NullBadPointer");
    
    //Calss level head and tail nodes
    SLNode<Word> head = new SLNode<>(negInf);
    SLNode<Word> tail = new SLNode<>(posInf);
    SLNode<Word> nullPointerNode = new SLNode<>(nullBadPointer);
    
    SLNode<Word> start = null;
    SLNode<Word> leftNodeOfInsert = null;
    
	@Override
	public void encounter(String word)
	{
		Word w = new Word(word);
		SLNode<Word> target = new SLNode<>(w);
		
        int headComparison = start != null ? start.getValue().compareTo(w) : -1;
		
		//There's nothing in the list
		if(head.right() == null)
		{
			head.linkRight(target);
			target.linkLeft(head);
			target.linkRight(tail);
			tail.linkLeft(target);
			start = target;
			
			referenceChanges +=4;
			n++;
			
			SLNode<Word> placeHolder = target;
			//Add another node above the current one if flip() method is 0. If flip() is 1, then we do nothing
			while(flip() == 0)
			{
				h++;
				
				//New negative and positive nodes. Will link them to the originial head and tail nodes
				SLNode<Word> p = new SLNode<>(negInf);
				SLNode<Word> q = new SLNode<>(posInf);
				
				//Random generated word that we are dealing with, want to create a new node above our current one
				//and set the orrunces of this node to 0 (becasuse we have already accounted for the 1 occurrence in the node below)
				SLNode<Word> above = new SLNode<>(new Word(word, 0));
				
				//set links and create new head
				p.linkDown(head);
				head.linkUp(p);
				p.linkRight(above);
				above.linkLeft(p);
				head = p;
				
				//set links and create new tail
				q.linkDown(tail);
				tail.linkUp(q);
				q.linkLeft(above);
				above.linkRight(q);
				tail = q;
				
				//set links for above node and then update the above node to be the new place holder in
				//case any new nodes are addeed above are already created above node
				above.linkDown(placeHolder);
				placeHolder.linkUp(above);
				
				referenceChanges +=12;
				
				placeHolder = above;
			}
		}
		//we found the word and its the first thing in the list
		else if(headComparison == 0)
		{
			start.getValue().increment();
			comparisons++;
		}
		//the word needs to become the first node in the list
		else if(headComparison > 0)
		{
			n++;
			comparisons++;
			
			SLNode<Word> before = start.left();
			
			before.linkRight(target);
			target.linkLeft(before);
			target.linkRight(start);
			start.linkLeft(target);
			start = target;
			
			referenceChanges +=4;
			
			SLNode<Word> placeHolder = start;
			SLNode<Word> leftHolder = null;
			while(flip() == 0)
			{	
				//New negative and positive nodes. Will link them to the originial head and tail nodes
				SLNode<Word> p = new SLNode<>(negInf);
				SLNode<Word> q = new SLNode<>(posInf);
				
				//Random generated word that we are dealing with, want to create a new node above our current one
				//and set the orrunces of this node to 0 (becasuse we have already accounted for the 1 occurrence in the node below)
				SLNode<Word> above = new SLNode<>(new Word(word, 0));
				
				//left link will always be neg inf in this case
				leftHolder = placeHolder.left();
				
				//creating a new row, need to increase the hegiht of the list by 1
				if(leftHolder.up() == null)
				{
					//left links for node (neg inf)
					p.linkDown(leftHolder);
					leftHolder.linkUp(p);
					p.linkRight(above);
					above.linkLeft(p);
					head = p;
					
					//right links for node (pos inf)
					q.linkDown(tail);
					tail.linkUp(q);
					q.linkLeft(above);
					above.linkRight(q);
					tail = q;
					
					referenceChanges +=10;
					h++;
				}
				//there is another row, need to link the ABOVE node to the left negative infinity and to a right word node
				else
				{
					//setting up left links (neg inf)
					leftHolder.up().linkRight(above);
					above.linkLeft(leftHolder.up());
					
					referenceChanges +=2;
					
					//setting up right links (pos inf or to a word node)
					SLNode<Word> hold = placeHolder;
					while(hold.right() != null)
					{
						if(hold.right().up() != null)
						{
							above.linkRight(hold.right().up());
							hold.right().up().linkLeft(above);
							
							referenceChanges +=2;
							break;
						}
						
						hold = hold.right();
					}
				}
				
				above.linkDown(placeHolder);
				placeHolder.linkUp(above);
				
				referenceChanges +=2;
				
				placeHolder = above;
			}
		}
		//the word belongs somewhere in the list, we need to search the list and find out where
		else
		{
			SLNode<Word> searchWord = search(word);
			
			//the node is already in the list, only need to increment the occurrence count of the word
			if(searchWord != nullPointerNode)
			{
				searchWord.getValue().increment();
			}
			//the node is not in the list, need to create a new node and then coin flip to see
			//if there needs to be any more nodes added after
			else
			{
				n++;	
				SLNode<Word> left = leftNodeOfInsert;
				SLNode<Word> right = leftNodeOfInsert.right();
				
				//adding the node to the list
				right.linkLeft(target);
				target.linkRight(right);
				left.linkRight(target);
				target.linkLeft(left);
				
				referenceChanges +=4;
			
				SLNode<Word> placeHolder = target;
				SLNode<Word> tailCheck = target;
				SLNode<Word> headCheck = target;
				//need to see how many nodes need to be added above
				while(flip() == 0)
				{
					SLNode<Word> above = new SLNode<>(new Word(word, 0));
					
					//getting the head node of the list
					while(headCheck.left() != null)
						headCheck = headCheck.left();
					
					//getting the tail node of the list
					while(tailCheck.right() != null)
						tailCheck = tailCheck.right();
					
					//checking to see if there needs to be another row added
					if(tailCheck.up() == null)
					{
						//New negative and positive nodes. Will link them to the originial head and tail nodes
						SLNode<Word> newHead = new SLNode<>(negInf);
						SLNode<Word> newTail = new SLNode<>(posInf);
						
						//left links for node (neg inf)
						newHead.linkDown(headCheck);
						headCheck.linkUp(newHead);
						newHead.linkRight(above);
						above.linkLeft(newHead);
						head = newHead;
						
						//right links for node (pos inf)
						newTail.linkDown(tailCheck);
						tailCheck.linkUp(newTail);
						newTail.linkLeft(above);
						above.linkRight(newTail);
						tail = newTail;
						
						referenceChanges +=10;
						h++;
					}
					//there does not need to be another row added
					else 
					{
						SLNode<Word> node = placeHolder;
						
						//getting the right link of the current placeholder
						if(node.right().getValue().getKey().compareTo("+oo") == 0)
						{
							above.linkRight(node.right().up());
							node.right().up().linkLeft(above);
							
							referenceChanges +=2;
						}
						else
						{
							while(node.right() != null)
							{
								if(node.right().up() != null)
								{	
									above.linkRight(node.right().up());
									node.right().up().linkLeft(above);
									
									referenceChanges +=2;
									break;
								}
							
								node = node.right();
							}
						}
						
						
						//getting the left link of the current placeholder
						if(node.left().getValue().getKey().compareTo("-oo") == 0)
						{
							above.linkLeft(node.left().up());
							node.left().up().linkRight(above);
							
							referenceChanges +=2;
						}
						else
						{
							while(node.left() != null)
							{
								if(node.left().up() != null)
								{
									above.linkLeft(node.left().up());
									node.left().up().linkRight(above);
									
									referenceChanges +=2;
									break;
								}
							
								node = node.left();
							}
						}
						
					}
					
					//setting the placeholder to the above node that we just added
					//also need to update the tail and head checkers
					above.linkDown(placeHolder);
					placeHolder.linkUp(above);
					
					referenceChanges +=2;
					
					placeHolder = above;
					tailCheck = above;
					headCheck = above;
				}
			}
		}
	}

	@Override
	public void remove(String word)
	{
		//the remove method that is used during tear down after the list is constructed
		//to tear down the list
		
		SLNode<Word> delete = search(word);
		SLNode<Word> top = delete;
		
		//only decrementing the occurence of the node
		delete.getValue().decrement();
		
		//need to actually delete the node if the number of occurrences is 0
		if(delete.getValue().getValue() == 0)
		{
			n--;
			
			//we are deleting the first node, so need to reasign the first node pointer
			if(delete.left().getValue().getKey().compareTo("-oo") == 0)
				referenceChanges++;
			
			//get top node
			while(top.up() != null)
				top = top.up();
			
			while(top.down() != null)
			{
				top.left().linkRight(top.right());
				top.right().linkLeft(top.left());
				top.linkRight(null);
				top.linkLeft(null);
				
				referenceChanges +=2;
				
				top = top.down();
			}
		}
	}
	
	public SLNode<Word> search(String word)
	{
		//used to traverse through the skip list and find the node we are looking for
		
		Word w = new Word(word);
		SLNode<Word> ref = head;
		
		outerloop:
		while(true)
		{
			while(ref.right().getValue().getKey().compareTo("+oo") != 0 && ref.right().getValue().getKey().compareTo(word) <=0)
			{
				comparisons++;
				ref = ref.right();
			}
			
			if(ref.down() == null)
			{
				//we found the value we are looking for
				if(ref.getValue().compareTo(w) == 0)
				{
					leftNodeOfInsert = ref.left();
					return ref;
				}
				//the value is not in the list, return null and the left node of where the insertion should be made
				else
				{
					break outerloop;
				}
			}
			
			comparisons++;
			ref = ref.down();
		}
		
		//this means that the node is not in the list, instead we have to insert it so keep track
		//of the node to the left of the point of insertion
		leftNodeOfInsert = ref;
		return nullPointerNode;
	}
	
	public int flip()
	{
		//method used to show a 50/50 probability to add a new node or not
		
		Random r = new Random();
		int result = r.nextInt(2);
		return result;
	}

	@Override
	public long getWordCount()
	{
        long count = 0;
        SLNode<Word> ref = start;
        
        while(ref.getValue().getKey().compareTo("+oo") != 0)
        {
            count += ref.getValue().getValue();
        	ref = ref.right();
        }

        return count;
	}

	@Override
	public long getDistinctWordCount()
	{
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
