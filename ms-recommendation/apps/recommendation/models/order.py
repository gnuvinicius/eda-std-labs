import uuid

from django.db import models

class Orders(models.Model):
    id = models.BigAutoField(primary_key=True)
    order_id = models.UUIDField()
    customer_id = models.UUIDField()
    checked_out_at = models.DateTimeField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'orders'