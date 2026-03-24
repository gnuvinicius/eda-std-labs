import logging
from django.utils import timezone
from django.db import transaction
from apps.recommendation.models import Order, OrderItem

logger = logging.getLogger(__name__)


def save_order_message(payload: dict) -> None:
    now = timezone.now()
    logger.info("payload: %s", payload)

    with transaction.atomic():
        order, created = Order.objects.get_or_create(
            order_id=payload['cartId'],
            defaults={
                "created_at": now,
                "updated_at": now,
            }
        )
        logger.info("adicionado novo order: %s", order)
        logger.info("created: %s", created)

        if not created:
            order.updated_at = now
            order.save(update_fields=['updated_at'])

        for item in payload.get("items", []):
            OrderItem.objects.update_or_create(
                order=order,
                product_id=item["productId"],
                defaults={
                    "product_name": item["productName"],
                    "quantity": item["quantity"],
                    "unit_price": item["unitPrice"],
                }
            )
