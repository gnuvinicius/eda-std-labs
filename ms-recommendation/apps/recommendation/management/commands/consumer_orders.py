from django.core.management.base import BaseCommand
from apps.recommendation.services.rabbit_consumer import start_consumer

class Command(BaseCommand):

    def handle(self, *args, **options):
        start_consumer()