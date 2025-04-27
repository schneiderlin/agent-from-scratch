from dotenv import load_dotenv, dotenv_values
import os

load_dotenv()

config = {
    **dotenv_values(".env.shared"),  # load shared development variables
    **dotenv_values(".env.secret"),  # load sensitive variables
    **os.environ,  # override loaded values with environment variables
}

print(config["mykey"])