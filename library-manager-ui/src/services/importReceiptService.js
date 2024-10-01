import { axiosPrivate } from '~/apis/configHttp';

export const createImportReceipt = (values) => {
    return axiosPrivate.post('import-receipts', values);
};

export const updateImportReceipt = (id, values) => {
    return axiosPrivate.put(`import-receipts/${id}`, values);
};

export const getImportReceiptById = (id) => {
    return axiosPrivate.get(`import-receipts/${id}`);
};

export const getImportReceipts = (params) => {
    return axiosPrivate.get(`import-receipts?${params}`);
};
