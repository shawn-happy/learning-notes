import datetime
import os

from dotenv import load_dotenv
from langchain_core.messages import AIMessage, ToolMessage, HumanMessage
from langchain_core.tools import tool
from langchain_openai import ChatOpenAI

# 加载环境变量
load_dotenv()
api_key = os.getenv('OPENAI_API_KEY')
base_url = os.getenv('OPENAI_BASE_URL')

# ========== 1. 初始化 LLM ==========
llm = ChatOpenAI(
    base_url=base_url,
    api_key=api_key,
    model="deepseek-ai/DeepSeek-V3",
    temperature=0.7
)


# ========== 2. 定义工具 ==========
@tool(description="Returns the current time in yyyy-MM-dd HH:mm:ss format.")
def get_current_time(*args, **kwargs) -> str:
    """获取当前系统时间。"""
    now = datetime.datetime.now()
    return now.strftime("%Y-%m-%d %H:%M:%S")


# ========== 方式1：自动工具调用（推荐） ==========
def demo_simple_tool_calling():
    """
    方式1：使用 bind_tools + 手动执行
    特点：需要手动处理工具调用和结果
    """
    print("=" * 50)
    print("🛠️ 方式1：bind_tools + 手动执行工具")
    print("=" * 50)

    llm_tools = llm.bind_tools(tools=[get_current_time])
    messages = [
        {"role": "user", "content": "请获取当前时间"}
    ]

    # 第一步：LLM 返回工具调用请求
    result = llm_tools.invoke(messages)
    print(f"\n📤 LLM 响应类型: {type(result)}")
    print(f"📤 LLM 内容: {result.content}")
    print(f"📤 工具调用请求: {result.tool_calls}")

    # 第二步：手动执行工具
    if result.tool_calls:
        for tool_call in result.tool_calls:
            print(f"\n🔧 执行工具: {tool_call['name']}")
            print(f"🔧 工具参数: {tool_call['args']}")

            if tool_call['name'] == 'get_current_time':
                # 手动调用工具
                tool_result = get_current_time.invoke(tool_call['args'])
                print(f"✅ 工具执行结果: {tool_result}")

                # 第三步：将工具结果返回给 LLM（可选）
                messages.append(result)  # 添加 AI 的工具调用消息
                messages.append(ToolMessage(
                    content=tool_result,
                    tool_call_id=tool_call['id']
                ))

                # 让 LLM 根据工具结果生成最终回答
                final_response = llm_tools.invoke(messages)
                print(f"\n💬 最终回答: {final_response.content}")


# ========== 方式2：模拟工具调用流程 ==========
def demo_simple_tool_calling_2():
    """
    方式2：手动构造 AIMessage 和 ToolMessage
    特点：完全手动控制消息流，适合测试和调试
    """
    print("\n" + "=" * 50)
    print("🛠️ 方式2：手动构造消息流")
    print("=" * 50)

    # 手动构造 AI 的工具调用消息
    ai_message = AIMessage(
        content="",  # 通常为空，因为 AI 选择调用工具而不是直接回答
        tool_calls=[{
            "name": "get_current_time",
            "args": {},
            "id": "call_123"
        }]
    )
    print(f"\n📤 模拟 AI 工具调用: {ai_message.tool_calls}")

    # 执行工具并创建结果消息
    tool_result = get_current_time.invoke({})
    print(f"✅ 工具执行结果: {tool_result}")

    tool_message = ToolMessage(
        content=tool_result,
        tool_call_id="call_123"
    )

    # 构造完整的消息历史
    messages = [
        HumanMessage("请获取当前时间"),
        ai_message,  # AI 的工具调用
        tool_message,  # 工具执行结果
    ]

    # LLM 根据工具结果生成最终回答
    response = llm.invoke(messages)
    print(f"\n💬 最终回答: {response.content}")


# ========== 方式3：使用完整的消息循环（最佳实践） ==========
def demo_complete_tool_calling():
    """
    方式3：完整的工具调用循环
    特点：自动处理多轮工具调用，更接近实际应用
    """
    print("\n" + "=" * 50)
    print("🛠️ 方式3：完整的工具调用循环（推荐）")
    print("=" * 50)

    llm_tools = llm.bind_tools(tools=[get_current_time])
    tool_map = {"get_current_time": get_current_time}

    messages = [HumanMessage(content="请获取当前时间")]

    max_iterations = 5  # 防止无限循环
    iteration = 0

    while iteration < max_iterations:
        iteration += 1
        print(f"\n🔄 第 {iteration} 轮调用")

        # 调用 LLM
        response = llm_tools.invoke(messages)
        messages.append(response)

        # 检查是否有工具调用
        if not response.tool_calls:
            print(f"✅ 完成！最终回答: {response.content}")
            break

        # 执行所有工具调用
        for tool_call in response.tool_calls:
            tool_name = tool_call['name']
            tool_args = tool_call['args']
            tool_id = tool_call['id']

            print(f"🔧 执行工具: {tool_name}")
            print(f"🔧 参数: {tool_args}")

            # 执行工具
            tool_result = tool_map[tool_name].invoke(tool_args)
            print(f"✅ 结果: {tool_result}")

            # 添加工具结果到消息历史
            messages.append(ToolMessage(
                content=tool_result,
                tool_call_id=tool_id
            ))

    if iteration >= max_iterations:
        print("⚠️ 达到最大迭代次数")


# ========== 额外示例：多个工具 ==========
@tool
def calculate(expression: str) -> str:
    """计算数学表达式。"""
    try:
        result = eval(expression)
        return f"计算结果: {result}"
    except Exception as e:
        return f"计算错误: {str(e)}"


def demo_multiple_tools():
    """演示多个工具的使用"""
    print("\n" + "=" * 50)
    print("🛠️ 多工具示例")
    print("=" * 50)

    llm_tools = llm.bind_tools(tools=[get_current_time, calculate])
    tool_map = {
        "get_current_time": get_current_time,
        "calculate": calculate
    }

    messages = [HumanMessage(content="现在几点？然后帮我计算 15 * 8")]

    max_iterations = 10
    iteration = 0

    while iteration < max_iterations:
        iteration += 1
        response = llm_tools.invoke(messages)
        messages.append(response)

        if not response.tool_calls:
            print(f"\n✅ 最终回答: {response.content}")
            break

        for tool_call in response.tool_calls:
            tool_name = tool_call['name']
            print(f"\n🔧 调用工具: {tool_name}")

            tool_result = tool_map[tool_name].invoke(tool_call['args'])
            print(f"✅ 结果: {tool_result}")

            messages.append(ToolMessage(
                content=tool_result,
                tool_call_id=tool_call['id']
            ))


# ========== 主函数 ==========
if __name__ == '__main__':
    # 基础示例
    demo_simple_tool_calling()
    demo_simple_tool_calling_2()

    # 进阶示例
    demo_complete_tool_calling()

    # 多工具示例
    demo_multiple_tools()
