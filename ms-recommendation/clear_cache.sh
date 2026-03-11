# Na raiz do ms-recommendation/

# 1. Remover __pycache__ de todos os diretórios
find . -type d -name __pycache__ -exec rm -rf {} + 2>/dev/null

# 2. Remover arquivos .pyc compilados
find . -type f -name "*.pyc" -delete

# 3. Remover arquivos .pyo
find . -type f -name "*.pyo" -delete

# 4. Remover arquivos .py[cod] (Python compiled)
find . -type f -name "*.py[cod]" -delete

# 5. Remover diretórios .pytest_cache
find . -type d -name ".pytest_cache" -exec rm -rf {} + 2>/dev/null

# 6. Remover diretórios .egg-info
find . -type d -name "*.egg-info" -exec rm -rf {} + 2>/dev/null

# 7. Limpar cache do PyCharm (se estiver usando)
rm -rf .idea/