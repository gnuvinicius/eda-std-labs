import uuid

from django.db import models

class Order(models.Model):
    order_id = models.UUIDField(default=uuid.uuid4, editable=False)
    created_at = models.DateTimeField()
    updated_at = models.DateTimeField()

    class Meta:
        db_table = 'orders'
        indexes = [models.Index(fields=['order_id'])]

    def __str__(self):
        return str(self.id)