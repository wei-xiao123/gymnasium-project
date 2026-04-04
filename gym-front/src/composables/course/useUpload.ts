import { ref } from 'vue'
import { ElMessage,type  UploadFile } from 'element-plus'
import { uploadImageApi } from '@/api/course/index'
export default function useUpload() {
    //图片上传组件ref属性
    const uploadRef = ref()
    //图片预览地址
    const dialogImageUrl = ref('')
    //是否点击预览
    const dialogVisible = ref(false)
    const disabled = ref(false)
    //上传的文件
    const fileList = ref<UploadFile[]>([])
    //上传图片的地址
    const imgurl = ref('');
    //删除图片
    const handleRemove = (file: UploadFile) => {
        fileList.value = fileList.value.filter(item => item.name != file.name)
        imgurl.value = ''
    }
    //点击预览图片
    const handlePictureCardPreview = (file: UploadFile) => {
        dialogImageUrl.value = file.url!
        dialogVisible.value = true
    }
    //图片上传
    const uploadFile = async (file: any) => {
        const typeArr = ["image/png", "image/gif", "image/jpeg", "image/jpg"];
        const isImg = typeArr.indexOf(file.raw.type) !== -1;
        const isMore3M = file.size / 1024 / 1024 < 3;
        if (!isImg) {
            ElMessage.warning("只能上传图片类型!");
            uploadRef.value?.clearFiles()
            return;
        }
        if (!isMore3M) {
            ElMessage.warning("只能上传图片类型!");
            uploadRef.value?.clearFiles()
            return;
        }
        const formData = new FormData();
        formData.append("file", file.raw);
        try {
            let res = await uploadImageApi(formData);
            if (res && res.code == 200 && res.data) {
                imgurl.value = res.data.msg || res.data.url || res.data.path || res.data;
                ElMessage.success("图片上传成功!");
            }
        } catch (e) {
            ElMessage.error("图片上传失败，请检查图片服务")
        }
    }

    return {
        dialogImageUrl,
        dialogVisible,
        disabled,
        handleRemove,
        handlePictureCardPreview,
        fileList,
        uploadFile,
        uploadRef,
        imgurl
    }
}