# client.py - Streamable HTTP Protocol MCP Client
import sys
import urllib.parse
from mcp.client.streamable_http import streamablehttp_client

from mcp.client.session import ClientSession

import asyncio
import logging
import json

from mcp.types import TextContent, TextResourceContents
from mcp.server.fastmcp.prompts import base

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


async def main():
    """Main client function that demonstrates MCP client features with streamable protocol"""
    logger.info("Starting Streamable MCP Client")

    try:
        # Connect to streamable HTTP server
        logger.info("Connecting to streamable server at http://localhost:8081/mcp...")
        async with streamablehttp_client("http://localhost:8081/mcp") as (reader, writer, callback):
            async with ClientSession(reader, writer) as session:
                logger.info("Initializing session")
                await session.initialize()

                # 1. Call the greeting tool
                logger.info("Testing greeting tool")
                greeting_result = await session.call_tool("greeting", arguments={"name": "World"})
                if greeting_result and greeting_result.content:
                    text_content = next((content for content in greeting_result.content
                                         if isinstance(content, TextContent)), None)
                    if text_content:
                        print(f"\n1. Greeting: {text_content.text}")

                # 2. Call the add tool
                logger.info("Testing calculator tool")
                add_result = await session.call_tool("add", arguments={"a": 5, "b": 7})
                if add_result and add_result.content:
                    text_content = next((content for content in add_result.content
                                         if isinstance(content, TextContent)), None)
                    if text_content:
                        print(f"\n2. Calculator result (5 + 7) = {text_content.text}")

                # 3. Get models resource
                logger.info("Testing models resource")
                models_response = await session.read_resource("models://")
                if models_response and models_response.contents:
                    text_resource = next((content for content in models_response.contents
                                          if isinstance(content, TextResourceContents)), None)
                    if text_resource:
                        models = json.loads(text_resource.text)
                        print("\n3. Available models:")
                        for model in models.get("models", []):
                            print(f"   - {model['name']} ({model['id']}): {model['description']}")

                # 4. Get greeting resource
                logger.info("Testing greeting resource")
                name = "MCP Explorer"
                encoded_name = urllib.parse.quote(name)
                greeting_response = await session.read_resource(f"greeting://{encoded_name}")
                if greeting_response and greeting_response.contents:
                    text_resource = next((content for content in greeting_response.contents
                                          if isinstance(content, TextResourceContents)), None)
                    if text_resource:
                        print(f"\n4. Greeting: {text_resource.text}")

                # 5. Get document resource
                logger.info("Testing document resource")
                document_response = await session.read_resource("file://documents/example.txt")
                if document_response and document_response.contents:
                    text_resource = next((content for content in document_response.contents
                                          if isinstance(content, TextResourceContents)), None)
                    if text_resource:
                        print(f"\n5. Document content:")
                        print(f"   {text_resource.text}")

                # 6. Use code review prompt
                logger.info("Testing code review prompt")
                sample_code = "def hello_world():\n    print('Hello, world!')"
                prompt_response = await session.get_prompt("review_code", {"code": sample_code})
                if prompt_response and prompt_response.messages:
                    message = next((msg for msg in prompt_response.messages if msg.content), None)
                    if message and message.content:
                        text_content = next((content for content in [message.content]
                                             if isinstance(content, TextContent)), None)
                        if text_content:
                            print("\n6. Code review prompt:")
                            print(f"   {text_content.text}")

                # 7. Use debug error prompt (multi-message format)
                logger.info("Testing debug assistant prompt")
                error_message = "AttributeError: 'NoneType' object has no attribute 'method'"
                debug_response = await session.get_prompt("debug_error", {"error": error_message})
                if debug_response and debug_response.messages:
                    print("\n7. Debug assistant prompt (multi-message):")
                    for idx, msg in enumerate(debug_response.messages):
                        if isinstance(msg, base.UserMessage):
                            print(f"   [User Message {idx + 1}]: {msg.content.text if hasattr(msg.content, 'text') else msg.content}")
                        elif isinstance(msg, base.AssistantMessage):
                            print(f"   [Assistant Message {idx + 1}]: {msg.content.text if hasattr(msg.content, 'text') else msg.content}")

    except Exception:
        logger.exception("An error occurred")
        sys.exit(1)


if __name__ == "__main__":
    asyncio.run(main())

