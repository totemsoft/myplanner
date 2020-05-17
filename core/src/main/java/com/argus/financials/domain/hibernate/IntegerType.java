package com.argus.financials.domain.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

public class IntegerType implements UserType, Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -7970464050046601185L;

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException
    {
        return cached;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException
    {
        return value;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException
    {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException
    {
        return (x == y) || (x instanceof Number && y instanceof Number && x.equals(y));
    }

    @Override
    public int hashCode(Object x) throws HibernateException
    {
        return x.hashCode();
    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {
        return new Integer(rs.getInt(names[0]));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        st.setInt(index, ((Number) value).intValue());
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException
    {
        return original;
    }

    @Override
    public Class returnedClass()
    {
        return Integer.class;
    }

    @Override
    public int[] sqlTypes()
    {
        return new int[]
        {
            Types.INTEGER
        };
    }

}
