import pandas as pd


def mergeByOneKey():
    left = pd.DataFrame({
        "key": ["k0", "k1", "k2", "k3"],
        "a": ["a0", "a1", "a2", "a3"],
        "b": ["b0", "b1", "b2", "b3"],
    })

    right = pd.DataFrame({
        "key": ["k0", "k1", "k2", "k3"],
        "c": ["c0", "c1", "c2", "c3"],
        "d": ["d0", "d1", "d2", "d3"],
    })

    res = pd.merge(left, right, on="key")
    print(res)


def mergeByTwoKey():
    left = pd.DataFrame({
        "key1": ["k0", "k1", "k2", "k3"],
        "key2": ["k0", "k1", "k2", "k3"],
        "a": ["a0", "a1", "a2", "a3"],
        "b": ["b0", "b1", "b2", "b3"],
    })

    right = pd.DataFrame({
        "key1": ["k0", "k1", "k0", "k2"],
        "key2": ["k1", "k1", "k0", "k3"],
        "c": ["c0", "c1", "c2", "c3"],
        "d": ["d0", "d1", "d2", "d3"],
    })

    # how = ["left", "right", "outer", "inner"]
    # indicator = True
    res = pd.merge(left, right, on=["key1", "key2"])
    print(res)


def mergeByIndex():
    left = pd.DataFrame({
        "a": ["a0", "a1", "a2", "a3"],
        "b": ["b0", "b1", "b2", "b3"]
    }, index=["k0", "k1", "k2", "k3"])

    right = pd.DataFrame({
        "c": ["c0", "c1", "c2", "c3"],
        "d": ["d0", "d1", "d2", "d3"],
    }, index=["k0", "k1", "k3", "k4"])

    # how = ["left", "right", "outer", "inner"]
    # indicator = True
    # suffixes
    res = pd.merge(left, right, left_index=True, right_index=True, how="inner")
    print(res)


mergeByIndex()

# join
