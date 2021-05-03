<style>
label {
  float: left;
}
</style>

<template>
  <div id="app">
    <div class="container">
      <div class="d-flex flex-column">
        <div v-if="logined && (userDetails.authorityType === 'Admin' || userDetails.authorityType === 'Company')">
          <!-- ========== Header starts ========== -->
          <header>
            <div class="p-2">
              <p class="text-right">[logined]&nbsp;{{user}}&nbsp;様</p>
            </div>
            <div class="p-2">
              <div class="breadcrumb">
                <router-link to="/">
                  <li v-if="$route.path === '/'" class="breadcrumb-item active">Home</li>
                  <li v-else>Home</li>
                </router-link>
                <div aria-hidden="true">/</div>
                <router-link to="/employee">
                  <li v-if="$route.path === '/employee'" class="breadcrumb-item active">Employee</li>
                  <li v-else>Employee</li>
                </router-link>
              </div>
            </div>
          </header>
          <!-- ========== /Header ends ========== -->
          <!-- ========== Contents starts ========== -->
          <router-view />
          <!-- ========== /Contents ends ========== -->
          <!-- ========== Footer starts ========== -->
          <footer></footer>
          <!-- ========== /Footer ends ========== -->
        </div>
        <div v-if="!logined || userDetails.authorityType === 'Employee'">
          <App />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
axios.defaults.baseURL = "http://localhost:8080/hos-web/eim/api";
import App from "./DefaultApp.vue";
import OpUserDetails from "@/api/operator";
export default {
  components: {
    App
  },
  data() {
    return {
      logined: false,
      userDetails: {
        lastName: null,
        firstName: null,
        authorityType: null
      }
    };
  },
  mounted() {
    OpUserDetails.logined(v => {
      this.logined = v.data;
    }),
      OpUserDetails.getOperatorInfo(v => {
        this.userDetails = v.data;
      });
  },
  computed: {
    user() {
      return this.userDetails.lastName + " " + this.userDetails.firstName;
    }
  }
};
</script>