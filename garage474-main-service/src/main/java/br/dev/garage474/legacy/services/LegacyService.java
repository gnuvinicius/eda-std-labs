package br.dev.garage474.legacy.services;

import br.dev.garage474.legacy.models.User;
import br.dev.garage474.legacy.repositories.UserRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.util.List;

@Stateless
@WebService(serviceName = "LegacyService", targetNamespace = "http://legacy.garage474.dev.br:8080/")
public class LegacyService {

    @EJB
    private UserRepository userRepository;

    @WebMethod
    public List<User> listUsers() {
        // This is a placeholder implementation.
        // In a real scenario, you would implement logic to retrieve users from the repository.
        return userRepository.findAllUsers();
    }

    private void processPaymentNotification(String notificationData) {
        try (PaymentNotificationHandler handler = new PaymentNotificationHandler()) {
            handler.handleNotification(notificationData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
