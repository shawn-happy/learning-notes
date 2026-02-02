from openai import OpenAI
from dotenv import load_dotenv

load_dotenv()
client = OpenAI()


response = client.responses.create(
    model="gpt-5",
    input="一句话介绍你自己"
)

print(response.output_text)

response = client.chat.completions.create(
    model="gpt-5",
    messages=[
        {"role": "user", "content": "Hello world!"}
    ]
)

print(response.choices[0].message.content)