<template>
  <div>
    <!-- 搜索框 -->
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 280px; margin-right: 10px" v-model="data.courseCode" placeholder="请输入课程编号进行查询" :prefix-icon="Search" @keyup.enter="load" />
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!-- 展示数据 -->
<!--    <div class="card" style="margin-bottom: 5px">-->
<!--      <el-table :data="data.tableData" style="width: 100%;" max-height="500" empty-text="请在上方搜索框中输入课程编号">-->
<!--        <el-table-column class="tighter-column" type="index" label="序号" width="80" />-->
<!--        <el-table-column class="tighter-column" prop="courseName" label="课程名" sortable />-->
<!--        <el-table-column class="tighter-column" prop="courseCode" label="课程编号" sortable />-->
<!--        <el-table-column class="tighter-column" prop="scoreRange" label="成绩区间" sortable />-->
<!--        <el-table-column class="tighter-column" prop="count" label="人数" sortable />-->
<!--      </el-table>-->
<!--    </div>-->

    <!-- ECharts 图表 -->
    <div class="card" style="margin-bottom: 5px">
      <div ref="echartsRef" style="width: 100%; height: 500px;"></div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, reactive, watch } from 'vue';
import * as echarts from 'echarts';
import { Search } from '@element-plus/icons-vue';
import request from '@/utils/request.js';
import { ElMessage } from 'element-plus';

const data = reactive({
  courseName: '',
  courseCode: '',
  tableData: [],
  total: 0,
  pageSize: 10,
  pageNum: 1,
  FormVisible: false,
  form: {},
});

const echartsRef = ref(null);
let myChart = null;

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
        text: '成绩分布',
        left: 'center', // 标题居中
        textStyle: { // 更改标题字体样式
          color: '#333',
          fontStyle: 'normal',
          fontWeight: 'bold',
          fontSize: 18,
        },
      },
      tooltip: {},
      xAxis: {
        type: 'category',
        data: hasData ? data.tableData.map(item => item.scoreRange) : [],
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
          type: 'bar',
          data: hasData ? data.tableData.map(item => item.count) : [],
        },
      ],
      graphic: !hasData
          ? {
            type: 'text',
            left: 'center',
            top: 'middle',
            style: {
              text: '请在上方输入课程编号',
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
  request
      .get('http://localhost:8080/grade/dis/course', {
        params: {
          courseCode: data.courseCode,
        },
      })
      .then(res => {
        data.tableData = res;
        // 如果获取到的数据不为空，则隐藏文本
        if (res && res.length > 0) {
          myChart.setOption({
            graphic: null,
          });
        }
        updateChart();
      });
};

onMounted(() => {
  initChart();
  load();
});

// 重置按钮
const reset = () => {
  data.courseCode = '';
  load();
};
</script>

