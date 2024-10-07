import { axiosPrivate } from '~/apis/configHttp';

export const createReader = (values) => {
    const formData = new FormData();

    for (const key in values) {
        if (values.hasOwnProperty(key)) {
            const value = values[key];
            if (value !== null && value !== undefined) {
                formData.append(key, value);
            }
        }
    }

    return axiosPrivate.post('readers', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};

export const updateReader = (id, values) => {
    return axiosPrivate.put(`readers/${id}`, values);
};

export const deleteReader = (id) => {
    return axiosPrivate.delete(`readers/${id}`);
};

export const toggleActiveFlag = (id) => {
    return axiosPrivate.patch(`readers/${id}/toggle-active`);
};

export const getReaderById = (id) => {
    return axiosPrivate.get(`readers/${id}`);
};

export const getReaders = (params) => {
    return axiosPrivate.get(`readers?${params}`);
};
