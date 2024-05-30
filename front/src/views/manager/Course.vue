<template>
  <div>

<!--    搜索框-->
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 260px; margin-right: 10px" v-model="data.name" placeholder="请输入名称进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-input style="width: 260px; margin-right: 10px" v-model="data.courseCode" placeholder="请输入编号进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

<!--    展示数据-->
    <div class="card" style="margin-bottom: 5px">
      <div>
        <el-button type="primary" @click="handelAdd">新增</el-button>
      </div>

      <div>
        <el-table :data="data.tableData" style="width: 100%">
          <el-table-column type="index" label="序号" width="80px" sortable/>
          <el-table-column prop="courseName" label="名称" sortable/>
          <el-table-column prop="courseCode" label="编号" sortable/>
          <el-table-column prop="courseCredit" label="学分" sortable/>
          <el-table-column prop="coursePeriod" label="教学日期" sortable/>
<!--          <el-table-column prop="teacherName" label="任课教师" sortable/>-->

          <el-table-column>
            <template #default="scope">
              <el-button type="primary" @click="handelEdit(scope.row)">编辑</el-button>
              <el-button type="danger" @click="handelDelete(scope.row.courseId)">删除</el-button>
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

    <el-dialog v-model="data.FormVisible" title="课程信息" width="35%">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="名称">
          <el-input v-model="data.form.courseName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="学分">
          <el-input v-model="data.form.courseCredit" autocomplete="off" />
        </el-form-item>
        <el-form-item label="教学日期">
          <el-input v-model="data.form.coursePeriod" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程编号">
          <el-input v-model="data.form.courseCode" autocomplete="off" />
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
    courseCode:'',
    tableData: [],
    total:0, //总数据数量
    pageSize:10, //每页个数
    pageNum:1 ,//当前页码
    FormVisible:false, //是否展示新增信息的弹窗
    form:{

    }
  })

  //拿到分页数据
  const load = () => {
    request.get('http://localhost:8080/course/page',{
      params: {
        pageNum: data.pageNum,
        pageSize:data.pageSize,
        courseName:data.name,
        courseCode:data.courseCode,
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
    data.courseCode = ''
    data.teacher = ''
    load()
  }

  //新增课程信息点击事件
  const handelAdd = () => {
    data.form = {
      courseId:'',
      courseName: '',
      courseCredit: '',
      coursePeriod: '',
      courseCode: ''
    }
    data.FormVisible = true
  }

  //保存新增课程信息
  const saveCourse = () => {
    //由于新增和编辑调用的是同一个弹窗，为了点击保存能产生不一样的效果
    //当新增的时候courseId是空，但当编辑的时候就不是空的
    //新增
    if(data.form.courseId === ''){
      request.post("http://localhost:8080/course/insert",data.form).then(res => {
        console.log(res)
        if(res === 201){
          ElMessage.error('操作失败')
        } else if(res === 202){
          ElMessage.error('没有此教师')
        } else {
          load()  //重新加载数据
          data.FormVisible = false  //隐藏弹窗
          ElMessage.success('操作成功')
        }

      })
    } else {    //编辑
      request.post('http://localhost:8080/course/update',data.form).then(res => {
        if(res === 201) {
          ElMessage.error('没有此课程')
        } else if(res === 202) {
          ElMessage.error('没有此教师')
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
  const handelDelete = (courseId) => {
    //弹出二次确认
    ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', {type : 'warning'}).then(res => {
      request.post('http://localhost:8080/course/delete',courseId).then(res => {
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