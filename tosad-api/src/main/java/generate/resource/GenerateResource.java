package generate.resource;

import generate.business.controller.GenerateController;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Path("/generate")
public class GenerateResource {
    GenerateController controller = new GenerateController();

    @POST
    public Response getTriggerInfo(String data) {
        ArrayList<String> response = controller.returnTriggers(data);
        Response.ResponseBuilder builder = Response.ok(response);

        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        builder.header("Access-Control-Allow-Credentials", "true");
        builder.header("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        return builder.build();
    }

    @POST
    @Path("/getbusinessrules")
    @Produces("application/json")
    public Response getBusinessrulesByTrigger(String data) {
        ArrayList response = controller.returnRulesByTrigger(data);
        Response.ResponseBuilder builder = Response.ok(response);

        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        builder.header("Access-Control-Allow-Credentials", "true");
        builder.header("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        return builder.build();
    }

    @POST
    @Path("/generateTriggerCode")
    @Produces("application/json")
    public Response generateTriggerCode(String data) {
        ArrayList response = controller.generateTriggerCode(data);
        Response.ResponseBuilder builder = Response.ok(response);

        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        builder.header("Access-Control-Allow-Credentials", "true");
        builder.header("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");


        return builder.build();
    }

    @POST
    @Path("/generateTrigger")
    @Produces("application/json")
    public Response generateTrigger(String data) {
        ArrayList response = controller.generateTrigger(data);
        Response.ResponseBuilder builder = Response.ok(response);

        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        builder.header("Access-Control-Allow-Credentials", "true");
        builder.header("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");


        return builder.build();
    }

    @POST
    @Path("/deleteTrigger")
    @Produces("application/json")
    public Response deleteOrUpdateTrigger(String data) {
        ArrayList response = controller.deleteOrUpdateTrigger(data);
        Response.ResponseBuilder builder = Response.ok(response);

        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        builder.header("Access-Control-Allow-Credentials", "true");
        builder.header("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");


        return builder.build();
    }
}
