<template>
  <div>

    <!--    搜索框-->
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px; margin-right: 10px" v-model="data.name" placeholder="请输入名称进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-input style="width: 260px; margin-right: 10px" v-model="data.course" placeholder="请输入课程进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-input style="width: 260px" v-model="data.teacher" placeholder="请输入教师进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!--    展示数据-->
    <div class="card" style="margin-bottom: 10px">
      <div>
        <el-button type="primary" @click="handelAdd">新增</el-button>
      </div>

      <div>
        <el-table :data="data.tableData" style="width: 100%">
          <el-table-column type="index" label="序号" width="80px" />
          <el-table-column prop="className" label="名称" sortable/>
          <el-table-column prop="classId" label="编号" sortable/>
          <el-table-column prop="semester" label="学期" sortable/>
          <el-table-column prop="courseName" label="课程" sortable/>
          <el-table-column prop="teacherName" label="教师" sortable/>
          <el-table-column prop="totalStudents" label="总人数" sortable/>

          <el-table-column>
            <template #default="scope">
              <el-button type="primary" @click="handelEdit(scope.row)">编辑</el-button>
              <el-button type="danger" @click="handelDelete(scope.row.classId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

    </div>

    <!--    分页-->
    <div class="card">
      <el-pagination  v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                      :page-sizes="[10, 20, 30, 40]"  @size-change="handleSizeChange"
                      @current-change="handelCurrentChange"
                      background layout="sizes, prev, pager, next" :total="data.total"
      />
    </div>

    <el-dialog v-model="data.FormVisible" title="班级信息" width="35%">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="名称">
          <el-input v-model="data.form.className" autocomplete="off" />
        </el-form-item>
        <el-form-item label="学期">
          <el-input v-model="data.form.semester" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程">
          <el-input v-model="data.form.courseName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="教师">
          <el-input v-model="data.form.teacherName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="总人数">
          <el-input v-model="data.form.totalStudents" autocomplete="off" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.FormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveCourse">保 存</el-button>
        </div>
      </template>

    </el-dialog>

  </div>
</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js"
import {ElMessage} from "element-plus";

const data = reactive({
  name: '',
  course:'',
  teacher:'',
  tableData: [],
  total:0, //总数据数量
  pageSize:10, //每页个数
  pageNum:1 ,//当前页码
  FormVisible:false, //是否展示新增信息的弹窗
  form:{}
})

//拿到分页数据
const load = () => {
  request.get('http://localhost:8080/class/page',{
    params: {
      pageNum: data.pageNum,
      pageSize:data.pageSize,
      courseName:data.course,
      className:data.name,
      teacherName:data.teacher
    }
  }).then(res => {

    //res为空的时候这两个赋值就为空
    data.tableData = res?.records || []
    data.total = res?.total || []
  })
}

//调用方法获取后台分页数据
load()

//翻页
const handelCurrentChange = (pageNum) => {
  //当翻页的时候，重新加载数据即可（pageNum在上面已经绑定了，不用再主动赋值
  load()
}
//更改每页显示数量
const handleSizeChange = (pageSize) => {
  //更改每页显示的时候和上面一样
  load()
}
//重置按钮
const reset = () => {
  data.name = ''
  data.course = ''
  data.teacher = ''
  load()
}

//新增课程信息点击事件
const handelAdd = () => {
  data.form = {
    classId:'',
    className: '',
    totalStudents: '',
    semester: '',
    courseName: '',
    teacherName:''
  }
  data.FormVisible = true
}

//保存新增课程信息
const saveCourse = () => {
  //由于新增和编辑调用的是同一个弹窗，为了点击保存能产生不一样的效果
  //当新增的时候classId是空，但当编辑的时候就不是空的
  //新增
  console.log(data.form)
  if(data.form.classId === ''){
    console.log('111')
    request.post("http://localhost:8080/class/insert",data.form).then(res => {
      console.log(res)
      if(res === 203){
        ElMessage.error('操作失败')
      } else if(res === 201){
        ElMessage.error('没有此教师')
      } else if(res === 202) {
        ElMessage.error('没有此课程')
      } else if(res === 204) {
        ElMessage.error('班级名重复')
      } else {
        load()  //重新加载数据
        data.FormVisible = false  //隐藏弹窗
        ElMessage.success('操作成功')
      }

    })
  } else {    //编辑
    console.log('222')
    request.post('http://localhost:8080/class/update',data.form).then(res => {
      if(res === 201) {
        ElMessage.error('没有此教师')
      } else if(res === 202) {
        ElMessage.error('没有此班级')
      } else if(res === 203) {
        ElMessage.error('没有此课程')
      } else if(res === 204) {
        ElMessage.error('班级名重复')
      }  else {
        load()  //重新加载
        data.FormVisible = false
        ElMessage.success('操作成功')
      }
    })
  }

}

//编辑
const handelEdit = (row) => {
  //深拷贝
  data.form = JSON.parse(JSON.stringify(row))
  data.FormVisible = true
}

//删除
const handelDelete = (classId) => {
  //弹出二次确认
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', {type : 'warning'}).then(res => {
    console.log(classId)
    request.post('http://localhost:8080/class/delete',classId).then(res => {
      if(res === 201) {
        ElMessage.error('没有此课程')
      } else {
        load()  //重新加载
        ElMessage.success('操作成功')
      }
    })
  }).catch(res => {})

}

</script>