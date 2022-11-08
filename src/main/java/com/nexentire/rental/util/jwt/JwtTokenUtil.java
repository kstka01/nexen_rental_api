package com.nexentire.rental.util.jwt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtTokenUtil {

	private static final String SECRET_KEY = Base64.getEncoder().encodeToString("nexen".getBytes());
	
	// jwt access 토큰 생성
	public String create(String id) {
		
		Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
		
		String subject = convertSerialize(id);
		
		return Jwts.builder()
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				.setSubject(subject)
				.setIssuer("nexen_exports")
				.setIssuedAt(new Date())
				.setExpiration(expireDate)
				.compact();
	}
	
	public String convertSerialize(String id) {
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {      
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {        
				oos.writeObject(id);        // 직렬화 코드        
				
				byte[] data = baos.toByteArray();        // 직렬화된 것은 Base64로 암호화        
				return Base64.getEncoder().encodeToString(data);      
			} catch(Throwable e) {
					e.printStackTrace();      
					return null;
			}
		} catch (Throwable e) {      
				e.printStackTrace();      
				return null;    
		}
	}
	
	public String convertData(String subject) {
		
		byte[] data = Base64.getDecoder().decode(subject);
		
		try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {      
			try (ObjectInputStream ois = new ObjectInputStream(bais)) {        
				Object objectMember = ois.readObject();      
				return (String) objectMember;      
			} catch (Throwable e) {
				e.printStackTrace();      
				return null;    
			}
		} catch (Throwable e) {      
			e.printStackTrace();      
			return null;    
		}
	}
	
	// jwt refresh 토큰 생성
    public String createRefreshToken() {
    	Date expireDate = Date.from(Instant.now().plus(30, ChronoUnit.DAYS));
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    //유효한 토큰 여부 확인
    public boolean validateToken(String token) throws Exception {
    	
    	Claims claims = null;
    	try {
    		
    		claims = Jwts.parser()
    				.setSigningKey(SECRET_KEY)
    				.parseClaimsJws(token)
    				.getBody();
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
			throw new Exception("Invalid JWT signature: {}" + e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
			throw new Exception("Invalid JWT token: {}" + e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
			throw new Exception("JWT token is expired: {}" + e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
			throw new Exception("JWT token is unsupported: {}" + e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
			throw new Exception("JWT claims string is empty: {}" + e.getMessage());
		}
    	
    	return !claims.getExpiration().before(new Date());
    }
    
    public String validateAndgetUserId(String token) throws Exception {
		
    	Claims claims = null;
    	try {
    		
    		claims = Jwts.parser()
    				.setSigningKey(SECRET_KEY)
    				.parseClaimsJws(token)
    				.getBody();
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
			throw new Exception("Invalid JWT signature: {}" + e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
			throw new Exception("Invalid JWT token: {}" + e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
			throw new Exception("JWT token is expired: {}" + e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
			throw new Exception("JWT token is unsupported: {}" + e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
			throw new Exception("JWT claims string is empty: {}" + e.getMessage());
		}
		
		return convertData(claims.getSubject());
	}

    public String parseBearerToken(HttpServletRequest request) {
		
    	try {
    		String bearerToken = request.getHeader("Authorization");
    		
    		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
    			return bearerToken.substring(7);
    		}
    	}catch (Exception e) {
			// TODO: handle exception
    		return null;
		}
		
		return null;
	}
}
