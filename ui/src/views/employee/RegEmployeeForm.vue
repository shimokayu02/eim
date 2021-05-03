<template>
  <div class="l-view-reg-employee-form">
    <div class="container">
      <div class="d-flex flex-column">
        <div class="p-2">
          <form onsubmit="return false;">
            <div class="form-group">
              <label>メールアドレス</label>
              <input
                class="form-control"
                placeholder="HERMES@example.com"
                v-model="form.mail"
                maxlength="255"
              />
              <label class="text-danger">
                <label class="text-dark" v-if="failure.mail != null" aria-hidden="true">※</label>
                {{failure.mail}}
              </label>
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>姓</label>
              <label aria-hidden="true">｜※</label>
              <label v-if="failure.lastName == null" class="text-primary">必須</label>
              <label class="text-danger">{{failure.lastName}}</label>
              <input class="form-control" v-model="form.lastName" maxlength="30" />
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>名</label>
              <label aria-hidden="true">｜※</label>
              <label v-if="failure.firstName == null" class="text-primary">必須</label>
              <label class="text-danger">{{failure.firstName}}</label>
              <input class="form-control" v-model="form.firstName" maxlength="30" />
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>姓&nbsp;(カナ)</label>
              <label aria-hidden="true">｜※</label>
              <label v-if="failure.lastNameKana == null" class="text-primary">必須</label>
              <label class="text-danger">{{failure.lastNameKana}}</label>
              <input class="form-control" v-model="form.lastNameKana" maxlength="80" />
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>名&nbsp;(カナ)</label>
              <label aria-hidden="true">｜※</label>
              <label v-if="failure.firstNameKana == null" class="text-primary">必須</label>
              <label class="text-danger">{{failure.firstNameKana}}</label>
              <input class="form-control" v-model="form.firstNameKana" maxlength="80" />
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>生年月日</label>
              <Datepicker placeholder="選択してください。" v-model="form.birthday" :bootstrap-styling="true"></Datepicker>
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>出身地</label>
              <select class="custom-select" v-model="form.birthplace">
                <option></option>
                <option
                  v-for="item in prefecture"
                  :key="item.key"
                  :value="item.value"
                >{{item.value}}</option>
              </select>
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>性別</label>
              <select class="custom-select" v-model="form.sex">
                <option></option>
                <option value="MALE">男</option>
                <option value="FEMALE">女</option>
              </select>
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>入社日</label>
              <Datepicker placeholder="選択してください。" v-model="form.hireDate" :bootstrap-styling="true"></Datepicker>
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>部</label>
              <select
                class="custom-select"
                v-model="form.departmentCode"
                v-on:click="clearSectionCodeAndGroupCode()"
              >
                <option></option>
                <option value="01">総務部</option>
                <option value="02">人事教育部</option>
                <option value="03">システム営業部</option>
                <option value="04">システム開発部</option>
                <option value="05">技術翻訳部</option>
              </select>
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>課</label>
              <select
                class="custom-select"
                v-model="form.sectionCode"
                v-on:click="clearGroupCode()"
              >
                <option></option>
                <option v-if="form.departmentCode === '01'" value="01">総務課</option>
                <option v-if="form.departmentCode === '01'" value="02">経理課</option>
                <option v-if="form.departmentCode === '03'" value="01">システム営業課</option>
              </select>
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>班</label>
              <select class="custom-select" v-model="form.groupCode">
                <option></option>
              </select>
            </div>
          </form>
          <form onsubmit="return false;">
            <div class="form-group">
              <label>役職</label>
              <select class="custom-select" v-model="form.positionValue">
                <option value="10"></option>
                <option value="40">部長</option>
                <option value="30">課長</option>
                <option value="20">主任</option>
              </select>
            </div>
          </form>
        </div>
        <div class="p-2">
          <button
            class="btn btn-primary btn-block"
            v-on:click.prevent.self="save()"
            v-on:click="clearErrorMsg()"
            :disabled="form.lastName === null || form.lastName === ''
            || form.firstName === null || form.firstName === ''
            || form.lastNameKana === null || form.lastNameKana === ''
            || form.firstNameKana === null || form.firstNameKana === ''"
          >登録</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Datepicker from "vuejs-datepicker";
import { Prefecture } from "@/enums";
export default {
  components: {
    Datepicker
  },
  data() {
    return {
      form: {
        employeeId: null,
        mail: null,
        lastName: null,
        firstName: null,
        lastNameKana: null,
        firstNameKana: null,
        birthday: null,
        birthplace: null,
        sex: "",
        hireDate: null,
        departmentCode: null,
        sectionCode: null,
        groupCode: null,
        positionValue: "10"
      },
      failure: {
        mail: null,
        lastName: null,
        firstName: null,
        lastNameKana: null,
        firstNameKana: null
      }
    };
  },
  computed: {
    prefecture() {
      const list = Object.keys(Prefecture).map(v => ({
        key: v,
        value: Prefecture[v]
      }));
      return list;
    }
  },
  methods: {
    clearSectionCodeAndGroupCode() {
      this.form.sectionCode = null;
      this.form.groupCode = null;
    },
    clearGroupCode() {
      this.form.groupCode = null;
    },
    clearErrorMsg() {
      this.failure.mail = null;
      this.failure.lastName = null;
      this.failure.firstName = null;
      this.failure.lastNameKana = null;
      this.failure.firstNameKana = null;
    },
    save() {
      const params = new URLSearchParams();
      Object.keys(this.form).forEach(key => params.append(key, this.form[key]));
      const config = {
        headers: { "Content-Type": "application/x-www-form-urlencoded" }
      };
      axios
        .post("/employee/save", params, config)
        .then(response => {
          if (response.data === "success") {
            this.$router.go({ path: this.$router.currentRoute.path });
          }
        })
        .catch(error => {
          console.log(error);
          this.failure = error.response.data;
        });
    }
  }
};
</script>