from django.db.models import Count, Max
from apps.recommendation.models import OrderItem

def recommend_common_products(product_id, limit=10):

    order_ids = (
        OrderItem.objects
        .filter(product_id=product_id)
        .values_list('order_id', flat=True)
        .distinct()
    )

    recommendations = (
        OrderItem.objects
        .filter(order_id__in=order_ids)
        .exclude(product_id=product_id)
        .values("product_id")
        .annotate(
            product_name=Max("product_name"),
            frequency=Count("id"),
            order_frequency=Count("order", distinct=True),
        )
        .order_by("-order_frequency", "-frequency")[:limit]
    )

    return list(recommendations)