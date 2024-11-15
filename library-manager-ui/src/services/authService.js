import httpRequest, { axiosPrivate } from '~/apis/configHttp.js';

export const readerLogin = (values) => {
    return httpRequest.post('auth/login', values);
};

export const readerForgotPassword = (values) => {
    return httpRequest.post('auth/forget-password', values);
};

export const readerChangePassword = (values) => {
    return axiosPrivate.patch('auth/change-password', values);
};

export const logoutToken = () => {
    return axiosPrivate.post('auth/logout');
};

export const adminLogin = (values) => {
    return httpRequest.post('admin/auth/login', values);
};

export const adminForgotPassword = (values) => {
    return httpRequest.post('admin/auth/forget-password', values);
};

export const adminChangePassword = (values) => {
    return axiosPrivate.patch('admin/auth/change-password', values);
};
