package com.clubedeassinaturas.resource;

import com.clubedeassinaturas.entity.OutboxEvent;
import com.clubedeassinaturas.entity.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(UserRequestDTO request) {
        try {
            User user = User.of(request.name(), request.email());

            var event = OutboxEvent.of("UserRegistered", user.toJson());
            event.persist();

            return Response.ok(user).status(201).build();
        } catch (Exception e) {
            log.error("Error creating user", e);
            return Response.serverError().build();
        }
    }
}
