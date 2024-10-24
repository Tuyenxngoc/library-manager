import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, DatePicker, Divider, message, Select, Space, Table } from 'antd';
import { FaPlusCircle } from 'react-icons/fa';
import { FaRegTrashAlt } from 'react-icons/fa';
import { FaPlus } from 'react-icons/fa6';
import queryString from 'query-string';
import dayjs from 'dayjs';
import FormInput from '~/components/FormInput';
import { handleError } from '~/utils/errorHandler';
import { checkIdIsNumber } from '~/utils/helper';
import { createExportReceipt, getExportReceiptById, updateExportReceipt } from '~/services/exportReceiptService';
import { getBooks } from '~/services/bookService';
import FormTextArea from '~/components/FormTextArea';

const defaultValue = {
    receiptNumber: '',
    exportDate: new Date(),
    exportReason: '',
    bookIds: [],
};

const validationSchema = yup.object({
    receiptNumber: yup.string().required('Số phiếu xuất là bắt buộc'),

    exportDate: yup.date().nullable().required('Ngày xuất là bắt buộc').typeError('Ngày xuất không hợp lệ'),

    bookIds: yup
        .array()
        .of(yup.number().required('Mã sách là bắt buộc'))
        .min(1, 'Bạn phải chọn ít nhất một cuốn sách để xuất'),
});

function OutwardBookForm() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [messageApi, contextHolder] = message.useMessage();

    const [books, setBooks] = useState([]);
    const [isBooksLoading, setIsBooksLoading] = useState(true);

    const [selectedBookId, setSelectedBookId] = useState(null);

    const handleSubmit = async (values, { setSubmitting }) => {
        try {
            let response;
            if (id) {
                response = await updateExportReceipt(id, values);
            } else {
                response = await createExportReceipt(values);
            }

            if (response.status === 200 || response.status === 201) {
                messageApi.success(response.data.data.message);
            }
        } catch (error) {
            handleError(error, formik, messageApi);
        } finally {
            setSubmitting(false);
        }
    };

    const formik = useFormik({
        initialValues: defaultValue,
        validationSchema: validationSchema,
        onSubmit: handleSubmit,
        enableReinitialize: true,
    });

    const fetchBooks = async (keyword = '') => {
        setIsBooksLoading(true);
        try {
            const params = queryString.stringify({ keyword, searchBy: 'bookCode' });
            const response = await getBooks(params);
            const { items } = response.data.data;
            setBooks(items);
        } catch (error) {
            messageApi.error(error.message || 'Có lỗi xảy ra khi tải sách.');
        } finally {
            setIsBooksLoading(false);
        }
    };

    const handleAddNewColum = () => {
        if (!selectedBookId) {
            messageApi.error('Bạn phải chọn một cuốn sách');
            return;
        }

        const currentBooks = formik.values.bookIds;

        if (currentBooks.includes(selectedBookId)) {
            messageApi.error('Cuốn sách đã tồn tại trong danh sách');
            return;
        }

        const updatedBooks = [...currentBooks, selectedBookId];
        formik.setFieldValue('bookIds', updatedBooks);

        setSelectedBookId(null);
    };

    const handleDeleteColum = (id) => {
        const updatedBooks = formik.values.bookIds.filter((bookId) => bookId !== id);
        formik.setFieldValue('bookIds', updatedBooks);
    };

    useEffect(() => {
        fetchBooks();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (id) {
            if (!checkIdIsNumber(id)) {
                navigate('/admin/books/outward');
                return;
            }

            // Nếu có id, lấy thông tin phiếu xuất
            getExportReceiptById(id)
                .then((response) => {
                    const { receiptNumber, exportDate, exportReason, bookIds } = response.data.data;

                    formik.setValues({
                        receiptNumber,
                        exportDate: exportDate ? dayjs(exportDate) : null,
                        exportReason,
                        bookIds: bookIds,
                    });
                })
                .catch((error) => {
                    messageApi.error(error.message || 'Có lỗi xảy ra khi tải dữ liệu phiếu xuất.');
                });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);

    const columns = [
        {
            title: 'Id',
            dataIndex: 'bookId',
            key: 'bookId',
            align: 'center',
        },
        {
            title: '',
            key: 'action',
            render: (_, record) => (
                <Button type="text" danger icon={<FaRegTrashAlt />} onClick={() => handleDeleteColum(record.bookId)} />
            ),
        },
    ];

    return (
        <div>
            {contextHolder}

            <h2>{id ? 'Sửa phiếu xuất' : 'Thêm phiếu xuất'}</h2>

            <form onSubmit={formik.handleSubmit}>
                <div className="row g-3">
                    <FormInput id="receiptNumber" label="Số phiếu xuất" className="col-md-6" formik={formik} required />

                    <div className="col-md-3">
                        <label htmlFor="exportDate">
                            <span className="text-danger">*</span> Ngày xuất:
                        </label>
                        <DatePicker
                            id="exportDate"
                            name="exportDate"
                            value={formik.values.exportDate ? dayjs(formik.values.exportDate) : null}
                            onChange={(date) => formik.setFieldValue('exportDate', date ? date.toISOString() : null)}
                            onBlur={() => formik.setFieldTouched('exportDate', true)}
                            format="DD/MM/YYYY"
                            status={formik.touched.exportDate && formik.errors.exportDate ? 'error' : undefined}
                            className="w-100"
                        />
                        <div className="text-danger">{formik.touched.exportDate && formik.errors.exportDate}</div>
                    </div>

                    <FormTextArea id="exportReason" label="Lý do xuất" className="col-md-12" formik={formik} />

                    <div className="col-md-9">
                        <Select
                            showSearch
                            allowClear
                            onSearch={fetchBooks}
                            filterOption={false}
                            name="selectBooks"
                            placeholder="Chọn sách"
                            style={{ width: '100%' }}
                            options={books.map((book) => ({
                                label: book.bookCode,
                                value: book.id,
                            }))}
                            loading={isBooksLoading}
                            value={selectedBookId}
                            onChange={(value) => setSelectedBookId(value)}
                            dropdownRender={(menu) => (
                                <>
                                    {menu}
                                    <Divider className="my-2" />
                                    <Space className="py-2 px-1 pt-0">
                                        <a
                                            className="d-flex align-items-center"
                                            href="/admin/book-definitions/new"
                                            target="_blank"
                                        >
                                            <FaPlus />
                                            Thêm mới
                                        </a>
                                    </Space>
                                </>
                            )}
                        />
                    </div>

                    <div className="col-md-3">
                        <Button type="primary" icon={<FaPlusCircle />} onClick={handleAddNewColum}>
                            Thêm dòng mới
                        </Button>
                    </div>

                    <div className="col-md-12">
                        <Table
                            bordered
                            columns={columns}
                            dataSource={formik.values.bookIds.map((bookId) => ({ bookId }))}
                            rowKey="bookId"
                            pagination={false}
                        />
                        <div className="text-danger">{formik.touched.bookIds && formik.errors.bookIds}</div>
                    </div>

                    <div className="col-md-12 text-end">
                        <Space>
                            <Button onClick={() => navigate('/admin/books/outward')}>Quay lại</Button>
                            <Button type="primary" htmlType="submit" loading={formik.isSubmitting}>
                                Lưu
                            </Button>
                        </Space>
                    </div>
                </div>
            </form>
        </div>
    );
}

export default OutwardBookForm;
