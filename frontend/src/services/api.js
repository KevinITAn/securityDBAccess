import axios from 'axios';

// Create a centralized Axios instance with base configuration
const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api', // Backend API endpoint
    headers: {
        'Content-Type': 'application/json',
    },
});

/*
 * Request Interceptor
 * -------------------
 * Automatically attaches the JWT Token to the 'Authorization' header
 * for every outgoing request, if the token exists in LocalStorage.
 * This ensures the Backend can authenticate the user.
 */
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default apiClient;