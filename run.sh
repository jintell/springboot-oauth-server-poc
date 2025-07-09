for i in {1..101}; do
  curl -s -o /dev/null -w "%{http_code}\n" \
       -H "X-API-KEY: f68b52dc-c193-4ded-9a9e-89196a5f89e5" http://localhost:8080/api/v1/hello
#       -H "X-API-KEY: 00f74ed0-29e4-4a19-b0a8-e3b28de907ff" http://localhost:8080/api/v1/hello
done
