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
        String uuid = UUID.randomUUID().toString();
        Logger.info("Create Case: {}",uuid);
        return ok(uuid).as("application/json");
    }

    public Result save() {
        DecodedJWT jwt = JWTUtil.getToken(request());
        if (jwt != null) {
            String pid = PersonController.produceIdFormAuth(jwt);
            try {
                JsonNode node = request().body().asJson();
                Logger.info("Save case {} for user: {}",node,pid);
                File userCaseDir = new File(pid);
                if (!userCaseDir.exists()) {
                    Logger.info("Make userDir {}",userCaseDir);
                    userCaseDir.mkdir();
                }
                String fileName = pid+"/"+node.get("id").asText();
                try (FileWriter fw = new FileWriter(new File(fileName))) {
                    fw.write(node.toString());
                    fw.flush();
                    Logger.info("Written case to {}",fileName);
                }
                return noContent();
            } catch (Exception e) {
                Logger.error("Something when wrong",e);
                return internalServerError(e.getMessage());
            }
        } else {
            Logger.error("User not authorized");
            return unauthorized();
        }
    }

}
