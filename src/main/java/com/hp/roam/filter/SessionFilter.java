/*package com.hp.roam.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

*//**
 * @author ck
 * @date 2019年5月16日 下午1:12:04
 *//*
@WebFilter(filterName="seesionFilter",urlPatterns="/*")
public class SessionFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String requestUri = request.getRequestURI();

        if (requestUri.indexOf("/login.html") > -1 || requestUri.indexOf("/login") > -1) {
            return ;
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            // 如果是session超时，在此处做处理。
            response.sendRedirect(request.getContextPath() + "/login");
            return ;
        }
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
*/