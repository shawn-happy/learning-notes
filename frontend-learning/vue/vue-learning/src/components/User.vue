<template>
    <div>
        <h2>用户姓名：{{ user.name }}</h2>
        <h2>用户年龄：{{ user.age }}</h2>
        <h2>用户性别：{{ user.sex }}</h2>
        <button @click="changeName">修改用户姓名</button>

        <table>
            <tr>
                <th>姓名</th>
                <th>年龄</th>
                <th>性别</th>
            </tr>
            <tr v-for="user in users" :key="user.id">
                <td>{{ user.name }}</td>
                <td>{{ user.age }}</td>
                <td>{{ user.sex }}</td>
            </tr>
        </table>
        
    </div>
</template>

<script lang="ts" setup>
    import { reactive } from 'vue'
    import {ref} from 'vue'

    let user = reactive({
        id: 1,
        name: '张三',
        age: 18,
        sex: '男'
    })

    let users = reactive([
        user,
        {
            id: 2,
            name: 'wangwu',
            age: 21,
            sex: '男'
        },
        {
            id: 3,
            name: 'juice',
            age: 18,
            sex: '女'
        }
    ])


    function changeName() {
        user.name = '王五'
    }


</script>


<style scoped>

</style>