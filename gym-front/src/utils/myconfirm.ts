/**
 * 信息确定提示框
 */
import { ElMessageBox } from 'element-plus'

export default function myconfirm(text: string): Promise<boolean> {
  return new Promise<boolean>((resolve, reject) => {
    ElMessageBox.confirm(text, '系统提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(() => {
        // 确定按钮
        resolve(true)
      })
      .catch(() => {
        // 取消按钮
        reject(false)
      })
  }).catch(() => {
    return false
  })
}