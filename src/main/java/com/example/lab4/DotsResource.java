package com.example.lab4;

import database.DotChecker;
import database.UserChecker;
import jakarta.ejb.EJB;
import jakarta.json.*;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Dot;
import model.User;
import org.hibernate.JDBCException;

import java.io.StringReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/dots")
public class DotsResource {
    @EJB
    DotChecker dotChecker;
    @EJB
    UserChecker userChecker;

    @Path("/newDot")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkDot(String json){
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(json));
            JsonObject object = jsonReader.readObject();
            Double x = Double.parseDouble(object.getString("x"));
            Double y = Double.parseDouble(object.getString("y"));
            Double r = Double.parseDouble(object.getString("r"));
            String token = object.getString("userToken");
            jsonReader.close();
            User user = userChecker.getUserByToken(token);

            if (user == null) return ResponseUtils.accessResponse(403);

            if (dotChecker.addDot(x, y, r, user)) {
                return ResponseUtils.accessResponseWithEntity(200, ResponseUtils.successResult);
            } else return ResponseUtils.accessResponseWithEntity(501, ResponseUtils.failResult);
        } catch (JDBCException e){
            return ResponseUtils.accessResponse(503);
        }
    }

    @Path("/allDots")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDots(String json) {
        try {
            if(json.isEmpty()) return ResponseUtils.accessResponse(400);
            JsonReader jsonReader = Json.createReader(new StringReader(json));
            JsonObject object = jsonReader.readObject();
            String token = object.getString("userToken");
            jsonReader.close();

            User user = userChecker.getUserByToken(token);
            if (user == null) return ResponseUtils.accessResponse(403);

            List<Dot> dots = dotChecker.getDotsByUser(user);
            if (dots == null) return ResponseUtils.accessResponse(204);

            dots = dots.stream().sorted(Comparator.comparing(Dot::getTime).reversed()).collect(Collectors.toList());

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (Dot dot : dots) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("x", dot.getX())
                        .add("y", dot.getY())
                        .add("r", dot.getR())
                        .add("result", dot.getResult())
                        .add("resultClass", dot.getResultClass())
                        .add("resultString", dot.getResultString())
                        .add("time", dot.getTime())
                );
            }

            JsonArray value = arrayBuilder.build();
            String result = value.toString();
            System.out.println(result);

            return ResponseUtils.accessResponseWithEntity(200, result);
        } catch (ClassCastException e) {
            return ResponseUtils.accessResponse(400);
        } catch (JDBCException e){
            return ResponseUtils.accessResponse(503);
        }
    }
}
