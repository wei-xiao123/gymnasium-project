export const normalizeImageUrl = (url?: string): string => {
  if (!url) {
    return "";
  }

  return url
    .replace("http://localhost:8089", "http://localhost:8088")
    .replace("http://127.0.0.1:8089", "http://127.0.0.1:8088");
};
