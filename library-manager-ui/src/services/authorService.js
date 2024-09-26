import { axiosPrivate } from '~/apis/configHttp';

export const getAuthorById = (id) => {
    return axiosPrivate.get(`authors/${id}`);
};

export const getAuthors = (params) => {
    return axiosPrivate.get(`authors?${params}`);
};

export const updateAuthor = (id, values) => {
    return axiosPrivate.put(`authors/${id}`, values);
};
