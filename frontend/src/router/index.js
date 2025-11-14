import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/layout/index.vue'
import { getToken } from '@/utils/auth'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    hidden: true,
    meta: { noAuth: true }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'el-icon-s-home' }
      }
    ]
  },
  {
    path: '/archive',
    component: Layout,
    children: [
      {
        path: 'list',
        name: 'ArchiveList',
        component: () => import('@/views/archive/list.vue'),
        meta: { title: '档案列表', icon: 'el-icon-folder' }
      }
    ]
  },
  {
    path: '/patient',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'create',
        name: 'PatientCreate',
        component: () => import('@/views/patient/create.vue'),
        meta: { title: '患者建档', icon: 'el-icon-user' }
      },
      {
        path: 'health-record',
        name: 'HealthRecord',
        component: () => import('@/views/patient/health-record.vue'),
        meta: { title: '健康档案' }
      },
      {
        path: 'disease-record',
        name: 'DiseaseRecord',
        component: () => import('@/views/patient/disease-record.vue'),
        meta: { title: '专病档案' }
      },
      {
        path: 'enrollment-assessment',
        name: 'EnrollmentAssessment',
        component: () => import('@/views/patient/enrollment-assessment.vue'),
        meta: { title: '入组评估' }
      }
    ]
  },
  {
    path: '/scheme',
    component: Layout,
    meta: { title: '方案管理', icon: 'el-icon-document' },
    children: [
      {
        path: 'list',
        name: 'SchemeList',
        component: () => import('@/views/scheme/list.vue'),
        meta: { title: '方案列表' }
      },
      {
        path: 'create',
        name: 'SchemeCreate',
        component: () => import('@/views/scheme/create.vue'),
        meta: { title: '创建方案' },
        hidden: true
      },
      {
        path: 'config-patient',
        name: 'SchemeConfigPatient',
        component: () => import('@/views/scheme/config-patient.vue'),
        meta: { title: '患者管理方案配置' },
        hidden: true
      },
      {
        path: 'query',
        name: 'SchemeQuery',
        component: () => import('@/views/scheme/query.vue'),
        meta: { title: '方案查询' }
      },
      {
        path: 'center',
        name: 'SchemeCenter',
        component: () => import('@/views/scheme/center.vue'),
        meta: { title: '方案中心' }
      }
    ]
  },
  {
    path: '/equipment',
    component: Layout,
    children: [
      {
        path: 'list',
        name: 'EquipmentList',
        component: () => import('@/views/equipment/list.vue'),
        meta: { title: '设备列表', icon: 'el-icon-monitor' }
      }
    ]
  },
  {
    path: '/prescription',
    component: Layout,
    meta: { title: '运动处方管理', icon: 'el-icon-postcard' },
    children: [
      {
        path: 'config',
        name: 'PrescriptionConfig',
        component: () => import('@/views/prescription/config.vue'),
        meta: { title: '运动项目配置' }
      },
      {
        path: 'list',
        name: 'PrescriptionList',
        component: () => import('@/views/prescription/list.vue'),
        meta: { title: '运动处方' }
      },
      {
        path: 'execution',
        name: 'PrescriptionExecution',
        component: () => import('@/views/prescription/execution.vue'),
        meta: { title: '处方执行' }
      }
    ]
  },
  {
    path: '/assessment',
    component: Layout,
    meta: { title: '评定管理', icon: 'el-icon-document-checked' },
    children: [
      {
        path: 'list',
        name: 'AssessmentList',
        component: () => import('@/views/assessment/list.vue'),
        meta: { title: '评定列表' }
      },
      {
        path: 'execution',
        name: 'AssessmentExecution',
        component: () => import('@/views/assessment/execution.vue'),
        meta: { title: '评定执行' }
      }
    ]
  },
  {
    path: '/scheduling',
    component: Layout,
    meta: { title: '排课管理', icon: 'el-icon-date' },
    children: [
      {
        path: 'classroom',
        name: 'ClassroomConfig',
        component: () => import('@/views/scheduling/classroom.vue'),
        meta: { title: '教室配置' }
      },
      {
        path: 'schedule',
        name: 'Schedule',
        component: () => import('@/views/scheduling/schedule.vue'),
        meta: { title: '排课' }
      },
      {
        path: 'reservation',
        name: 'Reservation',
        component: () => import('@/views/scheduling/reservation.vue'),
        meta: { title: '预约' }
      },
      {
        path: 'timetable',
        name: 'Timetable',
        component: () => import('@/views/scheduling/timetable.vue'),
        meta: { title: '课表' }
      },
      {
        path: 'execution',
        name: 'ReservationExecution',
        component: () => import('@/views/scheduling/execution.vue'),
        meta: { title: '预约执行' }
      }
    ]
  },
  {
    path: '/followup',
    component: Layout,
    meta: { title: '随访管理', icon: 'el-icon-phone' },
    children: [
      {
        path: 'project',
        name: 'FollowupProject',
        component: () => import('@/views/followup/project.vue'),
        meta: { title: '随访项目' }
      },
      {
        path: 'plan',
        name: 'FollowupPlan',
        component: () => import('@/views/followup/plan.vue'),
        meta: { title: '随访计划' }
      },
      {
        path: 'list',
        name: 'FollowupList',
        component: () => import('@/views/followup/list.vue'),
        meta: { title: '随访列表' }
      }
    ]
  },
  {
    path: '/education',
    component: Layout,
    meta: { title: '健康宣教管理', icon: 'el-icon-reading' },
    children: [
      {
        path: 'image-library',
        name: 'ImageLibrary',
        component: () => import('@/views/education/image-library.vue'),
        meta: { title: '图文素材库' }
      },
      {
        path: 'video-library',
        name: 'VideoLibrary',
        component: () => import('@/views/education/video-library.vue'),
        meta: { title: '视频素材库' }
      },
      {
        path: 'video-add',
        name: 'VideoAdd',
        component: () => import('@/views/education/video-add.vue'),
        meta: { title: '新增视频' },
        hidden: true
      },
      {
        path: 'content-add',
        name: 'ContentAdd',
        component: () => import('@/views/education/content-add.vue'),
        meta: { title: '新增图文' },
        hidden: true
      },
      {
        path: 'content-management',
        name: 'ContentManagement',
        component: () => import('@/views/education/content-management.vue'),
        meta: { title: '内容应用管理' }
      },
      {
        path: 'content-config',
        name: 'ContentConfig',
        component: () => import('@/views/education/content-config.vue'),
        meta: { title: '内容配置' },
        hidden: true
      }
    ]
  },
  {
    path: '/communication',
    component: Layout,
    meta: { title: '医患沟通平台', icon: 'el-icon-chat-line-square' },
    children: [
      {
        path: 'chat',
        name: 'Chat',
        component: () => import('@/views/communication/chat.vue'),
        meta: { title: '消息聊天' }
      },
      {
        path: 'file-share',
        name: 'FileShare',
        component: () => import('@/views/communication/file-share.vue'),
        meta: { title: '文件共享' }
      }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404.vue'),
    hidden: true
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫：登录验证
router.beforeEach((to, from, next) => {
  const token = getToken()

  // 白名单：不需要认证的页面
  const whiteList = ['/login', '/404']

  // 如果在白名单中，直接放行
  if (whiteList.includes(to.path)) {
    next()
    return
  }

  // 如果没有token，跳转到登录页
  if (!token) {
    next('/login')
    return
  }

  // 有token，正常访问
  next()
})

export default router
