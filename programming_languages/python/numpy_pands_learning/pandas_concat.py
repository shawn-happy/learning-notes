import pandas as pd
import numpy as np

df_1 = pd.DataFrame(np.ones((3, 4)) * 0, columns=["a", "b", "c", "d"])
df_2 = pd.DataFrame(np.ones((3, 4)) * 1, columns=["a", "b", "c", "d"])
df_3 = pd.DataFrame(np.ones((3, 4)) * 2, columns=["a", "b", "c", "d"])

# print(df_1)
# print(df_2)
# print(df_3)

# 0 y , 1, x
res = pd.concat([df_1, df_2, df_3], axis=0, ignore_index=True)
# print(res)

res = pd.concat([df_1, df_2, df_3], axis=1)
# print(res)

df_1 = pd.DataFrame(np.ones((3, 4)) * 0, columns=["a", "b", "c", "d"], index=[1, 2, 3])
df_2 = pd.DataFrame(np.ones((3, 4)) * 1, columns=["b", "c", "d", "e"], index=[2, 3, 4])
print(df_1)
print(df_2)
res = pd.concat([df_1, df_2], join='outer')
print(res)

res = pd.concat([df_1, df_2], join='inner', ignore_index=True)
print(res)

res = pd.concat([df_1, df_2.reindex(df_1.index)])
print(res)

res = pd.concat([df_1, df_2.reindex(df_1.index)], axis=1)
print(res)

res = df_1.append(df_2, ignore_index=True)
print(res)