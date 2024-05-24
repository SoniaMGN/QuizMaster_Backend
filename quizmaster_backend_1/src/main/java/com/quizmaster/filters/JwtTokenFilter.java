package com.quizmaster.filters;

import com.quizmaster.Auth.RegisteredUserDetailService;

import com.quizmaster.configurations.WebSecurityConfig;
import com.quizmaster.entities.AuthorizationTokens;
import com.quizmaster.repositories.AuthorizationTokensRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {


    @Autowired
    RegisteredUserDetailService registeredUserDetailService;


    @Autowired
    private AuthorizationTokensRepository authorizationTokensRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {



        String jwtToken = null;
        String username = null;
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {

            Boolean hasFailed=false;
            Optional<AuthorizationTokens> optionalAuthorizationTokens =authorizationTokensRepository.findByValue(bearerToken);
            if(optionalAuthorizationTokens.isEmpty())
                hasFailed=true;

            if(!hasFailed) {
                AuthorizationTokens authorizationTokens = optionalAuthorizationTokens.get();
                if (authorizationTokens.getHasLoggedOut())
                    hasFailed = true;

            }
            jwtToken = bearerToken.substring(7, bearerToken.length());


            Jws<Claims> claims=Jwts.parser().setSigningKey(WebSecurityConfig.secretKey).build().parseClaimsJws(jwtToken);
            username=claims.getBody().getSubject();
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null&&!hasFailed) {
                UserDetails userDetails= registeredUserDetailService.loadUserByUsername(username);
                Date expiry = claims.getBody().getExpiration();
                Date current=new Date();
                if(current.before(expiry))
                {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

