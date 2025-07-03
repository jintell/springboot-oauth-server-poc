for i in {1..101}; do
  curl -s -o /dev/null -w "%{http_code}\n" \
       -H "X-API-KEY: 7b1ee07e-f7d8-42ec-b15b-6f9b23e92497" http://localhost:8080/api/v1/hello
done
