import datetime
import os
from typing import List, Dict, Optional, Tuple, Any
from enum import Enum

from dotenv import load_dotenv
from langchain_core.messages import AIMessage, ToolMessage, HumanMessage
from langchain_core.tools import tool, StructuredTool, BaseTool
from langchain_openai import ChatOpenAI
from pydantic import BaseModel, Field, field_validator

# 加载环境变量
load_dotenv()
api_key = os.getenv('OPENAI_API_KEY')
base_url = os.getenv('OPENAI_BASE_URL')

llm = ChatOpenAI(
    base_url=base_url,
    api_key=api_key,
    model="deepseek-ai/DeepSeek-V3",
    temperature=0.7
)


# ========== 1. 基础结构化入参 ==========
class SearchInput(BaseModel):
    """搜索工具的输入参数"""
    query: str = Field(description="搜索关键词")
    max_results: int = Field(default=10, description="最大返回结果数", ge=1, le=100)
    language: str = Field(default="zh", description="语言代码，如 zh, en")


@tool(args_schema=SearchInput)
def search_web(query: str, max_results: int = 10, language: str = "zh") -> str:
    """在网络上搜索信息。"""
    return f"搜索 '{query}'，返回 {max_results} 条 {language} 结果"


def demo_basic_structured_input():
    """演示基础结构化入参"""
    print("=" * 60)
    print("📝 示例1：基础结构化入参")
    print("=" * 60)

    llm_tools = llm.bind_tools([search_web])
    messages = [HumanMessage("搜索 LangChain 相关信息，返回5条结果")]

    response = llm_tools.invoke(messages)
    print(f"\n🤖 LLM 工具调用:")
    for tool_call in response.tool_calls:
        print(f"  工具: {tool_call['name']}")
        print(f"  参数: {tool_call['args']}")

        # 执行工具
        result = search_web.invoke(tool_call['args'])
        print(f"  结果: {result}")


# ========== 2. 复杂结构化入参（嵌套对象） ==========
class Address(BaseModel):
    """地址信息"""
    street: str = Field(description="街道地址")
    city: str = Field(description="城市")
    country: str = Field(default="中国", description="国家")
    postal_code: Optional[str] = Field(None, description="邮政编码")


class UserInfo(BaseModel):
    """用户信息"""
    name: str = Field(description="用户姓名")
    age: int = Field(description="年龄", ge=0, le=150)
    email: str = Field(description="邮箱地址")
    address: Address = Field(description="地址信息")
    tags: List[str] = Field(default=[], description="用户标签")

    @field_validator('email')
    def validate_email(cls, v):
        if '@' not in v:
            raise ValueError('邮箱格式不正确')
        return v


@tool(args_schema=UserInfo)
def create_user(name: str, age: int, email: str, address: Address, tags: List[str] = None) -> str:
    """创建新用户。"""
    tags = tags or []
    return f"创建用户: {name}, {age}岁, {email}, 地址: {address.city}, 标签: {tags}"


def demo_nested_structured_input():
    """演示嵌套结构化入参"""
    print("\n" + "=" * 60)
    print("📝 示例2：嵌套结构化入参")
    print("=" * 60)

    # 手动测试
    user_data = {
        "name": "张三",
        "age": 25,
        "email": "zhangsan@example.com",
        "address": {
            "street": "中关村大街1号",
            "city": "北京",
            "country": "中国",
            "postal_code": "100000"
        },
        "tags": ["VIP", "技术"]
    }

    tool_call = {
        "name": "create_user",
        "args": user_data,
        "id": "call_123",  # 必须提供 tool_call_id
        "type": "tool_call"
    }

    result = create_user.invoke(tool_call)
    print(f"\n✅ 执行结果: {result}")


# ========== 3. 枚举类型入参 ==========
class OrderStatus(str, Enum):
    """订单状态枚举"""
    PENDING = "pending"
    PROCESSING = "processing"
    SHIPPED = "shipped"
    DELIVERED = "delivered"
    CANCELLED = "cancelled"


class OrderFilter(BaseModel):
    """订单查询过滤条件"""
    status: Optional[OrderStatus] = Field(None, description="订单状态")
    min_amount: Optional[float] = Field(None, description="最小金额", ge=0)
    max_amount: Optional[float] = Field(None, description="最大金额", ge=0)
    start_date: Optional[str] = Field(None, description="开始日期 (YYYY-MM-DD)")
    end_date: Optional[str] = Field(None, description="结束日期 (YYYY-MM-DD)")


@tool(args_schema=OrderFilter)
def query_orders(
        status: Optional[OrderStatus] = None,
        min_amount: Optional[float] = None,
        max_amount: Optional[float] = None,
        start_date: Optional[str] = None,
        end_date: Optional[str] = None
) -> str:
    """查询订单列表。"""
    filters = []
    if status:
        filters.append(f"状态={status.value}")
    if min_amount:
        filters.append(f"金额>={min_amount}")
    if max_amount:
        filters.append(f"金额<={max_amount}")
    if start_date:
        filters.append(f"日期>={start_date}")
    if end_date:
        filters.append(f"日期<={end_date}")

    filter_str = ", ".join(filters) if filters else "无过滤条件"
    return f"查询订单: {filter_str}"


def demo_enum_input():
    """演示枚举类型入参"""
    print("\n" + "=" * 60)
    print("📝 示例3：枚举类型入参")
    print("=" * 60)

    # 测试不同的查询条件
    test_cases = [
        {"status": "shipped", "min_amount": 100},
        {"start_date": "2024-01-01", "end_date": "2024-01-31"},
        {"status": "pending", "max_amount": 500}
    ]

    for i, params in enumerate(test_cases, 1):
        result = query_orders.invoke(params)
        print(f"\n测试 {i}: {params}")
        print(f"结果: {result}")


# ========== 4. 结构化出参 (content_and_artifact) ==========
class ProductInfo(BaseModel):
    """产品信息"""
    id: int
    name: str
    price: float
    stock: int
    category: str


class SearchResult(BaseModel):
    """搜索结果"""
    total: int
    products: List[ProductInfo]
    page: int
    page_size: int


class ProductSearchInput(BaseModel):
    """产品搜索输入"""
    keyword: str = Field(description="搜索关键词")
    category: Optional[str] = Field(None, description="产品分类")
    min_price: Optional[float] = Field(None, description="最低价格")
    max_price: Optional[float] = Field(None, description="最高价格")
    page: int = Field(default=1, description="页码", ge=1)
    page_size: int = Field(default=10, description="每页数量", ge=1, le=100)


@tool(
    args_schema=ProductSearchInput,
    response_format="content_and_artifact"
)
def search_products(
        keyword: str,
        category: Optional[str] = None,
        min_price: Optional[float] = None,
        max_price: Optional[float] = None,
        page: int = 1,
        page_size: int = 10
) -> Tuple[str, SearchResult]:
    """搜索产品。"""
    # 模拟数据库查询
    mock_products = [
        ProductInfo(id=1, name="iPhone 15", price=6999, stock=50, category="手机"),
        ProductInfo(id=2, name="MacBook Pro", price=12999, stock=30, category="电脑"),
        ProductInfo(id=3, name="AirPods Pro", price=1999, stock=100, category="耳机"),
    ]

    # 过滤产品
    filtered = mock_products
    if category:
        filtered = [p for p in filtered if p.category == category]
    if min_price:
        filtered = [p for p in filtered if p.price >= min_price]
    if max_price:
        filtered = [p for p in filtered if p.price <= max_price]

    # 构造结果
    result = SearchResult(
        total=len(filtered),
        products=filtered,
        page=page,
        page_size=page_size
    )

    # content: 给 LLM 的简洁描述
    content = f"找到 {result.total} 个产品"
    if category:
        content += f"，分类: {category}"

    # artifact: 完整的结构化数据
    return content, result


def demo_structured_output():
    """演示结构化出参"""
    print("\n" + "=" * 60)
    print("📝 示例4：结构化出参 (content_and_artifact)")
    print("=" * 60)

    llm_with_tools = llm.bind_tools([search_products])

    messages = [HumanMessage(content="搜索价格在5000以上的手机")]

    # LLM 决策
    response = llm_with_tools.invoke(messages)

    if response.tool_calls:
        tool_call = response.tool_calls[0]
        print(f"\n🤖 LLM 决定调用工具:")
        print(f"   工具: {tool_call['name']}")
        print(f"   参数: {tool_call['args']}")

        # 执行工具
        result = search_products.invoke(tool_call)

        print(f"\n✅ 工具执行结果:")
        print(f"   Content: {result.content}")
        print(f"   Artifact 类型: {result.artifact}")
        print(f"   产品数量: {result.artifact.total}")

# ========== 5. 使用 StructuredTool 创建工具 ==========
class CalculatorInput(BaseModel):
    """计算器输入"""
    expression: str = Field(description="数学表达式，如 '2 + 3 * 4'")
    precision: int = Field(default=2, description="小数精度", ge=0, le=10)


class CalculatorOutput(BaseModel):
    """计算器输出"""
    expression: str
    result: float
    formatted_result: str


def calculate_func(expression: str, precision: int = 2) -> Dict[str, Any]:
    """计算数学表达式"""
    try:
        result = eval(expression)
        formatted = f"{result:.{precision}f}"
        return {
            "expression": expression,
            "result": result,
            "formatted_result": formatted
        }
    except Exception as e:
        return {
            "expression": expression,
            "result": 0,
            "formatted_result": f"错误: {str(e)}"
        }


calculator_tool = StructuredTool.from_function(
    func=calculate_func,
    name="calculator",
    description="计算数学表达式",
    args_schema=CalculatorInput,
    return_direct=False
)


def demo_structured_tool():
    """演示 StructuredTool"""
    print("\n" + "=" * 60)
    print("📝 示例5：StructuredTool 创建工具")
    print("=" * 60)

    test_cases = [
        {"expression": "15 + 25 * 3", "precision": 2},
        {"expression": "100 / 3", "precision": 4},
        {"expression": "2 ** 10", "precision": 0}
    ]

    for params in test_cases:
        result = calculator_tool.invoke(params)
        print(f"\n表达式: {params['expression']}")
        print(f"结果: {result}")


# ========== 6. 继承 BaseTool 实现完全自定义 ==========
class WeatherInput(BaseModel):
    """天气查询输入"""
    city: str = Field(description="城市名称")
    date: Optional[str] = Field(None, description="日期 (YYYY-MM-DD)，默认今天")
    unit: str = Field(default="celsius", description="温度单位: celsius 或 fahrenheit")


class WeatherOutput(BaseModel):
    """天气查询输出"""
    city: str
    date: str
    temperature: float
    condition: str
    humidity: int
    wind_speed: float


class WeatherTool(BaseTool):
    name: str = "get_weather"
    description: str = "获取指定城市的天气信息"
    args_schema: type[BaseModel] = WeatherInput
    response_format: str = "content_and_artifact"

    # 自定义属性
    api_key: str = ""

    def _run(
            self,
            city: str,
            date: Optional[str] = None,
            unit: str = "celsius"
    ) -> Tuple[str, WeatherOutput]:
        """同步执行"""
        # 模拟 API 调用
        date = date or datetime.datetime.now().strftime("%Y-%m-%d")

        weather_data = {
            "北京": {"temp": 25, "condition": "晴天", "humidity": 45, "wind": 3.5},
            "上海": {"temp": 28, "condition": "多云", "humidity": 60, "wind": 4.2},
            "广州": {"temp": 32, "condition": "小雨", "humidity": 75, "wind": 2.8},
        }

        data = weather_data.get(city, {"temp": 20, "condition": "未知", "humidity": 50, "wind": 3.0})
        temp = data["temp"]

        if unit == "fahrenheit":
            temp = temp * 9 / 5 + 32

        output = WeatherOutput(
            city=city,
            date=date,
            temperature=temp,
            condition=data["condition"],
            humidity=data["humidity"],
            wind_speed=data["wind"]
        )

        content = f"{city} {date}: {output.condition}, {temp}°{'F' if unit == 'fahrenheit' else 'C'}"

        return content, output

    async def _arun(self, *args, **kwargs):
        """异步执行"""
        return self._run(*args, **kwargs)


def demo_base_tool():
    """演示继承 BaseTool"""
    print("\n" + "=" * 60)
    print("📝 示例6：继承 BaseTool 实现自定义工具")
    print("=" * 60)

    weather_tool = WeatherTool(api_key="your-api-key")

    test_cases = [
        {"city": "北京"},
        {"city": "上海", "unit": "fahrenheit"},
        {"city": "广州", "date": "2024-01-15"}
    ]

    for params in test_cases:
        tool_call = {
            "name": weather_tool.name,
            "args": params,
            "id": "call_123" + params["city"],
            "type": "tool_call"
        }
        result = weather_tool.invoke(tool_call)
        print(f"\n查询: {params}")
        print(f"Content: {result.content}")
        print(f"Artifact: {result.artifact.model_dump()}")


# ========== 7. 完整的 Agent 示例 ==========
def demo_complete_agent():
    """完整的 Agent 示例，结合所有工具"""
    print("\n" + "=" * 60)
    print("📝 示例7：完整 Agent 示例")
    print("=" * 60)

    tools = [
        search_web,
        query_orders,
        search_products,
        calculator_tool,
        WeatherTool(api_key="test-key")
    ]

    llm_tools = llm.bind_tools(tools)
    tool_map = {t.name: t for t in tools}

    # 测试查询
    test_queries = [
        "搜索 Python 教程，返回3条结果",
        "查询已发货的订单，金额在100到500之间",
        "搜索价格在5000以上的手机",
        "计算 (15 + 25) * 3.5，保留3位小数",
        "查询北京的天气"
    ]

    for query in test_queries:
        print(f"\n{'=' * 50}")
        print(f"🙋 用户: {query}")
        print(f"{'=' * 50}")

        messages = [HumanMessage(content=query)]

        max_iterations = 5
        for iteration in range(max_iterations):
            response = llm_tools.invoke(messages)
            messages.append(response)

            if not response.tool_calls:
                print(f"\n🤖 助手: {response.content}")
                break

            for tool_call in response.tool_calls:
                tool_name = tool_call['name']
                tool_args = tool_call['args']
                tool_id = tool_call['id']

                print(f"\n🔧 调用工具: {tool_name}")
                print(f"   参数: {tool_args}")

                tool = tool_map[tool_name]
                result = tool.invoke(tool_args)

                if hasattr(result, 'content'):
                    # content_and_artifact 格式
                    print(f"   结果: {result.content}")
                    if hasattr(result, 'artifact'):
                        print(f"   数据: {result.artifact}")
                    content = result.content
                else:
                    # 普通格式
                    print(f"   结果: {result}")
                    content = str(result)

                messages.append(ToolMessage(
                    content=content,
                    tool_call_id=tool_id
                ))


# ========== 8. 数据验证示例 ==========
class OrderInput(BaseModel):
    """订单输入（带验证）"""
    product_id: int = Field(description="产品ID", gt=0)
    quantity: int = Field(description="数量", ge=1, le=1000)
    price: float = Field(description="单价", gt=0)
    customer_email: str = Field(description="客户邮箱")
    shipping_address: Address = Field(description="收货地址")
    notes: Optional[str] = Field(None, description="备注", max_length=500)

    @field_validator('customer_email')
    def validate_email(cls, v):
        if '@' not in v or '.' not in v:
            raise ValueError('邮箱格式不正确')
        return v.lower()

    @field_validator('price')
    def validate_price(cls, v):
        if v > 1000000:
            raise ValueError('单价不能超过100万')
        return round(v, 2)


class OrderOutput(BaseModel):
    """订单输出"""
    order_id: str
    product_id: int
    quantity: int
    unit_price: float
    total_amount: float
    customer_email: str
    status: str
    created_at: str


@tool(
    args_schema=OrderInput,
    response_format="content_and_artifact"
)
def create_order(
        product_id: int,
        quantity: int,
        price: float,
        customer_email: str,
        shipping_address: Address,
        notes: Optional[str] = None
) -> Tuple[str, OrderOutput]:
    """创建订单。"""
    total = quantity * price
    order_id = f"ORD-{datetime.datetime.now().strftime('%Y%m%d%H%M%S')}"

    output = OrderOutput(
        order_id=order_id,
        product_id=product_id,
        quantity=quantity,
        unit_price=price,
        total_amount=total,
        customer_email=customer_email,
        status="created",
        created_at=datetime.datetime.now().isoformat()
    )

    content = f"订单创建成功！订单号: {order_id}, 总金额: ¥{total:.2f}"

    return content, output


def demo_validation():
    """演示数据验证"""
    print("\n" + "=" * 60)
    print("📝 示例8：数据验证")
    print("=" * 60)

    # 正确的数据
    valid_data = {
        "product_id": 123,
        "quantity": 2,
        "price": 99.99,
        "customer_email": "customer@example.com",
        "shipping_address": {
            "street": "中关村大街1号",
            "city": "北京",
            "country": "中国"
        },
        "notes": "请尽快发货"
    }
    tool_call = {
        "name": "create_order",
        "args": valid_data,
        "id": "call_123",
        "type": "tool_call"
    }

    print("\n✅ 测试有效数据:")
    result = create_order.invoke(tool_call)
    print(f"Content: {result.content}")
    print(f"Artifact: {result.artifact.model_dump()}")

    # 错误的数据
    print("\n\n❌ 测试无效数据:")
    invalid_cases = [
        {**valid_data, "quantity": 0},  # 数量为0
        {**valid_data, "price": -10},  # 负价格
        {**valid_data, "customer_email": "invalid-email"},  # 无效邮箱
    ]

    for i, invalid_data in enumerate(invalid_cases, 1):
        try:
            result = create_order.invoke(invalid_data)
            print(f"测试 {i}: 应该失败但成功了")
        except Exception as e:
            print(f"测试 {i}: 验证失败 - {str(e)}")


# ========== 主函数 ==========
if __name__ == '__main__':
    # 基础示例
    demo_basic_structured_input()
    demo_nested_structured_input()
    demo_enum_input()

    # 结构化输出
    demo_structured_output()
    demo_structured_output2()

    # 不同创建方式
    demo_structured_tool()
    demo_base_tool()

    # 数据验证
    demo_validation()

    # 完整示例
    demo_complete_agent()
