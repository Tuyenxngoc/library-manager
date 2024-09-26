import axios, { axiosPrivate } from '~/apis/configHttp.js';

export const readerLogin = (values) => {
    return axios.post('auth/login', values);
};

export const adminLogin = (values) => {
    return axios.post('admin/auth/login', values);
};

export const logoutToken = () => {
    return axiosPrivate.post('auth/logout');
};

export const forgotPassword = (values) => {
    return axios.post('auth/forgot-password', values);
};

export const changePassword = (values) => {
    return axiosPrivate.patch('auth/change-password', values);
};
