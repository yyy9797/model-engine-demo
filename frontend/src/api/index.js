import axios from 'axios'
const api = axios.create({ baseURL: '/api', timeout: 5000 })
export function fetchModels() { return api.get('/models') }
export function fetchModel(id) { return api.get(`/models/${id}`) }
export function saveModel(payload) { return api.post('/models', payload) }
export function publishModel(id) { return api.post(`/models/${id}/publish`) }
export function fetchDictionaries() { return api.get('/dictionaries') }
export function fetchDictionary(code) { return api.get(`/dictionaries?code=${code}`) }
export function fetchRecords(modelId) { return api.get(`/records?model_id=${modelId}`) }
export function createRecord(modelId, data) { return api.post(`/models/${modelId}/records`, { data, geom: data.location || null }) }
export function submitApproval(modelId, payload) { return api.post('/approvals', { model_id: modelId, ...payload }) }
export default api
