# configuration
from flask import json
import base64
import requests
import jwt
URL_TOKEN = "https://devpanel.aku.mx/api/getToken"
URL_USERS = "https://devpanel.aku.mx/api/updateUsers"
API_KEY = "my_api_key"
API_SECRET = 'my_api_secret'
ALGORITHM = 'HS256'

# example for data
company_data = [
    {'name': 'Maria Eugenia',
     'surname': 'Fernandez Vazquez',
     'phone': '5512312300',
     'email': 'maria@empresa.mx',
     'net_salary': 10000.0},
    {'name': 'Juan Carlos',
     'surname': 'Rodriguez Mora',
     'phone': '2278978900',
     'email': 'juan@empresa.mx',
     'net_salary': 10000.0},
]

# import libraries

# obtain token
access_code = jwt.encode({'api_key': API_KEY}, API_SECRET, algorithm=ALGORITHM)
payload = {'company': 'empresa_chida', 'access_code': access_code}
paylod_base64 = base64.b64encode(str(json.dumps(payload)).encode('ascii'))
print(payload)
headers = {'X-Access-Token': paylod_base64}
response = requests.post(URL_TOKEN, headers=headers)

# make a request
headers = {'X-Access-Token': json.loads(response.content.decode())['token']}
payload = json.dumps(company_data)
data = jwt.encode({'data': payload}, API_SECRET, algorithm=ALGORITHM)
response = requests.post(URL_USERS, data=data, headers=headers)
print(response.content.decode())
