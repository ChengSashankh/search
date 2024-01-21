//package fyi.deeno.searchservice.auth;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class OurCorsFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        response.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) servletRequest).getHeader("Origin"));
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, OPTIONS");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Auth-Token, Content-Type, Accept, X-Requested-With, remember-me");
//
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
