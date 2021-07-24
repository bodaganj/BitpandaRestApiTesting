package com.bitpanda.integration.base;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.print.RequestPrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.AllArgsConstructor;

import java.io.PrintStream;
import java.util.Collections;

@AllArgsConstructor
public class ApiRequestLoggingFilter implements Filter {

   private final LogDetail logDetail;
   private final PrintStream stream;
   private final boolean shouldPrettyPrint;

   @Override
   public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
      stream.println("================[ Request ]================");
      RequestPrinter.print(requestSpec,
                           requestSpec.getMethod(),
                           requestSpec.getURI(),
                           this.logDetail,
                           Collections.emptySet(),
                           this.stream,
                           this.shouldPrettyPrint);
      stream.println("===========================================");
      return ctx.next(requestSpec, responseSpec);
   }
}
