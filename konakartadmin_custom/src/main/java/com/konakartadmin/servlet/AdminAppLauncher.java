/**
 * 
 */
package com.konakartadmin.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * Servlet used to launch the admin app
 */
public class AdminAppLauncher extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    /** the log */
    protected static Log log = LogFactory.getLog(AdminAppLauncher.class);

    /**
     * Receives the className and storeId as parameters in the URL. If the className is not passed
     * as a parameter, then a manager called
     * <code>com.konakartadmin.servlet.AdminAppAuthenticationMgr</code> is instantiated. The userId
     * should also be in the request but the launcher doesn't have to know the name of the parameter
     * that it is assigned to since the AuthenticationMgr returns the userId to use for the login.
     * 
     * If the authenticateRequest() method of the AuthenticationMgr returns a userId, this means
     * that the user is logged in and this userId is passed to the GWT application. If null is
     * returned, we test to see whether we need to redirect to a login page of the authentication
     * system.
     * 
     * If the authenticateRequest() method returns null and the sendRedirect() method returns null,
     * we still pass the storeId to the GWT application so that it doesn't show the drop list of
     * available stores in the login dialog.
     * 
     * Finally, if the storeId is also null, then KonaKart starts up normally showing the login
     * dialog with a list of available stores if in multi-store mode.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse httpServletResponse)
            throws ServletException, IOException
    {

        String storeId = request.getParameter("storeId");
        String className = request.getParameter("className");
        if (className == null || className.length() == 0)
        {
            className = "com.konakartadmin.servlet.AdminAppAuthenticationMgr";
        }

        try
        {
            AdminAppAuthenticationMgrIf authMgr = getAuthenticationMgr(className);

            // Attempt to authenticate the user
            String userId = authMgr.authenticateRequest(request);

            // See whether we need to redirect to a login page
            if (userId == null)
            {
                StringBuffer url = request.getRequestURL();
                if (request.getQueryString() != null)
                {
                    url.append("?");
                    url.append(request.getQueryString());
                }
                URL loginUrl = authMgr.buildManualLoginURL(new URL(url.toString()));
                if (loginUrl != null)
                {
                    httpServletResponse.sendRedirect(loginUrl.toString());
                    return;
                }
            }

            httpServletResponse.setContentType("text/html");
            PrintWriter writer = httpServletResponse.getWriter();

            // k
            writer.println("<link rel='stylesheet' href='" + request.getContextPath()
                    + "/KonakartAdmin.css'>");

            // GWT code
            writer.println("<div id='kkAdmin'></div>");
            writer.println("<script language='javascript' src='" + request.getContextPath()
                    + "/konakartadmin.nocache.js'></script>");
            writer
                    .println("<iframe id='__gwt_historyFrame' style='width:0;height:0;border:0'></iframe>");
            writer
                    .println("<iframe id='__printingFrame' style='width:0;height:0;border:0'></iframe>");

            // Form for user info
            writer
                    .println("<form id=\"kkUserForm\" action=\"http://somesite.com/prog/adduser\" method=\"post\">");
            if (userId != null && userId.length() > 0)
            {
                writer.println("<input type=\"hidden\" name=\"user\" value=\"" + userId + "\"/>");
            }

            if (storeId != null && storeId.length() > 0)
            {
                writer.println("<input type=\"hidden\" name=\"store\" value=\"" + storeId + "\">");
            }
            writer.println("</form>");

            writer.close();

        } catch (ClassNotFoundException e)
        {
            log.error("Could not find the class with name " + className, e);
        } catch (InstantiationException e)
        {
            log.error("Could not instantiate the class with name " + className, e);
        } catch (IllegalAccessException e)
        {
            log.error("Could not access the class with name " + className, e);
        } catch (Exception e)
        {
            log.error("Exception caught while authenticating a user", e);
        }
    }

    /**
     * Instantiate an AdminAppAuthenticationMgr
     * 
     * @param className
     *            If not null or empty, we instantiate a class with this name.
     * @return Returns an instantiated manager object
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected AdminAppAuthenticationMgrIf getAuthenticationMgr(String className)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<?> theClass = Class.forName(className);
        AdminAppAuthenticationMgrIf authMgr = (AdminAppAuthenticationMgrIf) theClass.newInstance();
        return authMgr;
    }

}