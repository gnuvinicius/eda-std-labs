from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView

from apps.recommendation.api.serializers import ProductRecommendationSerializer
from apps.recommendation.services.order_service import recommend_common_products


class ProductRecommendationAPIView(APIView):

    def get(self, request, product_id) -> Response:
        result = recommend_common_products(product_id=product_id, limit=request.query_params.get('limit', 10))
        seriealize = ProductRecommendationSerializer(result, many=True)
        return Response(seriealize.data, status=status.HTTP_200_OK)
