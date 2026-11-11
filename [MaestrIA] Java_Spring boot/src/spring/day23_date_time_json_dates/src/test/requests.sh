echo "Successful PIX"
curl -X POST http://localhost:8081/payments/preview \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 250.00,
    "method": "PIX"
}'

echo "Successful credit card"
curl -X POST http://localhost:8081/payments/preview \
   -H "Content-Type: application/json" \
   -d '{
     "amount": 900.00,
     "method": "CREDIT_CARD"
}'

echo "Broken enum test"
curl -X POST http://localhost:8081/payments/preview \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 250.00,
    "method": "BITCOIN"
}'