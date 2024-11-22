import { axiosPrivate } from '~/apis/configHttp';

export const updateLibraryRules = (values) => {
    return axiosPrivate.put('system-settings/library-rules', values);
};

export const getLibraryRules = () => {
    return axiosPrivate.get('system-settings/library-rules');
};
