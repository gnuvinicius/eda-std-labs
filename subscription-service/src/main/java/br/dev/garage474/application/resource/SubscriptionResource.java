package br.dev.garage474.application.resource;

import br.dev.garage474.application.dto.SubscriptionRequest;
import br.dev.garage474.application.dto.SubscriptionResponse;
import br.dev.garage474.domain.model.Subscription;
import br.dev.garage474.domain.service.SubscriptionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Path("/subscriptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubscriptionResource {

    @Inject
    private SubscriptionService service;

    @POST
    public Response create(SubscriptionRequest req) {
        try {
            var subscription = Subscription.of(req.userId(), req.planName(), Instant.now().plus(1, ChronoUnit.YEARS));
            Subscription created = service.create(subscription);
            return Response.ok(SubscriptionResponse.fromEntity(created)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/user/{userId}")
    public List<SubscriptionResponse> listByUser(@PathParam("userId") Long userId) {
        return service.listByUser(userId)
                .stream()
                .map(SubscriptionResponse::fromEntity)
                .toList();
    }

    @PUT
    @Path("/{id}/cancel")
    public Response cancel(@PathParam("id") Long id) {
        service.cancel(id);
        return Response.noContent().build();
    }
}
