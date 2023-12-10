package com.example.lab4;

import database.UserChecker;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.StringReader;

@Path("/reg")
public class RegistrationResource {

    @EJB
    UserChecker userChecker;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response registration(String json){
        System.out.println("I want to die");
        if(json.length() > 0) {
            JsonReader jsonReader = Json.createReader(new StringReader(json));
            JsonObject object = jsonReader.readObject();
//            String login = object.getString("login");
//            String password = object.getString("password");
            String result;

            if (userChecker.registration(object.getString("login"), object.getString("password"))) {
               // System.out.println("reg ok");
                result = ResponseUtils.successResult;
            } else {
                //System.out.println("reg fail");
                result = ResponseUtils.failResult;
            }

            return ResponseUtils.accessResponseWithEntity(200, result);
        } else {
            return ResponseUtils.accessResponse(204);
        }

    }
}
