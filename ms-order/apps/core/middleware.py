from django.http import JsonResponse

EXCLUDED_PATH = [
    '/order/static',
    '/order/swagger',
    '/order/schema',
    '/order/docs',
]


class CustomerIdMiddleware:

    def __init__(self, get_response):
        self.get_response = get_response

    def __call__(self, request):

        if any(request.path.startswith(path) for path in EXCLUDED_PATH):
            return self.get_response(request)

        customer_id = request.headers.get('Customer-Id')

        if not customer_id:
            return JsonResponse(
                {'error': 'Customer-Id header missing'},
                status=400,
            )
        request.session['customer_id'] = customer_id

        return self.get_response(request)
