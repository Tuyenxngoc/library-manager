import { axiosPrivate } from '~/apis/configHttp';

export const getNewsArticleById = (id) => {
    return axiosPrivate.get(`news-articles/${id}`);
};

export const getNewsArticles = (params) => {
    return axiosPrivate.get(`news-articles?${params}`);
};

export const updateNewsArticle = (id, values) => {
    const formData = new FormData();

    for (const key in values) {
        if (values.hasOwnProperty(key)) {
            const value = values[key];
            if (value !== null) {
                formData.append(key, value);
            }
        }
    }

    return axiosPrivate.put(`news-articles/${id}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};

export const createNewsArticle = (values) => {
    const formData = new FormData();

    for (const key in values) {
        if (values.hasOwnProperty(key)) {
            const value = values[key];
            if (value !== null) {
                formData.append(key, value);
            }
        }
    }

    return axiosPrivate.post('news-articles', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};

export const deleteNewsArticle = (id) => {
    return axiosPrivate.delete(`news-articles/${id}`);
};

export const toggleActiveFlag = (id) => {
    return axiosPrivate.patch(`news-articles/${id}/toggle-active`);
};
