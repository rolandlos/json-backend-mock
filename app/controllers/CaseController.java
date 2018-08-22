package controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import utils.JWTUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class CaseController extends Controller {


    public static final String APPLICATION_JSON = "application/json";

    public Result create() {
        String uuid = "\""+UUID.randomUUID().toString()+"\"";
        Logger.info("Create Case: {}",uuid);
        return ok(uuid).as(APPLICATION_JSON);
    }


    public Result list() {
        DecodedJWT jwt = JWTUtil.getToken(request());
        if (jwt != null) {
            String pid = PersonController.produceIdFormAuth(jwt);
            File dir = new File(pid);
            if (dir.exists() && dir.isDirectory()) {
                String collect = Arrays.stream(dir.listFiles()).map(f -> {
                    try {
                        return new String(Files.readAllBytes(f.toPath()));
                    } catch (IOException e) {
                        Logger.error("Error when reading file {}",f,e);
                    }
                    return "{}";
                }).collect(Collectors.joining(","));
                Logger.info("Return: [{}]",collect);
                return ok("[" + collect + "]").as(APPLICATION_JSON);
            } else {
                return ok("[]").as(APPLICATION_JSON);
            }
        }
        return unauthorized();
    }

    public Result delete(String id) {
        DecodedJWT jwt = JWTUtil.getToken(request());
        if (jwt != null) {
            String pid = PersonController.produceIdFormAuth(jwt);
            String fileName = pid + "/" + id;
            File f = new File(fileName);
            if (f.exists()) {
                f.delete();
                return noContent();
            } else {
                return notFound();
            }
        }
        return unauthorized();
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
