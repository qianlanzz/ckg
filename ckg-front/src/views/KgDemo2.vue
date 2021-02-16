<template>
  <div>
    <div id="kg_container" class="home">
      <KGBuilderPath pid="kg_container" />
    </div>
  </div>
</template>

<script>
import KGBuilderPath from '@/components/KGBuilderPath.vue'
export default {
  data(){
    return{
      search: ""
    }
  },
  name: 'KgDemo2',
  components: {
    KGBuilderPath
  },
}
</script>
<style>
#kg_container {
  height: 100vh;
  width: 100%;
}
</style>
