import logging
from django.core.management import BaseCommand
from django.conf import settings
from kombu import Exchange, Queue, Connection, Consumer

from apps.orders.services import save_order_message

logger = logging.getLogger(__name__)


class Command(BaseCommand):
    def handle(self, *args, **kwargs):
        start_consumer()


def start_consumer():
    exchange = Exchange(
        name=settings.ORDER_EXCHANGE_NAME,
        type=settings.ORDER_EXCHANGE_TYPE,
        durable=True
    )

    queue = Queue(
        name=settings.ORDER_QUEUE_NAME,
        routing_key=settings.ORDER_QUEUE_NAME,
        exchange=exchange,
        durable=True,
        queue_arguments={
            'x-dead-letter-exchange': settings.ORDER_DLX_NAME,
            'x-dead-letter-routing-key': settings.ORDER_DLQ_ROUTING_KEY,
        }
    )

    def _processar_mensagem(body, message):
        try:
            save_order_message(body)
            message.ack()
        except ValueError as e:
            logger.exception("Erro ao processar evento order.created.v1")
            message.reject(requeue=False)

        except ConnectionError as e:
            logger.exception("Erro ao conectar ao RabbitMQ")
            message.reject(requeue=True)

    with Connection(settings.RABBITMQ_URL) as connection:
        with Consumer(connection, queues=[queue], callbacks=[_processar_mensagem], accept=['json']):
            logger.info('Conectado ao RabbitMQ')
            while True:
                connection.drain_events()
