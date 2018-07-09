package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import play.Logger;
import play.mvc.Http;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

public class JWTUtil {


    static JWTVerifier verifier;

    static {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            String publicKeyContent = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkKhzz7RKWxT+KVyPVRnw59ITfuvF4ZvWNQ6/Qmpih+ZIzCCgpxX3r3yaGysWVkIxWl5WPrUoYdtkOBQ/1aY+58xig/neJ+S0HX2SS1P7/2fegL7EXz9rgsWfZwHpGdQM+9v2LF9np+WBcRbSUVaQPrDy77mc0gqEJ5n5ex/80+K6Cw0THDKqoMFkprRMZdahx4z4t74yDZB4fxzE571rhKWBAgP7iXTVeNRQ7q6ewUA82MCdcCn7xIyuOyQV2Cr+YnBaSO+Tc5vtNEYa08lwVFBUVYmW+nYJsI8guAxuLMvHN/IK4D3/Y6NjJT9xTtCDpA+kuk2jYMaRtlUFRaP0YQIDAQAB";
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
            RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
            Algorithm algorithm = Algorithm.RSA256(pubKey, null);
            verifier = JWT.require(algorithm)
                    .withIssuer("https://demo.oevfahrplan.ch/auth/realms/epoliuce")
                    .build(); //Reusable verifier instance
        } catch (Exception e) {
            Logger.error("Error when init keys",e);
        }
    }


    public static DecodedJWT getToken(Http.Request request) {
        Optional<String> authHeader = request.getHeaders().get("Authorization");
        if (authHeader.isPresent()) {
            String tk = authHeader.get().split(" ")[1];
            return verifier.verify(tk);
        }
        return null;
    }

}
