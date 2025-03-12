import numpy as np

a = np.array([1, 1, 1])
b = np.array([2, 2, 2])

# vertical stack
print(np.vstack((a, b)))
print(np.vstack((a, b)).shape)

# horizontal stack
print(np.hstack((a, b)))
print(np.hstack((a, b)).shape)

print(a.T)
print(a[np.newaxis, :])
print(a[:, np.newaxis])
print(b[:, np.newaxis])
print(np.vstack((a[:, np.newaxis], b[:, np.newaxis])))
print(np.hstack((a[:, np.newaxis], b[:, np.newaxis])))

# print(np.concatenate((a, b, b, a), axis=1))

c = np.reshape(np.arange(12), (3, 4))
print(c)
print(np.split(c, 2, axis=1))
print(np.split(c, 3, axis=0))

print(np.array_split(c, 2, axis=1))

print(np.vsplit(c, 3))
print(np.hsplit(c, 2))
