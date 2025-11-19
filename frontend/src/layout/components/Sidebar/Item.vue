<template>
  <div class="menu-item-wrapper">
    <i v-if="icon" :class="icon"></i>
    <span slot="title" class="menu-title">{{ title }}</span>
  </div>
</template>

<script>
export default {
  name: 'MenuItem',
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  }
}
</script>

<style lang="scss" scoped>
.menu-item-wrapper {
  display: inline-block;
  width: 100%;

  .menu-title {
    font-family: 'SimHei', '黑体', 'Microsoft YaHei', sans-serif;
  }
}
</style>
