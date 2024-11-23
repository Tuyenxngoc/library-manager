import { axiosPrivate } from '~/apis/configHttp';

export const updateLibraryRules = (values) => {
    return axiosPrivate.put('system-settings/library-rules', values);
};

export const getLibraryRules = () => {
    return axiosPrivate.get('system-settings/library-rules');
};

export const getAllHolidays = (params) => {
    return axiosPrivate.get(`system-settings/holidays?${params}`);
};

export const getHolidayById = (id) => {
    return axiosPrivate.get(`system-settings/holidays/${id}`);
};

export const addHoliday = (values) => {
    return axiosPrivate.post('system-settings/holidays', values);
};

export const updateHoliday = (id, values) => {
    return axiosPrivate.put(`system-settings/holidays/${id}`, values);
};

export const deleteHoliday = (id) => {
    return axiosPrivate.delete(`system-settings/holidays/${id}`);
};

export const getLibraryConfig = () => {
    return axiosPrivate.get('system-settings/library-config');
};

export const updateLibraryConfig = (values) => {
    return axiosPrivate.put('system-settings/library-config', values);
};
