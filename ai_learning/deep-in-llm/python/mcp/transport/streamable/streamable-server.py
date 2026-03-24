# main.py
from mcp.server.fastmcp import FastMCP
from mcp.server.fastmcp.prompts import base

import logging
import json

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

# 创建MCP实例
mcp = FastMCP(name="streamable-demo", port=8081, stateless_http=False)


# 添加一个简单的资源
@mcp.tool()
def greeting(name: str = "World") -> str:
    """返回问候语"""
    return f"Hello, {name}!"


# 添加计算功能
@mcp.tool()
def add(a: int, b: int) -> int:
    """加法计算器"""
    return a + b


@mcp.resource("models://")
def get_models() -> str:
    """Get information about available AI models"""
    logger.info("Retrieving available models")
    models_data = [
        {
            "id": "gpt-4",
            "name": "GPT-4",
            "description": "OpenAI's GPT-4 large language model"
        },
        {
            "id": "llama-3-70b",
            "name": "LLaMA 3 (70B)",
            "description": "Meta's LLaMA 3 with 70 billion parameters"
        },
        {
            "id": "claude-3-sonnet",
            "name": "Claude 3 Sonnet",
            "description": "Anthropic's Claude 3 Sonnet model"
        }
    ]

    return json.dumps({"models": models_data})


# Define a greeting resource that dynamically constructs a personalized greeting
@mcp.resource("greeting://{name}")
def get_greeting(name: str) -> str:
    """Return a greeting for the given name

    Args:
        name: The name to greet

    Returns:
        A personalized greeting message
    """
    import urllib.parse
    # Decode URL-encoded name
    decoded_name = urllib.parse.unquote(name)
    logger.info(f"Generating greeting for {decoded_name}")
    return f"Hello, {decoded_name}!"


@mcp.resource("file://documents/{name}")
def read_document(name: str) -> str:
    """Read a document by name."""
    # This would normally read from disk
    return f"Content of {name}"


@mcp.prompt(title="Code Review")
def review_code(code: str) -> str:
    return f"Please review this code:\n\n{code}"


@mcp.prompt(title="Debug Assistant")
def debug_error(error: str) -> list[base.Message]:
    return [
        base.UserMessage("I'm seeing this error:"),
        base.UserMessage(error),
        base.AssistantMessage("I'll help debug that. What have you tried so far?"),
    ]


if __name__ == "__main__":
    mcp.run(transport="streamable-http")
