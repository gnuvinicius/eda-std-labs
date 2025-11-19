package br.dev.garage474.legacy.services;

import br.dev.garage474.legacy.repositories.UserRepository;
import br.dev.garage474.legacy.services.dto.UserDTO;
import br.dev.garage474.legacy.services.dto.UserListResponse;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@Stateless
@WebService(serviceName = "LegacyService", targetNamespace = "http://legacy.garage474.dev.br:8080/")
public class LegacyService {

    @Inject
    private UserRepository userRepository;

    @WebMethod
    public UserListResponse listUsers(
            @WebParam(name = "page") Integer page,
            @WebParam(name = "perPage") Integer perPage
    ) {
        return new UserListResponse(
            userRepository.findAllUsers(page, perPage)
                .stream()
                .map(UserDTO::fromEntity)
                .toList(),
                userRepository.countAllUsers(),
                page,
                perPage
        );
    }

    private void processPaymentNotification(String notificationData) {
        try (PaymentNotificationHandler handler = new PaymentNotificationHandler()) {
            handler.handleNotification(notificationData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
