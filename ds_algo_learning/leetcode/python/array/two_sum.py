from typing import List


class TwoSum:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        pass


class TwoSumSolutionOne(TwoSum):
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        for i in range(0, len(nums)):
            for j in range(i + 1, len(nums)):
                if target == nums[i] + nums[j]:
                    return [i, j]
        return []


class TwoSumSolutionTwo(TwoSum):
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        map = {}
        for i in range(0, len(nums)):
            map[target - nums[i]] = i

        for i in range(0, len(nums)):
            if nums[i] in map and map[nums[i]] != i:
                return [map[nums[i]], i]

        return []


class TwoSumSolutionThree(TwoSum):
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        map = {}
        for i in range(0, len(nums)):
            if nums[i] in map:
                return [map[nums[i]], i]
            map[target-nums[i]] = i
        return []


class TwoSumSolutionFour(TwoSum):
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        copy_nums = nums.copy()
        nums.sort()
        indices = [1] * 2
        left, right = 0, len(nums) - 1
        while left < right:
            num = nums[left] + nums[right]
            if num == target:
                indices[0] = left
                indices[1] = right
                break
            if num < target:
                left += 1
            elif num > target:
                right -= 1
        
        for i in range(0, len(nums)):
            if nums[indices[0]] == copy_nums[i]:
                indices[0] = i
                break

        for i in range(len(nums) - 1, -1, -1):
            if nums[indices[1]] == copy_nums[i]:
                indices[1] = i
                break
        return indices


if __name__ == '__main__':
    test_cases = [[2, 7, 11, 15], [3, 2, 4], [3, 3]]
    targets = [9, 6, 6]

    print("Approach 1: Brute Force: ")
    sum = TwoSumSolutionOne()
    for i in range(0, len(test_cases)):
        nums = test_cases[i]
        target = targets[i]
        result = sum.twoSum(nums=nums, target=target)
        print(result)

    print("Approach 2: Two Loop With Hash Table:")
    sum = TwoSumSolutionTwo()
    for i in range(0, len(test_cases)):
        nums = test_cases[i]
        target = targets[i]
        result = sum.twoSum(nums=nums, target=target)
        print(result)

    print("Approach 3: One Loop With Hash Table:")
    sum = TwoSumSolutionThree()
    for i in range(0, len(test_cases)):
        nums = test_cases[i]
        target = targets[i]
        result = sum.twoSum(nums=nums, target=target)
        print(result)

    print("Approach 4: Binary Search:")
    sum = TwoSumSolutionFour()
    for i in range(0, len(test_cases)):
        nums = test_cases[i]
        target = targets[i]
        result = sum.twoSum(nums=nums, target=target)
        print(result)
