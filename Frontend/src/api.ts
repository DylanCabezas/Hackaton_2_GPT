import axios from 'axios';

const API_URL = 'http://3.90.3.179:8000/api.';

interface User {
  fullName: string;
  email: string;
  password: string;
  username: string;
}


// Autenticación y Registro
export const login = async (username: string, password: string) => {
  try {
    const response = await axios.post<{ token: string }>(`${API_URL}/auth/login`, { username, password });
    localStorage.setItem('token', response.data.token);
    console.log('Login exitoso');
    return response.data;
  } catch (error) {
    console.error('Error en el login:', error);
    throw error;
  }
};

export const register = async (user: User) => {
  try {
    const response = await axios.post(`${API_URL}/auth/register`, user);
    console.log('Registro exitoso');
    return response.data;
  } catch (error) {
    console.error('Error en el registro:', error);
    throw error;
  }
};

// Configuración de Axios para incluir el token en las solicitudes de productos
const getAuthHeaders = () => {
  const token = localStorage.getItem('token');
  return {
    headers: { Authorization: `Bearer ${token}` },
  };
};