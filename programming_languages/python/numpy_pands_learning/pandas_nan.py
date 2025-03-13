import pandas as pd
import numpy as np

data = pd.date_range("20250313", periods=6)
df = pd.DataFrame(np.reshape(np.arange(24), (6, 4)), index=data, columns=["a", "b", "c", "d"])
df.iloc[0, 1] = np.nan
df.iloc[1, 2] = np.nan
print(df)

# print(df.dropna(axis=0, how="any"))  # how={'any', 'all'}

# print(df.fillna(value=0))

print(np.any(df.isnull()) == True)
