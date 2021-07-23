package com.bitpanda.integration.base;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.print.RequestPrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.NonNull;

import java.io.PrintStream;
import java.util.Collections;

public class ApiRequestLoggingFilter implements Filter {

   private final LogDetail logDetail;
   private final PrintStream stream;
   private final boolean shouldPrettyPrint;

   public ApiRequestLoggingFilter(LogDetail logDetail) {
      this(logDetail, System.out);
   }

   public ApiRequestLoggingFilter(LogDetail logDetail, PrintStream stream) {
      this(logDetail, true, stream);
   }

   public ApiRequestLoggingFilter(@NonNull LogDetail logDetail, boolean shouldPrettyPrint, @NonNull PrintStream stream) {
      if (logDetail == LogDetail.STATUS) {
         throw new IllegalArgumentException(String.format("%s is not a valid %s for a request.",
                                                          LogDetail.STATUS,
                                                          LogDetail.class.getSimpleName()));
      } else {
         this.stream = stream;
         this.logDetail = logDetail;
         this.shouldPrettyPrint = shouldPrettyPrint;
      }
   }

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
