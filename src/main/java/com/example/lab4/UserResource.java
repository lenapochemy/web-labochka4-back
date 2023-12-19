package com.example.lab4;

import database.TokenUtils;
import database.UserChecker;
import exceptions.DBException;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.stream.JsonParsingException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;
import org.hibernate.JDBCException;

import java.io.StringReader;

@Path("/user")
public class UserResource {
    @EJB
    UserChecker userChecker;

    @Path("/logIn")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(String json){
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(json));
            JsonObject object = jsonReader.readObject();
            String login = object.getString("login");
            String password = object.getString("password");
            jsonReader.close();

            String result;
            if (userChecker.login(login, password)) {
                User user = userChecker.getUserByLogin(login);
                result = "{ \"token\" : \"" + user.getToken() + "\" }";
            } else {
                return ResponseUtils.accessResponse(401);
            }
            return ResponseUtils.accessResponseWithEntity(200, result);
        } catch (DBException e){
            return ResponseUtils.accessResponse(503);
        } catch (JsonParsingException e){
            return ResponseUtils.accessResponse(400);
        }
    }

    @Path("/logOut")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response logOut(String json){
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(json));
            JsonObject object = jsonReader.readObject();
            String token = object.getString("userToken");
            User user = userChecker.getUserByToken(token);
            if (user == null) return ResponseUtils.accessResponse(403);

            int status;
            if (userChecker.logout(user)) status = 200;
            else status = 401;
            return ResponseUtils.accessResponse(status);
        } catch (DBException e){
            return ResponseUtils.accessResponse(503);
        } catch (JsonParsingException e){
            return ResponseUtils.accessResponse(400);
        }
    }

    @Path("/reg")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response registration(String json){
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(json));
            JsonObject object = jsonReader.readObject();
            String login = object.getString("login");
            String password = object.getString("password");
            jsonReader.close();

            if (userChecker.registration(login, password)) {
                return ResponseUtils.accessResponse(200);
            } else {
                return ResponseUtils.accessResponse(400);
            }
        } catch (JsonParsingException e){
            return ResponseUtils.accessResponse(400);
        } catch (DBException e){
            return ResponseUtils.accessResponse(503);
        }
    }
}
