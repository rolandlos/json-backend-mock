package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import static controllers.PersonController.APPLICATION_JSON;

public class EditorTextsController extends Controller {


    public Result search(String title, String language, String corps,String businessCase) {

        String html = "<h1>"+title+"."+language+"</h1><p>Hier käme ein dynamischer Webcontent für "+corps+" und "+businessCase+"</p>";
        String json = "{ \"text\": \""+html+"\"}";

        return ok(json).as(APPLICATION_JSON);


    }
}
