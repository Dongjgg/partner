<script setup>
import request from "@/utils/request";

const load = () => {
  request.get('/user').then(res => {
    console.log(res.data)
  })
}


</script>

<template>
  <div>
    主页
  </div>

</template>

