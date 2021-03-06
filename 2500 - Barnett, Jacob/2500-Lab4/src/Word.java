
//Written by Jacob Barnett on 11/6/2019

//Records a word and the occurrence of the word
public class Word extends KeyValuePair<String, Integer> implements Comparable<Word>
{
	
    public Word(String value)
    {
    	//default constructor
    	
        this(value, 1);
    }

    public Word(String value, int count)
    {
    	//constructor with specified occurrence
    	
        super(value, count);
    }

    public static String sanitize(String input)
    {
        //Converts the input string to lower case and strips all non-letter characters
        //from the beginning and end of the string.
        //return the sanitized string
    	
    	
        char[] parts = input.toLowerCase().toCharArray();
        int lastLetterIndex = parts.length;

        //Walk backwards until we hit a letter. Ignore everything until we get there
        StringBuilder output = new StringBuilder(parts.length);
        boolean start = false;
        for (int i = parts.length - 1; i >= 0; i--)
        {
            boolean isLetter = (parts[i] >= 'a' && parts[i] <= 'z');
            if (start || isLetter)
            {
                start = true;
                output.append(parts[i]);
                if (isLetter)
                {
                    //Remember the last index of a letter that we encountered
                    lastLetterIndex = i;
                }
            }
        }

        //Reverse the temporary buffer and take the substring up until the "last" letter
        //This cuts off all punctuation from the front of the string
        String temp = output.reverse().toString();
        return temp.isEmpty() ? "" : temp.substring(lastLetterIndex);
    }

    public void increment()
    {
        //Increase the occurrence count of this word by one
    	
        this.value++;
    }
    
    public void decrement()
    {
        //Decrease the occurrence count of this word by one
    	
    	this.value--;
    }
    
    @Override
    public int compareTo(Word o)
    {
        // Compare to the value of the word instead of the word itself
        return o != null ? this.key.compareTo(o.key) : 1;
    }

    @Override
    public boolean equals(Object obj)
    {
        //Two words are "equal" when the string they contain is equal
        return obj != null && obj instanceof Word && this.key.equals(((Word) obj).key);
    }
}
