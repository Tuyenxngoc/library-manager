import httpRequest, { axiosPrivate } from '~/apis/configHttp';

export const getLibraryInfo = () => {
    return httpRequest.get('stats/library');
};

export const getBorrowStats = () => {
    return axiosPrivate.get('stats/borrow');
};
