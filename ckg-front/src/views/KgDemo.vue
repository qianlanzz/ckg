<template>
  <div>
    <div id="kg_container" class="home">
      <KGBuilder1 pid="kg_container" />
    </div>
  </div>
</template>

<script>
import KGBuilder1 from '@/components/KGBuilder1.vue'
export default {
  name: 'KgDemo',
  components: {
    KGBuilder1
  }
}
</script>
<style>
#kg_container {
  height: 100vh;
  width: 100%;
}
#kg_container2 {
  height: 50vh;
  width: 100%;
}
</style>
