package com.quizmaster.filters;

import com.quizmaster.entities.AuthorizationTokens;
import com.quizmaster.repositories.AuthorizationTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;



public class VerifiedFilter extends OncePerRequestFilter {

    @Autowired
    private AuthorizationTokensRepository authorizationTokensRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(SecurityContextHolder.getContext().getAuthentication() != null) {

            String bearerToken = request.getHeader("Authorization");
            Boolean hasFailed = false;

            Optional<AuthorizationTokens> optionalAuthorizationTokens = authorizationTokensRepository.findByValue(bearerToken);
            if (optionalAuthorizationTokens.isEmpty())
                hasFailed = true;

            if (!hasFailed) {
                AuthorizationTokens authorizationTokens = optionalAuthorizationTokens.get();
                if (!authorizationTokens.isVerified())
                    hasFailed = true;
            }

            if (hasFailed) {
                System.out.println("Authorization Error");
//                response.setStatus(HttpStatus.UNAUTHORIZED.value(), "Authorization Not verified");
                throw new RuntimeException("Email Not Verified");
            }


        }

        filterChain.doFilter(request, response);

    }
}
