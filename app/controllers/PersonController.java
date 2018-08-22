package controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import models.NaturalPerson;
import models.Self;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.JWTUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class PersonController extends Controller {

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String APPLICATION_JSON = "application/json";
    public static final String WILDCARD = "*";


    public Result getPerson(String id) {
        DecodedJWT jwt = JWTUtil.getToken(request());
        if (jwt != null) {
            String fileName = produceFileNameFromAuth(jwt);
            if (fileName.contains(id)) {
                try {
                    return ok(new File(fileName)).withHeader(ACCESS_CONTROL_ALLOW_ORIGIN, WILDCARD).as(APPLICATION_JSON);
                } catch (Exception e) {
                    return userNotFound(fileName);
                }
            } else {
                return unauthorized("Trying to access another Person");
            }
        } else {
            return unauthorized().withHeader(ACCESS_CONTROL_ALLOW_ORIGIN, WILDCARD);
        }

    }

    private Result userNotFound(String fileName) {
        return notFound("User with email " + fileName + " not found").withHeader(ACCESS_CONTROL_ALLOW_ORIGIN, WILDCARD);
    }


    private String produceFileNameFromAuth(DecodedJWT jwt) {
        return produceIdFormAuth(jwt)+".json";
    }

    static String produceIdFormAuth(DecodedJWT jwt) {
        if (jwt != null) {
            String email = jwt.getClaims().get("email").asString();
            Logger.info("E-Mail of User is {}",email);
            return String.format("%d", Math.abs(email.hashCode()));
        }
        return null;
    }



    public Result self() {
        Logger.info("Call to self");
        DecodedJWT jwt = JWTUtil.getToken(request());
        String id = produceIdFormAuth(jwt);
        if (id != null) {
            try {
                File jsonFile = new File(produceFileNameFromAuth(jwt));
                if (!jsonFile.exists()) {
                    producePerson(jwt, jsonFile);
                }
                Self self = new Self(id,id);
                JsonNode json = Json.toJson(self);
                Logger.info("Return json {}",json);
                return ok(json).withHeader(ACCESS_CONTROL_ALLOW_ORIGIN, WILDCARD);
            } catch (Exception e) {
                Logger.error("Error",e);
                return userNotFound(id);
            }
        } else {
            Logger.warn("Not authorized");
            return unauthorized().withHeader(ACCESS_CONTROL_ALLOW_ORIGIN, WILDCARD);
        }
    }

    private void producePerson(DecodedJWT jwt,File jsonFile) {
        String email = jwt.getClaims().get("email").asString();
        String familyName = jwt.getClaims().get("family_name").asString();
        String givenName = jwt.getClaims().get("given_name").asString();

        NaturalPerson np = NaturalPerson.builder().eMail(email).lastName(familyName).firstName(givenName).build();

        try {
            Logger.info("produce Person {}",jsonFile);
            try (FileWriter fw = new FileWriter(jsonFile)) {
                fw.write(Json.toJson(np).toString());
                fw.flush();
            }
        } catch (IOException e) {
            Logger.error("Error when producing Person for "+email,e);
        }
    }

    public Result updatePerson(String id) {
        DecodedJWT jwt = JWTUtil.getToken(request());
        if (jwt != null) {
            String pid = this.produceIdFormAuth(jwt);
            if (pid.equals(id)) {
                try {
                    JsonNode node = request().body().asJson();
                    Logger.info("Update Person {} with {}",id,node);
                    try (FileWriter fw = new FileWriter(new File(produceFileNameFromAuth(jwt)))) {
                        fw.write(node.toString());
                        fw.flush();
                    }
                    return noContent();
                } catch (Exception e) {
                    return userNotFound(id);
                }
            } else {
                Logger.warn("Trying to update another person id sent: {} != id from token {}",id,pid);
                return unauthorized("Trying to update another person");
            }
        } else {
            return unauthorized();
        }
    }

    public Result options() {
        final Optional<String> origin = request().getHeaders().get("Origin");
        if (origin.isPresent()) {
            return ok("").withHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin.get());
        } else {
            return badRequest();
        }
    }

}
