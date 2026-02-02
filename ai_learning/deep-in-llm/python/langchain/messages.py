import os

import requests
from dotenv import load_dotenv
from langchain.messages import SystemMessage, HumanMessage, AIMessage
from langchain_core.messages import ToolMessage
from langchain_openai import ChatOpenAI

load_dotenv()
api_key = os.getenv('OPENAI_API_KEY')
base_url = os.getenv('OPENAI_BASE_URL')
q_weather_api_key = os.getenv('QWEATHER_API_KEY')

llm = ChatOpenAI(
    base_url=base_url,
    api_key=api_key,
    model="deepseek-ai/DeepSeek-V3.2",
    temperature=0.7  # LangChain 特点：统一的参数配置
)

# messages = [
#     SystemMessage(content="你是一名资深的Python后端工程师面试官，负责考察候选人的技术能力。"),
#     HumanMessage(content="请问什么是装饰器？"),
#     AIMessage(content="装饰器是Python中用于修改函数或类行为的语法糖..."),
#     HumanMessage(content="能举个实际应用的例子吗？")
# ]
messages = [
    {"role": "system", "content": "你是一名资深的Python后端工程师面试官，负责考察候选人的技术能力。"},
    {"role": "user", "content": "请问什么是装饰器？"},
    {"role": "assistant", "content": "装饰器是Python中用于修改函数或类行为的语法糖..."},
    {"role": "user", "content": "能举个实际应用的例子吗？"}
]

# response = llm.invoke(messages)
#
# print(f"📝 LLMChain 输出：\n{response.content}\n")


def get_weather(location: str) -> str:
    """Get the weather at a location."""
    # 先获取城市ID
    location_url = f"https://n53h2qt5jy.re.qweatherapi.com/geo/v2/city/lookup?location={location}"
    location_response = (requests.get(url=location_url,headers={"Content-Type": "application/json","X-QW-Api-Key":  q_weather_api_key}).json())

    if location_response['code'] != '200':
        return f"未找到城市：{location}"

    location_id = location_response['location'][0]['id']

    # 查询天气
    weather_url = f"https://n53h2qt5jy.re.qweatherapi.com/v7/weather/now?location={location_id}"
    weather_response = requests.get(url=weather_url, headers={"Content-Type": "application/json", "X-QW-Api-Key":  q_weather_api_key}).json()

    if weather_response['code'] == '200':
        now = weather_response['now']
        return f"{location}当前天气：{now['text']}，温度{now['temp']}°C，体感温度{now['feelsLike']}°C"

    return "天气查询失败"

model_with_tools = llm.bind_tools([get_weather])
response = model_with_tools.invoke("What's the weather in 上海?")

for tool_call in response.tool_calls:
    print(f"Tool: {tool_call['name']}")
    print(f"Args: {tool_call['args']}")
    print(f"ID: {tool_call['id']}")

# After a model makes a tool call
# (Here, we demonstrate manually creating the messages for brevity)
ai_message = AIMessage(
    content=[],
    tool_calls=[{
        "name": "get_weather",
        "args": {"location": "上海"},
        "id": "call_123"
    }]
)

# Execute tool and create result message
tool_message = ToolMessage(
    content=get_weather("上海"),
    tool_call_id="call_123"  # Must match the call ID
)

# Continue conversation
messages = [
    HumanMessage("上海天气如何?"),
    ai_message,  # Model's tool call
    tool_message,  # Tool execution result
]
response = llm.invoke(messages)  # Model processes the result
print("==================")
print(f"📝 LLMChain 输出：\n{response.content}\n")

print(f"llm 元数据：{response.usage_metadata}")

message = AIMessage(
    content=[
        {
            "type": "reasoning",
            "id": "rs_abc123",
            "summary": [
                {"type": "summary_text", "text": "summary 1"},
                {"type": "summary_text", "text": "summary 2"},
            ],
        },
        {"type": "text", "text": "...", "id": "msg_abc123"},
    ],
    response_metadata={"model_provider": "openai"}
)
print(message.content_blocks)