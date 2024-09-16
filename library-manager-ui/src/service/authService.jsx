import axiosInstance from '../api/configHttp';

export const logoutToken = async () => {
    try {
        const response = await axiosInstance.get(`auth/logout`);
        return response.data;
    } catch (error) {
        console.error('Error fetching user:', error);
        throw error;
    }
};

export const loginUser = async (values) => {
    try {
        const response = await axiosInstance.post('auth/login', values);
        return response.data;
    } catch (error) {
        console.error('Error fetching user:', error);
        throw error;
    }
};
