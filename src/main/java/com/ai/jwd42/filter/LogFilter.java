
package com.ai.jwd42.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import jakarta.servlet.FilterConfig;

@Component
public class LogFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
		// Initialization code if needed
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Example: Check if the user is logged in
		HttpSession session = httpRequest.getSession(false);
		if (session == null || session.getAttribute("role") == null) {
			String requestURI = httpRequest.getRequestURI();
			System.out.println(requestURI);
			if (requestURI.startsWith(httpRequest.getContextPath() + "/owner/login")
					|| requestURI.startsWith(httpRequest.getContextPath() + "/access-denied")) {
				// Forward to the login or access-denied page directly
				chain.doFilter(request, response);
				return;
			} else if (requestURI.startsWith(httpRequest.getContextPath() + "/admin/login")) {
				chain.doFilter(request, response);
				return;
			} else if (requestURI.startsWith(httpRequest.getContextPath() + "/owner")) {
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/owner/login");
				return;
			} else if (requestURI.startsWith(httpRequest.getContextPath() + "/admin")) {
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/login");
				return;
			}

		}

		// User is logged in, check role and URI
		String role = (String) session.getAttribute("role");
		String requestURI = httpRequest.getRequestURI();
		if ((requestURI.startsWith(httpRequest.getContextPath() + "/owner") && role.equals("owner"))
				|| (requestURI.startsWith(httpRequest.getContextPath() + "/admin") && role.equals("admin"))) {
			chain.doFilter(request, response);
		} else {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/access-denied");
		}

	}

	@Override
	public void destroy() {
		// Cleanup code if needed
	}
}