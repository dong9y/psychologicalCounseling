-- 校园心理疏导情感分析系统 V1 增量脚本

UPDATE sys_menu SET visible = '1' WHERE menu_id IN (3, 4);

CREATE TABLE IF NOT EXISTS psy_student_profile (
  student_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  student_no varchar(32) DEFAULT '',
  real_name varchar(64) DEFAULT '',
  college varchar(64) DEFAULT '',
  major varchar(64) DEFAULT '',
  class_name varchar(64) DEFAULT '',
  contact_phone varchar(32) DEFAULT '',
  status char(1) DEFAULT '0',
  create_by varchar(64) DEFAULT '',
  create_time datetime DEFAULT NULL,
  update_by varchar(64) DEFAULT '',
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (student_id),
  UNIQUE KEY uk_psy_student_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生档案表';

CREATE TABLE IF NOT EXISTS psy_counselor_profile (
  counselor_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  counselor_no varchar(32) DEFAULT '',
  real_name varchar(64) DEFAULT '',
  title varchar(64) DEFAULT '',
  specialty varchar(255) DEFAULT '',
  introduction varchar(1000) DEFAULT '',
  available_status char(1) DEFAULT '0',
  contact_phone varchar(32) DEFAULT '',
  service_score decimal(4,2) DEFAULT 4.80,
  create_by varchar(64) DEFAULT '',
  create_time datetime DEFAULT NULL,
  update_by varchar(64) DEFAULT '',
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (counselor_id),
  UNIQUE KEY uk_psy_counselor_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询师档案表';

CREATE TABLE IF NOT EXISTS psy_emotion_record (
  record_id bigint(20) NOT NULL AUTO_INCREMENT,
  student_user_id bigint(20) NOT NULL,
  anonymous_flag tinyint(1) DEFAULT 0,
  original_text text,
  processed_text text,
  primary_emotion varchar(32) DEFAULT '',
  emotion_tags varchar(255) DEFAULT '',
  confidence decimal(5,2) DEFAULT 0,
  risk_level varchar(32) DEFAULT '',
  emotion_score decimal(8,2) DEFAULT 0,
  analysis_summary varchar(1000) DEFAULT '',
  guidance_advice varchar(2000) DEFAULT '',
  warning_flag tinyint(1) DEFAULT 0,
  create_by varchar(64) DEFAULT '',
  create_time datetime DEFAULT NULL,
  PRIMARY KEY (record_id),
  KEY idx_emotion_student_time (student_user_id, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理倾诉情绪分析记录表';

CREATE TABLE IF NOT EXISTS psy_scale (
  scale_id bigint(20) NOT NULL AUTO_INCREMENT,
  scale_code varchar(32) DEFAULT '',
  scale_name varchar(128) DEFAULT '',
  scale_type varchar(64) DEFAULT '',
  description varchar(1000) DEFAULT '',
  question_count int(11) DEFAULT 0,
  max_score decimal(8,2) DEFAULT 0,
  warning_threshold decimal(8,2) DEFAULT 0,
  status char(1) DEFAULT '0',
  result_template varchar(1000) DEFAULT '',
  create_by varchar(64) DEFAULT '',
  create_time datetime DEFAULT NULL,
  update_by varchar(64) DEFAULT '',
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (scale_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理量表表';

CREATE TABLE IF NOT EXISTS psy_scale_question (
  question_id bigint(20) NOT NULL AUTO_INCREMENT,
  scale_id bigint(20) NOT NULL,
  question_sort int(11) DEFAULT 0,
  question_title varchar(500) DEFAULT '',
  PRIMARY KEY (question_id),
  KEY idx_psy_scale_question_scale (scale_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='量表题目表';

CREATE TABLE IF NOT EXISTS psy_scale_option (
  option_id bigint(20) NOT NULL AUTO_INCREMENT,
  question_id bigint(20) NOT NULL,
  option_sort int(11) DEFAULT 0,
  option_label varchar(255) DEFAULT '',
  option_score decimal(8,2) DEFAULT 0,
  PRIMARY KEY (option_id),
  KEY idx_psy_scale_option_question (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='量表选项表';

CREATE TABLE IF NOT EXISTS psy_assessment_record (
  record_id bigint(20) NOT NULL AUTO_INCREMENT,
  student_user_id bigint(20) NOT NULL,
  scale_id bigint(20) NOT NULL,
  scale_name varchar(128) DEFAULT '',
  total_score decimal(8,2) DEFAULT 0,
  result_level varchar(32) DEFAULT '',
  result_summary varchar(1000) DEFAULT '',
  guidance_advice varchar(2000) DEFAULT '',
  warning_flag tinyint(1) DEFAULT 0,
  create_by varchar(64) DEFAULT '',
  create_time datetime DEFAULT NULL,
  PRIMARY KEY (record_id),
  KEY idx_assessment_student_time (student_user_id, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理测评记录表';

CREATE TABLE IF NOT EXISTS psy_assessment_answer (
  answer_id bigint(20) NOT NULL AUTO_INCREMENT,
  record_id bigint(20) NOT NULL,
  question_id bigint(20) NOT NULL,
  option_id bigint(20) NOT NULL,
  option_score decimal(8,2) DEFAULT 0,
  PRIMARY KEY (answer_id),
  KEY idx_assessment_answer_record (record_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理测评答案表';

CREATE TABLE IF NOT EXISTS psy_appointment (
  appointment_id bigint(20) NOT NULL AUTO_INCREMENT,
  student_user_id bigint(20) NOT NULL,
  counselor_user_id bigint(20) NOT NULL,
  appointment_time datetime DEFAULT NULL,
  topic varchar(128) DEFAULT '',
  problem_description varchar(1000) DEFAULT '',
  status varchar(32) DEFAULT '待确认',
  review_note varchar(1000) DEFAULT '',
  counseling_summary varchar(1000) DEFAULT '',
  evaluation_content varchar(1000) DEFAULT '',
  evaluation_score decimal(4,2) DEFAULT NULL,
  create_by varchar(64) DEFAULT '',
  create_time datetime DEFAULT NULL,
  update_by varchar(64) DEFAULT '',
  update_time datetime DEFAULT NULL,
  PRIMARY KEY (appointment_id),
  KEY idx_appointment_student (student_user_id),
  KEY idx_appointment_counselor (counselor_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理咨询预约表';

CREATE TABLE IF NOT EXISTS psy_warning (
  warning_id bigint(20) NOT NULL AUTO_INCREMENT,
  warning_no varchar(64) DEFAULT '',
  student_user_id bigint(20) NOT NULL,
  warning_source varchar(64) DEFAULT '',
  source_record_id bigint(20) DEFAULT NULL,
  risk_level varchar(32) DEFAULT '',
  status varchar(32) DEFAULT '待处理',
  counselor_user_id bigint(20) DEFAULT NULL,
  trigger_summary varchar(1000) DEFAULT '',
  process_note varchar(1000) DEFAULT '',
  follow_plan varchar(1000) DEFAULT '',
  handled_time datetime DEFAULT NULL,
  create_by varchar(64) DEFAULT '',
  create_time datetime DEFAULT NULL,
  update_by varchar(64) DEFAULT '',
  update_time datetime DEFAULT NULL,
  PRIMARY KEY (warning_id),
  KEY idx_warning_student (student_user_id),
  KEY idx_warning_counselor (counselor_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理预警记录表';

CREATE TABLE IF NOT EXISTS psy_message (
  message_id bigint(20) NOT NULL AUTO_INCREMENT,
  receiver_user_id bigint(20) NOT NULL,
  message_type varchar(64) DEFAULT '',
  title varchar(128) DEFAULT '',
  content varchar(1000) DEFAULT '',
  read_status char(1) DEFAULT '0',
  create_by varchar(64) DEFAULT '',
  create_time datetime DEFAULT NULL,
  PRIMARY KEY (message_id),
  KEY idx_message_receiver (receiver_user_id, read_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理中心通知消息表';

DELETE FROM sys_dict_data WHERE dict_type IN ('psy_risk_level', 'psy_warning_status', 'psy_appointment_status');
DELETE FROM sys_dict_type WHERE dict_type IN ('psy_risk_level', 'psy_warning_status', 'psy_appointment_status');

INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES
(20, '心理风险等级', 'psy_risk_level', '0', 'admin', sysdate(), '', NULL, '心理风险等级'),
(21, '预警处理状态', 'psy_warning_status', '0', 'admin', sysdate(), '', NULL, '心理预警处理状态'),
(22, '预约状态', 'psy_appointment_status', '0', 'admin', sysdate(), '', NULL, '心理咨询预约状态');

INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES
(200, 1, '正常', '正常', 'psy_risk_level', '', 'success', 'N', '0', 'admin', sysdate(), '', NULL, '心理风险等级-正常'),
(201, 2, '关注', '关注', 'psy_risk_level', '', 'warning', 'N', '0', 'admin', sysdate(), '', NULL, '心理风险等级-关注'),
(202, 3, '中风险', '中风险', 'psy_risk_level', '', 'warning', 'N', '0', 'admin', sysdate(), '', NULL, '心理风险等级-中风险'),
(203, 4, '高风险', '高风险', 'psy_risk_level', '', 'danger', 'N', '0', 'admin', sysdate(), '', NULL, '心理风险等级-高风险'),
(210, 1, '待处理', '待处理', 'psy_warning_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', NULL, '预警处理状态-待处理'),
(211, 2, '处理中', '处理中', 'psy_warning_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', NULL, '预警处理状态-处理中'),
(212, 3, '已处理', '已处理', 'psy_warning_status', '', 'success', 'N', '0', 'admin', sysdate(), '', NULL, '预警处理状态-已处理'),
(220, 1, '待确认', '待确认', 'psy_appointment_status', '', 'info', 'N', '0', 'admin', sysdate(), '', NULL, '预约状态-待确认'),
(221, 2, '已确认', '已确认', 'psy_appointment_status', '', 'primary', 'N', '0', 'admin', sysdate(), '', NULL, '预约状态-已确认'),
(222, 3, '已拒绝', '已拒绝', 'psy_appointment_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', NULL, '预约状态-已拒绝'),
(223, 4, '已完成', '已完成', 'psy_appointment_status', '', 'success', 'N', '0', 'admin', sysdate(), '', NULL, '预约状态-已完成'),
(224, 5, '已取消', '已取消', 'psy_appointment_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', NULL, '预约状态-已取消');

DELETE FROM sys_role WHERE role_id IN (100, 101);
INSERT INTO sys_role(role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES
(100, '学生角色', 'student', 100, 1, 1, 1, '0', '0', 'admin', sysdate(), '', NULL, '心理中心学生角色'),
(101, '咨询师角色', 'counselor', 101, 1, 1, 1, '0', '0', 'admin', sysdate(), '', NULL, '心理中心咨询师角色');

DELETE FROM sys_menu WHERE menu_id BETWEEN 2000 AND 2300;

INSERT INTO sys_menu VALUES
(2000, '学生心理中心', '0', '10', 'psych/student', '', '', '', 1, 0, 'M', '0', '0', '', 'form', 'admin', sysdate(), '', NULL, '学生心理中心目录'),
(2001, '学生首页', '2000', '1', 'index', 'psych/student/index', '', '', 1, 0, 'C', '0', '0', 'psych:student:portal', 'dashboard', 'admin', sysdate(), '', NULL, '学生首页菜单'),
(2002, '心理倾诉', '2000', '2', 'confession', 'psych/student/confession', '', '', 1, 0, 'C', '0', '0', 'psych:student:confession', 'edit', 'admin', sysdate(), '', NULL, '心理倾诉菜单'),
(2003, '心理测评', '2000', '3', 'assessment', 'psych/student/assessment', '', '', 1, 0, 'C', '0', '0', 'psych:student:assessment', 'chart', 'admin', sysdate(), '', NULL, '心理测评菜单'),
(2004, '咨询预约', '2000', '4', 'appointment', 'psych/student/appointment', '', '', 1, 0, 'C', '0', '0', 'psych:student:appointment', 'date', 'admin', sysdate(), '', NULL, '咨询预约菜单'),
(2005, '个人中心', '2000', '5', 'profile', 'psych/student/profile', '', '', 1, 0, 'C', '0', '0', 'psych:student:profile', 'user', 'admin', sysdate(), '', NULL, '学生个人中心菜单'),
(2100, '咨询师工作台', '0', '11', 'psych/counselor', '', '', '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', sysdate(), '', NULL, '咨询师工作台目录'),
(2101, '工作台首页', '2100', '1', 'index', 'psych/counselor/index', '', '', 1, 0, 'C', '0', '0', 'psych:counselor:portal', 'dashboard', 'admin', sysdate(), '', NULL, '咨询师首页菜单'),
(2102, '预警处理', '2100', '2', 'warning', 'psych/counselor/warning', '', '', 1, 0, 'C', '0', '0', 'psych:counselor:warning', 'message', 'admin', sysdate(), '', NULL, '咨询师预警处理菜单'),
(2200, '心理中心管理', '0', '12', 'psych/admin', '', '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', sysdate(), '', NULL, '心理中心管理目录'),
(2201, '管理概览', '2200', '1', 'index', 'psych/admin/index', '', '', 1, 0, 'C', '0', '0', 'psych:admin:portal', 'dashboard', 'admin', sysdate(), '', NULL, '心理中心管理首页'),
(2202, '量表管理', '2200', '2', 'scale', 'psych/admin/scale', '', '', 1, 0, 'C', '0', '0', 'psych:scale:list', 'dict', 'admin', sysdate(), '', NULL, '量表管理菜单'),
(2203, '预警管理', '2200', '3', 'warning', 'psych/admin/warning', '', '', 1, 0, 'C', '0', '0', 'psych:warning:list', 'message', 'admin', sysdate(), '', NULL, '预警管理菜单'),
(2204, '量表新增', '2202', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'psych:scale:add', '#', 'admin', sysdate(), '', NULL, '量表新增按钮'),
(2205, '量表修改', '2202', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'psych:scale:edit', '#', 'admin', sysdate(), '', NULL, '量表修改按钮'),
(2206, '预警查询', '2203', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'psych:warning:list', '#', 'admin', sysdate(), '', NULL, '预警查询按钮'),
(2207, '咨询预约审核', '2100', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'psych:counselor:appointment', '#', 'admin', sysdate(), '', NULL, '咨询师预约审核权限');

DELETE FROM sys_role_menu WHERE role_id IN (1, 100, 101) AND menu_id BETWEEN 2000 AND 2300;

INSERT INTO sys_role_menu VALUES
('100', '2000'), ('100', '2001'), ('100', '2002'), ('100', '2003'), ('100', '2004'), ('100', '2005'),
('101', '2100'), ('101', '2101'), ('101', '2102'), ('101', '2207'),
('1', '2200'), ('1', '2201'), ('1', '2202'), ('1', '2203'), ('1', '2204'), ('1', '2205'), ('1', '2206');

DELETE FROM sys_user_role WHERE user_id IN (100, 101);
DELETE FROM sys_user WHERE user_id IN (100, 101);

INSERT INTO sys_user VALUES
(100, 103, 'student01', '张同学', '00', 'student01@campus.edu', '13800000001', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', NULL, '学生演示账号'),
(101, 103, 'counselor01', '王老师', '1', 'counselor01@campus.edu', '13800000002', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', NULL, '咨询师演示账号');

INSERT INTO sys_user_role VALUES ('100', '100'), ('101', '101');

DELETE FROM psy_student_profile WHERE user_id IN (100);
DELETE FROM psy_counselor_profile WHERE user_id IN (101);
DELETE FROM psy_message WHERE receiver_user_id IN (100, 101);
DELETE FROM psy_warning WHERE student_user_id IN (100);
DELETE FROM psy_appointment WHERE student_user_id IN (100);
DELETE FROM psy_assessment_answer WHERE record_id IN (select record_id from psy_assessment_record where student_user_id = 100);
DELETE FROM psy_assessment_record WHERE student_user_id IN (100);
DELETE FROM psy_emotion_record WHERE student_user_id IN (100);

INSERT INTO psy_student_profile(user_id, student_no, real_name, college, major, class_name, contact_phone, status, create_by, create_time, remark) VALUES
(100, '20260001', '张同学', '计算机学院', '软件工程', '软件工程2班', '13800000001', '0', 'admin', sysdate(), '学生演示档案');

INSERT INTO psy_counselor_profile(user_id, counselor_no, real_name, title, specialty, introduction, available_status, contact_phone, service_score, create_by, create_time, remark) VALUES
(101, 'C2026001', '王老师', '国家二级心理咨询师', '学业压力、焦虑干预、人际关系辅导', '校心理中心专职咨询师', '0', '13800000002', 4.90, 'admin', sysdate(), '咨询师演示档案');

DELETE FROM psy_scale_option WHERE question_id IN (3001, 3002, 3003);
DELETE FROM psy_scale_question WHERE scale_id IN (3000);
DELETE FROM psy_scale WHERE scale_id IN (3000);

INSERT INTO psy_scale(scale_id, scale_code, scale_name, scale_type, description, question_count, max_score, warning_threshold, status, result_template, create_by, create_time, remark) VALUES
(3000, 'SAS', 'SAS 焦虑自评量表', '焦虑评估', '用于评估学生近期焦虑情绪与压力表现。', 3, 12, 7, '0', '系统根据总分输出正常、关注、中风险、高风险结论。', 'admin', sysdate(), '演示量表');

INSERT INTO psy_scale_question(question_id, scale_id, question_sort, question_title) VALUES
(3001, 3000, 1, '最近两周，我经常感到紧张或难以放松。'),
(3002, 3000, 2, '面对学习任务时，我会明显感到担忧和压力。'),
(3003, 3000, 3, '我会因为情绪问题影响睡眠或作息。');

INSERT INTO psy_scale_option(option_id, question_id, option_sort, option_label, option_score) VALUES
(3101, 3001, 1, '从不', 1), (3102, 3001, 2, '偶尔', 2), (3103, 3001, 3, '经常', 3), (3104, 3001, 4, '总是', 4),
(3201, 3002, 1, '从不', 1), (3202, 3002, 2, '偶尔', 2), (3203, 3002, 3, '经常', 3), (3204, 3002, 4, '总是', 4),
(3301, 3003, 1, '从不', 1), (3302, 3003, 2, '偶尔', 2), (3303, 3003, 3, '经常', 3), (3304, 3003, 4, '总是', 4);

INSERT INTO psy_message(receiver_user_id, message_type, title, content, read_status, create_by, create_time) VALUES
(100, 'system', '欢迎使用心理中心', '您可以通过心理倾诉、测评和预约咨询获得持续支持。', '0', 'admin', sysdate()),
(101, 'system', '欢迎进入咨询师工作台', '请及时关注学生预警和预约申请。', '0', 'admin', sysdate());
