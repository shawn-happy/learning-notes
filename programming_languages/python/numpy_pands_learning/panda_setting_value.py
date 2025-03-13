import pandas as pd
import numpy as np

data = pd.date_range("20250313", periods=6)
df = pd.DataFrame(np.reshape(np.arange(24), (6, 4)), index=data, columns=["a", "b", "c", "d"])
print(df)

df.iloc[2, 2] = 22
print(df)

df.loc['20250315', 'b'] = 33
print(df)

df[df.a > 8] = 10
print(df)

df.a[df.a < 8] = 0
print(df)

df["e"] = np.nan
print(df)

df["f"] = pd.Series(np.arange(6), index=data)
print(df)
