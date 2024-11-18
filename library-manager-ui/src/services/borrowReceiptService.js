import { axiosPrivate } from '~/apis/configHttp';

export const createBorrowReceipt = (values) => {
    return axiosPrivate.post('admin/borrow-receipts', values);
};

export const updateBorrowReceipt = (id, values) => {
    return axiosPrivate.put(`admin/borrow-receipts/${id}`, values);
};

export const deleteBorrowReceipt = (id) => {
    return axiosPrivate.delete(`admin/borrow-receipts/${id}`);
};

export const getBorrowReceiptById = (id) => {
    return axiosPrivate.get(`admin/borrow-receipts/${id}`);
};

export const getBorrowReceiptByCartId = (id) => {
    return axiosPrivate.get(`admin/borrow-receipts/cart/${id}`);
};

export const getBorrowReceipts = (params) => {
    return axiosPrivate.get(`admin/borrow-receipts?${params}`);
};

export const getBorrowReceiptsForReader = (params) => {
    return axiosPrivate.get(`borrow-receipts?${params}`);
};

export const getBorrowReceiptDetails = (id) => {
    return axiosPrivate.get(`admin/borrow-receipts/details/${id}`);
};
