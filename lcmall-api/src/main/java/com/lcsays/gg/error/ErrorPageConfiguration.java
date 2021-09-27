package com.lcsays.gg.error;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorPageConfiguration implements ErrorPageRegistrar {
  @Override
  public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
      // 想生效就把这个打开，并把errorbak.html改成error.html
//    errorPageRegistry.addErrorPages(
//        new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"),
//        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500")
//    );
  }

}
