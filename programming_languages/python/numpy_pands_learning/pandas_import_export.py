import pandas as pd

data = pd.read_csv('user.csv')
print(data)

pd.to_pickle(data, 'user.pickle')