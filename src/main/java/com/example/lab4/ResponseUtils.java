package com.example.lab4;

import jakarta.ws.rs.core.Response;

public class ResponseUtils {

    public static final String successResult = "{ \"result\" : \"success\" }";
    public static final String failResult = "{ \"result\" : \"fail\"}";

    public static Response accessResponse(int status){
        return Response
                .status(status)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "application/json")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "POST")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }

    public static Response accessResponseWithEntity(int status, Object entity){
        return Response
                .status(status)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "application/json")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "POST")
                .header("Access-Control-Max-Age", "1209600")
                .entity(entity)
                .build();
    }
}
