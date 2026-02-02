import os
from dotenv import load_dotenv
from langchain_openai import ChatOpenAI

# 加载环境变量
load_dotenv()
api_key = os.getenv('OPENAI_API_KEY')
base_url = os.getenv('OPENAI_BASE_URL')

reasoning = {
    "effort": "medium"
}

reasoning_model = ChatOpenAI(
    model="gpt-5.2",
    temperature=0.7,
    reasoning=reasoning
)

for chunk in reasoning_model.stream("大模型的原理"):
    reasoning_steps = [r for r in chunk.content_blocks if r["type"] == "reasoning"]
    print(reasoning_steps if reasoning_steps else chunk.text)