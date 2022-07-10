#include <iostream>
#include <thread>

using namespace std;

void sayHi(){
    cout << "Hello World;\n";
}

int main(){
    thread t(sayHi);
    t.join();
}