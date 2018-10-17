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

import static controllers.CaseController.APPLICATION_JSON;

public class AddendumController extends Controller {


    public Result create(String id) {
        String uuid = "\""+ UUID.randomUUID().toString()+"\"";
        Logger.info("Create Addendum for CaseID: {} -> {}",id,uuid);
        return ok(uuid).as(APPLICATION_JSON);
    }

    public Result save(String caseId, String addendumId) {
        DecodedJWT jwt = JWTUtil.getToken(request());
        if (jwt != null) {
            String pid = PersonController.produceIdFormAuth(jwt);
            try {
                JsonNode node = request().body().asJson();
                Logger.info("Save Addendumg {} for case {} for user: {}",node,caseId,pid);
                File caseAddendumDir = new File(pid+"/"+caseId+".addendum");
                if (!caseAddendumDir.exists()) {
                    Logger.info("Make caseAddendumDir {}",caseAddendumDir);
                    caseAddendumDir.mkdir();
                }
                String fileName = pid+"/"+caseId+".addendum"+"/"+addendumId;
                try (FileWriter fw = new FileWriter(new File(fileName))) {
                    fw.write(node.toString());
                    fw.flush();
                    Logger.info("Written Addendum to {}",fileName);
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
