import httpRequest from '~/apis/configHttp';

export const getLibraryInfo = () => {
    return httpRequest.get('stats/library');
};
