package com.example.lab4;

import database.UserChecker;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/logout")
public class LogOutResource {

    @EJB
    UserChecker userChecker;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response logOut(){
        int status;
        if(userChecker.logout()) status = 200;
            else status = 401;

        return ResponseUtils.accessResponse(status);
    }
}
