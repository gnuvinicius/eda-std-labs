import logging
from django.utils import timezone
from django.db import transaction
from apps.recommendation.models import Orders, OrderItems

logger = logging.getLogger(__name__)


def save_order_message(payload: dict) -> None:
    now = timezone.now()
    logger.info("payload: %s", payload)

    with transaction.atomic():
        order, created = Orders.objects.get_or_create(
            order_id=payload['orderId'],
            customer_id=payload['customerId'],
            defaults={
                "checked_out_at": payload['checkedOutAt'],
            }
        )
        logger.info("adicionado novo order: %s", order)
        logger.info("created: %s", created)

        if not created:
            order.checked_out_at = now
            order.save(update_fields=['checked_out_at'])

        for item in payload.get("items", []):
            OrderItems.objects.update_or_create(
                order=order,
                product_id=item["productId"],
                defaults={
                    "product_name": item["productName"],
                    "quantity": item["quantity"],
                    "unit_price": item["unitPrice"],
                }
            )
