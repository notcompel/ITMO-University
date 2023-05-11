<template>
  <div class="enter form-box">
    <div class="header">Register</div>
    <div class="body">
      <form @submit.prevent="onRegister">
        <div class="field">
          <div class="name">
            <label for="login">Login</label>
          </div>
          <div class="value">
            <input autofocus id="login" name="login" v-model="login"/>
          </div>
        </div>
        <div class="field">
          <div class="name">
            <label for="name">Password</label>
          </div>
          <div class="value">
            <input type="password" id="name" name="name" v-model="password"/>
          </div>
        </div>
        <div class="field error">{{ error }}</div>
        <div class="button-field">
          <input type="submit" value="Register">
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data: function () {
    return {
      login: "",
      password: "",
      error: ""
    }
  },
  methods: {
    onRegister: function () {
      this.error = "";
      this.$root.$emit("onRegister", this.login, this.password);
    }
  },
  beforeMount() {
    this.$root.$on("onRegisterValidationError", error => this.error = error);
  }
}
</script>

<style scoped>

</style>