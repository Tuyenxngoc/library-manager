import { useEffect, useState } from 'react';
import { Button, Col, DatePicker, Flex, Form, Table, Tag } from 'antd';
import { Input, InputNumber, message, Modal, Popconfirm, Row, Select, Space } from 'antd';
import { MdOutlineModeEdit } from 'react-icons/md';
import { FaRegTrashAlt } from 'react-icons/fa';
import queryString from 'query-string';
import dayjs from 'dayjs';
import { INITIAL_FILTERS, INITIAL_META } from '~/common/commonConstants';
import { getReaders } from '~/services/readerService';
import { createReaderViolation, deleteReaderViolation } from '~/services/readerViolationsService';
import { getReaderViolations, updateReaderViolation } from '~/services/readerViolationsService';
import { cardPenaltyForm } from '~/common/cardConstants';

const options = [
    { value: 'cardNumber', label: 'Số thẻ' },
    { value: 'fullName', label: 'Họ tên' },
    { value: 'penaltyForm', label: 'Trạng thái' },
];

const penaltyFormMapping = {
    CARD_SUSPENSION: { label: 'Tạm dừng thẻ', color: 'blue' },
    CARD_REVOCATION: { label: 'Thu hồi thẻ', color: 'warning' },
    FINE: { label: 'Phạt tiền', color: 'red' },
};

const getTagByPenaltyForm = (text) => {
    const { label, color } = penaltyFormMapping[text] || { label: 'Khác', color: 'default' };
    return <Tag color={color}>{label}</Tag>;
};

function ReaderViolations() {
    const [meta, setMeta] = useState(INITIAL_META);
    const [filters, setFilters] = useState(INITIAL_FILTERS);

    const [entityData, setEntityData] = useState(null);
    const [readers, setReaders] = useState([]);
    const [isReadersLoading, setIsReadersLoading] = useState(true);

    const [searchInput, setSearchInput] = useState('');
    const [activeFilterOption, setActiveFilterOption] = useState(options[0].value);

    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);

    const [messageApi, contextHolder] = message.useMessage();

    // Modal thêm mới
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [addForm] = Form.useForm();

    // Modal chỉnh sửa
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editingItem, setEditingItem] = useState(null);
    const [editForm] = Form.useForm();

    const showAddModal = () => {
        setIsAddModalOpen(true);
    };

    const closeAddModal = () => {
        setIsAddModalOpen(false);
        addForm.resetFields();
    };

    const showEditModal = (record) => {
        const values = {
            ...record,
            penaltyDate: record.penaltyDate ? dayjs(record.penaltyDate) : null,
            endDate: record.endDate ? dayjs(record.endDate) : null,
            readerId: record.readerId,
        };

        setEditingItem(values);
        editForm.setFieldsValue(values);
        setIsEditModalOpen(true);
    };

    const closeEditModal = () => {
        setIsEditModalOpen(false);
        editForm.resetFields();
    };

    const handleChangePage = (newPage) => {
        setFilters((prev) => ({ ...prev, pageNum: newPage }));
    };

    const handleChangeRowsPerPage = (current, size) => {
        setFilters((prev) => ({
            ...prev,
            pageNum: 1,
            pageSize: size,
        }));
    };

    const handleSortChange = (pagination, filters, sorter) => {
        const sortOrder = sorter.order === 'ascend' ? true : sorter.order === 'descend' ? false : undefined;
        setFilters((prev) => ({
            ...prev,
            sortBy: sorter.field,
            isAscending: sortOrder,
        }));
    };

    const handleSearch = (searchBy, keyword) => {
        setFilters((prev) => ({
            ...prev,
            pageNum: 1,
            searchBy: searchBy || activeFilterOption,
            keyword: keyword || searchInput,
        }));
    };

    const handleCreateEntity = async (values) => {
        setIsLoading(true);
        try {
            const formattedValues = {
                ...values,
                penaltyDate: values.penaltyDate ? values.penaltyDate.format('YYYY-MM-DD') : undefined,
                endDate: values.endDate ? values.endDate.format('YYYY-MM-DD') : undefined,
            };

            const response = await createReaderViolation(formattedValues);
            if (response.status === 201) {
                const { data, message } = response.data.data;
                messageApi.success(message);

                setEntityData((prevData) =>
                    prevData.length >= filters.pageSize ? [data, ...prevData.slice(0, -1)] : [data, ...prevData],
                );
                closeAddModal();
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi thêm mới.';
            messageApi.error(errorMessage);
        } finally {
            setIsLoading(false);
        }
    };

    const handleUpdateEntity = async (values) => {
        setIsLoading(true);
        try {
            const formattedValues = {
                ...values,
                penaltyDate: values.penaltyDate ? values.penaltyDate.format('YYYY-MM-DD') : undefined,
                endDate: values.endDate ? values.endDate.format('YYYY-MM-DD') : undefined,
            };

            const response = await updateReaderViolation(editingItem.id, formattedValues);
            if (response.status === 200) {
                const { data, message } = response.data.data;
                messageApi.success(message);

                setEntityData((prevData) => prevData.map((item) => (item.id === editingItem.id ? data : item)));
                closeEditModal();
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi cập nhật.';
            messageApi.error(errorMessage);
        } finally {
            setIsLoading(false);
        }
    };

    const handleDeleteEntity = async (id) => {
        try {
            const response = await deleteReaderViolation(id);
            if (response.status === 200) {
                setEntityData((prev) => prev.filter((a) => a.id !== id));

                messageApi.success(response.data.data.message);
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi xóa.';
            messageApi.error(errorMessage);
        }
    };

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

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const params = queryString.stringify(filters);
                const response = await getReaderViolations(params);
                const { meta, items } = response.data.data;
                setEntityData(items);
                setMeta(meta);
            } catch (error) {
                setErrorMessage(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchEntities();
    }, [filters]);

    useEffect(() => {
        fetchReaders();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const columns = [
        {
            title: 'Số thẻ',
            dataIndex: 'cardNumber',
            key: 'cardNumber',
        },
        {
            title: 'Họ tên',
            dataIndex: 'fullName',
            key: 'fullName',
        },
        {
            title: 'Lý do phạt',
            dataIndex: 'violationDetails',
            key: 'violationDetails',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Hình thức phạt',
            dataIndex: 'penaltyForm',
            key: 'penaltyForm',
            sorter: true,
            showSorterTooltip: false,
            render: getTagByPenaltyForm,
        },
        {
            title: 'Số tiền',
            dataIndex: 'fineAmount',
            key: 'fineAmount',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Ngày phạt',
            dataIndex: 'penaltyDate',
            key: 'penaltyDate',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Ngày kết thúc',
            dataIndex: 'endDate',
            key: 'endDate',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: '',
            key: 'action',
            render: (_, record) => (
                <Space>
                    <Button type="text" icon={<MdOutlineModeEdit />} onClick={() => showEditModal(record)} />
                    <Popconfirm
                        title="Thông báo"
                        description={
                            <div>
                                Bạn có chắc muốn xóa <b>{record.fullName}</b> không?
                            </div>
                        }
                        onConfirm={() => handleDeleteEntity(record.id)}
                        okText="Xóa"
                        cancelText="Hủy"
                    >
                        <Button type="text" danger icon={<FaRegTrashAlt />} />
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    if (errorMessage) {
        return (
            <div className="alert alert-danger p-2" role="alert">
                Lỗi: {errorMessage}
            </div>
        );
    }

    return (
        <div>
            {contextHolder}

            {/* Modal thêm mới */}
            <Modal
                title="Thêm mới xử lý vi phạm"
                open={isAddModalOpen}
                onOk={addForm.submit}
                onCancel={closeAddModal}
                width={600}
                footer={[
                    <Button key="back" onClick={closeAddModal}>
                        Hủy
                    </Button>,
                    <Button key="submit" type="primary" loading={isLoading} onClick={addForm.submit}>
                        {isLoading ? 'Đang xử lý...' : 'Thêm mới'}
                    </Button>,
                ]}
            >
                <Form
                    form={addForm}
                    layout="vertical"
                    onFinish={handleCreateEntity}
                    initialValues={{
                        penaltyForm: cardPenaltyForm[0].value,
                        penaltyDate: dayjs(),
                        endDate: dayjs().add(1, 'month'),
                    }}
                >
                    <Row gutter={16}>
                        {/* Bạn đọc */}
                        <Col span={12}>
                            <Form.Item
                                label="Bạn đọc"
                                name="readerId"
                                hasFeedback
                                rules={[{ required: true, message: 'Vui lòng chọn bạn đọc' }]}
                            >
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
                                />
                            </Form.Item>
                        </Col>

                        {/* Nội dung vi phạm */}
                        <Col span={12}>
                            <Form.Item
                                label="Nội dung vi phạm"
                                name="violationDetails"
                                hasFeedback
                                rules={[
                                    { required: true, message: 'Vui lòng nhập nội dung vi phạm' },
                                    { max: 100, message: 'Nội dung vi phạm quá dài' },
                                ]}
                            >
                                <Input.TextArea
                                    rows={1}
                                    placeholder="Nhập nội dung vi phạm"
                                    maxLength={100}
                                    showCount
                                />
                            </Form.Item>
                        </Col>

                        {/* Hình thức phạt */}
                        <Col span={12}>
                            <Form.Item
                                label="Hình thức phạt"
                                name="penaltyForm"
                                hasFeedback
                                rules={[{ required: true, message: 'Vui lòng chọn hình thức phạt' }]}
                            >
                                <Select placeholder="Chọn hình thức phạt">
                                    {cardPenaltyForm.map((card) => (
                                        <Select.Option key={card.value} value={card.value}>
                                            {card.label}
                                        </Select.Option>
                                    ))}
                                </Select>
                            </Form.Item>
                        </Col>

                        {/* Hình thức khác */}
                        <Col span={12}>
                            <Form.Item
                                label="Hình thức khác"
                                name="otherPenaltyForm"
                                hasFeedback
                                rules={[{ max: 100, message: 'Độ dài tối đa 100 ký tự' }]}
                            >
                                <Input placeholder="Nhập hình thức khác" />
                            </Form.Item>
                        </Col>

                        {/* Ngày phạt */}
                        <Col span={12}>
                            <Form.Item
                                label="Ngày phạt"
                                name="penaltyDate"
                                hasFeedback
                                rules={[{ required: true, message: 'Vui lòng chọn ngày phạt' }]}
                            >
                                <DatePicker format="YYYY-MM-DD" placeholder="Chọn ngày phạt" className="w-100" />
                            </Form.Item>
                        </Col>

                        {/* Ngày hết hạn */}
                        <Col span={12}>
                            <Form.Item label="Ngày hết hạn" name="endDate">
                                <DatePicker format="YYYY-MM-DD" placeholder="Chọn ngày hết hạn" className="w-100" />
                            </Form.Item>
                        </Col>

                        {/* Số tiền phạt */}
                        <Col span={12}>
                            <Form.Item label="Số tiền phạt" name="fineAmount">
                                <InputNumber min={0} placeholder="Nhập số tiền phạt" className="w-100" addonAfter="đ" />
                            </Form.Item>
                        </Col>

                        {/* Ghi chú */}
                        <Col span={12}>
                            <Form.Item
                                label="Ghi chú"
                                name="notes"
                                rules={[{ max: 255, message: 'Độ dài tối đa 255 ký tự' }]}
                            >
                                <Input.TextArea rows={1} placeholder="Nhập ghi chú" maxLength={255} showCount />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>

            {/* Modal chỉnh sửa */}
            <Modal
                title="Chỉnh sửa xử lý vi phạm"
                open={isEditModalOpen}
                onOk={editForm.submit}
                onCancel={closeEditModal}
                width={600}
                footer={[
                    <Button key="back" onClick={closeEditModal}>
                        Hủy
                    </Button>,
                    <Button key="submit" type="primary" loading={isLoading} onClick={editForm.submit}>
                        {isLoading ? 'Đang xử lý...' : 'Lưu thay đổi'}
                    </Button>,
                ]}
            >
                <Form form={editForm} layout="vertical" onFinish={handleUpdateEntity}>
                    <Row gutter={16}>
                        {/* Bạn đọc */}
                        <Col span={12}>
                            <Form.Item
                                label="Bạn đọc"
                                name="readerId"
                                hasFeedback
                                rules={[{ required: true, message: 'Vui lòng chọn bạn đọc' }]}
                            >
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
                                />
                            </Form.Item>
                        </Col>

                        {/* Nội dung vi phạm */}
                        <Col span={12}>
                            <Form.Item
                                label="Nội dung vi phạm"
                                name="violationDetails"
                                hasFeedback
                                rules={[
                                    { required: true, message: 'Vui lòng nhập nội dung vi phạm' },
                                    { max: 100, message: 'Nội dung vi phạm quá dài' },
                                ]}
                            >
                                <Input.TextArea
                                    rows={1}
                                    placeholder="Nhập nội dung vi phạm"
                                    maxLength={100}
                                    showCount
                                />
                            </Form.Item>
                        </Col>

                        {/* Hình thức phạt */}
                        <Col span={12}>
                            <Form.Item
                                label="Hình thức phạt"
                                name="penaltyForm"
                                hasFeedback
                                rules={[{ required: true, message: 'Vui lòng chọn hình thức phạt' }]}
                            >
                                <Select placeholder="Chọn hình thức phạt">
                                    {cardPenaltyForm.map((card) => (
                                        <Select.Option key={card.value} value={card.value}>
                                            {card.label}
                                        </Select.Option>
                                    ))}
                                </Select>
                            </Form.Item>
                        </Col>

                        {/* Hình thức khác */}
                        <Col span={12}>
                            <Form.Item
                                label="Hình thức khác"
                                name="otherPenaltyForm"
                                hasFeedback
                                rules={[{ max: 100, message: 'Độ dài tối đa 100 ký tự' }]}
                            >
                                <Input placeholder="Nhập hình thức khác" />
                            </Form.Item>
                        </Col>

                        {/* Ngày phạt */}
                        <Col span={12}>
                            <Form.Item
                                label="Ngày phạt"
                                name="penaltyDate"
                                hasFeedback
                                rules={[{ required: true, message: 'Vui lòng chọn ngày phạt' }]}
                            >
                                <DatePicker format="YYYY-MM-DD" placeholder="Chọn ngày phạt" className="w-100" />
                            </Form.Item>
                        </Col>

                        {/* Ngày hết hạn */}
                        <Col span={12}>
                            <Form.Item label="Ngày hết hạn" name="endDate">
                                <DatePicker format="YYYY-MM-DD" placeholder="Chọn ngày hết hạn" className="w-100" />
                            </Form.Item>
                        </Col>

                        {/* Số tiền phạt */}
                        <Col span={12}>
                            <Form.Item label="Số tiền phạt" name="fineAmount">
                                <InputNumber min={0} placeholder="Nhập số tiền phạt" className="w-100" addonAfter="đ" />
                            </Form.Item>
                        </Col>

                        {/* Ghi chú */}
                        <Col span={12}>
                            <Form.Item
                                label="Ghi chú"
                                name="notes"
                                rules={[{ max: 255, message: 'Độ dài tối đa 255 ký tự' }]}
                            >
                                <Input.TextArea rows={1} placeholder="Nhập ghi chú" maxLength={255} showCount />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>

            <Flex className="py-2" wrap justify="space-between" align="center">
                <h2>Xử lý vi phạm</h2>

                <Space>
                    <Space.Compact className="my-2">
                        <Select
                            options={options}
                            disabled={isLoading}
                            value={activeFilterOption}
                            onChange={(value) => setActiveFilterOption(value)}
                        />
                        <Input
                            allowClear
                            name="searchInput"
                            placeholder="Nhập từ cần tìm..."
                            value={searchInput}
                            disabled={isLoading}
                            onChange={(e) => setSearchInput(e.target.value)}
                        />
                        <Button type="primary" loading={isLoading} onClick={() => handleSearch()}>
                            Tìm
                        </Button>
                    </Space.Compact>

                    <Button type="primary" onClick={showAddModal} loading={isLoading}>
                        Thêm mới
                    </Button>
                </Space>
            </Flex>

            <Table
                bordered
                rowKey="id"
                dataSource={entityData}
                columns={columns}
                loading={isLoading}
                onChange={handleSortChange}
                pagination={{
                    current: filters.pageNum,
                    pageSize: filters.pageSize,
                    total: meta.totalElements,
                    onChange: handleChangePage,
                    showSizeChanger: true,
                    onShowSizeChange: handleChangeRowsPerPage,
                }}
            />
        </div>
    );
}

export default ReaderViolations;
