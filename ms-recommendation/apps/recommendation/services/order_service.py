from typing import TypedDict, cast

from django.db.models import Count, Max
from apps.recommendation.models import OrderItems

# TypedDict
class RecommendationItem(TypedDict):
    product_id: str
    product_name: str
    frequency: int
    order_frequency: int


def recommend_common_products(product_id, limit=10) -> list[RecommendationItem]:
    order_ids = (
        OrderItems.objects
        .filter(product_id=product_id)
        .values_list('order_id', flat=True)
        .distinct()
    )

    recommendations = (
        OrderItems.objects
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

    # list comprehension
    result: list[RecommendationItem] = [
        {
            'product_id': row['product_id'],
            'product_name': row['product_name'],
            'frequency': row['frequency'],
            'order_frequency': row['order_frequency'],
        }
        for row in recommendations
    ]

    return result
