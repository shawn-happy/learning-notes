import asyncio

from google.genai import types
from google.adk.agents.llm_agent import Agent
from google.adk.models.lite_llm import LiteLlm
from google.adk.sessions.in_memory_session_service import InMemorySessionService
from google.adk.runners import Runner
from adk_learning.common.utils import Colors, process_agent_response
import datetime

def get_current_time():
    return datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")

root_agent = Agent(
    model=LiteLlm(model="openai/Qwen/Qwen3-8B"),
    name='root_agent',
    description='A helpful assistant for user questions.',
    instruction=
    """
    You are a helpful assistant that can use the following tools:
    - get_current_time
    """,
    tools=[get_current_time]
)

APP_NAME = "basic-agent"
SESSION_ID = "session-1"
USER_ID = "user-1"

session_service = InMemorySessionService()

initial_state = {}

async def call_agent_async(runner, user_id, session_id, query):
    """Call the agent asynchronously with the user's query."""
    content = types.Content(role="user", parts=[types.Part(text=query)])
    print(
        f"\n{Colors.BG_GREEN}{Colors.BLACK}{Colors.BOLD}--- Running Query: {query} ---{Colors.RESET}"
    )
    final_response_text = None
    agent_name = None


    try:
        async for event in runner.run_async(
            user_id=user_id, session_id=session_id, new_message=content
        ):
            # Capture the agent name from the event if available
            if event.author:
                agent_name = event.author

            response = await process_agent_response(event)
            if response:
                final_response_text = response
    except Exception as e:
        print(f"{Colors.BG_RED}{Colors.WHITE}ERROR during agent run: {e}{Colors.RESET}")

    return final_response_text
async def main_async():
    # Setup constants
    APP_NAME = "Customer Support"
    USER_ID = "aiwithbrandon"

    # ===== PART 3: Session Creation =====
    # Create a new session with initial state
    new_session = await session_service.create_session(
        app_name=APP_NAME,
        user_id=USER_ID,
        state=initial_state,
    )
    SESSION_ID = new_session.id
    print(f"Created new session: {SESSION_ID}")

    # ===== PART 4: Agent Runner Setup =====
    # Create a runner with the main customer service agent
    runner = Runner(
        agent=root_agent,
        app_name=APP_NAME,
        session_service=session_service,
    )

    # ===== PART 5: Interactive Conversation Loop =====
    print("\nWelcome to Customer Service Chat!")
    print("Type 'exit' or 'quit' to end the conversation.\n")

    while True:
        # Get user input
        user_input = input("You: ")

        # Check if user wants to exit
        if user_input.lower() in ["exit", "quit"]:
            print("Ending conversation. Goodbye!")
            break

        # Process the user query through the agent
        await call_agent_async(runner, USER_ID, SESSION_ID, user_input)

    # ===== PART 6: State Examination =====
    # Show final session state
    final_session = session_service.get_session(
        app_name=APP_NAME, user_id=USER_ID, session_id=SESSION_ID
    )
    print("\nFinal Session State:")
    for key, value in final_session.state.items():
        print(f"{key}: {value}")


def main():
    """Entry point for the application."""
    asyncio.run(main_async())


if __name__ == "__main__":
    main()
