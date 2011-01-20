package com.argus.financials.myplanner.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.argus.financials.utils.ThreadLocalUtils;
import com.argus.util.DateUtils;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
public class TransactionFilter implements Filter {

    /** Used for logging. */
    private static final Logger LOG = Logger.getLogger(TransactionFilter.class);

    private PlatformTransactionManager transactionManager;

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext sc = filterConfig.getServletContext();
        // mandatory
        transactionManager = (PlatformTransactionManager) WebUtils.getBean(sc, "transactionManager");
        LOG.info("transactionManager: " + transactionManager);
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        transactionManager = null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        //eg /myplanner/login.do
//        Object p = WebUtils.getPrincipal();
//        if (httpRequest.getSession(false) == null || !httpRequest.isRequestedSessionIdValid()
//            || !(p instanceof Principal)) {
//            //execute business logic here
//            chain.doFilter(request, response);
//            return;
//        }

        // user logged in already (has valid authentication)
        try {
            // set current date/user (will/can be used in AuditableListener for Auditable data)
            ThreadLocalUtils.setDate(DateUtils.getCurrentDateTime());
//            ThreadLocalUtils.setUser(new StringPair(((Principal) p).getUsername()));
            // process filter chain
            doFilterInTransaction(httpRequest, (HttpServletResponse) response, chain);
        } finally {
            //cleanup thread (will clear ALL thread local values stored for this thread)
            ThreadLocalUtils.clear();
        }
    }

    /**
     * 
     * @param httpRequest
     * @param httpResponse
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    private void doFilterInTransaction(HttpServletRequest httpRequest,
        HttpServletResponse httpResponse, FilterChain chain) throws IOException, ServletException {

        String uri = httpRequest.getRequestURI(); // eg /myplanner/xxx.do

        boolean readOnly = WebUtils.isReadOnly(httpRequest);

        //get/set propogationBehavior
        Integer p = WebUtils.getPropogationBehavior(httpRequest);
        if (p == null) {
            p = TransactionDefinition.PROPAGATION_REQUIRED; //default
        } else if (p == TransactionDefinition.PROPAGATION_NOT_SUPPORTED || p == TransactionDefinition.PROPAGATION_NEVER) {
            //execute business logic here
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        DefaultTransactionDefinition txnDef = new DefaultTransactionDefinition(p);

        //set readOnly flag
        txnDef.setReadOnly(readOnly);

        // Return a currently active transaction or create a new one,
        // according to the specified propagation behaviour.
        TransactionStatus txnStatus = transactionManager.getTransaction(txnDef);
        int txnId = txnStatus.hashCode();
        if (readOnly) {
            //set rollbackOnly for readOnly transaction
            txnStatus.setRollbackOnly();
            LOG.debug("Transaction #" + txnId + " readOnly=" + readOnly);
        } else {
            LOG.info("Transaction #" + txnId + " readOnly=" + readOnly);
        }
    
        try {
            //execute business logic here
            chain.doFilter(httpRequest, httpResponse);
            //
            if (txnStatus.isRollbackOnly()) {
                transactionManager.rollback(txnStatus);
                LOG.debug(".. Transaction #" + txnId + " rollback [" + uri + "].");
            } else if (txnStatus.isNewTransaction()) {
                transactionManager.commit(txnStatus);
                LOG.info(".. Transaction #" + txnId + " commit [" + uri + "].");
            } else {
                //transactionManager.commit(txnStatus);
                LOG.warn(".. Transaction #" + txnId + " no rollback/commit [" + uri + "].");
            }
        } catch (Throwable e) {
            LOG.error(".. Transaction #" + txnId + " - unable to doFilter: [" + uri + "] " + e.getMessage(), e);
            //
            if (txnStatus.isNewTransaction()) {
                transactionManager.rollback(txnStatus);
                LOG.error(".. Transaction #" + txnId + " rollback [" + uri + "[" + httpRequest.getQueryString() + "].");
            }
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Transaction #" + txnId + " - unable to doFilter: [" + uri + "] " + e.getMessage());
        }
    }

}