from django.db import models

class OrderItems(models.Model):
    id = models.BigAutoField(primary_key=True)
    order = models.ForeignKey('Orders', models.DO_NOTHING)
    product_id = models.UUIDField()
    product_name = models.CharField(max_length=255)
    quantity = models.IntegerField()
    unit_price = models.DecimalField(max_digits=12, decimal_places=2)

    class Meta:
        managed = False
        db_table = 'order_items'

    def __str__(self):
        return f'{self.product_id} {self.product_name} {self.quantity} {self.unit_price}'