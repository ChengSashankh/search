FROM python:3.8-slim-buster

WORKDIR /python-docker

COPY requirements.txt requirements.txt
RUN pip3 install -r requirements.txt
#ENV PATH="/venv/bin:$PATH"

COPY . .

CMD ["python","vector_index.py"]