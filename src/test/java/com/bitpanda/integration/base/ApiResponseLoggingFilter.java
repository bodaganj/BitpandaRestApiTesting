package com.bitpanda.integration.base;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.internal.print.ResponsePrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ApiResponseLoggingFilter implements Filter {

   private final PrintStream stream;
   private final Matcher<?> matcher;
   private final List<LogDetail> logDetail;
   private final boolean shouldPrettyPrint;

   public ApiResponseLoggingFilter(List<LogDetail> logDetail) {
      this(logDetail, isPrettyPrintingEnabled(), System.out, Matchers.any(Integer.class));
   }

   public ApiResponseLoggingFilter(@NonNull List<LogDetail> logDetail,
                                   boolean prettyPrint,
                                   @NonNull PrintStream stream,
                                   @NonNull Matcher<? super Integer> matcher) {
      if (!logDetail.contains(LogDetail.PARAMS) && !logDetail.contains(LogDetail.URI) && !logDetail.contains(LogDetail.METHOD)) {
         this.shouldPrettyPrint = prettyPrint;
         this.logDetail = logDetail;
         this.stream = stream;
         this.matcher = matcher;
      } else {
         throw new IllegalArgumentException(String.format("%s is not a valid %s for a response.",
                                                          logDetail,
                                                          LogDetail.class.getSimpleName()));
      }
   }

   private static boolean isPrettyPrintingEnabled() {
      return Objects.isNull(RestAssured.config) || RestAssured.config.getLogConfig().isPrettyPrintingEnabled();
   }

   @Override
   public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
      Response response = ctx.next(requestSpec, responseSpec);
      int statusCode = response.statusCode();

      if (this.matcher.matches(statusCode)) {
         stream.println("================[ Response ================");
         for (LogDetail ld : logDetail) {
            ResponsePrinter.print(response, response, this.stream, ld, this.shouldPrettyPrint, Collections.emptySet());
         }
         stream.println("===========================================");

         byte[] responseBody;
         if (!logDetail.contains(LogDetail.BODY) && !logDetail.contains(LogDetail.ALL)) {
            responseBody = null;
         } else {
            responseBody = response.asByteArray();
         }
         response = this.cloneResponseIfNeeded(response, responseBody);
      }

      return response;
   }

   private Response cloneResponseIfNeeded(Response response, byte[] responseAsString) {
      if (!Objects.isNull(responseAsString) &&
         response instanceof RestAssuredResponseImpl &&
         !((RestAssuredResponseImpl) response).getHasExpectations()) {
         Response build = (new ResponseBuilder()).clone(response).setBody(responseAsString).build();
         ((RestAssuredResponseImpl) build).setHasExpectations(true);
         return build;
      } else {
         return response;
      }
   }
}
