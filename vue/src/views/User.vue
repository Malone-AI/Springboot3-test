<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input clearable="" @clear="load" style="width: 240px; margin-right: 5px" v-model="data.username" placeholder="请输入账号查询" :prefix-icon="Search"></el-input>
      <el-input clearable="" @clear="load" style="width: 240px; margin-right: 5px" v-model="data.name" placeholder="请输入名称查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="info" @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px;">
      <el-button type="primary" @click="handlerAdd">新增</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
      <el-upload
        action="http://localhost:9999/user/import"
        style="display: inline-block; margin-left: 10px"
        :show-file-list="false"
        :on-success="handlerImport">
        <el-button type="success">批量导入</el-button>
      </el-upload>
      <el-button type="info" @click="exportData" style="margin-left: 10px">批量导出</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" @selection-change="handlerSelectionChange" style="width: 100%" :header-cell-style="{color: '#333', backgroundColor: '#eaf4FF'}">
        <el-table-column type="selection" width="55"/>
        <el-table-column label="头像">
          <template #default="scope">
            <img v-if="scope.row.avatar" :src="scope.row.avatar" style="width: 40px; height: 40px; border-radius: 50%; display: block"/>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="email" label="邮箱"/>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handlerEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card">
      <el-pagination
        v-model:current-page="data.pageNum"
        v-model:page-size="data.pageSize"
        layout="total, sizes, prev, pagar, next"
        :total="data.total"
        :page-sizes="[5, 10, 20]"
        @current-change="load"
        @size-change="load"
        />
    </div>

    <el-dialog title="新增用户信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="username" label="账号">
          <el-input v-model="data.form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="name" label="名称">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="data.form.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="data.form.email" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="avatar" label="头像">
          <el-upload
            action="http://localhost:9999/files/upload"
            :headers="{token: data.user.token}"
            :on-success="handlerFileSuccess"
            list-type="picture"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  username: null,
  name: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    username: [
      {required: true, message: "请填写账号", trigger: 'blur'}
    ],
    name: [
      {required: true, message: "请填写名称", trigger: 'blur'}
    ],
    phone: [
      {required: true, message: "请填写电话", trigger: 'blur'}
    ],
    email: [
      {required: true, message: "请填写邮箱", trigger: 'blur'}
    ]
  },
  rows: []
})

const formRef = ref()

const load = () => {
  request.get('/user/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      username: data.username
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

load()

const reset = () => {
  data.name = null
  data.username = null
  load()
}

const handlerAdd = () => {
  data.formVisible = true
  data.form = {}
}

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) { // 验证通过时
      request.post("/user/add", data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('新增成功')
          load()
        } else {
          data.formVisible = false
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handlerEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) { // 验证通过时
      request.put("/user/update", data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('修改成功')
          load()
        } else {
          data.formVisible = false
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm("该操作不可逆，您确认删除改行数据吗？", '删除确认', {type: 'warning'}).then(res => {
    request.delete('/user/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const handlerSelectionChange = (rows) => {
  data.rows = rows
}

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择后再进行此操作')
    return
  }
  ElMessageBox.confirm("该操作不可逆，您确认删除吗？", '删除确认', {type: 'warning'}).then(res => {
    request.delete('/user/deleteBatch', {data: data.rows}).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const exportData = () => {
  if (data.rows.length > 0) {
    const ids = data.rows.map(item => item.id)
    let url = `http://localhost:9999/admin/exportBatch?ids=${ids}&token=${data.user.token}`
    window.open(url)
  } else {
    let url = `http://localhost:9999/admin/export?username=${data.username === null ? '' : data.username}&name=${data.name === null ? '' : data.name}`
        + `&token=${data.user.token}`
    window.open(url)
  }
}

const handlerImport = () => {
  ElMessage.success("批量导入成功")
  load()
}

const handlerFileSuccess = (res) => {
  data.form.avatar = res.data
}

</script>