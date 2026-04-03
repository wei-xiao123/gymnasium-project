import {type  MemberParam } from "@/api/member/MemberModel";
import { reactive,ref,nextTick,onMounted } from "vue";
import { getListApi} from '@/api/member'
import { userStore } from '@/store/user'

export default function useTable() {
    const store = userStore()

    //定义表格的高度
    const tableHeight =  ref(0)
    //表格数据的定义
    const tableList = reactive({
        list:[]
    })

    //参数列表
    const listParam = reactive<MemberParam>({
        name: '',
        phone: '',
        username: '',
        pageSize: 10,
        currentPage: 1,
        memberId:'',
        userType:'',
        total: 0
    })
    //列表
    const getList = async ()=>{
        // 补齐后端查询所需的用户上下文参数
        listParam.userType = store.getUserType || ''
        listParam.memberId = store.getUserId || ''

        try {
            let res = await getListApi(listParam)
            if(res && res.code == 200){
                tableList.list = res.data.records
                listParam.total = res.data.total
            }
        } catch (error) {
            tableList.list = []
            listParam.total = 0
        }
    }
    //搜索
    const searchBtn = ()=>{
        getList()
    }
    //重置
    const resetBtn = ()=>{
        getList()
    }
    
    //页面容量改变时触发
    const sizeChange = (size:number)=>{
        listParam.pageSize = size
        getList()
    }

    //页数改变时触发
    const currentChange = (page:number)=>{
        listParam.currentPage = page
        getList()
    }

    //刷新表格
    const refresh = ()=>{
        getList()
    }

    onMounted(()=>{
        getList()
        nextTick(()=>{
            tableHeight.value = window.innerHeight - 230
        })
    })

    return{
        listParam,
        getList,
        searchBtn,
        resetBtn,
        tableList,
        tableHeight,
        sizeChange,
        currentChange,
        refresh
    }
}