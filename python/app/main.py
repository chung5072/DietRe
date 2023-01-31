from fastapi import FastAPI, HTTPException, UploadFile
from detection import *
from pydantic import BaseModel
import uuid
import os
import requests

app = FastAPI()
params = load_model()

@app.get("/internal")
def run():
    """
    if error -> raise HTTPException(status_code=500, detail="Failed")
    현재 예외처리하지 않았으므로, 이후 예외처리 필요함
    """
    
    return {"message" : "Success"}


@app.post("/internal/detection")
async def detections(file: UploadFile):
    content = await file.read()
    filename = f"{str(uuid.uuid4())}.jpg"
    with open(filename, "wb") as fp:
        fp.write(content)

    clss,save_path = detect(params, filename)

    files = {'image': open(save_path, 'rb')}
    res = requests.post(url=os.environ['IMAGE_UPLOAD_URL'],files=files)
    if res.status_code != 200:
        raise HTTPException(status_code =500, detail = "Failed")

    prefix = os.environ['IMAGE_PREFIX']
    path = prefix + res.text

    os.remove(filename)
    return { "detected" : clss,
    'path' : path }
