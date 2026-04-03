import {ref,onMounted} from 'vue';
import {type  MemberType} from '@/api/member/MemberModel'
import { getCardListApi } from '@/api/member'
import { getListApi as getMemberCardListApi } from '@/api/member_card'
import {type  CardType} from '@/api/member_card/MemberModel'

const normalizeCardList = (data: any): CardType[] => {
    if (Array.isArray(data)) {
        return data
    }
    if (Array.isArray(data?.records)) {
        return data.records
    }
    if (Array.isArray(data?.list)) {
        return data.list
    }
    return []
}

export default function useJoin(){
    //会员卡列表数据
    const cardList = ref<CardType[]>([])
    //弹框属性
    const joinRef = ref<{show:(row:MemberType)=>void}>()
    //点击办卡按钮进行弹框
    const joinBtn = (row:MemberType)=>{
        joinRef.value?.show(row)
    }
    //获取会员卡列表
    const getCardList = async()=>{
        try {
            let res = await getCardListApi()
            if(res && res.code == 200){
                let list = normalizeCardList(res.data)
                // 兜底: 首选接口异常或返回空时，尝试从会员卡管理分页接口读取
                if(list.length === 0){
                    const backupRes = await getMemberCardListApi({
                        title:'',
                        currentPage:1,
                        pageSize:999,
                        total:0
                    })
                    if(backupRes && backupRes.code == 200){
                        list = normalizeCardList(backupRes.data).filter(item => item.status === '1')
                    }
                }
                cardList.value = list
            }
        } catch (error) {
            try {
                const backupRes = await getMemberCardListApi({
                    title:'',
                    currentPage:1,
                    pageSize:999,
                    total:0
                })
                if(backupRes && backupRes.code == 200){
                    cardList.value = normalizeCardList(backupRes.data).filter(item => item.status === '1')
                }else{
                    cardList.value = []
                }
            } catch (e) {
                cardList.value = []
            }
        }
    }

    onMounted(()=>{
        getCardList()
    })

    return {
        joinRef,
        joinBtn,
		getCardList,
        cardList
    }
}