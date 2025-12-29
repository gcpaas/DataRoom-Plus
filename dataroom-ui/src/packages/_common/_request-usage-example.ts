/**
 * HTTP 请求工具使用示例
 * 
 * 此文件展示了优化后的请求工具的使用方法
 */

import {request} from './_request'
import {setCookie, getCookie, removeCookie} from './_cookie'

// ===== Cookie 工具使用示例 =====

// 1. 设置 Cookie（例如登录后保存 token）
export function saveAuthToken(token: string) {
  setCookie(token) // 使用默认配置的 Cookie 名称
  // 或者指定其他名称和过期时间
  // setCookie(token, 'MY_TOKEN', 30) // 30天过期
}

// 2. 获取 Cookie
export function getAuthToken(): string {
  return getCookie() // 使用默认配置的 Cookie 名称
  // 或者获取其他 Cookie
  // return getCookie('MY_TOKEN')
}

// 3. 删除 Cookie（例如退出登录）
export function clearAuthToken() {
  removeCookie() // 删除默认配置的 Cookie
  // 或者删除其他 Cookie
  // removeCookie('MY_TOKEN')
}

// ===== 请求工具使用示例 =====

// 定义返回数据类型
interface User {
  id: number
  name: string
  email: string
}

interface PageList<T> {
  list: T[]
  total: number
  pageNum: number
  pageSize: number
}

// 4. GET 请求示例 - 直接获取 data 数据
export async function getUserInfo(userId: number) {
  try {
    // 返回的 user 直接是 data 中的数据，不需要再 .data
    const user = await request.get<User>('/api/user/info', {id: userId})
    console.log('用户信息:', user.name, user.email)
    return user
  } catch (error) {
    console.error('获取用户信息失败:', error)
    throw error
  }
}

// 5. POST 请求示例
export async function createUser(userData: Partial<User>) {
  try {
    const newUser = await request.post<User>('/api/user/create', userData)
    console.log('创建成功:', newUser)
    return newUser
  } catch (error) {
    console.error('创建用户失败:', error)
    throw error
  }
}

// 6. PUT 请求示例
export async function updateUser(userId: number, userData: Partial<User>) {
  try {
    const updatedUser = await request.put<User>(`/api/user/update/${userId}`, userData)
    console.log('更新成功:', updatedUser)
    return updatedUser
  } catch (error) {
    console.error('更新用户失败:', error)
    throw error
  }
}

// 7. DELETE 请求示例
export async function deleteUser(userId: number) {
  try {
    const result = await request.delete<{success: boolean}>('/api/user/delete', {id: userId})
    console.log('删除成功:', result.success)
    return result
  } catch (error) {
    console.error('删除用户失败:', error)
    throw error
  }
}

// 8. 分页查询示例
export async function getUserList(pageNum: number, pageSize: number) {
  try {
    const pageData = await request.get<PageList<User>>('/api/user/list', {
      pageNum,
      pageSize
    })
    console.log('用户列表:', pageData.list)
    console.log('总数:', pageData.total)
    return pageData
  } catch (error) {
    console.error('获取用户列表失败:', error)
    throw error
  }
}

// ===== 完整的登录流程示例 =====

interface LoginRequest {
  username: string
  password: string
}

interface LoginResponse {
  token: string
  user: User
}

export async function login(username: string, password: string) {
  try {
    // 1. 发送登录请求
    const loginData = await request.post<LoginResponse>('/api/auth/login', {
      username,
      password
    })
    
    // 2. 登录成功后保存 token 到 Cookie
    setCookie(loginData.token)
    
    // 3. 后续的所有请求都会自动携带 Cookie 中的 token
    console.log('登录成功:', loginData.user)
    return loginData
  } catch (error) {
    // 登录失败会自动弹出错误提示
    console.error('登录失败:', error)
    throw error
  }
}

export async function logout() {
  try {
    // 1. 调用退出登录接口
    await request.post('/api/auth/logout')
    
    // 2. 清除本地 Cookie
    removeCookie()
    
    console.log('退出登录成功')
  } catch (error) {
    console.error('退出登录失败:', error)
    throw error
  }
}

// ===== 请求响应处理说明 =====
/**
 * 响应处理逻辑：
 * 
 * 1. 当后端返回 { code: 200, message: '成功', data: {...} } 时：
 *    - 请求成功，直接返回 data 中的数据
 *    - 不会弹出任何提示
 * 
 * 2. 当后端返回 { code: 401, message: '未授权', data: null } 时：
 *    - 自动弹出错误提示："未授权，请重新登录"
 *    - 自动跳转到 /login 页面
 *    - Promise 被 reject
 * 
 * 3. 当后端返回其他错误码（如 400, 500 等）时：
 *    - 自动弹出 message 中的错误信息
 *    - Promise 被 reject
 * 
 * 4. 当网络错误或超时时：
 *    - 自动弹出对应的错误提示（如"网络连接异常"、"请求超时"等）
 *    - HTTP 401 状态码也会跳转到登录页
 *    - Promise 被 reject
 */
