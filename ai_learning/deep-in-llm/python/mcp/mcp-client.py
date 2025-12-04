# client.py
from mcp.client.sse import sse_client
from mcp.client.session import ClientSession

import asyncio

async def main():
    async with sse_client(url="http://localhost:8000/sse") as (read, write) :
        async with ClientSession(read, write) as session:
            await session.initialize()
            while True:
                print("Enter a command:")
                command = input()
                if command == "exit":
                    break
                elif command == "resources":
                    resources = await session.list_resources()
                    print(resources)
                elif command == 'tools':
                    tools = await session.list_tools()
                    print(tools)

if __name__ == "__main__":
    asyncio.run(main())
