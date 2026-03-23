import logging
from django.utils import timezone
from django.db import transaction
from celery import shared_task
from apps.recommendation.models import Order, OrderItem

logger = logging.getLogger(__name__)

@shared_task(bind=True, autoretry_for=(Exception,), retry_backoff=True, retry_kwargs={"max_retries": 5})
def ingest_order_task(self, payload: dict) -> None:
    save_order_message(payload)
    logger.info("Order processada com sucesso: cartId=%s", payload.get('cartId'))

def save_order_message(payload: dict) -> None:
    now = timezone.now()

    with transaction.atomic():
        order, created = Order.objects.get_or_create(
            order_id=payload['cartId'],
            defaults={
                "created_at": now,
                "updated_at": now,
            }
        )

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