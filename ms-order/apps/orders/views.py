from drf_spectacular.utils import extend_schema, OpenApiParameter
from rest_framework.generics import ListAPIView
from rest_framework.views import APIView
from rest_framework.response import Response

from apps.orders.services import get_order_by_id, get_all_orders
from apps.orders.serializers import OrderSerializer

customer_header = OpenApiParameter(name='Customer-Id',
                                   required=True,
                                   type=int,
                                   description='Customer ID',
                                   location='header')


class OrderDetailAPIView(APIView):

    @extend_schema(parameters=[customer_header])
    def get(self, request, order_id):
        result = get_order_by_id(order_id=order_id)
        order_serializer = OrderSerializer(instance=result, many=False)
        return Response(order_serializer.data)


class OrderListAPIView(ListAPIView):

    @extend_schema(parameters=[customer_header])
    def get(self, request):
        customer_id = request.session['customer_id']
        orders = get_all_orders(customer_id=int(customer_id))
        order_serializer = OrderSerializer(instance=orders, many=True)
        return Response(order_serializer.data)
