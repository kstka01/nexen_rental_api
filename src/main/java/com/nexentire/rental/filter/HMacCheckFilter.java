package com.nexentire.rental.filter;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nexentire.rental.constants.NexenConstants;
import com.nexentire.rental.util.hmac.HMacUtil;
import com.nexentire.rental.util.jwt.JwtTokenUtil;

/**
 * 무결성 체크 필터
 * HMAC
 */
@Component
@WebFilter(urlPatterns = "/v1/apis/api/vendor/*")
@Order(1)
public class HMacCheckFilter extends OncePerRequestFilter {

	@Autowired
	private HMacUtil hMacUtil;
	@Autowired
	private JwtTokenUtil tokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub

		RereadableRequestWrapper rereadableRequestWrapper = null;
		HttpServletResponse httpServletResponse = null;
		try {
			rereadableRequestWrapper = new RereadableRequestWrapper((HttpServletRequest)request);
			httpServletResponse = (HttpServletResponse)response;
			
			String remoteAddr = rereadableRequestWrapper.getRemoteAddr();
			String remoteUri = rereadableRequestWrapper.getRequestURI();
			if(remoteAddr.equals(NexenConstants.LOCAL)) {
				String messages = rereadableRequestWrapper.getHeader("Nexen-Vendor-MESSAGE-HEADER");
				String token = tokenUtil.parseBearerToken(rereadableRequestWrapper);
				
				if(!String.valueOf(token).equals("null")) {
					
					JsonParser jsonParser = new JsonParser();
					JsonObject jsonObject = (JsonObject)jsonParser.parse(messages);
							
					String messageId = jsonObject.get("messageId").getAsString();
					
					//System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
					String hmacString = hMacUtil.generateHMac(token, rereadableRequestWrapper.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
					if(!String.valueOf(messageId).equals("null")) {
						if(!messageId.equals(hmacString)) {
							throw new Exception("Error: Data is changed!!");
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Data is changed!!");
			//httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
		
		filterChain.doFilter(rereadableRequestWrapper, response);
	}
}
