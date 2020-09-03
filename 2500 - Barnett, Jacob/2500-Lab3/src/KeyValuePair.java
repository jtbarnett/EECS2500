
//A data type for a key-value relationship. The key should be immutable.
public class KeyValuePair<K, V>
{	
    //The key of the element
    protected final K key;
    //The value of the element
    protected V value;

    public KeyValuePair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public K getKey()
    {
        //return The key of the element
    	
        return key;
    }

    public V getValue()
    {
        //return The value of the element
    	
        return value;
    }

    public void setValue(V value)
    {
        //Sets the value of the element
        //param value the new value to set
    	
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "{\"key\": \"" + key.toString() + "\", \"value\": \"" + value.toString() + "\"}";
    }
}
