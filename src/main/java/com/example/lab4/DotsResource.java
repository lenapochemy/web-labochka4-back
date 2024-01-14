package com.example.lab4;

import database.DotChecker;
import database.UserChecker;
import exceptions.DBException;
import exceptions.TokenException;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.stream.JsonParsingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Dot;
import model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/dots")
public class DotsResource {
    @EJB
    DotChecker dotChecker;
    @EJB
    UserChecker userChecker;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkDot(Dot requestDot, @Context HttpHeaders headers){
        try {
            System.out.println("\u001B[36m" + "запрос на новую точку" + "\u001B[0m");
            String token = headers.getHeaderString("Authorization");
            if(requestDot == null || token == null || token.isEmpty() )
                return Response.status(400).build();
            User user = userChecker.getUserFromToken(token);
            Dot dot = new Dot(requestDot.getX(), requestDot.getY(), requestDot.getR(), user);
            dotChecker.addDot(dot);
            return Response.ok().build();

        } catch (DBException e){
            return Response.status(503).build();
        } catch (TokenException e){
            return Response.status(403).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDots(@Context HttpHeaders headers) {
        try {
            System.out.println("\u001B[36m" + "запрос на получение точек" + "\u001B[0m");
            String token = headers.getHeaderString("Authorization");
            if(token == null || token.isEmpty()) return Response.status(400).build();
            User user = userChecker.getUserFromToken(token);
            if (user == null) return Response.status(403).build();

            List<Dot> dots = dotChecker.getDotsByUser(user);
            if (dots == null) return Response.status(204).build();

            dots = dots.stream().sorted(Comparator.comparing(Dot::getTime).reversed()).collect(Collectors.toList());

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (Dot dot : dots) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("x", dot.getX())
                        .add("y", dot.getY())
                        .add("r", dot.getR())
                        .add("result", dot.getResult())
                        .add("time", dot.getTime())
                );
            }

            String result = arrayBuilder.build().toString();
//            System.out.println(result);
            return Response.ok().entity(result).build();
        } catch (JsonParsingException e){
            return Response.status(400).build();
        } catch (DBException e){
            return Response.status(503).build();
        } catch (TokenException e){
            return Response.status(403).build();
        }
    }
}
