for i in {1..101}; do
  curl -s -o /dev/null -w "%{http_code}\n" \
       -H "X-API-KEY: f68b52dc-c193-4ded-9a9e-89196a5f89e5" http://localhost:8080/api/v1/hello
done
