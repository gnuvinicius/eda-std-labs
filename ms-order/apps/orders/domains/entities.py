"""
Classes de domínio para o microserviço de pedidos (ms-order).

Este módulo implementa o Domain-Driven Design (DDD) com os seguintes padrões:

1. **Value Objects** (_parse_* funções e Decimal/UUID handling):
   - Objetos imutáveis que representam conceitos de negócio
   - Validam dados na criação e garantem que sempre são válidos
   - Exemplo: _parse_uuid(), _parse_decimal(), _parse_datetime()

2. **Objetos de domínio** (Customer, OrderItem, Order):
   - Representam conceitos centrais do negócio
   - Validam invariantes antes de permitir uso no sistema
   - Não carregam detalhes de framework ou persistência
   
   - **Customer**: Imutável (frozen=True) pois é um snapshot do cliente
   - **OrderItem**: Imutável (frozen=True) pois é um snapshot do produto/preço
   - **Order**: Mutável (slots=True) pois transita entre estados

3. **Aggregate Root** (Order):
   - Entidade raiz que coordena o agregado de pedido
   - Encapsula Customer (esses dados só existem dentro do pedido)
   - Encapsula OrderItem (itens só existem dentro do pedido)
   - Garante invariantes do agregado (always >= 1 item)
   
4. **Enums** (OrderStatus):
   - Representa estados válidos de um pedido
   - Previne estados inválidos (type safety)

5. **Factory Methods** (from_checkout, from_dict):
   - Constroem entidades validadas a partir de dados externos
   - Implementam o mapeamento entre APIs e domínio
"""

from __future__ import annotations

from dataclasses import dataclass, field
from datetime import UTC, datetime
from decimal import Decimal, InvalidOperation, ROUND_HALF_UP
from enum import StrEnum
from typing import Any, Iterable
from uuid import UUID, uuid4

_MONEY_PLACES = Decimal("0.01")


class DomainValidationError(ValueError):
    """Erro levantado quando uma classe de domínio viola uma regra de negócio."""


class OrderStatus(StrEnum):
    """
    Enum que representa os possíveis estados de um pedido.
    
    Estados válidos:
    - RECEIVED: Pedido recebido do carrinho (estado inicial)
    - PENDING: Aguardando confirmação de pagamento/estoque
    - PROCESSING: Sendo processado (pagamento confirmado, estoque reservado)
    - CONFIRMED: Confirmado, pronto para envio
    - CANCELLED: Cancelado pelo cliente ou sistema
    - FAILED: Falha durante processamento (erro irrecuperável)
    """
    RECEIVED = "RECEIVED"
    PENDING = "PENDING"
    PROCESSING = "PROCESSING"
    CONFIRMED = "CONFIRMED"
    CANCELLED = "CANCELLED"
    FAILED = "FAILED"


def _utcnow() -> datetime:
    """Retorna a data/hora atual em UTC. Usado como factory default para timestamps."""
    return datetime.now(UTC)


def _parse_uuid(value: UUID | str, field_name: str) -> UUID:
    """
    Converte e valida um UUID a partir de string ou UUID.
    
    Args:
        value: Uma string ou UUID para converter
        field_name: Nome do campo para mensagem de erro (ex: "customer.id")
    
    Returns:
        UUID: O UUID validado
    
    Raises:
        DomainValidationError: Se o valor não for um UUID válido
    """
    if isinstance(value, UUID):
        return value

    try:
        return UUID(str(value))
    except (TypeError, ValueError) as exc:
        raise DomainValidationError(f"{field_name} deve ser um UUID válido.") from exc


def _parse_datetime(value: datetime | str | None, field_name: str) -> datetime | None:
    """
    Converte e valida um datetime a partir de string ISO-8601 ou datetime.
    
    Args:
        value: Uma string ISO-8601 (com ou sem 'Z'), datetime ou None
        field_name: Nome do campo para mensagem de erro (ex: "order.created_at")
    
    Returns:
        datetime: O datetime validado ou None se entrada for None
    
    Raises:
        DomainValidationError: Se o valor não for um datetime válido
    """
    if value is None:
        return None

    if isinstance(value, datetime):
        return value

    try:
        normalized_value = value.replace("Z", "+00:00")
        return datetime.fromisoformat(normalized_value)
    except (AttributeError, ValueError) as exc:
        raise DomainValidationError(
            f"{field_name} deve ser um datetime ISO-8601 válido."
        ) from exc


def _parse_decimal(value: Decimal | int | float | str, field_name: str) -> Decimal:
    """
    Converte e normaliza um valor numérico para Decimal com 2 casas decimais.
    
    Importante para operações monetárias: garante precisão e arredondamento correto
    de valores em reais (BRL).
    
    Args:
        value: Um número em qualquer formato (Decimal, int, float, str)
        field_name: Nome do campo para mensagem de erro (ex: "item.unit_price")
    
    Returns:
        Decimal: O valor normalizado com 2 casas decimais
    
    Raises:
        DomainValidationError: Se o valor não for numérico válido
    """
    try:
        return Decimal(str(value)).quantize(_MONEY_PLACES, rounding=ROUND_HALF_UP)
    except (InvalidOperation, TypeError, ValueError) as exc:
        raise DomainValidationError(
            f"{field_name} deve ser numérico e compatível com moeda."
        ) from exc


def _require_non_empty_text(value: str, field_name: str) -> str:
    """
    Valida e normaliza um campo de texto obrigatório.
    
    Args:
        value: Texto para validar
        field_name: Nome do campo para mensagem de erro (ex: "customer.name")
    
    Returns:
        str: O texto normalizado (trimado)
    
    Raises:
        DomainValidationError: Se vazio ou não for texto
    """
    if not isinstance(value, str):
        raise DomainValidationError(f"{field_name} deve ser um texto válido.")

    normalized_value = value.strip()
    if not normalized_value:
        raise DomainValidationError(f"{field_name} é obrigatório.")
    return normalized_value


@dataclass(frozen=True, slots=True)
class Customer:
    """
    Classe de domínio que representa um cliente no contexto de um pedido.
    
    É imutável (frozen=True) para garantir que um snapshot do cliente em um pedido
    não mude após sua criação. Isso preserva a auditoria e a consistência do pedido.
    
    Atributos:
        id: UUID identificadora única do cliente no sistema
        name: Nome completo do cliente (obrigatório, não vazio)
        email: Email para contato (obrigatório, não vazio)
        document: Documento de identificação (CPF/CNPJ, obrigatório)
        phone: Telefone para contato (opcional)
        created_at: Data/hora de criação no sistema de origem (snapshot do carrinho)
        updated_at: Data/hora da última atualização no sistema de origem
    """
    id: UUID
    name: str
    email: str
    document: str
    phone: str | None = None
    created_at: datetime | None = None
    updated_at: datetime | None = None

    def __post_init__(self) -> None:
        # Validação e normalização de atributos após inicialização da dataclass.
        # Como a classe é frozen=True, usamos object.__setattr__ para modificar atributos.
        # Todos os dados passam por validadores (_parse_*, _require_*) que garantem consistência.

        object.__setattr__(self, "id", _parse_uuid(self.id, "customer.id"))
        object.__setattr__(self, "name", _require_non_empty_text(self.name, "customer.name"))
        object.__setattr__(self, "email", _require_non_empty_text(self.email, "customer.email"))
        object.__setattr__(
            self,
            "document",
            _require_non_empty_text(self.document, "customer.document"),
        )
        object.__setattr__(
            self,
            "created_at",
            _parse_datetime(self.created_at, "customer.created_at"),
        )
        object.__setattr__(
            self,
            "updated_at",
            _parse_datetime(self.updated_at, "customer.updated_at"),
        )

        if self.phone is not None:
            normalized_phone = self.phone.strip()
            object.__setattr__(self, "phone", normalized_phone or None)

    @classmethod
    def from_dict(cls, payload: dict[str, Any]) -> Customer:
        return cls(
            id=payload["id"],
            name=payload["name"],
            email=payload["email"],
            document=payload["document"],
            phone=payload.get("phone"),
            created_at=payload.get("created_at"),
            updated_at=payload.get("updated_at"),
        )


@dataclass(frozen=True, slots=True)
class OrderItem:
    """
    Classe de domínio que representa um item (produto) dentro de um pedido.
    
    É imutável (frozen=True) pois cada item é um snapshot do que o cliente pediu
    naquele momento específico, incluindo preço unitário naquele instante.
    
    Atributos:
        product_id: UUID identificadora única do produto no catálogo
        product_name: Nome do produto no momento do pedido (snapshot)
        quantity: Quantidade de unidades pedidas (deve ser > 0)
        unit_price: Preço unitário em reais no momento do pedido (>= 0.00)
        source_item_id: ID sequencial do item no carrinho de origem (auditoria)
        created_at: Data/hora de adição ao carrinho (snapshot)
        updated_at: Data/hora da última modificação no carrinho (snapshot)
    
    Propriedades:
        subtotal: Calcula unit_price * quantity com arredondamento correto em moeda
    """
    product_id: UUID
    product_name: str
    quantity: int
    unit_price: Decimal
    source_item_id: int | None = None
    created_at: datetime | None = None
    updated_at: datetime | None = None

    def __post_init__(self) -> None:
        # Validação de todos os atributos. Como a classe é frozen=True, usamos object.__setattr__.
        # Cada validador é responsável por verificar o tipo e o valor do atributo.

        object.__setattr__(self, "product_id", _parse_uuid(self.product_id, "item.product_id"))
        object.__setattr__(
            self,
            "product_name",
            _require_non_empty_text(self.product_name, "item.product_name"),
        )
        object.__setattr__(
            self,
            "unit_price",
            _parse_decimal(self.unit_price, "item.unit_price"),
        )
        object.__setattr__(
            self,
            "created_at",
            _parse_datetime(self.created_at, "item.created_at"),
        )
        object.__setattr__(
            self,
            "updated_at",
            _parse_datetime(self.updated_at, "item.updated_at"),
        )

        # Validações de regras de negócio (invariantes do domínio)
        if self.quantity <= 0:
            raise DomainValidationError("item.quantity deve ser maior que zero.")

        if self.unit_price < Decimal("0.00"):
            raise DomainValidationError("item.unit_price não pode ser negativo.")

    @property
    def subtotal(self) -> Decimal:
        return (self.unit_price * self.quantity).quantize(
            _MONEY_PLACES,
            rounding=ROUND_HALF_UP,
        )

    @classmethod
    def from_dict(cls, payload: dict[str, Any]) -> OrderItem:
        return cls(
            product_id=payload["product_id"],
            product_name=payload["product_name"],
            quantity=int(payload["quantity"]),
            unit_price=payload["unit_price"],
            source_item_id=payload.get("id"),
            created_at=payload.get("created_at"),
            updated_at=payload.get("updated_at"),
        )


@dataclass(slots=True)
class Order:
    """
    Aggregate Root que representa um pedido de compra no e-commerce.
    
    Order é o agregado principal do domínio de pedidos. Ele encapsula:
    - Um cliente (Customer)
    - Uma lista de itens (OrderItem)
    - O estado do pedido (OrderStatus)
    - Timestamps e referências de auditoria
    
    É mutável (não frozen) porque permite transições de estado durante o ciclo
    de vida do pedido (RECEIVED -> PROCESSING -> CONFIRMED, etc).
    
    Atributos principais:
        customer: Entity com dados do cliente no momento do pedido (imutável)
        items: Lista de OrderItem (itens que o cliente pediu)
        cart_id: UUID referência ao carrinho de origem no microserviço de carrinho
        id: UUID identificadora única do pedido
        status: Estado atual do pedido (RECEIVED, PENDING, PROCESSING, etc)
        checked_out_at: Data/hora exata quando o checkout foi feito
        created_at: Data/hora de criação do pedido no ms-order
        updated_at: Data/hora da última modificação do pedido
        source_cart_status: Preserva o status original do carrinho para auditoria
    
    Regras de negócio (invariantes):
        - Um pedido DEVE ter pelo menos 1 item
        - IDs devem ser UUIDs válidos
        - Status deve ser um valor válido de OrderStatus
        - Campos de texto obrigatórios não podem ser vazios
        - Quantidade de itens deve ser > 0
        - Preços devem ser >= 0.00
    
    Propriedades calculadas:
        customer_id: Extrai facilmente o ID do cliente do agregado
        total_items: Soma de quantity de todos os items
        total_amount: Soma de subtotal de todos os items (com arredondamento correto)
    """
    customer: Customer
    items: list[OrderItem]
    cart_id: UUID
    id: UUID = field(default_factory=uuid4)
    status: OrderStatus = OrderStatus.RECEIVED
    checked_out_at: datetime | None = None
    created_at: datetime = field(default_factory=_utcnow)
    updated_at: datetime | None = None
    source_cart_status: str | None = None

    def __post_init__(self) -> None:
        # Validação de todos os atributos do agregado Order
        self.id = _parse_uuid(self.id, "order.id")
        self.cart_id = _parse_uuid(self.cart_id, "order.cart_id")
        self.checked_out_at = _parse_datetime(self.checked_out_at, "order.checked_out_at")

        # created_at é obrigatório
        created_at = _parse_datetime(self.created_at, "order.created_at")
        if created_at is None:
            raise DomainValidationError("order.created_at é obrigatório.")
        self.created_at = created_at

        self.updated_at = _parse_datetime(self.updated_at, "order.updated_at")

        # Validação de status: garante que sempre é um valor válido de OrderStatus
        if not isinstance(self.status, OrderStatus):
            try:
                self.status = OrderStatus(str(self.status).upper())
            except ValueError as exc:
                raise DomainValidationError("order.status é inválido.") from exc

        # Invariante crítico do agregado: um pedido sempre tem pelo menos 1 item
        if not self.items:
            raise DomainValidationError("O pedido deve possuir ao menos um item.")

        # Normaliza a lista de items (cria nova lista a partir da iterável)
        self.items = list(self.items)

    @property
    def customer_id(self) -> UUID:
        """Extrai facilmente o ID do cliente do agregado."""
        return self.customer.id

    @property
    def total_items(self) -> int:
        """Calcula a quantidade total de itens no pedido (soma das quantities)."""
        return sum(item.quantity for item in self.items)

    @property
    def total_amount(self) -> Decimal:
        """Calcula o valor total do pedido (soma de todos os subtotals com arredondamento correto)."""
        total = sum((item.subtotal for item in self.items), start=Decimal("0.00"))
        return total.quantize(_MONEY_PLACES, rounding=ROUND_HALF_UP)

    def add_item(self, item: OrderItem) -> None:
        """Adiciona um novo item ao pedido e atualiza o timestamp de modificação."""
        self.items.append(item)
        self.updated_at = _utcnow()

    def replace_items(self, items: Iterable[OrderItem]) -> None:
        """
        Substitui a lista completa de items do pedido.
        
        Valida que a nova lista não está vazia (invariante do agregado).
        """
        normalized_items = list(items)
        if not normalized_items:
            raise DomainValidationError("O pedido deve possuir ao menos um item.")

        self.items = normalized_items
        self.updated_at = _utcnow()

    def update_status(self, status: OrderStatus) -> None:
        """
        Atualiza o status do pedido e registra o momento da mudança.
        
        Transições típicas:
        RECEIVED -> PENDING -> PROCESSING -> CONFIRMED
        ou RECEIVED -> CANCELLED/FAILED em caso de erro
        """
        self.status = status
        self.updated_at = _utcnow()

    @classmethod
    def from_checkout(cls, payload: dict[str, Any]) -> Order:
        """
        Factory method que constrói um Order a partir do payload de checkout vindo do RabbitMQ.
        
        Este método implementa o mapeamento entre o formato do evento de checkout do 
        microserviço de carrinho e a entidade de domínio Order.
        
        Estratégia de mapeamento:
        - customer: extrai do campo 'customer' do payload (obrigatório)
        - items: extrai de 'items' ou 'cart_items' (obrigatório e não vazio)
        - cart_id: tenta usar 'cart_id', fallback para 'id' (obrigatório)
        - order_id: gera UUID novo se não fornecido (para pedidos criados via checkout)
        - status: tenta usar 'order_status', padrão RECEIVED (não reutiliza cart status)
        - checked_out_at: preserva quando o checkout foi concluído
        - source_cart_status: preserva o status original do carrinho para auditoria
        
        Args:
            payload: Dicionário com os dados do checkout vindo do tópico RabbitMQ
        
        Retorna:
            Order: Agregado de pedido validado e pronto para persistência
        
        Levanta:
            DomainValidationError: Se campos obrigatórios faltarem ou forem inválidos
        """
        customer_payload = payload.get("customer")
        if not isinstance(customer_payload, dict):
            raise DomainValidationError("checkout.customer deve ser um objeto válido.")

        items_payload = payload.get("items") or payload.get("cart_items")
        if not isinstance(items_payload, list) or not items_payload:
            raise DomainValidationError("checkout.items deve conter ao menos um item.")

        checked_out_at = payload.get("checked_out_at")
        created_at = payload.get("created_at") or checked_out_at or _utcnow()
        order_status = payload.get("order_status", OrderStatus.RECEIVED)

        return cls(
            id=payload.get("order_id") or payload.get("id") or uuid4(),
            cart_id=payload.get("cart_id") or payload.get("id"),
            customer=Customer.from_dict(customer_payload),
            items=[OrderItem.from_dict(item) for item in items_payload],
            status=order_status,
            checked_out_at=checked_out_at,
            created_at=created_at,
            updated_at=payload.get("updated_at"),
            source_cart_status=payload.get("cart_status") or payload.get("status"),
        )
