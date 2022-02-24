// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.sociallogin;

import javax.annotation.security.DeclareRoles;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.security.jwt.JwtToken;
import com.ibm.websphere.security.social.UserProfileManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "HelloServlet", urlPatterns = "/hello")
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"students", "professor"},
        transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL))
@DeclareRoles("techA")
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter pwriter = response.getWriter();
        HttpSession session = request.getSession();           
        
        String username = request.getUserPrincipal().getName();
        session.setAttribute("userRole", "admin");

        String role = (String) session.getAttribute("userRole");
        String authType = request.getAuthType();
        
        String name = request.getRemoteUser();
        // String role = request.getParameter("security-role");

        Boolean res = request.isUserInRole("students");
        
        Principal p =  request.getUserPrincipal();

        String accessToken = UserProfileManager.getUserProfile().getAccessToken();
        String encryptedAccessToken = UserProfileManager.getUserProfile().getEncryptedAccessToken();
        long tokenLifeTime = UserProfileManager.getUserProfile().getAccessTokenLifeTime();
        JwtToken jwtToken = UserProfileManager.getUserProfile().getIdToken();
        String refreshToken = UserProfileManager.getUserProfile().getRefreshToken();

        session.setAttribute("accessToken", accessToken);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("access_token", accessToken);


        
        pwriter.println("user name is: " +username+ " ------------- ");
        pwriter.println("user role is: " +role+ "-----------------");
        pwriter.println("isUserInRole('admin'): " +res+ "------------");
        pwriter.println("authType is: " +authType+ "----------------");
        pwriter.println("accessToken is: " +requestBody+ "----------------");
        pwriter.println("encryptedAccessToken is: " +encryptedAccessToken+ "----------------");
        pwriter.println("tokenLifeTime is: " +tokenLifeTime+ "----------------");
        pwriter.println("jwtToken is: " +jwtToken+ "----------------");
        pwriter.println("refreshToken is: " +refreshToken+ "----------------");

        
        
        pwriter.close();





        // response.setContentType("text/plain");
        // response.setCharacterEncoding("UTF-8");

        // String username = request.getUserPrincipal().getName();
        // request.setAttribute("username", username);

        // request
        //         .getRequestDispatcher("securedHello.jsp")
        //         .forward(request,response);
    
    }
}
