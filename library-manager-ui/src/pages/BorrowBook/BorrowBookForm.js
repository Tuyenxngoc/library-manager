import { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, DatePicker, Divider, message, Select, Space, Table } from 'antd';
import { FaPlusCircle } from 'react-icons/fa';
import { FaRegTrashAlt } from 'react-icons/fa';
import { FaPlus } from 'react-icons/fa6';
import queryString from 'query-string';
import dayjs from 'dayjs';
import FormInput from '~/components/FormInput';
import FormTextArea from '~/components/FormTextArea';
import { handleError } from '~/utils/errorHandler';
import { getBooks } from '~/services/bookService';
import { createBorrowReceipt, getBorrowReceiptById, updateBorrowReceipt } from '~/services/borrowReceiptService';
import { checkIdIsNumber } from '~/utils/helper';
import { getReaders } from '~/services/readerService';

const defaultValue = {
    receiptNumber: '',
    createdDate: dayjs().toISOString(),
    note: '',
    readerId: null,
    books: [],
};

const validationSchema = yup.object({
    receiptNumber: yup.string().required('Số phiếu mượn là bắt buộc'),
    createdDate: yup.date().nullable().required('Ngày tạo là bắt buộc').typeError('Ngày tạo không hợp lệ'),
    note: yup.string(),
    readerId: yup.number().min(1, 'ID bạn đọc phải là số hợp lệ').required('ID bạn đọc là bắt buộc'),
    books: yup
        .array()
        .of(
            yup.object({
                bookCode: yup.string().required('Mã sách là bắt buộc'),
                dueDate: yup.date().required('Ngày hẹn trả là bắt buộc').typeError('Ngày hẹn trả không hợp lệ'),
            }),
        )
        .min(1, 'Bạn phải chọn ít nhất một cuốn sách'),
});

function BorrowBookForm() {
    const { id } = useParams();
    const location = useLocation();
    const navigate = useNavigate();

    // Hàm để lấy giá trị tham số cartId từ URL
    const getQueryParams = () => {
        const queryParams = new URLSearchParams(location.search);
        return queryParams.get('cartId');
    };
    const cartId = getQueryParams();

    const [messageApi, contextHolder] = message.useMessage();

    const [books, setBooks] = useState([]);
    const [isBooksLoading, setIsBooksLoading] = useState(true);

    const [readers, setReaders] = useState([]);
    const [isReadersLoading, setIsReadersLoading] = useState(true);

    const [selectedBookCode, setSelectedBookCode] = useState(null);
    const [dueDate, setDueDate] = useState(dayjs());

    const handleSubmit = async (values, { setSubmitting }) => {
        try {
            let response;
            const dataToSubmit = {
                ...values,
                createdDate: values.createdDate ? dayjs(values.createdDate).toISOString() : null,
            };
            if (id) {
                response = await updateBorrowReceipt(id, dataToSubmit);
            } else {
                response = await createBorrowReceipt(dataToSubmit);
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

    const fetchReaders = async (keyword = '') => {
        setIsReadersLoading(true);
        try {
            const params = queryString.stringify({ keyword, searchBy: 'fullName' });
            const response = await getReaders(params);
            const { items } = response.data.data;
            setReaders(items);
        } catch (error) {
            messageApi.error(error.message || 'Có lỗi xảy ra khi tải bạn đọc.');
        } finally {
            setIsReadersLoading(false);
        }
    };

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
        if (!selectedBookCode) {
            messageApi.error('Bạn phải chọn một cuốn sách');
            return;
        }

        if (!dueDate) {
            messageApi.error('Vui lòng chọn ngày trả sách');
            return;
        }

        if (dayjs(dueDate).isBefore(dayjs(), 'day')) {
            messageApi.error('Ngày trả phải là một ngày trong tương lai');
            return;
        }

        const currentBooks = formik.values.books;
        if (currentBooks.some((book) => book.bookCode === selectedBookCode)) {
            messageApi.error('Cuốn sách đã tồn tại trong danh sách');
            return;
        }

        const updatedBooks = [...currentBooks, { bookCode: selectedBookCode, dueDate }];
        formik.setFieldValue('books', updatedBooks);
        setSelectedBookCode(null);
        setDueDate(dayjs());
    };

    const handleDeleteColum = (bookCode) => {
        const updatedBooks = formik.values.books.filter((book) => book.bookCode !== bookCode);
        formik.setFieldValue('books', updatedBooks);
    };

    useEffect(() => {
        fetchBooks();
        fetchReaders();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (id) {
            if (!checkIdIsNumber(id)) {
                navigate('/admin/circulation/borrow');
                return;
            }

            // Nếu có id, lấy thông tin phiếu mượn
            getBorrowReceiptById(id)
                .then((response) => {
                    const { receiptNumber, createdDate, note, readerId, books } = response.data.data;
                    formik.setValues({
                        receiptNumber,
                        createdDate: createdDate ? dayjs(createdDate) : null,
                        note,
                        readerId,
                        books: books.map((book) => ({ bookCode: book.bookCode, dueDate: book.dueDate })),
                    });
                })
                .catch((error) => {
                    messageApi.error(error.message || 'Có lỗi xảy ra khi tải dữ liệu phiếu mượn.');
                });
        } else if (cartId) {
            // getBorrowReceiptByCartId(cartId)
            //     .then((response) => {
            //         const { receiptNumber, createdDate, note, readerId, books } = response.data.data;
            //         formik.setValues({
            //             receiptNumber,
            //             createdDate: createdDate? dayjs(createdDate) : null,
            //             note,
            //             readerId,
            //             books: books.map((book) => ({ bookCode: book.bookCode, dueDate: book.dueDate })),
            //         });
            //     })
            //     .catch((error) => {
            //         messageApi.error(error.message || 'Có l��i xảy ra khi tải dữ liệu phiếu mượn.');
            //     });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);

    const columns = [
        {
            title: 'Mã Sách',
            dataIndex: 'bookCode',
            key: 'bookCode',
            align: 'center',
        },
        {
            title: 'Ngày hẹn trả',
            dataIndex: 'dueDate',
            key: 'dueDate',
            align: 'center',
            render: (text) => dayjs(text).format('DD/MM/YYYY'),
        },
        {
            title: '',
            key: 'action',
            render: (_, record) => (
                <Button
                    type="text"
                    danger
                    icon={<FaRegTrashAlt />}
                    onClick={() => handleDeleteColum(record.bookCode)}
                />
            ),
        },
    ];

    return (
        <div>
            {contextHolder}

            <h2>{id ? 'Sửa phiếu mượn' : 'Thêm phiếu mượn'}</h2>

            <form onSubmit={formik.handleSubmit}>
                <div className="row g-3">
                    <FormInput id="receiptNumber" label="Số phiếu mượn" className="col-md-6" formik={formik} required />

                    <div className="col-md-3">
                        <label htmlFor="createdDate">
                            <span className="text-danger">*</span> Ngày tạo:
                        </label>
                        <DatePicker
                            id="createdDate"
                            name="createdDate"
                            value={formik.values.createdDate ? dayjs(formik.values.createdDate) : null}
                            onChange={(date) => formik.setFieldValue('createdDate', date ? date.toISOString() : null)}
                            onBlur={() => formik.setFieldTouched('createdDate', true)}
                            format="DD/MM/YYYY"
                            status={formik.touched.createdDate && formik.errors.createdDate ? 'error' : undefined}
                            className="w-100"
                        />
                    </div>

                    <div className="col-12">
                        <Select
                            showSearch
                            placeholder="Chọn bạn đọc"
                            filterOption={false}
                            onSearch={fetchReaders}
                            loading={isReadersLoading}
                            options={readers.map((reader) => ({
                                value: reader.id,
                                label: `${reader.cardNumber} - ${reader.fullName}`,
                            }))}
                            className="w-100"
                            value={formik.values.readerId}
                            onChange={(value) => formik.setFieldValue('readerId', value)}
                            onBlur={() => formik.setFieldTouched('readerId', true)}
                            status={formik.touched.readerId && formik.errors.readerId ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.readerId && formik.errors.readerId}</div>
                    </div>

                    <FormTextArea id="note" label="Ghi chú" className="col-md-12" formik={formik} />

                    <div className="col-md-6">
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
                                value: book.bookCode,
                            }))}
                            loading={isBooksLoading}
                            value={selectedBookCode}
                            onChange={(value) => setSelectedBookCode(value)}
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
                        <DatePicker
                            name="dueDate"
                            value={dueDate}
                            onChange={(date) => setDueDate(date)}
                            format="DD/MM/YYYY"
                            className="w-100"
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
                            dataSource={formik.values.books}
                            rowKey="bookCode"
                            pagination={false}
                        />
                        <div className="text-danger">{formik.touched.books && formik.errors.books}</div>
                    </div>

                    <div className="col-md-12 text-end">
                        <Space>
                            <Button onClick={() => navigate('/admin/circulation/borrow')}>Quay lại</Button>
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

export default BorrowBookForm;
