<style scoped>
.table {
  text-align: center;
}
.modal {
  display: block;
}
</style>

<template>
  <div class="l-view-employee">
    <div class="container">
      <div class="d-flex flex-column">
        <div class="p-2">
          <div class="float-left">
            <!-- Button trigger modal -->
            <button
              class="btn btn-primary"
              v-on:click.prevent.self="modal.reg=true"
              v-if="userDetails.authorityType === 'Company'"
            >アカウント登録</button>
          </div>
        </div>
        <div class="p-2">
          <table class="table">
            <thead>
              <tr>
                <th>#</th>
                <th>名前</th>
                <th>メールアドレス</th>
                <th>利用状況</th>
                <th></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in items" :key="item.id">
                <th scope="row">{{item.employeeId}}</th>
                <td>{{item.lastName}}&nbsp;{{item.firstName}}</td>
                <td>{{item.mail}}</td>
                <td>{{item.statusType | statusType}}</td>
                <td>
                  <!-- Button trigger modal -->
                  <button
                    class="btn btn-outline-info"
                    v-on:click.prevent.self="modal.detail=true"
                    v-on:click="showDetail(item)"
                  >詳細</button>
                </td>
                <td v-if="userDetails.authorityType === 'Company'">
                  <button
                    class="btn btn-danger btn-block"
                    v-on:click="inactivate(item)"
                    v-if="item.statusType === 'Active'"
                    :disabled="item.employeeId === userDetails.employeeId"
                  >利用停止</button>
                  <button
                    class="btn btn-outline-danger btn-block"
                    v-on:click="activate(item)"
                    v-if="item.statusType === 'Inactive' || item.statusType === 'AccountLock'"
                  >利用停止解除</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div v-if="modal.reg">
      <div class="modal" v-on:click.prevent.self="modal.reg=false">
        <div class="modal-dialog modal-dialog-scrollable">
          <div class="modal-content">
            <div class="modal-header">
              <h3 class="modal-title">RegEmployeeForm</h3>
              <button class="close">
                <span aria-hidden="true" v-on:click.prevent.self="modal.reg=false">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <RegEmployeeForm />
            </div>
            <div class="modal-footer">
              <button class="btn btn-outline-secondary" v-on:click="modal.reg=false">閉じる</button>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop show"></div>
    </div>

    <!-- Modal -->
    <div v-if="modal.detail">
      <div class="modal" v-on:click.prevent.self="modal.detail=false">
        <div class="modal-dialog modal-dialog-scrollable">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="font-weight-bolder">従業員情報詳細</h5>
              <button class="close">
                <span aria-hidden="true" v-on:click="modal.detail=false">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <div class="p-2">
                <label>従業員ID</label>
                <input class="form-control" readonly v-model="selectedItem.employeeId" />
              </div>
              <div class="p-2">
                <label>メールアドレス</label>
                <input class="form-control" readonly v-model="selectedItem.mail" />
              </div>
              <div class="p-2">
                <label>姓</label>
                <input class="form-control" readonly v-model="selectedItem.lastName" />
              </div>
              <div class="p-2">
                <label>名</label>
                <input class="form-control" readonly v-model="selectedItem.firstName" />
              </div>
              <div class="p-2">
                <label>姓&nbsp;(カナ)</label>
                <input class="form-control" readonly v-model="selectedItem.lastNameKana" />
              </div>
              <div class="p-2">
                <label>名&nbsp;(カナ)</label>
                <input class="form-control" readonly v-model="selectedItem.firstNameKana" />
              </div>
              <div class="p-2">
                <label>生年月日</label>
                <input class="form-control" readonly v-model="selectedItem.listBirthday" />
              </div>
              <div class="p-2">
                <label>出身地</label>
                <input class="form-control" readonly v-model="selectedItem.birthplace" />
              </div>
              <div class="p-2">
                <label>性別</label>
                <input class="form-control" readonly v-model="selectedItem.listSex" />
              </div>
              <div class="p-2">
                <label>入社日</label>
                <input class="form-control" readonly v-model="selectedItem.listHireDate" />
              </div>
              <div class="p-2">
                <label>部</label>
                <input class="form-control" readonly v-model="selectedItem.departmentName" />
              </div>
              <div class="p-2">
                <label>課</label>
                <input class="form-control" readonly v-model="selectedItem.sectionName" />
              </div>
              <div class="p-2">
                <label>班</label>
                <input class="form-control" readonly v-model="selectedItem.groupName" />
              </div>
              <div class="p-2">
                <label>役職</label>
                <input class="form-control" readonly v-model="selectedItem.position" />
              </div>
              <div class="p-2" v-if="userDetails.authorityType === 'Company'">
                <label>権限</label>
                <input class="form-control" readonly v-model="selectedItem.authorityType" />
              </div>
            </div>
            <div class="modal-footer">
              <!-- Button trigger modal -->
              <button
                class="btn btn-outline-primary btn-block"
                v-on:click.prevent.self="modal.ed=true, modal.detail=false"
                v-on:click="showEdit()"
                v-if="userDetails.authorityType === 'Company' && selectedItem.employeeId !== userDetails.employeeId"
              >編集</button>
              <button
                class="btn btn btn-primary btn-block"
                v-on:click.prevent.self="modal.detail=false"
              >閉じる</button>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop show"></div>
    </div>

    <!-- Modal -->
    <div v-if="modal.ed">
      <div class="modal" v-on:click.prevent.self="modal.ed=false">
        <div class="modal-dialog modal-dialog-scrollable">
          <div class="modal-content">
            <div class="modal-header">
              <h3 class="modal-title">ChgEmployeeForm</h3>
              <button class="close">
                <span aria-hidden="true" v-on:click.prevent.self="modal.ed=false">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <ChgEmployeeForm :editedItem="this.editedItem"></ChgEmployeeForm>
            </div>
            <div class="modal-footer">
              <button
                class="btn btn-secondary"
                v-on:click.prevent.self="modal.detail=true, modal.ed=false"
              >キャンセル</button>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop show"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import OpUserDetails from "@/api/operator";
import ChgEmployeeForm from "@/views/employee/ChgEmployeeForm.vue";
import RegEmployeeForm from "@/views/employee/RegEmployeeForm.vue";
export default {
  components: {
    ChgEmployeeForm,
    RegEmployeeForm
  },
  data() {
    return {
      userDetails: {
        authorityType: null
      },
      items: [],
      modal: {
        reg: false,
        detail: false,
        ed: false
      },
      editedItem: null
    };
  },
  mounted() {
    OpUserDetails.getOperatorInfo(v => {
      this.userDetails = v.data;
      axios
        .get("/employee", {
          params: {
            employee_id: this.userDetails.employeeId
          }
        })
        .then(v => (this.items = v.data), console.log("同期中"))
        .catch(error => {
          console.log(error);
        });
    });
  },
  methods: {
    activate(item) {
      if (confirm("サービス利用の停止を解除します。")) {
        const params = new URLSearchParams();
        params.append("employeeId", item.employeeId);
        params.append("statusType", "Active");
        const config = {
          headers: { "Content-Type": "application/x-www-form-urlencoded" }
        };
        axios
          .post("/employee/activate", params, config)
          .then(response => {
            if (response.data === "success") {
              this.$router.go({ path: this.$router.currentRoute.path });
            }
          })
          .catch(error => {
            console.log(error);
          });
      }
    },
    inactivate(item) {
      if (confirm("サービス利用を停止します。")) {
        const params = new URLSearchParams();
        params.append("employeeId", item.employeeId);
        params.append("statusType", "Inactive");
        const config = {
          headers: { "Content-Type": "application/x-www-form-urlencoded" }
        };
        axios
          .post("/employee/activate", params, config)
          .then(response => {
            if (response.data === "success") {
              this.$router.go({ path: this.$router.currentRoute.path });
            }
          })
          .catch(error => {
            console.log(error);
          });
      }
    },
    showDetail(item) {
      this.selectedItem = item;
    },
    showEdit() {
      this.editedItem = this.defaultItem();
    },
    defaultItem() {
      return {
        employeeId: this.selectedItem.employeeId,
        mail: this.selectedItem.mail,
        lastName: this.selectedItem.lastName,
        firstName: this.selectedItem.firstName,
        lastNameKana: this.selectedItem.lastNameKana,
        firstNameKana: this.selectedItem.firstNameKana,
        birthday: this.selectedItem.crudBirthday,
        birthplace: this.selectedItem.birthplace,
        sex: this.selectedItem.crudSex,
        hireDate: this.selectedItem.crudHireDate,
        departmentCode: this.selectedItem.departmentCode,
        sectionCode: this.selectedItem.sectionCode,
        groupCode: this.selectedItem.groupCode,
        positionValue: this.selectedItem.positionValue,
        authorityType: this.selectedItem.authorityType
      };
    }
  }
};
</script>