import requests
import os
from time import time
files = {'file': open('1.jpg', 'rb')}
now = time()
res = requests.post(url=os.environ['INTERNAL_API_PATH'],files=files)
print(time()-now)
print(res.json())