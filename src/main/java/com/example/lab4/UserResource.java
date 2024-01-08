package com.example.lab4;

import database.UserChecker;
import exceptions.DBException;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;


@Path("/user")
public class UserResource {
    @EJB
    UserChecker userChecker;

    @Path("/logIn")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(User user){
        try {
            String token = userChecker.login(user);
            if(token != null){
                String result = "{ \"token\" : \"" + token + "\" }";
                return Response.ok().entity(result).build();
            } else {
                return Response.status(401).build();
            }
        } catch (DBException e){
            return Response.status(503).build();
        }
    }

    @Path("/reg")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registration(User user){
        try {
            if (userChecker.registration(user)) {
                return Response.ok().build();
            } else {
                return Response.status(401).build();
            }
        } catch (DBException e){
            return Response.status(503).build();
        }
    }

}
