export const saveData = (name, data) => {
  const dataStr = JSON.stringify(data)
  localStorage.setItem(name, dataStr)
}

export const getData = (name) => {
  const dataStr = localStorage.getItem(name)
  console.log(dataStr)
  return JSON.parse(dataStr)
}
