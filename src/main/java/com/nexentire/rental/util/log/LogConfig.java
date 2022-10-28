package com.nexentire.rental.util.log;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nexentire.rental.re.controller.RTREExportClosingController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class LogConfig {

  Logger logger = LoggerFactory.getLogger(LogConfig.class);
	
  @Around("within(com.nexentire.rental.*))") // ex. within(me.shinsunyoung.demo..*)) 1
  public Object logging(ProceedingJoinPoint pjp) throws Throwable { // 2

    String params = getRequestParams(); // request 값 가져오기

    long startAt = System.currentTimeMillis();

    logger.info("-----------> REQUEST : {}({}) = {}", pjp.getSignature().getDeclaringTypeName(),
        pjp.getSignature().getName(), params);

    Object result = pjp.proceed(); // 4

    long endAt = System.currentTimeMillis();

    logger.info("-----------> RESPONSE : {}({}) = {} ({}ms)", pjp.getSignature().getDeclaringTypeName(),
        pjp.getSignature().getName(), result, endAt - startAt);

    return result;
  }


  private String paramMapToString(Map<String, String[]> paramMap) {
    return paramMap.entrySet().stream().map(entry->String.format("%s -> (%s)", entry.getKey(), entry.getValue()[0])).collect(Collectors.joining(", "));
  }

  // Get request values 
  private String getRequestParams() {

    String params = "없음";

    RequestAttributes requestAttributes = RequestContextHolder
        .getRequestAttributes(); // 3

    if (requestAttributes != null) {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes()).getRequest(); 

      Map<String, String[]> paramMap = request.getParameterMap();
      if (!paramMap.isEmpty()) {
        params = " [" + paramMapToString(paramMap) + "]";
      }
    }

    return params;

  }
}
