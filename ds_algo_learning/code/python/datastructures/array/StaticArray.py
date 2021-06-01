class StaticArray:

    def __init__(self, capacity: int):
        self.data = []
        self.capacity = capacity
        self.size = 0
    
    def insert(self, index: int, value):
        if len(self.data) >= self.capacity:
            return None
        self.data.insert(index, value)

    def find(self, index: int):
        try:
            return self.data[index]
        except IndexError:
            return None

    def delete(self, index: int):
        try:
            return self.data.pop(index)
        except IndexError:
            return None
    
    def set(self, index: int, value):
        try:
            self.data[index] = value
        except IndexError:
            return None

    def print_all(self):
        for item in self.data:
            print(item)
    
    def size(self):
        return len(self.data)

    def __len__(self):
        return len(self.data)

def test_array():
    array = StaticArray(5)
    array.insert(0, 3)
    array.insert(0, 4)
    array.insert(1, 5)
    array.insert(3, 9)
    array.insert(3, 10)
    assert array.insert(0, 100) is None
    assert len(array) == 5
    assert array.find(1) == 5
    array.print_all()
    assert array.delete(4) == 9
    array.print_all()

if __name__ == "__main__":
    test_array()

