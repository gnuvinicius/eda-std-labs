from apps.orders.domain.entities import OrderEntity

from apps.orders.domain.order_repository import OrderRepository


class OrderService:
    def __init__(self, order_repository: OrderRepository):
        self.order_repository = order_repository

    def get_order_by_id(self, order_id: int) -> OrderEntity:

        order = self.order_repository.get_order_by_id(order_id)
        return order
