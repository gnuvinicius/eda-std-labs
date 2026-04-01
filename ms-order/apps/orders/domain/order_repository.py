from abc import ABC, abstractmethod

from apps.orders.domain.entities import OrderEntity


class OrderRepository(ABC):

    @abstractmethod
    def get_order_by_id(self, order_id: int) -> OrderEntity:
        pass
