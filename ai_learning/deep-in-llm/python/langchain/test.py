from langchain_core.tools import tool


@tool(response_format="content_and_artifact")
def product_search(query: str):
    """根据查询语句搜索产品。"""
    # 模拟从数据库获取数据
    raw_data = {"id": 123, "name": "AI 助手", "price": 999}

    # 必须返回元组
    content = f"找到产品：{raw_data['name']}，价格：{raw_data['price']}"
    artifact = raw_data

    return content, artifact


if __name__ == '__main__':
    # 方法1：直接获取 ToolMessage 对象
    tool_call = {
        "name": "product_search",
        "args": {"query": "AI 助手"},
        "id": "call_123",  # 必须提供 tool_call_id
        "type": "tool_call"
    }
    result = product_search.invoke(tool_call)
    print("Content:", result.content)
    print("Artifact:", result.artifact)

