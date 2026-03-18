from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView

from apps.recommendation.api.serializers import ProductRecommendationSerializer
from apps.recommendation.services.order_service import recommend_common_products

class ProductRecommendationAPIView(APIView):

    def get(self, request, product_id):
        limit = request.query_params.get('limit', 10)

        try:
            limit = int(limit)
            if limit <= 0:
                raise ValueError
        except ValueError:
            return Response(
                {"detail": "Limit must be an integer greater than 0."},
                status=status.HTTP_400_BAD_REQUEST,
            )

        data = recommend_common_products(product_id=product_id, limit=limit)
        serializer = ProductRecommendationSerializer(data=data, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)