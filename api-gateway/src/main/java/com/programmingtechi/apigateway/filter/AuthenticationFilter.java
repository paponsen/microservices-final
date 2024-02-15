package com.programmingtechi.apigateway.filter;

import com.programmingtechi.apigateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>  {
    public static class Config{
    }
    @Autowired
    RouteValidator validator;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    JwtUtil jwtUtil;
    public AuthenticationFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(validator.isSecured.test(exchange.getRequest())){
                // check header contains token or not
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("missing authorization header");
                }
                String authenticationToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authenticationToken!= null && authenticationToken.startsWith("Bearer ")){
                    authenticationToken = authenticationToken.substring(7);
                }
                try {
                    //restTemplate.getForObject("http://authentication-service/auth/validate?token="+authenticationToken, String.class);
                    jwtUtil.validateToken(authenticationToken);
                } catch (Exception e){
                    System.out.println("Unauthorized access to the api");
                    e.printStackTrace();
                    throw new RuntimeException("Unauthorized access to the api");
                }

            }
            return chain.filter(exchange);
        });
    }
}
