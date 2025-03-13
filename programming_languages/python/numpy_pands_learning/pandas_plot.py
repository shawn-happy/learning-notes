import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# plot data
data = pd.Series(np.random.randn(1000), index=np.arange(1000))
data = data.cumsum()

data = pd.DataFrame(np.random.randn(1000, 4), index=np.arange(1000), columns=list("abcd"))
print(data.head())
data = data.cumsum()
ax = data.plot.scatter(x='a', y='b', color='DarkBlue', label='Class 1')
data.plot.scatter(x='c', y='d', color='DarkGreen', label='Class 2', ax=ax)
plt.show()

# bar, hist, box, kde, area, scatter, hexbin, pie
