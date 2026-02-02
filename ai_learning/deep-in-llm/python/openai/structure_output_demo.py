from enum import Enum

from dotenv import load_dotenv
from openai import OpenAI
from pydantic import BaseModel, Field

load_dotenv()
client = OpenAI()


class Frequency(int, Enum):
    """执行频率枚举"""
    ONCE = 0  # 一次
    DAILY = 1  # 每天


class ReachChannel(int, Enum):
    """触达渠道枚举"""
    SMS = 0  # 短信
    EMAIL = 1  # 邮件


class ExecutionPlan(BaseModel):
    start_date: str = Field(
        description="开始执行日期",
        examples=["2023-01-01"],
        pattern=r"^\d{4}-\d{2}-\d{2}$",
    )
    end_date: str = Field(
        description="结束执行日期",
        examples=["2023-01-01"],
        pattern=r"^\d{4}-\d{2}-\d{2}$",
    )
    execution_time: str = Field(
        description="执行时间",
        examples=["2023-01-01"],
        pattern=r"^\d{2}:\d{2}$",
    )
    frequency: Frequency = Field(
        description="执行频率",
        examples=[Frequency.ONCE],
        default=Frequency.ONCE
    )
    interval: int = Field(
        description="执行间隔",
        examples=[1],
        gt=0,
        le=365
    )


class MarketingStratepyPlan(BaseModel):
    name: str = Field(
        description="策略名称",
        examples=["推荐xxx活动"],
    )
    target: str = Field(
        description="策略目标",
        examples=["提供购买率"],
    )
    description: str = Field(
        description="策略描述",
        examples=["通过推荐xxx产品，提高购买率"],
    )
    execution_plan: ExecutionPlan = Field(
        description="执行计划",
        examples=[
            {
                "start_date": "2023-01-01",
                "end_date": "2023-01-31",
                "execution_time": "09:00",
                "frequency": Frequency.DAILY,
                "interval": 1,
            }
        ],
    )
    reach_channels: list[ReachChannel] = Field(
        description="触达渠道",
        examples=[ReachChannel.SMS, ReachChannel.EMAIL],
        default=[ReachChannel.SMS],
    )
    customer_group: str = Field(
        description="策略人群",
        examples=["会员等级高于黄金阶"],
    )


# response = client.responses.parse(
#     model="gpt-5",
#     input=[
#         {
#             "role": "system",
#             "content": """
#             你是一个专业的营销策略规划助手，负责根据用户输入生成、保存和确认营销策略计划。
#             """
#         },
#         {"role": "user", "content": "创建一个双十一促销活动，针对高价值客户群，使用会员档案，每天上午10点通过短信和邮件推送，从11月1日到11月11日"},
#     ],
#     text_format=MarketingStratepyPlan,
# )
#
# math_reasoning = response.output_parsed
# print(math_reasoning)

response = client.chat.completions.parse(
    model="deepseek-ai/DeepSeek-V3.2",
    messages=[
        {
            "role": "system",
            "content": """
            你是一个专业的营销策略规划助手，负责根据用户输入生成、保存和确认营销策略计划。
            """
        },
        {"role": "user", "content": "创建一个双十一促销活动，针对高价值客户群，使用会员档案，每天上午10点通过短信和邮件推送，从11月1日到11月11日"},
    ],
    temperature=0.7,
    max_tokens=1000,
    response_format=MarketingStratepyPlan
)
print(response.choices[0].message.content)
