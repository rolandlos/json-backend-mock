package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;
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
        Logger.info("-------------------- new Person Request");
        final Map<String, List<String>> headers = request().getHeaders().toMap();
        headers.keySet().forEach(key -> {
            Logger.info(key+" -> "+headers.get(key));
        });
        String person = "{ name : \"Bornhauser\"}";
        return ok(new File("/Users/rolandloser/Downloads/person.json")).as("application/json");

    }

}
