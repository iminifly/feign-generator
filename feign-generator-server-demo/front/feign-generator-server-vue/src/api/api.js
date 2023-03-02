import request from '@/utils/request'

export function projectSearch(query) {
  return request({
    url: '/api/v1/feign/search',
    method: 'get',
    params: query
  })
}

export function allProjects() {
  return request({
    url: '/api/v1/feign/project/all',
    method: 'get'
  })
}

export function searchClients(query) {
  return request({
    url: '/api/v1/feign/clients',
    method: 'get',
    params: query
  })
}

export function client(id) {
  return request({
    url: '/api/v1/feign/client/' + id,
    method: 'get'
  })
}

export function searchModels(query) {
  return request({
    url: '/api/v1/feign/models',
    method: 'get',
    params: query
  })
}

export function model(id) {
  return request({
    url: '/api/v1/feign/model/' + id,
    method: 'get'
  })
}
