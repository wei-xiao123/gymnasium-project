export const normalizeImageUrl = (url?: string): string => {
  if (!url) {
    return "";
  }

  const value = String(url).trim();
  if (!value) {
    return "";
  }

  // 已是完整地址
  if (/^https?:\/\//i.test(value)) {
    return value
      .replace("http://localhost:8089", "http://localhost:8088")
      .replace("http://127.0.0.1:8089", "http://127.0.0.1:8088");
  }

  // 兼容历史仅文件名：gym_ 开头为 MinIO 对象，其它按历史 /images 路径
  if (value.startsWith("gym_")) {
    return `http://127.0.0.1:9000/gym/${value}`;
  }

  const normalizedName = value.replace(/^\/+/, "").replace(/^images\//, "");
  return `http://localhost:8088/images/${normalizedName}`;
};

export const splitImageUrls = (value?: string): string[] => {
  if (!value) {
    return [];
  }
  return value
    .split(",")
    .map((item) => item.trim())
    .filter((item) => !!item);
};

export const getPrimaryImageUrl = (value?: string): string => {
  const list = splitImageUrls(value);
  return normalizeImageUrl(list[0] || "");
};
