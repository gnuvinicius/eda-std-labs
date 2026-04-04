import logging
from django.conf import settings
from kombu import Connection, Consumer, Exchange, Queue
from apps.recommendation.services.order_ingest_service import save_order_message

logger = logging.getLogger(__name__)


def start_consumer() -> None:
    """
       Inicia o consumidor de eventos de pedidos no RabbitMQ.

       Fluxo de execução:
       1) Define a exchange de eventos (`ORDER_EXCHANGE_NAME`) como `fanout` e durável.
       2) Define a fila (`ORDER_QUEUE_NAME`) com:
          - dead-letter exchange (`ORDER_DLX_NAME`)
          - dead-letter routing key (`ORDER_DLQ_ROUTING_KEY`)
       3) Registra o callback `processar_mensagem(body, message)` para tratar cada evento recebido.
       4) Abre conexão com o broker usando `RABBITMQ_URL`.
       5) Cria o `Consumer` vinculado à fila e aceita mensagens JSON.
       6) Entra em loop contínuo com `drain_events()` aguardando novas mensagens.

       Tratamento de mensagens:
       - Sucesso:
         - chama `save_order_message(body)` para persistir os dados no banco
         - confirma o processamento com `message.ack()`
       - Erro:
         - registra exceção em log
         - rejeita a mensagem com `message.reject(requeue=False)`, evitando reprocessamento imediato
           na mesma fila (o broker pode encaminhar para DLQ conforme configuração).

       Observações:
       - Este consumidor é um processo de longa duração.
       - Deve ser executado separadamente da API web.
       - A exchange/fila/routing keys devem estar alinhadas com a definição existente no RabbitMQ.
    """

    exchange = Exchange(settings.ORDER_EXCHANGE_NAME,
                        type="fanout",
                        durable=True
                        )

    queue = Queue(
        settings.ORDER_QUEUE_NAME,
        exchange=exchange,
        durable=True,
        queue_arguments={
            "x-dead-letter-exchange": settings.ORDER_DLX_NAME,
            "x-dead-letter-routing-key": settings.ORDER_DLQ_ROUTING_KEY,
        }
    )

    def processar_mensagem(body, message):
        try:
            save_order_message(body)
            message.ack()
        except Exception:
            logger.exception("Erro ao processar evento order.created.v1")
            message.reject(requeue=False)

    with Connection(settings.RABBITMQ_URL) as connection:
        with Consumer(connection,
                      queues=[queue],
                      callbacks=[processar_mensagem],
                      accept=["json"]
                      ):
            logger.info("Conectado a RabbitMQ")
            while True:
                connection.drain_events()
