package com.example.springsecuritydemo.config;

import com.example.springsecuritydemo.entity.Customer;
import com.example.springsecuritydemo.service.CustomerService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.springsecuritydemo.config.AuthConstants.HEADER;
import static com.example.springsecuritydemo.config.AuthConstants.SECRET;
import static com.example.springsecuritydemo.config.AuthConstants.TOKEN_PREFIX;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private CustomerService customerService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CustomerService customerService) {
        super(authenticationManager);
        this.customerService = customerService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(AuthConstants.HEADER);
        if(header == null || !header.startsWith(AuthConstants.TOKEN_PREFIX)) {
            chain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getAuthenticationToken(request);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER);
        if(token == null ) return null;
        String username = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX,""))
                .getBody().getSubject();

        UserDetails userDetails = customerService.loadUserByUsername(username);
        Customer customer = customerService.loadCustomerByName(username);
        return username!=null ? new UsernamePasswordAuthenticationToken(customer,null,userDetails.getAuthorities()) : null;
    }
}
