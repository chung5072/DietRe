# 
FROM python:3.9

# 
WORKDIR /code

# 
COPY ./requirements.txt /code/requirements.txt
#
RUN apt-get update
RUN apt-get -y install libgl1-mesa-glx
RUN pip install --upgrade pip

RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt

RUN pip install matplotlib>=3.2.2
RUN pip install numpy>=1.18.5
RUN pip install opencv-python>=4.1.1
RUN pip install Pillow>=7.1.2
RUN pip install PyYAML>=5.3.1
RUN pip install requests>=2.23.0
RUN pip install scipy>=1.4.1
RUN pip install tqdm>=4.41.0
RUN pip install protobuf
# RUN pip install protobuf<4.21.3
RUN pip install pydantic==1.10.2

RUN pip install torch
# RUN pip install torch==1.11.0+cu113
# RUN pip install torchvision==0.12.0+cu113
RUN pip install torchvision

# RUN pip install -r /code/requirements.txt

RUN pip install tensorboard>=2.4.1

RUN pip install pandas>=1.1.4
RUN pip install seaborn>=0.11.0

RUN pip install fastapi==0.85.0
RUN pip install gunicorn==20.1.0
RUN pip install httptools==0.5.0
RUN pip install konlpy==0.6.0
RUN pip install PyMySQL==1.0.2
RUN pip install selenium==4.4.3
RUN pip install uvicorn==0.18.3
RUN pip install webdriver-manager==3.8.3
RUN pip install xlrd

RUN pip install ipython  # interactive notebook
RUN pip install psutil  # system utilization
RUN pip install thop  # FLOPs computation

RUN pip install python-multipart
# RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt

COPY . /code

# 
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "9090"]
