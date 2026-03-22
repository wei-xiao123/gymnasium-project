import { onBeforeUnmount, onMounted, ref, shallowRef } from "vue";
import type { IEditorConfig } from "@wangeditor/editor";

export default function useEditor() {
  type InsertFnType = (url: string, alt?: string, href?: string) => void;

  const editorRef = shallowRef();
  const valueHtml = ref("");
  const toolbarConfig = {
    excludeKeys: []
  };
  const editorConfig: Partial<IEditorConfig> = {
    MENU_CONF: {},
    placeholder: "请输入内容...",
    readOnly: false,
    autoFocus: false
  };

  // 上传图片的配置
  editorConfig.MENU_CONF!["uploadImage"] = {
    // form-data fieldName，默认值 'wangeditor-uploaded-image'
    fieldName: "file",
    // 上传图片后端地址
    server: `${import.meta.env.VITE_API_BASE_URL}/api/upload/uploadImage`,
    // 携带鉴权头
    headers: (() => {
      const token = sessionStorage.getItem("token");
      return token ? { token } : {};
    })(),
    // 自定义插入图片
    customInsert(res: any, insertFn: InsertFnType) {
      // res 即服务端的返回结果
      const urlFromRes = res?.data?.msg || res?.data?.url;
      if (!urlFromRes) return;
      const fullUrl = urlFromRes.startsWith("http")
        ? urlFromRes
        : `${import.meta.env.VITE_API_BASE_URL}${urlFromRes}`;
      insertFn(fullUrl);
    }
  };

  // 组件销毁时，也及时销毁编辑器
  onBeforeUnmount(() => {
    const editor = editorRef.value;
    if (editor == null) return;
    editor.destroy();
  });

  const handleCreated = (editor: any) => {
    editorRef.value = editor; // 记录 editor 实例，重要！
    // 延迟获得焦点，确保 DOM 完全渲染
    setTimeout(() => {
      if (editor && typeof editor.focus === 'function') {
        editor.focus();
      }
    }, 100);
  };

  return {
    editorRef,
    valueHtml,
    mode: "default", // 或 'simple'
    toolbarConfig,
    editorConfig,
    handleCreated
  };
}