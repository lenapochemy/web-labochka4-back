package com.example.lab4;

import database.DotChecker;
import database.UserChecker;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;

import java.io.StringReader;

@Path("/checkDot")
public class CheckDotResource {
    @EJB
    DotChecker dotChecker;
    @EJB
    UserChecker userChecker;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkDot(String json){
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject object = jsonReader.readObject();
        Double x = Double.parseDouble(object.getString("x"));
        Double y = Double.parseDouble(object.getString("y"));
        Double r = Double.parseDouble(object.getString("r"));
        String login = object.getString("user");
        jsonReader.close();
        User user = userChecker.getUserByLogin(login);

        if(user == null) return ResponseUtils.accessResponse(403);

        if(dotChecker.addDot(x, y, r, user)) {
            return ResponseUtils.accessResponseWithEntity(200, ResponseUtils.successResult);
        } else return ResponseUtils.accessResponseWithEntity(501, ResponseUtils.failResult);


    }
}
