import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, DatePicker, Divider, InputNumber, message, Select, Space, Table } from 'antd';
import { FaPlusCircle } from 'react-icons/fa';
import { FaRegTrashAlt } from 'react-icons/fa';
import { FaPlus } from 'react-icons/fa6';
import queryString from 'query-string';
import dayjs from 'dayjs';
import FormInput from '~/components/FormInput';
import { getBookDefinitions } from '~/services/bookDefinitionService';
import { createImportReceipt, getImportReceiptById, updateImportReceipt } from '~/services/importReceiptService';
import { handleError } from '~/utils/errorHandler';
import { checkIdIsNumber } from '~/utils/helper';
const { Option } = Select;

const defaultValue = {
    receiptNumber: '',
    importDate: new Date(),
    generalRecordNumber: '',
    fundingSource: '',
    importReason: '',
    bookRequestDtos: [],
};

const validationSchema = yup.object({
    receiptNumber: yup.string().required('Số phiếu nhập là bắt buộc'),

    importDate: yup.date().nullable().required('Ngày nhập là bắt buộc').typeError('Ngày nhập không hợp lệ'),

    fundingSource: yup.string().required('Nguồn cấp là bắt buộc'),

    bookRequestDtos: yup
        .array()
        .of(
            yup.object().shape({
                bookDefinitionId: yup.number().min(1, 'Bạn phải chọn một cuốn sách'),
                quantity: yup.number().min(1, 'Số lượng phải lớn hơn 0').required('Số lượng là bắt buộc'),
            }),
        )
        .min(1, 'Bạn phải chọn ít nhất một cuốn sách để nhập'),
});

function InwardBookForm() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [messageApi, contextHolder] = message.useMessage();

    const [bookDefinitions, setBookDefinitions] = useState([]);
    const [isBookDefinitionsLoading, setIsBookDefinitionsLoading] = useState(true);

    const [quantity, setQuantity] = useState(1);
    const [selectedBookDefinitionId, setSelectedBookDefinitionId] = useState(null);

    const handleSubmit = async (values, { setSubmitting }) => {
        try {
            let response;
            if (id) {
                response = await updateImportReceipt(id, values);
            } else {
                response = await createImportReceipt(values);
            }

            if (response.status === 200) {
                messageApi.success(response.data.data.message);
            } else if (response.status === 201) {
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

    const fetchBookDefinitions = async (keyword = '') => {
        setIsBookDefinitionsLoading(true);
        try {
            const params = queryString.stringify({ keyword, searchBy: 'title', activeFlag: true });
            const response = await getBookDefinitions(params);
            const { items } = response.data.data;
            setBookDefinitions(items);
        } catch (error) {
            messageApi.error(error.message || 'Có lỗi xảy ra khi tải biên mục.');
        } finally {
            setIsBookDefinitionsLoading(false);
        }
    };

    const handleAddNewColum = () => {
        if (!selectedBookDefinitionId) {
            messageApi.error('Bạn phải chọn một cuốn sách');
            return;
        }
        if (quantity < 1) {
            messageApi.error('Số lượng phải lớn hơn 0');
            return;
        }

        const currentBooks = formik.values.bookRequestDtos;

        if (currentBooks.some((book) => book.bookDefinitionId === selectedBookDefinitionId)) {
            messageApi.error('Cuốn sách đã tồn tại trong danh sách');
            return;
        }

        const updatedBooks = [...currentBooks, { bookDefinitionId: selectedBookDefinitionId, quantity }];
        formik.setFieldValue('bookRequestDtos', updatedBooks);

        setSelectedBookDefinitionId(null);
        setQuantity(1);
    };

    const handleDeleteColum = (id) => {
        const currentBooks = formik.values.bookRequestDtos;
        const updatedBooks = currentBooks.filter((book) => book.bookDefinitionId !== id);
        formik.setFieldValue('bookRequestDtos', updatedBooks);
    };

    useEffect(() => {
        fetchBookDefinitions();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (id) {
            if (!checkIdIsNumber(id)) {
                navigate('/admin/books/inward');
                return;
            }

            // Nếu có id, lấy thông tin phiếu nhập
            getImportReceiptById(id)
                .then((response) => {
                    const { receiptNumber, importDate, generalRecordNumber, fundingSource, importReason, books } =
                        response.data.data;

                    formik.setValues({
                        receiptNumber,
                        importDate: importDate ? dayjs(importDate) : null,
                        generalRecordNumber,
                        fundingSource,
                        importReason,
                        bookRequestDtos: books,
                    });
                })
                .catch((error) => {
                    console.log(error);
                });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);

    const columns = [
        {
            title: 'Id',
            dataIndex: 'bookDefinitionId',
            key: 'bookDefinitionId',
            align: 'center',
        },
        {
            title: 'Số lượng',
            dataIndex: 'quantity',
            key: 'quantity',
            align: 'center',
        },
        {
            title: '',
            key: 'action',
            render: (_, record) => (
                <Button
                    type="text"
                    danger
                    icon={<FaRegTrashAlt />}
                    onClick={() => handleDeleteColum(record.bookDefinitionId)}
                />
            ),
        },
    ];

    return (
        <div>
            {contextHolder}

            <h2>{id ? 'Sửa phiếu nhập' : 'Thêm phiếu nhập'}</h2>

            <form onSubmit={formik.handleSubmit}>
                <div className="row g-3">
                    <FormInput id="receiptNumber" label="Số phiếu nhập" className="col-md-6" formik={formik} required />

                    <div className="col-md-3">
                        <label htmlFor="importDate">
                            <span className="text-danger">*</span> Ngày nhập:
                        </label>
                        <DatePicker
                            id="importDate"
                            name="importDate"
                            value={formik.values.importDate ? dayjs(formik.values.importDate) : null}
                            onChange={(date) => formik.setFieldValue('importDate', date ? date.toISOString() : null)}
                            onBlur={() => formik.setFieldTouched('importDate', true)}
                            format="DD/MM/YYYY"
                            status={formik.touched.importDate && formik.errors.importDate ? 'error' : undefined}
                            className="w-100"
                        />
                        <div className="text-danger">{formik.touched.importDate && formik.errors.importDate}</div>
                    </div>

                    <div className="col-md-3">
                        <label htmlFor="fundingSource">
                            <span className="text-danger">*</span> Nguồn cấp:
                        </label>
                        <Select
                            id="fundingSource"
                            name="fundingSource"
                            value={formik.values.fundingSource}
                            onChange={(value) => formik.setFieldValue('fundingSource', value)}
                            onBlur={formik.handleBlur}
                            status={formik.touched.fundingSource && formik.errors.fundingSource ? 'error' : undefined}
                            className="w-100"
                        >
                            <Option value="Trường tự mua">Trường tự mua</Option>
                            <Option value="PGD cấp phát">PGD cấp phát</Option>
                            <Option value="SGD cấp phát">SGD cấp phát</Option>
                            <Option value="Biếu, tặng, quyên góp">Biếu, tặng, quyên góp</Option>
                            <Option value="Khác">Khác</Option>
                        </Select>
                        <div className="text-danger">{formik.touched.fundingSource && formik.errors.fundingSource}</div>
                    </div>

                    <FormInput
                        id="generalRecordNumber"
                        label="Số vào sổ tổng quát"
                        className="col-md-6"
                        formik={formik}
                    />

                    <FormInput id="importReason" label="Lý do nhập" className="col-md-6" formik={formik} />

                    <div className="col-md-6">
                        <Select
                            showSearch
                            allowClear
                            onSearch={fetchBookDefinitions}
                            filterOption={false}
                            name="selectBookDefinition"
                            placeholder="Chọn biên mục sách"
                            style={{ width: '100%' }}
                            options={bookDefinitions}
                            loading={isBookDefinitionsLoading}
                            value={selectedBookDefinitionId}
                            fieldNames={{ label: 'title', value: 'id' }}
                            onChange={(value) => setSelectedBookDefinitionId(value)}
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
                        <InputNumber
                            min={1}
                            max={100}
                            name="txtQuantity"
                            className="w-100"
                            value={quantity}
                            onChange={(value) => setQuantity(value)}
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
                            dataSource={formik.values.bookRequestDtos}
                            rowKey="bookDefinitionId"
                            pagination={false}
                        />
                        <div className="text-danger">
                            {formik.touched.bookRequestDtos && formik.errors.bookRequestDtos}
                        </div>
                    </div>

                    <div className="col-md-12 text-end">
                        <Space>
                            <Button onClick={() => navigate('/admin/books/inward')}>Quay lại</Button>
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

export default InwardBookForm;
