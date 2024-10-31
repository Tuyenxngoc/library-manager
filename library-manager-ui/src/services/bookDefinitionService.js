import httpRequest, { axiosPrivate } from '~/apis/configHttp';

export const getBookDefinitionById = (id) => {
    return axiosPrivate.get(`admin/book-definitions/${id}`);
};

export const getBookDefinitions = (params) => {
    return axiosPrivate.get(`admin/book-definitions?${params}`);
};

export const getBookByBookDefinitions = (params) => {
    return axiosPrivate.get(`admin/book-definitions/books?${params}`);
};

export const getBookByBookDefinitionsForUser = (params) => {
    return httpRequest.get(`book-definitions/books?${params}`);
};

export const getBookDetailForUser = (id) => {
    return httpRequest.get(`book-definitions/books/${id}`);
};

export const updateBookDefinition = (id, values) => {
    const formData = new FormData();

    for (const key in values) {
        if (values.hasOwnProperty(key)) {
            const value = values[key];
            if (value !== null) {
                formData.append(key, value);
            }
        }
    }

    return axiosPrivate.put(`admin/book-definitions/${id}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};

export const createBookDefinition = (values) => {
    const formData = new FormData();

    for (const key in values) {
        if (values.hasOwnProperty(key)) {
            const value = values[key];
            if (value !== null) {
                formData.append(key, value);
            }
        }
    }

    return axiosPrivate.post('admin/book-definitions', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};

export const deleteBookDefinition = (id) => {
    return axiosPrivate.delete(`admin/book-definitions/${id}`);
};

export const toggleActiveFlag = (id) => {
    return axiosPrivate.patch(`admin/book-definitions/${id}/toggle-active`);
};
