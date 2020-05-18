package com.argus.financials.myplanner.gwt.commons.client;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class BasePair extends Pair<Integer, String>
{

    /** serialVersionUID */
    private static final long serialVersionUID = 5542294252565244891L;

    public BasePair()
    {
        super();
    }

    public BasePair(Integer first, String second)
    {
        super(first, second);
    }

    public BasePair(Long first, String second)
    {
        this(first.intValue(), second);
    }

}