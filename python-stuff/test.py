from openai import OpenAI

# client = OpenAI(
#     base_url="https://chatapi.akash.network/api/v1",
#     api_key="sk-k7I8ftI2plZUARxUnD2GyA",
# )

client = OpenAI(
  base_url="https://openrouter.ai/api/v1",
  api_key="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",
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

print(response)