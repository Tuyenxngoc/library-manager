import axiosInstance from '../api/configHttp';

export const getCurrentUserLogin = async () => {
    try {
        const response = await axiosInstance.get(`users/current`);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching user:', error);
        throw error;
    }
};
