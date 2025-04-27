from openai import OpenAI
from dotenv import load_dotenv, dotenv_values
import os

load_dotenv()

config = {
    **dotenv_values(".env.shared"),
    **dotenv_values(".env.secret"),
    **os.environ,
}

client = OpenAI(
  base_url="https://openrouter.ai/api/v1",
  api_key=config["openrouter-apikey"],
)

tools = [
    {
        "type": "function",
        "function": {
            "name": "get_weather",
            "description": "Get current temperature for provided coordinates in celsius.",
            "parameters": {
                "type": "object",
                "properties": {
                    "latitude": {"type": "number"},
                    "longitude": {"type": "number"}
                },
                "required": ["latitude", "longitude"],
                "additionalProperties": False
            },
            "strict": True
        }
    }
]

response = client.chat.completions.create(
    model="deepseek/deepseek-chat-v3-0324",
    tools=tools,
    messages=[
        {"role": "user", "content": "How's the weather in guangzhou?"}
    ]
)

tool_calls = response.choices[0].message.tool_calls

for tool_call in tool_calls:
    print(tool_call.function.name)
    print(tool_call.function.arguments)

response1 = client.chat.completions.create(
    model="deepseek/deepseek-chat-v3-0324",
    tools=tools,
    messages=[
        {"role": "user", "content": "How's the weather in guangzhou?"},
        {"role": "tool", "content": "23 degree c, raining", "tool_call_id": tool_calls[0].id}
    ]
)