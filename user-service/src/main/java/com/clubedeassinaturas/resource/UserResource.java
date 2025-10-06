package com.clubedeassinaturas.resource;

import com.clubedeassinaturas.entity.OutboxEvent;
import com.clubedeassinaturas.entity.User;
import com.clubedeassinaturas.messaging.OutboxPublisher;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    OutboxPublisher publisher;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid CreateUserRequest request) {
        try {
            User.find("email", request.email()).firstResultOptional()
                    .ifPresent(u -> {
                        throw new IllegalArgumentException("Email already registered");
                    });

            User user = User.of(request.name(), request.email());

            var event = OutboxEvent.of("UserRegistered", user.toJson());
            event.persist();

            return Response.ok(user).status(201).build();
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage(e.getMessage()))
                    .build();
        } catch (Exception e) {
            log.error("Error creating user", e);
            return Response.serverError().build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{userId}")
    public Response disableUser(@PathParam("userId") Long userId) {
        try {
            User user = User.find("id = ?1 AND active = true", userId)
                    .firstResultOptional()
                    .map(User.class::cast)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            user.updateStatus(false);
            publisher.publishUserDisabledEvent(user.toJson());

            return Response.noContent().build();
        } catch (Exception e) {
            log.error("Error disabling user", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage(e.getMessage()))
                    .build();
        }
    }
}
