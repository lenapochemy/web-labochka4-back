package com.example.lab4;

import database.DotChecker;
import database.UserChecker;
import jakarta.ejb.EJB;
import jakarta.json.*;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Dot;
import model.User;

import java.io.StringReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/getDots")
public class GetDotsResource {
    @EJB
    DotChecker dotChecker;

    @EJB
    UserChecker userChecker;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDots(String json){
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject object = jsonReader.readObject();
        String login = object.getString("user");
        jsonReader.close();

        User user = userChecker.getUserByLogin(login);
        if(user == null) return ResponseUtils.accessResponse(403);

        List<Dot> dots = dotChecker.getDotsByUser(user);
        if(dots == null) return ResponseUtils.accessResponse(204);

        dots = dots.stream().sorted(Comparator.comparing(Dot::getTime).reversed()).collect(Collectors.toList());

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for(Dot dot : dots){
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("x", dot.getX())
                    .add("y", dot.getY())
                    .add("r", dot.getR())
                    .add("result", dot.getResult())
                    .add("time", dot.getTime())
            );
        }

        JsonArray value = arrayBuilder.build();
        String result = value.toString();
        System.out.println(result);

        return ResponseUtils.accessResponseWithEntity(200, result);
    }


}
