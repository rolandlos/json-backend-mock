package controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import utils.JWTUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class PersonController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result getPerson() {
        DecodedJWT jwt = JWTUtil.getToken(request());
        if (jwt != null) {
            String email = jwt.getClaims().get("email").asString();
            try {
                File jsonFile = new File(email+".json");
                if (!jsonFile.exists()) {
                    producePerson(jwt);
                }
                return ok(new File(email + ".json")).withHeader("Access-Control-Allow-Origin", "*").as("application/json");
            } catch (Exception e) {
                return notFound("User with email "+email+" not found").withHeader("Access-Control-Allow-Origin", "*");
            }
        } else {
            return unauthorized().withHeader("Access-Control-Allow-Origin", "*");
        }

    }

    private void producePerson(DecodedJWT jwt) {
        String email = jwt.getClaims().get("email").asString();
        String family_name = jwt.getClaims().get("family_name").asString();
        String given_name = jwt.getClaims().get("given_name").asString();
        String name = jwt.getClaims().get("name").asString();
        String json = "{ \n"+
                "\"firstName\": \""+given_name+"\",\n"+
                "\"officialName\": \""+family_name+"\",\n"+
                "\"email\": \""+email+"\"\n"+
               "}";
        try {
            Logger.info("produce Person "+email+".json");
            FileWriter fw = new FileWriter(new File(email + ".json"));
            fw.write(json);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Logger.error("Error when producing Person for "+email,e);
        }
    }

    public Result insertPerson() {
        DecodedJWT jwt = JWTUtil.getToken(request());
        if (jwt != null) {
            String email = jwt.getClaims().get("email").asString();
            try {
                JsonNode node = request().body().asJson();
                FileWriter fw = new FileWriter(new File(email + ".json"));
                fw.write(node.toString());
                fw.flush();
                fw.close();
                return ok().withHeader("Access-Control-Allow-Origin", "*");
            } catch (Exception e) {
                return notFound("User with email "+email+" not found").withHeader("Access-Control-Allow-Origin", "*");
            }
        } else {
            return unauthorized();
        }
    }

    public Result options() {
        return ok("").withHeader("Access-Control-Allow-Origin","*");
    }

}
