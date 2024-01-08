package com.example.lab4;

import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Objects;

@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(
            ContainerRequestContext requestContext
    ) throws IOException {
        String origin = requestContext.getHeaderString("Origin");
        String method = requestContext.getMethod();

        if (Objects.nonNull(origin)
                && Objects.nonNull(method)
                && method.equalsIgnoreCase("OPTIONS")) {
            Response response = Response.ok().build();
            requestContext.abortWith(response);
        }
    }
    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext
    ) throws IOException {

        responseContext.getHeaders().add(
                "Access-Control-Allow-Credentials",
                "true");
        responseContext.getHeaders().add(
                "Access-Control-Allow-Methods",
                "GET, POST, OPTIONS");
        responseContext.getHeaders().add(
                "Access-Control-Allow-Headers",
                "Origin, Content-Type, Authorization");
        responseContext.getHeaders().add(
                "Access-Control-Allow-Origin", "*");
    }


}