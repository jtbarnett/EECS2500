
//Written by Jacob Barnett on 11/6/2019

//An interface for performance tracing. Implementing data types
//keep track of all comparisons and reference assignments they make
public interface PerformanceTraceable
{
    //return The number of comparisons made during the lifetime of the implementing data type
    long getComparisonCount();

    //return The number of reference re-assignments during the lifetime of the implementing data type
    long getReferenceAssignmentCount();
}
