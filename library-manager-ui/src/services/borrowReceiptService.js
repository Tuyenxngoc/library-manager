import { axiosPrivate } from '~/apis/configHttp';

export const createBorrowReceipt = (values) => {
    return axiosPrivate.post('borrow-receipts', values);
};

export const updateBorrowReceipt = (id, values) => {
    return axiosPrivate.put(`borrow-receipts/${id}`, values);
};

export const deleteBorrowReceipt = (id) => {
    return axiosPrivate.delete(`borrow-receipts/${id}`);
};

export const getBorrowReceiptById = (id) => {
    return axiosPrivate.get(`borrow-receipts/${id}`);
};

export const getBorrowReceipts = (params) => {
    return axiosPrivate.get(`borrow-receipts?${params}`);
};
