package org.example.belqawright.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.HashMap;
import java.util.Map;

public class CustomRequestLoggingFilter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        Map<String, String> headers = new HashMap<>();
        requestSpec.getHeaders().forEach(header ->
                headers.put(header.getName(), header.getValue())
        );

        TestLoggingUtils.logRequestDetails(
                requestSpec.getMethod(),
                requestSpec.getURI(),
                headers,
                requestSpec.getBody()
        );

        return ctx.next(requestSpec, responseSpec);
    }
}