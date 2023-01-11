package kiis.edu.rating.helper;

import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;

public interface Constant {
//  JWT
    String SECRET_KEY = "ProgrammerClubVerySecretKeyCreatedBySon";
    SecretKey ENCODED_SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    String TOKEN_HEADER = "Authorization";
    String CLAIM_AUTHORITY = "authorities";
    String BEARER = "Bearer ";
//  Logger
    Logger LOGGER = LoggerFactory.getLogger(Logger.class);
}
