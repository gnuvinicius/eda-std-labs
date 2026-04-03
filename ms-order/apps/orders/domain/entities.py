from __future__ import annotations

from dataclasses import dataclass, field, fields
from datetime import datetime
from decimal import Decimal
from typing import Optional
from uuid import UUID, uuid4


def _map_model_to_dict(model, entity_cls, *, exclude: set[str] | None = None):
    exclude = exclude or set()
    result = {}
    for field in fields(entity_cls):
        if field.init and field.name not in exclude and hasattr(model, field.name):
            result[field.name] = getattr(model, field.name)
    return result


# slots cria instancia em __slots__ que é diferente de __dict__,
# sendo mais leve e rápido, mas não permite atributos dinâmicos
# define quais atributos a classe pode ter de forma fixa
@dataclass(slots=True)
class Address:
    id: Optional[int] = None
    city: Optional[str] = None
    complement: Optional[str] = None
    country: Optional[str] = None
    neighborhood: Optional[str] = None
    number: Optional[str] = None
    state: Optional[str] = None
    street: Optional[str] = None
    zipcode: Optional[str] = None


@dataclass(slots=True)
class Payment:
    payment_method: str
    total_amount: Decimal
    id: Optional[int] = None
    payment_date: Optional[datetime] = None
    payment_notes: Optional[str] = None
    payment_status: Optional[str] = None
    transaction_id: Optional[str] = None


@dataclass(slots=True)
class OrderItem:
    product_id: Optional[UUID] = None
    product_name: Optional[str] = None
    quantity: Optional[int] = None
    unit_price: Optional[Decimal] = None
    discount: Optional[Decimal] = None
    id: Optional[int] = None
    order_id: Optional[UUID] = None


@dataclass(slots=True)
class OrderEntity:
    id: UUID = field(default_factory=uuid4)
    cancellation_reason: Optional[str] = None
    customer_id: Optional[UUID] = None
    customer_notes: Optional[str] = None
    delivered_at: Optional[datetime] = None
    last_status: Optional[str] = None
    shipping_cost: Optional[Decimal] = None
    status: Optional[str] = None
    total_discount: Optional[Decimal] = None
    created_at: Optional[datetime] = None
    updated_at: Optional[datetime] = None
    payment: Optional[Payment] = None
    shipping_address: Optional[Address] = None
    items: list[OrderItem] = field(default_factory=list)

    def add_item(self, item: OrderItem) -> None:
        item.order_id = self.id
        self.items.append(item)

    @classmethod
    def from_model(cls, order):
        return cls(
            **_map_model_to_dict(
                order,
                cls,
                exclude={"payment", "shipping_address", "items"},
            ),
            payment=Payment(**_map_model_to_dict(order.payment, Payment)) if order.payment else None,
            shipping_address=Address(
                **_map_model_to_dict(order.shipping_address, Address)) if order.shipping_address else None,
        )
