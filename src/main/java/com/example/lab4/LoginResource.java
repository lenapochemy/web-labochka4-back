package com.example.lab4;

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

import java.io.StringReader;

@Path("/login")
public class LoginResource {
    @EJB
    UserChecker userChecker;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(String json){
        if(json.length() > 0) {
            JsonReader jsonReader = Json.createReader(new StringReader(json));
            JsonObject object = jsonReader.readObject();

            String result;
            if (userChecker.login(object.getString("login"), object.getString("password"))) {
                //System.out.println("login ok");
                result = ResponseUtils.successResult;
            } else {
                //System.out.println("login fail");
                result = ResponseUtils.failResult;
            }

            jsonReader.close();
            return ResponseUtils.accessResponseWithEntity(200, result);

        } else {
            return ResponseUtils.accessResponse(204);
        }

    }
}
