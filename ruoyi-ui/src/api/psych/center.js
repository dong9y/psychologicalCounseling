import request from '@/utils/request'

export function getStudentDashboard() {
  return request({ url: '/psych/student/dashboard', method: 'get' })
}

export function submitEmotion(data) {
  return request({ url: '/psych/student/emotion', method: 'post', data })
}

export function listEmotionRecords(query) {
  return request({ url: '/psych/student/emotion/list', method: 'get', params: query })
}

export function listScales(query) {
  return request({ url: '/psych/student/scale/list', method: 'get', params: query })
}

export function getScaleDetail(scaleId) {
  return request({ url: '/psych/student/scale/' + scaleId, method: 'get' })
}

export function submitAssessment(data) {
  return request({ url: '/psych/student/assessment', method: 'post', data })
}

export function listAssessmentRecords(query) {
  return request({ url: '/psych/student/assessment/list', method: 'get', params: query })
}

export function listCounselors(query) {
  return request({ url: '/psych/student/counselor/list', method: 'get', params: query })
}

export function createAppointment(data) {
  return request({ url: '/psych/student/appointment', method: 'post', data })
}

export function listStudentAppointments(query) {
  return request({ url: '/psych/student/appointment/list', method: 'get', params: query })
}

export function evaluateAppointment(appointmentId, data) {
  return request({ url: '/psych/student/appointment/' + appointmentId + '/evaluate', method: 'put', data })
}

export function getStudentProfile() {
  return request({ url: '/psych/student/profile', method: 'get' })
}

export function listStudentMessages(limit) {
  return request({ url: '/psych/student/messages', method: 'get', params: { limit } })
}

export function getCounselorDashboard() {
  return request({ url: '/psych/counselor/dashboard', method: 'get' })
}

export function listCounselorWarnings(query) {
  return request({ url: '/psych/counselor/warning/list', method: 'get', params: query })
}

export function getWarningDetail(warningId) {
  return request({ url: '/psych/counselor/warning/' + warningId, method: 'get' })
}

export function handleWarning(data) {
  return request({ url: '/psych/counselor/warning', method: 'put', data })
}

export function listTransferOptions() {
  return request({ url: '/psych/counselor/transfer/options', method: 'get' })
}

export function listCounselorAppointments(query) {
  return request({ url: '/psych/counselor/appointment/list', method: 'get', params: query })
}

export function reviewAppointment(appointmentId, data) {
  return request({ url: '/psych/counselor/appointment/' + appointmentId + '/review', method: 'put', data })
}

export function getStudentSummary(studentUserId) {
  return request({ url: '/psych/counselor/student/' + studentUserId, method: 'get' })
}

export function getAdminDashboard() {
  return request({ url: '/psych/admin/dashboard', method: 'get' })
}

export function listAdminScales(query) {
  return request({ url: '/psych/admin/scale/list', method: 'get', params: query })
}

export function addScale(data) {
  return request({ url: '/psych/admin/scale', method: 'post', data })
}

export function updateScale(data) {
  return request({ url: '/psych/admin/scale', method: 'put', data })
}

export function listAdminWarnings(query) {
  return request({ url: '/psych/admin/warning/list', method: 'get', params: query })
}
