import logging

from rest_framework import status, views
from rest_framework.response import Response

from apps.recommendation.api.serializers import ProductRecommendationSerializer
from apps.recommendation.services.order_service import recommend_common_products

logger = logging.getLogger(__name__)

class ProductRecommendationAPIView(views.APIView):

    def get(self, request, product_id) -> Response:
        limit = request.query_params.get('limit', 10)
        logger.info("Received recommendation request for product_id=%s with limit=%s", product_id, limit)

        try:
            self._parse_limit(limit)
        except ValueError as e:
            return Response({"error": str(e)}, status=status.HTTP_400_BAD_REQUEST)

        data = recommend_common_products(product_id=product_id, limit=limit)
        serializer = ProductRecommendationSerializer(instance=data, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def _parse_limit(self, limit):
        try:
            limit = int(limit)
            if limit <= 0:
                raise ValueError
            return limit
        except ValueError:
            raise ValueError("Limit must be an integer greater than 0.")
