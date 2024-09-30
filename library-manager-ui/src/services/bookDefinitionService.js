import { axiosPrivate } from '~/apis/configHttp';

export const getBookDefinitionById = (id) => {
    return axiosPrivate.get(`book-definitions/${id}`);
};

export const getBookDefinitions = (params) => {
    return axiosPrivate.get(`book-definitions?${params}`);
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

    return axiosPrivate.put(`book-definitions/${id}`, formData, {
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

    return axiosPrivate.post('book-definitions', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};

export const deleteBookDefinition = (id) => {
    return axiosPrivate.delete(`book-definitions/${id}`);
};

export const toggleActiveFlag = (id) => {
    return axiosPrivate.patch(`book-definitions/${id}/toggle-active`);
};
