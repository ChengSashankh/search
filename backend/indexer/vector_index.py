from google.cloud import secretmanager
from google.cloud import storage
from langchain.text_splitter import CharacterTextSplitter
from langchain_community.document_loaders.csv_loader import CSVLoader
from langchain_community.embeddings import OpenAIEmbeddings
from langchain_community.embeddings.huggingface import HuggingFaceEmbeddings
from langchain_community.vectorstores import Qdrant
import pandas as pd

OUTPUT_CSV_PATH = "./plots.csv"
AUTOCOMPLETE_OUTPUT_PATH = "./phrases.txt"


def upload_blob(bucket_name, source_file_name, destination_blob_name):
    """Uploads a file to the bucket."""
    # The ID of your GCS bucket
    # bucket_name = "your-bucket-name"
    # The path to your file to upload
    # source_file_name = "local/path/to/file"
    # The ID of your GCS object
    # destination_blob_name = "storage-object-name"

    storage_client = storage.Client()
    bucket = storage_client.bucket(bucket_name)
    blob = bucket.blob(destination_blob_name)

    # Optional: set a generation-match precondition to avoid potential race conditions
    # and data corruptions. The request to upload is aborted if the object's
    # generation number does not match your precondition. For a destination
    # object that does not yet exist, set the if_generation_match precondition to 0.
    # If the destination object already exists in your bucket, set instead a
    # generation-match precondition using its generation number.
    generation_match_precondition = 0

    blob.upload_from_filename(source_file_name, if_generation_match=generation_match_precondition)

    print(
        f"File {source_file_name} uploaded to {destination_blob_name}."
    )


def write_auto_complete_file(df):
    df = pd.read_csv(OUTPUT_CSV_PATH, usecols=["title"], delimiter=",", quotechar='|')
    df.to_csv(AUTOCOMPLETE_OUTPUT_PATH, index=False, header=False)


def access_secret_version(secret_id, version_id="latest"):
    # Create the Secret Manager client.
    client = secretmanager.SecretManagerServiceClient()

    # Build the resource name of the secret version.
    name = f"projects/deeno-417616/secrets/{secret_id}/versions/{version_id}"

    # Access the secret version.
    response = client.access_secret_version(name=name)

    # Return the decoded payload.
    return response.payload.data.decode('UTF-8')


def load_and_split_docs():
    text_splitter = CharacterTextSplitter(chunk_size=1000, chunk_overlap=0)

    loader = CSVLoader(
        file_path=OUTPUT_CSV_PATH,
        source_column="id",
        csv_args={
            "delimiter": ",",
            "quotechar": '|',
            "fieldnames": ["id", "title", "text"],
        }
    )

    print("Loading documents")
    documents = loader.load()
    docs = text_splitter.split_documents(documents)
    print("Documents loaded")

    return docs


def index():
    print("Building index")
    qdrant = Qdrant.from_documents(
        docs,
        embeddings,
        url=access_secret_version("qdrant_cluster_url"),
        prefer_grpc=True,
        api_key=access_secret_version("qdrant_api_key"),
        collection_name="deeno",
        force_recreate=True
    )
    print("Index built")


MODEL_NAME = 'sentence-transformers/msmarco-distilbert-base-v3'
embeddings = HuggingFaceEmbeddings(model_name=MODEL_NAME)

df = pd.read_csv("plots.csv", quotechar="|", nrows=50000)
docs = load_and_split_docs()
index()

write_auto_complete_file(df)
upload_blob("deeno-data", "phrases.txt", "phrases.txt")
