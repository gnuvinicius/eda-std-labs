from django.urls import path

from apps.orders.views import OrderListAPIView, OrderDetailAPIView

urlpatterns = [
    path('orders/', OrderListAPIView.as_view()),
    path('orders/<int:pk>/', OrderDetailAPIView.as_view()),
]
