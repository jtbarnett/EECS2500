import java.util.Iterator;
import java.util.function.Consumer;

//Written by Jacob Barnett on 11/7/2019

//A custom, generic, linked list implementation
//this linked list is used for the unsorted list, the alphabetically sorted list, and list 2a
public class LinkedList<T extends Comparable<T>> implements Iterable<T>, PerformanceTraceable
{
    //A pointer to the first item in the list
    public Node<T> head = null;
    //A pointer to the last item in the list
    public Node<T> tail = null;
    //a pointer to the previous item in the list
    public Node<T> prev = null;
    //marker for the node
    public Node<T> marker = null;
    //node before
    public Node<T> before = null;
    //The cached size of the list
    protected int size = 0;

    protected long comparisonCount = 0;
    protected long referenceChangeCount = 0;

    public void add(T value)
    {
        //Adds the specified item to the end of the list
    	
        add(this.size(), value);
    }

    public void add(int index, T value) throws IndexOutOfBoundsException
    {
        //Adds the specified item at the specified location in the list
        //index The location to insert the item at. Must be between 0 and the size of the list inclusive
    	
        Node<T> node = new Node<>(value);

        if (size == 0)
        {
            //This is the first element we've added to the list
            //So we can just update the head and tail pointers
            head = tail = node;
            referenceChangeCount++;
            
        }
        else if (index == 0)
        {
            //We have at least one element in the list already
            //So link the new node to the first node and update the head pointer
            node.linkTo(head);
            head = node;
            referenceChangeCount += 2;
        }
        else if (index == size)
        {
            //We have at least one element in the list already
            //Link the last element in the list to the new node and update the tail pointer
            tail.linkTo(node);
            tail = node;
            referenceChangeCount += 2;
        }
        else if (index > 0 && index < size)
        {
            //Were inserting somewhere in the middle
            //Get the node immediately preceding the target index and the node after it
            Node<T> parent = getNodeAt(index - 1);
            Node<T> next = parent.next();

            //Link all the things
            node.linkTo(next);
            parent.linkTo(node);
            referenceChangeCount += 2;
        }
        //We added something, increase the cached size of the list
        size++;
    }

    public void addOr(T value, Consumer<T> ifFound)
    {
    	//Add method that is used for the alphabetically sorted list
    	
        Node<T> toInsert = new Node<>(value);

        //Assume we're making a comparison, fix later
        comparisonCount++;

        int headComparison = head != null ? (head.getValue()).compareTo(value) : -1;

        if (size == 0)
        {
            //There's nothing in the list
            comparisonCount--;
            head = tail = toInsert;
            referenceChangeCount++;
            size++;
        }
        else if (headComparison == 0)
        {
            //The element we're interested in is the first thing in the list
            if (ifFound != null) ifFound.accept(head.getValue());
        }
        else if (headComparison > 0)
        {
            //The element needs to become the first element in the sorted list
            toInsert.linkTo(head);
            head = toInsert;
            referenceChangeCount +=2;
            size++;
        }
        else
        {
            //The element is not the first element, or doesn't belong at the very start of the list
            //Search for an element that matches the target, or insert it if we've passed where it
            //should "logically" be in terms of sorting order
            Node<T> ref = head;
            while (ref.next() != null)
            {
                comparisonCount++;
                int comparison = ref.next().getValue().compareTo(value);

                if (comparison > 0)
                {
                    //The element cannot possibly come after this element. Insert before it
                    toInsert.linkTo(ref.next());
                    ref.linkTo(toInsert);
                    referenceChangeCount +=2;
                    size++;
                    return;
                }
                else if (comparison == 0)
                {
                    //We found the element. Perform the specified action if one was provided
                    if (ifFound != null) ifFound.accept(ref.next().getValue());
                    return;
                }

                ref = ref.next();
            }

            //The element hasn't been inserted yet, it belongs at the tail of the list
            tail.linkTo(toInsert);
            tail = toInsert;
            referenceChangeCount++;
            size++;
        }
    }
    
    
    public void addOr2A(T value, Consumer<T> ifFound)
    {
    	//Add method that is used for the list 2a (the list that is an extention of the alphabetically sorted lsit)
    	
        Node<T> toInsert = new Node<>(value);

        //Assume we're making a comparison, fix later
        comparisonCount++;

        int headComparison = head != null ? head.getValue().compareTo(value) : -1;

        if (size == 0)
        {
            //There's nothing in the list
            comparisonCount--;
            head = tail = prev = toInsert;
            referenceChangeCount++;
            size++;
        }
        else if (headComparison == 0)
        {
            //The element we're interested in is the first thing in the list
            if (ifFound != null) ifFound.accept(head.getValue());
        }
        else if (headComparison > 0)
        {
            //The element needs to become the first element in the sorted list
            toInsert.linkTo(head);
            head = prev = toInsert;
            referenceChangeCount +=2;
            size++;
        }
        else
        {
            //The element is not the first element, or doesn't belong at the very start of the list
            //Search for an element that matches the target, or insert it if we've passed where it
            //should "logically" be in terms of sorting order
        	int compPrev = prev.getValue().compareTo(value);
        	
        	if(compPrev == 0)
        	{
    			//We found the element. Perform the specified action if one was provided
            	if (ifFound != null) ifFound.accept(prev.getValue());
        	}
        	else if(compPrev > 0)
        	{
            	Node<T> ref = head;
            	while (ref.next() != null)
            	{
            		comparisonCount++;
            		int comparison = ref.next().getValue().compareTo(value);

            		if (comparison > 0)
            		{
            			//The element cannot possibly come after this element. Insert before it
            			toInsert.linkTo(ref.next());
            			ref.linkTo(toInsert);
            			prev = ref.next();
                        referenceChangeCount +=2;
            			size++;
            			return;
            		}
            		else if (comparison == 0)
            		{
            			//We found the element. Perform the specified action if one was provided
                    	if (ifFound != null) ifFound.accept(ref.next().getValue());
            			prev = ref.next();
                    	return;
            		}

            		ref = ref.next();
            	}

            	//The element hasn't been inserted yet, it belongs at the tail of the list
            	tail.linkTo(toInsert);
            	tail = toInsert;
            	prev = tail;
                referenceChangeCount++;
            	size++;
        	}
        	else
        	{
            	Node<T> ref = prev;
            	while (ref.next() != null)
            	{
            		comparisonCount++;
            		int comparison = ref.next().getValue().compareTo(value);

            		if (comparison > 0)
            		{
            			//The element cannot possibly come after this element. Insert before it
            			toInsert.linkTo(ref.next());
            			ref.linkTo(toInsert);
            			prev = ref.next();
                        referenceChangeCount +=2;
            			size++;
            			return;
            		}
            		else if (comparison == 0)
            		{
            			//We found the element. Perform the specified action if one was provided
                    	if (ifFound != null) ifFound.accept(ref.next().getValue());
            			prev = ref.next();
                    	return;
            		}

            		ref = ref.next();
            	}

            	//The element hasn't been inserted yet, it belongs at the tail of the list
            	tail.linkTo(toInsert);
            	tail = toInsert;
                referenceChangeCount++;
            	prev = tail;
            	size++;
        	}
        }
    }
    
    
    public void remove(int index) throws IndexOutOfBoundsException
    {
        //Removes the specified element from the list
        //index the index of the element to remove

        if (index == 0)
        {
            //Removing the first element.
            //Point the head pointer to the proceeding node
            //Then link the old element to null so it can be GC'd
            Node<T> oldHead = head;

            head = head.next();
            referenceChangeCount++;

            oldHead.linkTo(null);
        }
        else
        {
            //Get the parent node and the node to remove from it
            Node<T> parent = getNodeAt(index - 1);
            Node<T> toRemove = parent.next();

            //Link the parent to the node after the target node
            parent.linkTo(toRemove.next());

            //Link the target node to null so it can be GC'd
            toRemove.linkTo(null);

            //If we removed the last element in the list, we need to update the tail pointer
            if (index == size - 1)
            {
                tail = parent;
            }
            
            referenceChangeCount++;
        }
        
        //We removed an element, decrease the cached size
        size--;
    }
    
    public void set(int index, T value) throws IndexOutOfBoundsException
    {
        //Updates the value at the specified location
        //index the index of the element to update
        //value the value to set
    	
        if (!validateBounds(index))
        {
            throw new IndexOutOfBoundsException("The Index " + index + " is not in the bounds of the list!");
        }

        getNodeAt(index).setValue(value);
    }

    public boolean contains(T value)
    {
        //Traverses the list and returns true if the element is found
        //value the value to search for
        //<code>True</code> if the element is in the list, false otherwise
    	
        //We implement Iterable<T>, so we can just use a for-each loop
        for (T element : this)
        {
            comparisonCount++;
            if (element.equals(value))
            {
                return true;
            }
        }

        return false;
    }

    public void clear()
    {
        //Removes all items from the list
    	
        //Simply set the head and tail pointers to null and reset the size cache
        head = tail = null;
        referenceChangeCount++;
        size = 0;
    }

    protected Node<T> getNodeAt(int index)
    {
        //A helper method to get the specified node in the list
        //param index the index of the target node
        //return the node at the specified index
    	
        //We should never be trying to get a node outside of the bounds of the list
        //An exception should have been thrown by now if the user was trying to
        assert (validateBounds(index));

        int i = 0;
        Node<T> node = head;
        while (i++ != index)
        {
            node = node.next();
        }

        return node;
    }

    public T get(int index) throws IndexOutOfBoundsException
    {
        //Gets the element at the specified index
        //param index the index of the element to get
        //return the value at the specified index
    	
        if (!validateBounds(index))
        {
            throw new IndexOutOfBoundsException("The Index " + index + " is not in the bounds of the list!");
        }

        return getNodeAt(index).getValue();
    }
    
    protected boolean validateInsertableBounds(int index)
    {
        //param index the index being inserted at
        //return <code>true</code> if and only if the specified index is greater than or equal to
        //zero and less than or equal to the size of the list
    	
        return index >= 0 && index <= size;
    }

    protected boolean validateBounds(int index)
    {
        //param index the index being accessed
        //return <code>true</code> if and only if the specified index is greater than or equal to
        //zero and less than the size of the list
    	
        return index >= 0 && index < size;
    }

    public T getFirst()
    {
        //return the first element in the list
    	
        return head == null ? null : head.getValue();
    }

    public T getLast()
    {
        //return the last element in the list. The list is not traversed
    	
        return tail == null ? null : tail.getValue();
    }

    public int size()
    {
        //return the size of the list. The list is not traversed
    	
        return size;
    }

    @Override
    public long getComparisonCount()
    {
        return comparisonCount;
    }

    @Override
    public long getReferenceAssignmentCount()
    {
        return referenceChangeCount;
    }
    
    @Override
    public Iterator<T> iterator()
    {
        return new NodeIterator<>(this.head);
    }
}
