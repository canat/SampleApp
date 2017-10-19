package io.antmedia.serverapp.pscp.filter;

import java.io.File;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.antmedia.serverapp.pscp.Application;



public class HttpForwardFilter implements javax.servlet.Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String requestURI = ((HttpServletRequest)request).getRequestURI();

		/*
		File f = new File("webapps/"+ requestURI);
		if (!f.exists()) 
		{
			String redirectUri = Application.STORAGE_FORWARD_URL + requestURI;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect(redirectUri);
			return;
		}
		*/
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
