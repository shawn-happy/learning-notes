# main.py
from mcp.server.fastmcp import FastMCP
from mcp.server.fastmcp.prompts import base

# 创建MCP实例
mcp = FastMCP(name="demo-mcp")

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

# 添加带文档的复杂资源
@mcp.resource("users:/users/profile/{user_id}")
def get_user_profile(user_id: int) -> dict:
    """
    获取用户资料
    
    Args:
        user_id: 用户ID
        
    Returns:
        包含用户信息的字典
    """
    print(f"Getting user profile for user {user_id}...")
    return {
        "id": user_id,
        "name": f"User {user_id}",
        "email": f"user{user_id}@example.com"
    }


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
    mcp.run(transport="sse")
    resources = mcp.list_resources()
    print(resources)