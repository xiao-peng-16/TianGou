<template>
    <div>
        <div class="box">
          <img :src="userPhotonameURL">
        </div>
    </div>
</template>

<script>
    export default {
        name: "changeUserPhotoname",
      computed:{
        userPhotonameURL() {
          return this.$store.getters.getUserPhotonameURL();
        }
      }
    }
</script>

<style scoped>
  .box{

    margin-left: 25%;
  }

  img{
    width: 300px;
  }
</style>
