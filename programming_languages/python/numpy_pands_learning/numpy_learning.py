import numpy as np

array = np.array([
    [1, 2, 3, 4],
    [5, 6, 7, 8]
], dtype=np.int32)

print(array)
print(type(array))
print(array.shape)
print(array.dtype)
print(array.size)
print(array.itemsize)
print(array.ndim)

shap = np.array([(1, 2), (3, 4)], dtype=[('x', 'i4'), ('y', 'i4')])
print(shap)

zero_array = np.zeros(shape=(2, 3), dtype=np.float32)
print(zero_array)

one_array = np.ones(shape=(3, 4), dtype=np.int32)
print(one_array)

range_array = np.arange(10, 20, 2, dtype=np.float32)
print(range_array)


range_array = np.arange(12, dtype=np.float32)
reshape_ary = np.reshape(range_array, (3, 4))
print(reshape_ary)

line_array = np.linspace(1, 10, 6, dtype=np.int32)
print(line_array)
