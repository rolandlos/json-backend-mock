package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class EditorTextsController extends Controller {


    public Result search(String title, String language, String corps,String businessCase) {

        return ok("<h1>"+title+"."+language+"</h1><p>Hier käme ein dynamischer Webcontent für "+corps+" und "+businessCase+"</p>").as("text/html");


    }
}
