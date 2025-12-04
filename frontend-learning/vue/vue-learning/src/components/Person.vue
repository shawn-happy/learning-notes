<!-- <template>
    <div class="person">
        <h2>姓名: {{ name }}</h2>
        <h2>年龄: {{ age }}</h2>
        <button @click="changeName">修改姓名</button>
        <button @click="changeAge">修改年龄</button>
        <button @click="getPwd">查看密码</button>
    </div>
</template>

<script lang="ts">
// options 模式
    // export default {
    //     data() {
    //         return {
    //             name: '张三',
    //             age: 18,
    //             pwd: "123456"
    //         }
    //     },
    //     methods: {
    //         changeName() {
    //             this.name = '张三1'
    //         },
    //         changeAge() {
    //             this.age = 19
    //         },
    //         getPwd() {
    //             alert(this.pwd)
    //         }
    //     }
    // }

// composition 模式
export default {
   
}
</script>

<script lang="ts" setup>

import { ref } from 'vue'
 // 不是响应式数据
        let name = ref("张三")
        let age = ref(18)
        let pwd = ref("123456")
        function changeName() {
            name.value = '张三1'
        }
        function changeAge() {
            age.value = 19
        }
        function getPwd() {
            alert(pwd.value)
        }
    
</script>

<style>

</style> -->


<template>
    <div>
        姓：<input type="text" v-model="firstName"> <br>
        名：<input type="text" v-model="lastName"> <br>
        全名：<span>{{ fullName }}</span> <br>
        <button @click="changeFullName">全名改为：li-si</button>
    </div>
</template>


<script lang="ts" setup>
import {ref,computed} from 'vue'

  let firstName = ref('zhang')
  let lastName = ref('san')

  // 计算属性——只读取，不修改
  /* let fullName = computed(()=>{
    return firstName.value + '-' + lastName.value
  }) */


  // 计算属性——既读取又修改
  let fullName = computed({
    // 读取
    get(){
      return firstName.value + '-' + lastName.value
    },
    // 修改
    set(val){
      console.log('有人修改了fullName',val)
      firstName.value = val.split('-')[0]
      lastName.value = val.split('-')[1]
    }
  })

  function changeFullName(){
    fullName.value = 'li-si'
  } 
</script>