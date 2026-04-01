from typing import Any

from django.core.exceptions import MultipleObjectsReturned, ObjectDoesNotExist

from apps.orders.domain.entities import OrderEntity
from apps.orders.domain.order_repository import OrderRepository

from apps.orders.infrastructure.models import Order

class OrderRepositoryPSQL(OrderRepository):

    def get_order_by_id(self, order_id: int) -> Exception | Any:
        order = Order.objects.get(order_id=order_id)

        try:
            return OrderEntity.from_model(order)
        except (ObjectDoesNotExist, MultipleObjectsReturned):
            return Exception('Order not found')