<template>
  <div>
    <!-- 搜索框 -->
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 200px; margin-right: 10px" v-model="data.studentName" placeholder="请输入学生姓名进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-input style="width: 200px; margin-right: 10px" v-model="data.studentId" placeholder="请输入学生学号进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-input style="width: 200px; margin-right: 10px" v-model="data.courseName" placeholder="请输入课程名进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-input style="width: 200px; margin-right: 10px" v-model="data.courseCode" placeholder="请输入课程编号进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-input style="width: 200px; margin-right: 10px" v-model="data.className" placeholder="请输入教学班名进行查询" :prefix-icon="Search" @keyup.enter="load"/>
      <el-button type="primary"  @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!-- 展示数据 -->
    <div class="card" style="margin-bottom: 5px">
      <div>
        <el-button type="primary" @click="handelAdd">新增</el-button>
        <el-button type="info" @click="generateData">生成数据</el-button>
        <el-button type="success" @click="showChart">显示图表</el-button>
      </div>

      <div>
        <el-table :data="data.tableData" style="width: 100%;" max-height="500">
          <el-table-column class="tighter-column" type="index" label="序号" width="80" />
          <el-table-column class="tighter-column" prop="name" label="姓名" width="100" sortable/>
          <el-table-column class="tighter-column" prop="studentId" label="学号" sortable/>
          <el-table-column prop="courseName" label="课程" sortable/>
          <el-table-column prop="courseCode" label="课程编号" sortable/>
          <el-table-column prop="className" label="教学班名" sortable/>
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

    <!-- 图表 -->
    <div class="card" v-if="showChartFlag" style="margin-bottom: 5px" >
      <div ref="echartsRef" style="width: 100%; height: 500px;"></div>
    </div>

    <!-- 分页 -->
    <div class="card">
      <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     :page-sizes="[10, 20, 30, 40]" @size-change="handleSizeChange"
                     @current-change="handelCurrentChange"
                     background layout="sizes, prev, pager, next" :total="data.total"
      />
    </div>

    <el-dialog v-model="data.FormVisible" title="成绩信息" width="35%">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 40px" ref="formRef" :rules="rules">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="data.form.studentId" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程名" prop="courseName">
          <el-input v-model="data.form.courseName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程编号" prop="courseCode">
          <el-input v-model="data.form.courseCode" autocomplete="off" />
        </el-form-item>
        <el-form-item label="教学班名" prop="className">
          <el-input v-model="data.form.className" autocomplete="off" />
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
          <el-button @click="data.FormVisible = false">取消</el-button>
          <el-button type="primary" @click="saveCourse">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import * as echarts from 'echarts';
import { Search } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";

const data = reactive({
  studentId: '',
  studentName: '',
  courseName: '',
  courseCode: '',
  classId: '',
  className: '',
  tableData: [],
  total: 0,
  pageSize: 10,
  pageNum: 1,
  FormVisible: false,
  form: {}
});

const echartsRef = ref(null);
let myChart = null;
const showChartFlag = ref(false);

// 初始化 ECharts
const initChart = () => {
  if (echartsRef.value) {
    myChart = echarts.init(echartsRef.value);
  }
};

// 更新 ECharts 数据
const updateChart = () => {
  if (myChart) {
    const hasData = data.tableData.length > 0;
    const option = {
      color: ['#3398DB'], // 更改主题颜色
      title: {
        text: '成绩信息',
        left: 'center', // 标题居中
        textStyle: { // 更改标题字体样式
          color: '#333',
          fontStyle: 'normal',
          fontWeight: 'bold',
          fontSize: 18,
        },
      },
      tooltip: {
        trigger: 'axis',
        formatter: function(params) {
          const dataItem = data.tableData[params[0].dataIndex];
          return `姓名：${dataItem.name}<br/>课程：${dataItem.courseName}
                    <br/>总成绩：${dataItem.totalScore}`;
        }
      },
      legend: { // 添加图例
        data: ['总成绩'],
        right: '10%', // 图例靠右显示
      },
      xAxis: {
        type: 'category',
        data: hasData ? data.tableData.map(item => item.name) : [],
        axisLine: { // 更改轴线样式
          lineStyle: {
            color: '#888',
            width: 1,
          },
        },
      },
      yAxis: {
        type: 'value',
        axisLine: { // 更改轴线样式
          lineStyle: {
            color: '#888',
            width: 1,
          },
        },
      },
      grid: { // 更改网格样式
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      series: [
        {
          name: '总成绩', // 与图例对应
          type: 'bar',
          data: hasData ? data.tableData.map(item => item.totalScore) : [],
        },
      ],
      graphic: !hasData
          ? {
            type: 'text',
            left: 'center',
            top: 'middle',
            style: {
              text: '暂无数据',
              fontSize: 20,
              fill: '#888',
            },
          }
          : null,
    };
    myChart.setOption(option);
  }
};

// 监听表格数据变化并更新图表
watch(
    () => data.tableData,
    () => {
      updateChart();
    }
);

// 获取数据并更新表格和图表
const load = () => {
  request.get('http://localhost:8080/grade/all', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      studentId: data.studentId,
      studentName: data.studentName,
      courseName: data.courseName,
      courseCode: data.courseCode,
      className: data.className
    }
  }).then(res => {
    data.tableData = res?.records || [];
    data.total = res?.total || 0;
  });
};

// 显示/隐藏图表
const showChart = () => {
  showChartFlag.value = !showChartFlag.value;
  if (showChartFlag.value) {
    nextTick(() => {
      initChart();
      updateChart();
    });
  }
};

// 调用方法获取后台分页数据
load();

// 翻页
const handelCurrentChange = (pageNum) => {
  load();
};

// 更改每页显示数量
const handleSizeChange = (pageSize) => {
  load();
};

// 重置按钮
const reset = () => {
  data.studentName = '';
  data.studentId = '';
  data.courseCode = '';
  data.courseName = '';
  data.classId = '';
  data.className = '';
  load();
};

// 弹窗中表单校验
const rules = reactive({
  name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
  studentId: [{ required: true, message: '学号不能为空', trigger: 'blur' }],
  courseName: [{ required: true, message: '课程名不能为空', trigger: 'blur' }],
  courseCode: [{ required: true, message: '课程编号不能为空', trigger: 'blur' }],
  regularScore: [{ required: true, message: '平时成绩不能为空', trigger: 'blur' }],
  midScore: [{ required: true, message: '期中成绩不能为空', trigger: 'blur' }],
  finalScore: [{ required: true, message: '期末成绩不能为空', trigger: 'blur' }],
  className: [{ required: true, message: '班级名不能为空', trigger: 'blur' }]
});
const formRef = ref();

// 新增课程信息点击事件
const handelAdd = () => {
  data.form = {
    gradeId: '',
    courseId: '',
    name: '',
    courseName: '',
    regularScore: '',
    midScore: '',
    finalScore: '',
    courseCode: '',
    classId: '',
    className: '',
    teacherId: -1
  };
  data.FormVisible = true;
};

// 保存新增课程信息
const saveCourse = () => {
  if (data.form.gradeId === '') {
    request.post("http://localhost:8080/grade/insert", data.form).then(res => {
      if (res === 201) {
        ElMessage.error('操作失败');
      } else {
        load();
        data.FormVisible = false;
        ElMessage.success('操作成功');
      }
    });
  } else {
    request.post('http://localhost:8080/grade/update', data.form).then(res => {
      if (res === 201) {
        ElMessage.error('操作失败');
      } else {
        load();
        data.FormVisible = false;
        ElMessage.success('操作成功');
      }
    });
  }
};

// 生成数据
const generateData = () => {
  request.get('http://localhost:8080/data').then(res => {
    if (res === 200) {
      ElMessage.success('生成成功');
      load();
    } else {
      ElMessage.error('生成失败');
    }
  });
};

// 编辑
const handelEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.FormVisible = true;
};

// 删除
const handelDelete = (gradeId) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(() => {
    request.post('http://localhost:8080/grade/delete', gradeId).then(res => {
      if (res === 201) {
        ElMessage.error('没有此课程');
      } else {
        load();
        ElMessage.success('操作成功');
      }
    });
  });
};

// 初始化 ECharts
onMounted(() => {
  nextTick(() => {
    if (showChartFlag.value) {
      initChart();
      updateChart();
    }
  });
});
</script>

<style scoped>
.tighter-column {
  padding: 0 5px !important;
}
</style>
