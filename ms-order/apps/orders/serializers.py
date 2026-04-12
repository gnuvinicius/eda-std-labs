from rest_framework import serializers


class OrderSerializer(serializers.Serializer):
    id = serializers.UUIDField(read_only=True)
    cancellation_reason = serializers.CharField()
    customer_id = serializers.UUIDField()
    customer_notes = serializers.CharField()
    delivered_at = serializers.DateTimeField()
    last_status = serializers.CharField()
    shipping_cost = serializers.DecimalField(max_digits=32, decimal_places=2)
    status = serializers.CharField()
    total_discount = serializers.DecimalField(max_digits=32, decimal_places=2)
    created_at = serializers.DateTimeField()
    updated_at = serializers.DateTimeField()
