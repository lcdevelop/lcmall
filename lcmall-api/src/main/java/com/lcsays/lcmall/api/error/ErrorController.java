package com.lcsays.lcmall.api.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

  @GetMapping(value = "/404")
  public String error404() {
    return "error";
  }

  @GetMapping(value = "/500")
  public String error500() {
    return "error";
  }

}
