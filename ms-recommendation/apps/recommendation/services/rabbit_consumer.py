# import json
# import logging
# import pika
# from django.conf import settings
# from apps.recommendation.services.order_ingest_service import save_order_message
#
# logger = logging.getLogger(__name__)
#
# def start_consumer():
#     params = pika.URLParameters(settings.RABBITMQ_URL)
#     connection = pika.BlockingConnection(params)
#     channel = connection.channel()
#     channel.queue_declare(queue=settings.RABBITMQ_QUEUE, passive=True)
#     channel.basic_qos(prefetch_count=10)
#
#     def callback(ch, method, properties, body):
#         try:
#             payload = json.loads(body.decode('utf-8'))
#             save_order_message(payload)
#             ch.basic_ack(delivery_tag=method.delivery_tag)
#         except ValueError as e:
#             logger.error("Mensagem invalida: %s", e, exc_info=True)
#             ch.basic_nack(delivery_tag=method.delivery_tag, requeue=False)
#         except Exception as e:
#             logger.error("Erro ao processar mensagem: %s", e, exc_info=True)
#             ch.basic_nack(delivery_tag=method.delivery_tag, requeue=True)
#
#     channel.basic_consume(queue=settings.RABBITMQ_QUEUE, on_message_callback=callback)
#     logger.info("Consumindo fila %s...", settings.RABBITMQ_QUEUE)
#     channel.start_consuming()