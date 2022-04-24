// 序列化存储到 localStorage
export const saveData = (name, data) => {
  const dataStr = JSON.stringify(data)
  localStorage.setItem(name, dataStr)
}

// 反序列化从 localStorage
export const getData = (name) => {
  const dataStr = localStorage.getItem(name)
  return JSON.parse(dataStr)
}

// 提取 markdown 文本
export const abstractFn = (length, res) => {
  if (!res) return ''
  return (
    res
      .replace(/(\*\*|__)(.*?)(\*\*|__)/g, '') // 全局匹配内粗体
      .replace(/\\!\[[\s\S]*?\]\([\s\S]*?\)/g, '') // 全局匹配图片
      .replace(/\[[\s\S]*?\]\([\s\S]*?\)/g, '') // 全局匹配连接
      .replace(/<\/?.+?\/?>/g, '') // 全局匹配内html标签
      .replace(/(\*)(.*?)(\*)/g, '') // 全局匹配内联代码块
      .replace(/`{1,2}[^`](.*?)`{1,2}/g, '') // 全局匹配内联代码块
      .replace(/```([\s\S]*?)```[\s]*/g, '') // 全局匹配代码块
      // eslint-disable-next-line no-useless-escape
      .replace(/\~\~(.*?)\~\~/g, '') // 全局匹配删除线
      // eslint-disable-next-line no-useless-escape
      .replace(/[\s]*[-\*\+]+(.*)/g, '') // 全局匹配无序列表
      .replace(/[\s]*[0-9]+\.(.*)/g, '') // 全局匹配有序列表
      .replace(/(#+)(.*)/g, '') // 全局匹配标题
      .replace(/(>+)(.*)/g, '') // 全局匹配摘要
      .replace(/\r\n/g, '') // 全局匹配换行
      .replace(/\n/g, '') // 全局匹配换行
      .replace(/\s/g, '') // 全局匹配空字符
      .slice(0, length)
  )
}
