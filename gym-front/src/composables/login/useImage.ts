import { onMounted, ref } from "vue"
import { getImageApi } from "@/api/login"

export default function useImage() {
  // 定义图片src
  const imgSrc = ref()

  // 获取图片验证码
  const getImage = async () => {
    try {
      const res = await getImageApi()
      if (res && res.code === 200) {
        imgSrc.value = res.data
      } else {
        console.error("获取验证码业务错误:", res?.msg)
      }
    } catch (error) {
      // 网络错误，拦截器已经弹窗，这里只记录
      console.error("获取验证码网络错误:", error)
    }
  }

  onMounted(() => {
    getImage()
  })

  return {
    imgSrc,
    getImage,
  }
}