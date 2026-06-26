export const getToken = () => localStorage.getItem('pf.token') || '';
export const setToken = (token) => localStorage.setItem('pf.token', token || '');
export const clearToken = () => localStorage.removeItem('pf.token');
