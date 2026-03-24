# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a learning repository for AI/LLM development in Python, organized by technology stack:

- **`mcp/`** - Model Context Protocol (MCP) implementations with three transport modes
- **`langchain/`** - LangChain framework examples with LCEL patterns
- **`openai/`** - OpenAI API usage demonstrations (function calling, structured output)
- **`adk_learning/`** - Google ADK (Agent Development Kit) agent examples

## MCP (Model Context Protocol)

### Server Implementation Pattern
All MCP servers use `FastMCP` with decorators:
```python
from mcp.server.fastmcp import FastMCP
mcp = FastMCP(name="server-name")

@mcp.tool()
def tool_name(param: str) -> str:
    """Tool description"""
    return result

@mcp.resource("uri://pattern/{var}")
def get_resource(var: str) -> str:
    """Resource description"""
    return data

@mcp.prompt(title="Prompt Name")
def prompt_func(param: str) -> str | list[Message]:
    """Prompt definition"""
    return template

if __name__ == "__main__":
    mcp.run()  # or mcp.run(transport="sse") for SSE mode
```

### Transport Modes
- **`stdio/`** - Uses `stdio_client` with `StdioServerParameters` for subprocess communication
- **`sse/`** - Uses `sse_client` with HTTP server at `http://localhost:8000/sse`
- **`streamable/`** - Similar to stdio but with streaming capabilities

### Client Pattern
```python
from mcp import stdio_client, StdioServerParameters
from mcp.client.session import ClientSession

async with stdio_client(params) as (reader, writer):
    async with ClientSession(reader, writer) as session:
        await session.initialize()
        result = await session.call_tool("tool_name", arguments={...})
        resource = await session.read_resource("uri://path")
        prompt = await session.get_prompt("prompt_name", {...})
```

## LangChain

Uses LCEL (LangChain Expression Language) pattern with pipe operators:
```python
from langchain_core.prompts import PromptTemplate
from langchain_core.output_parsers import StrOutputParser

chain = PromptTemplate.from_template(...) | model | StrOutputParser()
result = chain.invoke({"var": "value"})

# Streaming
for chunk in chain.stream({"var": "value"}):
    print(chunk.text, end="")

# Batch
results = chain.batch([{"var": "1"}, {"var": "2"}])
```

Model initialization uses `ChatOpenAI` or `init_chat_model()` with provider-specific base URLs.

## Google ADK (Agent Development Kit)

Agent-based pattern with sessions:
```python
from google.adk.agents.llm_agent import Agent
from google.adk.runners import Runner
from google.adk.sessions.in_memory_session_service import InMemorySessionService

agent = Agent(
    model=LiteLlm(model="provider/model"),
    name="agent_name",
    instruction="...",
    tools=[tool_func]
)

session_service = InMemorySessionService()
runner = Runner(agent=agent, app_name="app", session_service=session_service)

async for event in runner.run_async(user_id, session_id, new_message):
    # Process events
```

## Environment Configuration

API credentials are loaded from `.env` file:
- `OPENAI_API_KEY` - OpenAI API key
- `OPENAI_BASE_URL` - Custom base URL endpoint

Use `python-dotenv` to load: `load_dotenv()`

## Dependencies

Install dependencies from `requirements.txt`:
```bash
pip install -r requirements.txt
```

Key packages: `mcp~=1.16.0`, `langchain==1.1.3`, `google-adk==1.22.1`, `openai`, `litellm==1.81.0`
