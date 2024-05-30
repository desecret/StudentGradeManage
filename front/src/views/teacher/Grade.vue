<template>
  <div>

    <!--    搜索框-->
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 260px; margin-right: 10px" v-model="data.studentName" placeholder="请输入学生姓名进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-input style="width: 260px; margin-right: 10px" v-model="data.studentId" placeholder="请输入学生学号进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-button type="primary"  @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!--    展示数据-->
    <div class="card" style="margin-bottom: 5px">
      <div>
        <el-button type="primary" @click="handelAdd">新增</el-button>
      </div>

      <div>
        <el-table :data="data.tableData" style="width: 100%;" max-height="500">
          <el-table-column class="tighter-column" type="index" label="序号" width="80" />
          <el-table-column class="tighter-column" prop="name" label="姓名" width="100" sortable/>
          <el-table-column class="tighter-column" prop="studentId" label="学号" sortable/>
          <el-table-column prop="courseName" label="课程" sortable/>
          <el-table-column prop="courseCode" label="课程编号" sortable/>
          <el-table-column prop="regularScore" label="平时成绩" sortable/>
          <el-table-column prop="midScore" label="期中成绩" sortable/>
          <el-table-column prop="finalScore" label="期末成绩" sortable/>
          <el-table-column prop="totalScore" label="总成绩" sortable/>

          <el-table-column width="150">
            <template #default="scope">
              <el-button type="primary" @click="handelEdit(scope.row)" size="small">编辑</el-button>
              <el-button type="danger" @click="handelDelete(scope.row.gradeId)" size="small">删除</el-button>
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

    <el-dialog v-model="data.FormVisible" title="成绩信息" width="35%">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 40px"  ref="formRef" :rules="rules">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="data.form.studentId" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程名" prop="courseName" >
          <el-input v-model="data.form.courseName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程编号" prop="courseCode">
          <el-input v-model="data.form.courseCode" autocomplete="off" />
        </el-form-item>
        <el-form-item label="平时成绩" prop="regularScore">
          <el-input v-model="data.form.regularScore" autocomplete="off" />
        </el-form-item>
        <el-form-item label="期中成绩" prop="midScore">
          <el-input v-model="data.form.midScore" autocomplete="off" />
        </el-form-item>
        <el-form-item label="期末成绩" prop="finalScore">
          <el-input v-model="data.form.finalScore" autocomplete="off" />
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
  studentId: '',     //学号
  studentName:'', //学生名
  tableData: [],  //展示成绩数据集合
  total:0, //总数据数量
  pageSize:10, //每页个数
  pageNum:1 ,//当前页码
  FormVisible:false, //是否展示新增信息的弹窗
  form:{}   //弹窗中数据集合
})

//拿到分页数据
const load = () => {
  console.log(JSON.parse(localStorage.getItem('teacher-user-id') || '{}'))
  request.get('http://localhost:8080/grade/page',{
    params: {
      pageNum: data.pageNum,
      pageSize:data.pageSize,
      teacherId:JSON.parse(localStorage.getItem('teacher-user-id') || '{}'),
      studentName:data.studentName,
      studentId:data.studentId
    }
  }).then(res => {
    console.log(res)
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
  data.studentName = ''
  data.studentId = ''
  load()
}

//弹窗中表单校验
const rules = reactive({
  name: [
    { required: true, message: '姓名不能为空', trigger: 'blur' }
  ],
  studentId: [
    { required: true, message: '学号不能为空', trigger: 'blur' }
  ],
  courseName: [
    { required: true, message: '课程名不能为空', trigger: 'blur' }
  ],
  courseCode: [
    { required: true, message: '课程编号不能为空', trigger: 'blur' }
  ],
  regularScore: [
    { required: true, message: '平时成绩不能为空', trigger: 'blur' }
  ],
  midScore: [
    { required: true, message: '期中成绩不能为空', trigger: 'blur' }
  ],
  finalScore: [
    { required: true, message: '期末成绩不能为空', trigger: 'blur' }
  ]
})
const formRef = ref()


//新增课程信息点击事件
const handelAdd = () => {
  data.form = {
    gradeId:'',   //用来判断是新增操作还是编辑操作
    courseId:'',  //主键
    name: '',
    courseName: '',
    regularScore: '',
    midScore: '',
    finalScore:'',
    courseCode:'', //编号
    className:'',
    //用来插入成绩的时候根据courseCode和teacherId在teachingClass表中找到ClassId插入grade表
    teacherId:JSON.parse(localStorage.getItem('teacher-user-id') || '')
  }
  data.FormVisible = true
}

//保存新增课程信息
const saveCourse = () => {
  //由于新增和编辑调用的是同一个弹窗，为了点击保存能产生不一样的效果
  //当新增的时候courseId是空，但当编辑的时候就不是空的
  //新增
  if(data.form.gradeId === ''){
    request.post("http://localhost:8080/grade/insert",data.form).then(res => {
      if(res === 201){
        ElMessage.error('操作失败')
      } else if(res === 202){
        ElMessage.error('没有此学生')
      } else if(res === 203){
        ElMessage.error('没有此班级')
      } else if(res === 204){
        ElMessage.error('课程名或课程编号错误')
      } else if(res === 205){
        ElMessage.error('课程名或班级名错误')
      } else {
        load()  //重新加载数据
        data.FormVisible = false  //隐藏弹窗
        ElMessage.success('操作成功')
      }

    })
  } else {    //编辑
    console.log(data.form.gradeId)
    console.log('222')
    request.post('http://localhost:8080/grade/update',data.form).then(res => {
      if(res === 201) {
        ElMessage.error('操作失败')
      } else if(res === 202) {
        ElMessage.error('没有此学生')
      } else if(res === 203){
        ElMessage.error('没有此班级')
      } else if(res === 204){
        ElMessage.error('课程名或课程编号错误')
      } else if(res === 205){
        ElMessage.error('课程名或班级名错误')
      } else {
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
const handelDelete = (gradeId) => {
  console.log('gradeId:  ' + gradeId)
  //弹出二次确认
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', {type : 'warning'}).then(res => {
    request.post('http://localhost:8080/grade/delete',gradeId).then(res => {
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

<style scoped>
.tighter-column {
  padding: 0 5px !important; /* 调整这个值以改变列之间的间距 */
}
</style>