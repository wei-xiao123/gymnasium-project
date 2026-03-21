// 分页查询的数据类型
export type CourseListParam = {
  teacherName: string;
  courseName: string;
  currentPage: number;
  pageSize: number;
  total: number;
};

// 课程数据类型
export type CourseType = {
  type: string;
  courseId: string;
  courseName: string;
  image: string;
  teacherName: string;
  courseHour: number;
  courseDetails: string;
  coursePrice: number;
};