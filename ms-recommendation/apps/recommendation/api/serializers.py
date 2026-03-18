from rest_framework import serializers

class ProductRecommendationSerializer(serializers.Serializer):
    product_id = serializers.UUIDField()
    product_name = serializers.CharField()
    frequency = serializers.IntegerField()
    order_frequency = serializers.IntegerField()