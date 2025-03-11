import numpy as np

if __name__ == '__main__':
    a = np.array([1, 3, 5, 6])
    b = np.arange(0, 10, 3, dtype=np.int32)

    print(a)
    print(b)
    print(a - b)
    print(a + b)

    # 平方计算
    print(b ** 2)

    # 三角函数
    print(np.sin(a), np.cos(a), np.tan(a))

    print(b < 4)

    c = np.array([[1, 1], [1, 0]])
    d = np.reshape(np.arange(4), (2, 2))
    print(c, d)
    print(c * d)
    print(np.dot(c, d))
    print(c.dot(d))

    e = np.random.random((2, 4))
    print(e)
    print(e.sum())
    # 1表示行
    print(np.sum(e, axis=1))
    print(e.min())
    # 0表示列
    print(np.min(e, axis=0))
    print(e.max())
    print(np.max(e, axis=1))

    # 返回索引
    print("=========")
    print(a)
    print(np.argmin(a))
    print(np.argmax(a))

    # 求平均
    print(a.mean())
    print(np.mean(a))
    print(np.average(a))

    # 中位数
    print(np.median(a))

    # 累加
    print(np.cumsum(a))

    # 累差
    print(np.diff(a))

    # 输出非0的index
    print(a.nonzero())
    print(np.nonzero(a))

    # 排序
    print(a.sort())
    print(np.sort(a))

    # 转置
    print(a.transpose())
    print(a.T)
    print(np.transpose(a))

    # 小于4的都替换成4
    # 大于8的都替换成8
    print(a.clip(4, 8))
    print(np.clip(a, 4, 8))

    matrix = np.array([[1, 2, 3],
                       [4, 5, 6],
                       [7, 8, 9]])

    # 计算矩阵的秩
    rank = np.linalg.matrix_rank(matrix)

    print(f"矩阵的秩为: {rank}")
