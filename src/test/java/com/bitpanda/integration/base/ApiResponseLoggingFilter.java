package com.bitpanda.integration.base;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.print.ResponsePrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.AllArgsConstructor;
import org.hamcrest.Matcher;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Objects;

@AllArgsConstructor
public class ApiResponseLoggingFilter implements Filter {

   private final LogDetail logDetail;
   private final PrintStream stream;
   private final Matcher<?> matcher;
   private final boolean shouldPrettyPrint = Objects.isNull(RestAssured.config) || RestAssured.config.getLogConfig().isPrettyPrintingEnabled();

   @Override
   public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
      Response response = ctx.next(requestSpec, responseSpec);
      int statusCode = response.statusCode();

      if (this.matcher.matches(statusCode)) {
         stream.println("================[ Response ================");
         ResponsePrinter.print(response, response, this.stream, logDetail, this.shouldPrettyPrint, Collections.emptySet());
         stream.println("===========================================");
      }

      return response;
   }
}
