package controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {

        Algorithm algorithmHS = Algorithm.HMAC256("0fbd9e3d-acd5-4acb-adfe-bae1e260221a");
        JWTVerifier verifier = JWT.require(algorithmHS)
                .withIssuer("backend")
                .build();
        DecodedJWT jwt = verifier.verify("eyJhbGciOiJIUzI1NiIsImtpZCIgOiAiMDU1MzNlNWQtMDdlYy00MGYyLWE1Y2EtZjgzOThmMmRmMmY4In0.eyJqdGkiOiJjOTE0ZDg3MS04Njc2LTRkMTctOTMwMS1jODYzMWFmODMwNGQiLCJleHAiOjE1MzExNjc4NzgsIm5iZiI6MCwiaWF0IjoxNTMxMTMxODc4LCJpc3MiOiJodHRwczovL2RlbW8ub2V2ZmFocnBsYW4uY2gvYXV0aC9yZWFsbXMvZXBvbGljZSIsInN1YiI6IjEzZjAxZWVhLTg4MzAtNDdhNy1hMjQ3LTlhMzYzNTFiZDE5OSIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6IjM3NTVhYTRhLTVkNDAtNDg0Yy1hOGZmLWZhNjEwYjE5OWYxMCIsInJlc291cmNlX2FjY2VzcyI6e30sInN0YXRlX2NoZWNrZXIiOiJacGJna0pHNDB3TkFveHVibFEzcDlscFpCOTFfM19kZlEwQThUZjNCQ2hRIn0.TkgYCJ1AtJasM-3LbMfHZVbigjoin33uoNlvMKXwBEw;");
        System.out.println(jwt.toString());


        return ok(views.html.index.render());
    }

}
