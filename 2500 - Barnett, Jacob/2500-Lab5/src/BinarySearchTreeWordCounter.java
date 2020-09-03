import java.util.Iterator;

//Written by Jacob Barnett on 12/7/2019

//A word counter that places newly encountered words in a Binary Search Tree
//we also want to find the height of the binary tree (through traversal) and print out the first
//100 words of the tree
public class BinarySearchTreeWordCounter extends WordCounter
{
    //the number of comparisons made during the lifetime of the tree
    private long comparisons = 0;
    //the number of reference changes made during the lifetime of the tree
    private long referenceChanges = 0;

    //the first or "root" node of the tree. All other nodes are either in this node's left or right branch
    public static BSTNode<Word> root;

    @Override
    public void encounter(String word)
    {
        //Add the word to the tree, or increment its count if it's already in the tree

		Word w = new Word(word);
		BSTNode<Word> newNode = new BSTNode<>(w);
    	
        if (root == null)
        {
            //this is the first node in the tree, set the root element
            root = newNode;
            referenceChanges++;
        }
        else
        {
            BSTNode<Word> parent;
            BSTNode<Word> target = root;
            int branchComparison;

            do
            {
                //remember where we used to be
                parent = target;

                //find which branch to take
                comparisons++;
                branchComparison = w.compareTo(target.getValue());

                //the element we're inserting is less than the target
                //take the left branch
                if (branchComparison < 0)
                {
                    target = target.left();
                }
                //the element we're inserting has the same key as another element
                else if (branchComparison == 0)
                {
                	target.getValue().increment();
                    return;
                }
                //the element we're inserting is greater than the target
                //take the right branch
                else
                {
                    target = target.right();
                }
            } while (target != null);

            //we are supposed to insert a node
            //insert it on the correct branch
            referenceChanges++;
            parent.insert(newNode, branchComparison < 0);
            
            newNode.setParent(parent);
        }
    }

    public BSTNode<Word> successor(BSTNode<Word> input)
    {
        //get the current nodes successor
    	
    	//if the input node has a right node, then we want to return the minimum value of the
    	//nodes right branch, if the the minimum value is the current nodes right node, then we
    	//want to return that
    	if(input.right() != null)
    	{
    		return minimum(input.right());
    	}
    	
    	//the input node does not have a right node, thus we must search up the list while the
    	//current nodes parrent node is not null value and if the current node is the parent
    	//nodes right node
    	BSTNode<Word> p = input.parent();
    	while(p != null && input == p.right())
    	{
    		input = p;
    		p = p.parent();
    	}
		return p;
    }
    
    public BSTNode<Word> minimum(BSTNode<Word> input)
    {
        //return the minimum value in the tree
    	
    	BSTNode<Word> holder = input;
    	while(holder.left() != null)
    	{
    		holder = holder.left();
    	}
		return holder;
    }
    
	@Override
	public long getBSTHeight(BSTNode<Word> input)
	{
		//a method to return the overall height (or depth) of the binary tree
		
		if(input == null)
		{
			return -1;
		}
		
		long leftHeight = getBSTHeight(input.left());
		long rightHeight = getBSTHeight(input.right());
		
		if(leftHeight > rightHeight)
		{
			return leftHeight + 1;
		}
		else
		{
			return rightHeight + 1;
		}
	}
    
    public void displayFirst100()
    {
    	//a method to display the dirst 100 words in the binary search tree
    	
    	BSTNode<Word> p = minimum(root);
    	System.out.println("First 100 words of the Binary Tree:");
    	for(int i = 0; i < 100; i++)
    	{
    		if(p != null)
    		{
    			System.out.println(p.getValue().getKey() + "\t" + p.getValue().getValue());
    			p = successor(p);
    		}
    		else
    		{
    			break;
    		}
    	}
    }
    
    @Override
    public long getWordCount()
    {
    	//get the total word count by traversing the whole bianry tree and
    	//gathering all of the word counts per each node
    	
    	long count = 0;
    	
    	BSTNode<Word> p = minimum(root);
    	while(p != null)
    	{
    		count += p.getValue().getValue();
    		p = successor(p);
    	}
    	return count;
    }

    @Override
    public long getDistinctWordCount()
    {
    	//get the total number of distinct words (i.e. get the total number of nodes) in
    	//the binary tree by traversing it
    	
    	long count = 0;
    	
    	BSTNode<Word> p = minimum(root);
    	while(p != null)
    	{
    		count++;
    		p = successor(p);
    	}
    	return count;
    }

    @Override
    public Iterator<Word> iterator()
    {
        return null;
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
	public long getSLHeight()
	{
		return 0;
	}
}