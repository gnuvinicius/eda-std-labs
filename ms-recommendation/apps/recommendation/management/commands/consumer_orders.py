from django.core.management.base import BaseCommand
from apps.recommendation.services.rabbit_consumer import start_consumer

class Command(BaseCommand):
    help = 'Inicia consumer RabbitMQ para salvar orders no banco de dados'

    def handle(self, *args, **options):
        start_consumer()