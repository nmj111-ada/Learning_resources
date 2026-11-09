//读取本地登录信息；旧版本保存的无效JSON需要清理，避免整页报错。
export const getLoginUser = () => {
  try {
    return JSON.parse(localStorage.getItem('loginUser') || 'null')
  } catch {
    localStorage.removeItem('loginUser')
    return null
  }
}
