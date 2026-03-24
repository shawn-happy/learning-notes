from dotenv import load_dotenv
from mcp import ClientSession, StdioServerParameters, types

# llm
import os
import json

from mcp.client.streamable_http import streamablehttp_client
from openai import OpenAI

load_dotenv()
client = OpenAI()

# Create server parameters for stdio connection
server_params = StdioServerParameters(
    command="mcp",  # Executable
    args=["run", "server.py"],  # Optional command line arguments
    env=None,  # Optional environment variables
)


def call_llm(prompt, functions):

    print("CALLING LLM")
    response = client.chat.completions.create(
        model="deepseek-ai/DeepSeek-V3.2",
        tools=functions,
        messages=[
            {
                "role": "system",
                "content": "You are a helpful assistant.",
            },
            {
                "role": "user",
                "content": prompt,
            },
        ],
    )

    response_message = response.choices[0].message

    functions_to_call = []

    if response_message.tool_calls:
        for tool_call in response_message.tool_calls:
            print("TOOL: ", tool_call)
            name = tool_call.function.name
            args = json.loads(tool_call.function.arguments)
            functions_to_call.append({"name": name, "args": args})

    resp = client.chat.completions.create(
        model="deepseek-ai/DeepSeek-V3.2",
        tools=[
            {
                "type": "mcp",
                "server_label": "mcp-demo",
                "server_description": "A Dungeons and Dragons MCP server to assist with dice rolling.",
                "server_url": "http://localhost:8080/mcp",
                "require_approval": "never",
            },
        ],
        messages=[
            {
                "role": "system",
                "content": "You are a helpful assistant.",
            },
            {
                "role": "user",
                "content": prompt,
            },
        ]
    )
    print(resp.output_text)

    return functions_to_call


def convert_to_llm_tool(tool):
    tool_schema = {
        "type": "function",
        "function": {
            "name": tool.name,
            "description": tool.description,
            "type": "function",
            "parameters": {
                "type": "object",
                "properties": tool.inputSchema["properties"]
            }
        }
    }

    return tool_schema


async def run():
    async with streamablehttp_client("http://localhost:8080/mcp") as (reader, writer, callback):
        async with ClientSession(
                reader, writer
        ) as session:
            # Initialize the connection
            await session.initialize()

            # List available resources
            resources = await session.list_resources()
            print("LISTING RESOURCES")
            for resource in resources:
                print("Resource: ", resource)

            # List available tools
            tools = await session.list_tools()
            print("LISTING TOOLS")

            functions = []

            for tool in tools.tools:
                print("Tool: ", tool.name)
                print("Tool", tool.inputSchema["properties"])
                functions.append(convert_to_llm_tool(tool))

            prompt = "Add 2 to 20"

            # ask LLM what tools to all, if any
            functions_to_call = call_llm(prompt, functions)

            # call suggested functions
            for f in functions_to_call:
                result = await session.call_tool(f["name"], arguments=f["args"])
                print("TOOLS result: ", result.content)


if __name__ == "__main__":
    import asyncio

    asyncio.run(run())



