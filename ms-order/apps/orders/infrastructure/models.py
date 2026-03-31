from __future__ import annotations

import uuid

from django.db import models


class Address(models.Model):
    id = models.BigAutoField(primary_key=True)
    city = models.CharField(max_length=255, null=True, blank=True)
    complement = models.CharField(max_length=255, null=True, blank=True)
    country = models.CharField(max_length=255, null=True, blank=True)
    neighborhood = models.CharField(max_length=255, null=True, blank=True)
    number = models.CharField(max_length=255, null=True, blank=True)
    state = models.CharField(max_length=255, null=True, blank=True)
    street = models.CharField(max_length=255, null=True, blank=True)
    zipcode = models.CharField(max_length=255, null=True, blank=True)

    class Meta:
        managed = False
        db_table = "addresses"

    def __str__(self) -> str:
        return f"Address<{self.id}>"


class Payment(models.Model):
    id = models.BigAutoField(primary_key=True)
    payment_date = models.DateTimeField(null=True, blank=True)
    payment_method = models.CharField(max_length=255)
    payment_notes = models.CharField(max_length=255, null=True, blank=True)
    payment_status = models.CharField(max_length=255, null=True, blank=True)
    total_amount = models.DecimalField(max_digits=38, decimal_places=2)
    transaction_id = models.CharField(max_length=255, null=True, blank=True)

    class Meta:
        managed = False
        db_table = "payments"

    def __str__(self) -> str:
        return f"Payment<{self.id}>"


class Order(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    cancellation_reason = models.CharField(max_length=255, null=True, blank=True)
    customer_id = models.UUIDField(null=True, blank=True)
    customer_notes = models.CharField(max_length=255, null=True, blank=True)
    delivered_at = models.DateTimeField(null=True, blank=True)
    last_status = models.CharField(max_length=255, null=True, blank=True)
    shipping_cost = models.DecimalField(max_digits=38, decimal_places=2, null=True, blank=True)
    status = models.CharField(max_length=255, null=True, blank=True)
    total_discount = models.DecimalField(max_digits=38, decimal_places=2, null=True, blank=True)
    created_at = models.DateTimeField(null=True, blank=True)
    updated_at = models.DateTimeField(null=True, blank=True)
    payment = models.ForeignKey(
        Payment,
        on_delete=models.CASCADE,
        null=True,
        blank=True,
        db_column="payment_id",
        related_name="orders",
    )
    shipping_address = models.ForeignKey(
        Address,
        on_delete=models.CASCADE,
        null=True,
        blank=True,
        db_column="shipping_address_id",
        related_name="orders",
    )

    class Meta:
        managed = False
        db_table = "orders"

    def __str__(self) -> str:
        return f"Order<{self.id}>"


class OrderItem(models.Model):
    id = models.BigAutoField(primary_key=True)
    discount = models.DecimalField(max_digits=38, decimal_places=2, null=True, blank=True)
    product_id = models.UUIDField(null=True, blank=True)
    product_name = models.CharField(max_length=255, null=True, blank=True)
    quantity = models.IntegerField(null=True, blank=True)
    unit_price = models.DecimalField(max_digits=38, decimal_places=2, null=True, blank=True)
    order = models.ForeignKey(
        Order,
        on_delete=models.CASCADE,
        null=True,
        blank=True,
        db_column="order_id",
        related_name="items",
    )

    class Meta:
        managed = False
        db_table = "order_items"

    def __str__(self) -> str:
        return f"OrderItem<{self.id}>"
