<template>
    <div>
        <h2>部门名称：{{ dept.name }}</h2>
        <h2>部门编码: {{ dept.code }}</h2>
        <h2>部门负责人: {{ dept.manager }}</h2>
        <button @click="changeName">修改部门姓名</button>

        <table>
            <tr>
                <th>部门名称</th>
                <th>部门编码</th>
                <th>部门负责人</th>
            </tr>
            <tr v-for="dept in depts" :key="dept.id">
                <td>{{ dept.name }}</td>
                <td>{{ dept.code }}</td>
                <td>{{ dept.manager }}</td>
                <td><button @click="removeDept(dept.id)">删除</button></td>
                <td><button @click="editDept(dept.id, dept.name)">编辑</button></td>
            </tr>
        </table>

        <button @click="addDept">添加部门</button>

    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, toRefs, toRef } from 'vue'

const dept = reactive({
    id: 1,
    name: 'IT部',
    code: 'IT001',
    manager: '张三'
})

// let {name, code} = dept.value
// console.log(name, code)
let {name, code} = toRefs(dept)

let manager = toRef(dept, 'manager')
console.log(name.value, code.value, manager.value)


const depts = ref([
    dept, // ref 引用的对象，想要获取数据，必须用 .value
    {
        id: 2,
        name: '运维部',
        code: 'IT002',
        manager: '李四'
    },
    {
        id: 3,
        name: '测试部',
        code: 'IT003',
        manager: '王五'
    }
])

function changeName() {
    // dept.value.name = '开发部'
    name.value = '开发部'
    console.log(name.value)
}

function editDept(id: number, name: string) {
    const index = depts.value.findIndex(d => d.id === id)
    if (index != -1) {
        depts.value[index].name = name + 1
    }
}
function removeDept(id: number) {
    const index = depts.value.findIndex(d => d.id === id)
    if (index != -1) {
        depts.value.splice(index, 1)
    }
}

function addDept() {
    depts.value.push({
        id: 4,
        name: '财务部',
        code: 'IT004',
        manager: '赵六'
    })
}




</script>