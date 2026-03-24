# client.py
import sys
import urllib

from mcp.client.sse import sse_client
from mcp.client.session import ClientSession

import asyncio

import logging

import json

from mcp.types import TextContent, TextResourceContents

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


async def main():
    """Main client function that demonstrates MCP client features"""
    logger.info("Starting clean MCP client")

    try:
        logger.info("Connecting to server...")
        async with sse_client(url="http://localhost:8082/sse") as (reader, writer):
            async with ClientSession(reader, writer) as session:
                logger.info("Initializing session")
                await session.initialize()

                # 1. Call the add tool
                logger.info("Testing calculator tool")
                add_result = await session.call_tool("add", arguments={"a": 5, "b": 7})
                if add_result and add_result.content:
                    text_content = next((content for content in add_result.content
                                         if isinstance(content, TextContent)), None)
                    if text_content:
                        print(f"\n1. Calculator result (5 + 7) = {text_content.text}")


                # 2. Get models resource
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

    except Exception:
        logger.exception("An error occurred")
        sys.exit(1)


if __name__ == "__main__":
    asyncio.run(main())

