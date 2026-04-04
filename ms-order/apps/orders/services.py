from django.core.exceptions import ObjectDoesNotExist, MultipleObjectsReturned
from django.db.models import QuerySet

from apps.orders.models import Order


def get_order_by_id(order_id: int) -> Exception | Order:
    order = Order.objects.get(order_id=order_id)

    try:
        return order
    except (ObjectDoesNotExist, MultipleObjectsReturned):
        return Exception('Order not found')


def get_all_orders(customer_id: int) -> QuerySet[Order]:
    return Order.objects.filter(customer_id=customer_id).order_by('-created_at')