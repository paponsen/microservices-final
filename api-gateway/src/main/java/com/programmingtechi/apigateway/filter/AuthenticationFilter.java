package com.programmingtechi.apigateway.filter;

import com.programmingtechi.apigateway.util.JwtUtil;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate template;

    @Autowired
    private JwtUtil jwtUtil;
    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {
            if(validator.isSecured.test(exchange.getRequest())){
                //check header contains token or not
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw  new RuntimeException("Missing authorization header");
                }
                String authHeader= exchange.getRequest().getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader!= null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                }
                try {
                    //REST call to AUTH service
                    //Security risk
                    //template.getForObject("http://authentication-service//validate?token="+authHeader,String.class);
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e){
                    System.out.println("unauthorized access to application");
                    throw new RuntimeException("unauthorized access to application");
                }

            }

            return chain.filter(exchange);
        });
    }

    public static class Config{

    }
}
