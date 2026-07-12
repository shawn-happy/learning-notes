from dotenv import load_dotenv
from langchain.chat_models import init_chat_model
from langchain_core.callbacks import UsageMetadataCallbackHandler
from langchain_core.messages import AIMessageChunk

load_dotenv()


def chat_model_invoke():
    model = init_chat_model(
        model="deepseek-ai/DeepSeek-V4-Flash",
        model_provider="openai",
        temperature=0.5,
        timeout=600,
        max_tokens=25000,
        streaming=True,
    )

    message = model.invoke("100字以内介绍一下langchain")

    print(f"最终回答：{message.content}")


def chat_model_stream():
    model = init_chat_model(
        model="deepseek-ai/DeepSeek-V4-Flash",
        model_provider="openai",
        temperature=0.5,
        timeout=600,
        max_tokens=25000,
        streaming=True,
    )

    for chunk in model.stream("100字以内介绍一下langchain"):
        if isinstance(chunk, AIMessageChunk):
            print(chunk.content)


def chat_model_batch():
    model = init_chat_model(
        model="deepseek-ai/DeepSeek-V4-Flash",
        model_provider="openai",
        temperature=0.5,
        timeout=600,
        max_tokens=25000,
        streaming=True,
    )

    messages = [
        "100字以内介绍一下langchain",
        "100字以内介绍一下langgraph",
        "100字以内介绍一下deepagents",
    ]

    for chunk in model.batch(messages):
        print(chunk)


def chat_model_stream_with_reasoning():
    from langchain_openai import ChatOpenAI
    model = ChatOpenAI(
        model="deepseek-ai/DeepSeek-V3.2",
        temperature=0.6,
        timeout=600,
        max_tokens=25000,
        extra_body={
            "reasoning_effort": "high",
            "enable_thinking": True,
            "thinking_budget": 1024
        }
    )

    # for chunk in model.stream("介绍一下langchain"):
    #     reasoning_steps = [r for r in chunk.content_blocks if r["type"] == "reasoning"]
    #     if reasoning_steps:
    #         print(f"推理步骤：{reasoning_steps}")
    response = model.invoke("介绍一下langchain")
    # 🔍 o4-mini 的思考内容通过 content blocks 的 reasoning 类型返回
    for block in response.content_blocks if isinstance(response.content_blocks, list) else []:
        if isinstance(block, dict) and block.get("type") == "reasoning":
            print("💭 思考:", block.get("reasoning", ""))
        elif isinstance(block, dict) and block.get("type") == "text":
            print("📝 回答:", block.get("text", ""))


def save_user_profile(username: str, age: int, emails: str, position: str) -> dict[str, int]:
    """
    description:
        用于保存用户信息，最终返回用户主键

    Args:
        username: 用户名称
        age: 年龄
        emails: 邮箱
        position: 职位

    return:
        返回user_id
    """
    user = {
        "username": username,
        "age": age,
        "emails": emails,
        "position": position
    }
    print(f"保存用户信息：{user}")
    return {"user_id": 1}


def chat_model_bind_tool():
    model = init_chat_model(
        model="deepseek-ai/DeepSeek-V4-Flash",
        model_provider="openai",
        temperature=0.5,
        timeout=600,
        max_tokens=25000,
        streaming=True,
    )
    model_with_tools = model.bind_tools([save_user_profile])
    messages = [
        {
            "role": "system",
            "content": "你是一个智能助手，用于调用 `save_user_profile` 工具保存用户提供的用户信息, 最终输出用户id"
        },
        {
            "role": "user",
            "content": "用户名称是张三，年龄是18，邮箱是<EMAIL>，职位是程序员"
        }
    ]
    print("=== 发送请求 ===")
    response = model_with_tools.invoke(messages)

    print(f"\n=== 模型响应 ===")
    print(f"内容：{response.content}")

    # 检查并执行工具调用
    if hasattr(response, 'tool_calls') and response.tool_calls:
        print(f"\n=== 工具调用 ===")
        for tool_call in response.tool_calls:
            print(f"工具名: {tool_call['name']}")
            print(f"参数: {tool_call['args']}")

            # 执行工具
            if tool_call['name'] == 'save_user_profile':
                result = save_user_profile(username=tool_call['args']['username'],
                                           age=tool_call['args']['age'],
                                           emails=tool_call['args']['emails'],
                                           position=tool_call['args']['position'])
                print(f"执行结果: {result}")
    else:
        print("\n⚠️ 模型未调用工具")


from pydantic import BaseModel, Field


class UserProfile(BaseModel):
    """user profile info"""
    username: str = Field(description="用户名")
    email: str = Field(description="邮箱")
    age: int = Field(description="年龄")
    position: str = Field(description="岗位, 职位，职级")


def chat_model_structure_output():
    model = init_chat_model(
        model="deepseek-ai/DeepSeek-V4-Flash",
        model_provider="openai",
        temperature=0.5,
        timeout=600,
        max_tokens=25000,
        streaming=True,
    )

    messages = [
        {
            "role": "system",
            "content": """
            你是一个智能助手，根据用户的输入，提取出用户画像。输出的json如下：
            {
                "username" : "用户名",
                "age": 29,
                "email": "example@qq.com",
                "position": "prompt工程师"
            }
            """
        },
        {
            "role": "user",
            "content": "用户名称是张三，年龄是18，邮箱是<EMAIL>，职位是程序员"
        }
    ]
    print("=== 发送请求 ===")
    response = model.with_structured_output(UserProfile).invoke(messages)

    print(f"\n=== 模型响应 ===")
    print(response)


def save_user_profile_by_structured(user: UserProfile) -> dict[str, int]:
    """
    description:
        用于保存用户信息，最终返回用户主键

    Args:
        user: 用户画像

    return:
        返回user_id
    """
    print(f"保存用户信息：{user}")
    return {"user_id": 1}


def chat_model_bind_structure_tool():
    model = init_chat_model(
        model="deepseek-ai/DeepSeek-V4-Flash",
        model_provider="openai",
        temperature=0.5,
        timeout=600,
        max_tokens=25000,
        streaming=True,
    )

    messages = [
        {
            "role": "system",
            "content": """
            你是一个智能助手，根据用户的输入，提取出用户画像。输出的json如下：
            {
                "username" : "用户名",
                "age": 29,
                "email": "example@qq.com",
                "position": "prompt工程师"
            }
            调用 `save_user_profile_by_structured` 进行保存
            """
        },
        {
            "role": "user",
            "content": "用户名称是张三，年龄是18，邮箱是<EMAIL>，职位是程序员"
        }
    ]
    print("=== 发送请求 ===")
    model_with_tools = model.bind_tools([save_user_profile_by_structured])
    response = model_with_tools.invoke(messages)

    print(f"\n=== 模型响应 ===")
    print(f"内容：{response.content}")

    # 检查并执行工具调用
    if hasattr(response, 'tool_calls') and response.tool_calls:
        print(f"\n=== 工具调用 ===")
        for tool_call in response.tool_calls:
            print(f"工具名: {tool_call['name']}")
            print(f"参数: {tool_call['args']}")

            # 执行工具
            if tool_call['name'] == 'save_user_profile_by_structured':
                result = save_user_profile_by_structured(tool_call['args'])
                print(f"执行结果: {result}")
    else:
        print("\n⚠️ 模型未调用工具")


def chat_model_token_usage():
    model = init_chat_model(
        model="deepseek-ai/DeepSeek-V4-Flash",
        model_provider="openai",
        temperature=0.5,
        timeout=600,
        max_tokens=25000,
        streaming=True,
    )

    messages = [
        {
            "role": "system",
            "content": """
            你是一个智能助手，根据用户的输入，提取出用户画像。输出的json如下：
            {
                "username" : "用户名",
                "age": 29,
                "email": "example@qq.com",
                "position": "prompt工程师"
            }
            调用 `save_user_profile_by_structured` 进行保存
            """
        },
        {
            "role": "user",
            "content": "用户名称是张三，年龄是18，邮箱是<EMAIL>，职位是程序员"
        }
    ]
    print("=== 发送请求 ===")
    model_with_tools = model.bind_tools([save_user_profile_by_structured])
    callback = UsageMetadataCallbackHandler()
    response = model_with_tools.invoke(messages, config={"callbacks": [callback]})

    print(f"\n=== 模型响应 ===")
    print(f"内容：{response.content}")

    # 检查并执行工具调用
    if hasattr(response, 'tool_calls') and response.tool_calls:
        print(f"\n=== 工具调用 ===")
        for tool_call in response.tool_calls:
            print(f"工具名: {tool_call['name']}")
            print(f"参数: {tool_call['args']}")

            # 执行工具
            if tool_call['name'] == 'save_user_profile_by_structured':
                result = save_user_profile_by_structured(tool_call['args'])
                print(f"执行结果: {result}")
    else:
        print("\n⚠️ 模型未调用工具")

    print(f"\n=== 使用情况 ===")
    print(callback.usage_metadata)


if __name__ == '__main__':
    chat_model_batch()
