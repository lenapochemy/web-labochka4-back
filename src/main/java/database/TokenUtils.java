package database;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private final long expireTime = 30 * 60 * 1000; //30 минут
    private final String tokenSecret = "rybezhka";
    private final Algorithm algorithm = Algorithm.HMAC256(tokenSecret);

    public TokenUtils(){
    }

    public String generateToken(User user){
        Date date = new Date(System.currentTimeMillis() + expireTime);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        String token = JWT.create()
                .withHeader(header)
                .withClaim("login", user.getLogin())
                .withExpiresAt(date)
                .sign(algorithm);
        return token;
    }


}
