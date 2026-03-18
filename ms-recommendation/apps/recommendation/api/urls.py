from django.urls import path
from apps.recommendation.api.views import ProductRecommendationAPIView

urlpatterns = [
    path(
        "products/<uuid:product_id>/recommendations/",
        ProductRecommendationAPIView.as_view(),
        name="product_recommendation"
    )
]