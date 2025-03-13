# pandas 选择数据
import pandas as pd
import numpy as np

data = pd.date_range("20250313", periods=6)
df = pd.DataFrame(np.reshape(np.arange(24), (6, 4)), index=data, columns=["a", "b", "c", "d"])
print(df)

print(df['a'], df.a)
print(df[0:3], df['20250313': '20250315'])

# select by label: loc
print(df.loc['20250315'])
print(df.loc[:, ["a", "c"]])
print(df.loc['20250313', ["a", "c"]])

# select by position: iloc
print(df.iloc[3, 1])
print(df.iloc[3:5, 1:3])
print(df.iloc[[1, 3, 5], 1:3])

# boolean indexing
print(df[df.a > 8])
