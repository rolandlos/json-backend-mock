package controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import utils.JWTUtil;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

public class CaseController extends Controller {


    public Result create() {
        return ok(UUID.randomUUID().toString());
    }

    public Result save() {
        DecodedJWT jwt = JWTUtil.getToken(request());
        if (jwt != null) {
            String pid = PersonController.produceIdFormAuth(jwt);
            try {
                JsonNode node = request().body().asJson();
                File userCaseDir = new File(pid);
                if (!userCaseDir.exists()) {
                    userCaseDir.mkdir();
                }
                try (FileWriter fw = new FileWriter(new File(pid+"/"+node.get("id").asText()))) {
                    fw.write(node.toString());
                    fw.flush();
                }
                return noContent();
            } catch (Exception e) {
                return internalServerError(e.getMessage());
            }
        } else {
            return unauthorized();
        }
    }

}
