import numpy as np

a = np.arange(4)
b = a
c = a
d = b
print(a)
a[0] = 4
print(a)
print(b is a)

print(b)

d[1:3] = [22, 33]
print(d)
print(a)
print(c)


b = a.copy() # deep copy
print(b)
a[3] = 44
print(a)
print(b)