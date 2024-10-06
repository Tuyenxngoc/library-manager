import { axiosPrivate } from '~/apis/configHttp';

export const createExportReceipt = (values) => {
    return axiosPrivate.post('export-receipts', values);
};

export const updateExportReceipt = (id, values) => {
    return axiosPrivate.put(`export-receipts/${id}`, values);
};

export const deleteExportReceipt = (id) => {
    return axiosPrivate.delete(`export-receipts/${id}`);
};

export const getExportReceiptById = (id) => {
    return axiosPrivate.get(`export-receipts/${id}`);
};

export const getExportReceipts = (params) => {
    return axiosPrivate.get(`export-receipts?${params}`);
};
