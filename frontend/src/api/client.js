import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  auth: {
    username: 'admin',
    password: 'admin123'
  }
});

export const searchSubscriber = (key) => api.get(`/subscribers/search?key=${encodeURIComponent(key)}`);
export const createSubscriber = (payload) => api.post('/subscribers', payload, { headers: { 'X-Actor': 'admin' } });
export const simSwap = (id, payload) => api.post(`/subscribers/${id}/sim-swap`, payload);
export const numberChange = (id, payload) => api.post(`/subscribers/${id}/number-change`, payload);
export const changeState = (id, state) => api.post(`/subscribers/${id}/state/${state}`, null, { headers: { 'X-Actor': 'admin' } });
export const createChangeRequest = (payload) => api.post('/change-requests', payload);
export const listPending = () => api.get('/change-requests/pending');
export const approveChange = (id, payload) => api.post(`/change-requests/${id}/approval`, payload);

export default api;
