from dotenv import load_dotenv
from mcp import ClientSession, StdioServerParameters, types

# llm
import os
import json

from mcp.client.sse import sse_client
from mcp.client.streamable_http import streamablehttp_client
from openai import OpenAI

load_dotenv()
client = OpenAI()

print("CALLING LLM")

resp = client.responses.create(
    model="gpt-5.4",
    tools=[
        {
            "type": "mcp",
            "server_label": "mcp-demo",
            "server_description": "用于计算的agent",
            "server_url": "http://localhost:8082/sse",
            "require_approval": "never",
        },
    ],
    input="20 + 2等于多少？"
)
print(resp.output_text)



