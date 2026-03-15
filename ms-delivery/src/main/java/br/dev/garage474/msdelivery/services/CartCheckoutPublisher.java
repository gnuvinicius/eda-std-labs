package br.dev.garage474.msdelivery.services;

import br.dev.garage474.msdelivery.dtos.CartCheckoutEvent;

public interface CartCheckoutPublisher {

    void publish(CartCheckoutEvent event);
}

