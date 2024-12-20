import { axiosPrivate } from '~/apis/configHttp';

export const updateBook = (id, values) => {
    return axiosPrivate.put(`books/${id}`, values);
};

export const getBookById = (id) => {
    return axiosPrivate.get(`books/${id}`);
};

export const getBooks = (params) => {
    return axiosPrivate.get(`books?${params}`);
};

export const getBooksByIds = (values) => {
    return axiosPrivate.post('books/by-ids', values);
};
