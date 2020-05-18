package com.argus.financials.myplanner.gwt.commons.client;

import java.io.Serializable;


/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class Pair<T1, T2> implements Serializable
{

    /** serialVersionUID */
    private static final long serialVersionUID = 5399518109519592036L;

    private T1 first;

    private T2 second;

    public Pair()
    {
        super();
    }

    public Pair(T1 first, T2 second)
    {
        this.first = first;
        this.second = second;
    }

    /**
     * @return Returns the first.
     */
    public T1 getFirst()
    {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(T1 first)
    {
        this.first = first;
    }

    /**
     * @return Returns the second.
     */
    public T2 getSecond()
    {
        return second;
    }

    /**
     * @param second the second to set
     */
    public void setSecond(T2 second)
    {
        this.second = second;
    }

    public String toString()
    {
        return first + ":" + second;
    }

    public int hashCode()
    {
        return first.hashCode();
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object other)
    {
        Pair<T1, T2> otherCast = (Pair<T1, T2>) other;
        return first.equals(otherCast.first);
    }

}