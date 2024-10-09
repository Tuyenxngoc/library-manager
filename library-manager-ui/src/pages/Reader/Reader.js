import { useEffect, useState } from 'react';
import {
    Button,
    Col,
    DatePicker,
    Flex,
    Form,
    Input,
    message,
    Modal,
    Popconfirm,
    Row,
    Select,
    Space,
    Switch,
    Table,
} from 'antd';
import { MdOutlineModeEdit } from 'react-icons/md';
import { FaRegTrashAlt } from 'react-icons/fa';
import queryString from 'query-string';
import { INITIAL_FILTERS, INITIAL_META, REGEXP_PHONE_NUMBER } from '~/common/commonConstants';
import { createReader, deleteReader, getReaders, toggleActiveFlag, updateReader } from '~/services/readerService';
import moment from 'moment';

function Reader() {
    const [meta, setMeta] = useState(INITIAL_META);
    const [filters, setFilters] = useState(INITIAL_FILTERS);

    const [entityData, setEntityData] = useState(null);

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
            dateOfBirth: record.dateOfBirth ? moment(record.dateOfBirth, 'YYYY-MM-DD') : null,
            expiryDate: record.expiryDate ? moment(record.expiryDate, 'YYYY-MM-DD') : null,
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

    const handleCreateEntity = async (values) => {
        try {
            const formattedValues = {
                ...values,
                dateOfBirth: values.dateOfBirth ? values.dateOfBirth.format('YYYY-MM-DD') : undefined,
                expiryDate: values.expiryDate ? values.expiryDate.format('YYYY-MM-DD') : undefined,
            };

            const response = await createReader(formattedValues);
            if (response.status === 201) {
                const { data, message } = response.data.data;
                messageApi.success(message);

                setEntityData((prevData) => [...prevData, data]);
                closeAddModal();
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi thêm mới.';
            messageApi.error(errorMessage);
        }
    };

    const handleUpdateEntity = async (values) => {
        try {
            const formattedValues = {
                ...values,
                dateOfBirth: values.dateOfBirth ? values.dateOfBirth.format('YYYY-MM-DD') : undefined,
                expiryDate: values.expiryDate ? values.expiryDate.format('YYYY-MM-DD') : undefined,
            };

            const response = await updateReader(editingItem.id, formattedValues);
            if (response.status === 200) {
                const { data, message } = response.data.data;
                messageApi.success(message);

                setEntityData((prevData) => prevData.map((item) => (item.id === editingItem.id ? data : item)));
                closeEditModal();
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi cập nhật.';
            messageApi.error(errorMessage);
        }
    };

    const handleDeleteEntity = async (id) => {
        try {
            const response = await deleteReader(id);
            if (response.status === 200) {
                setEntityData((prev) => prev.filter((a) => a.id !== id));

                messageApi.success(response.data.data.message);
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi xóa.';
            messageApi.error(errorMessage);
        }
    };

    const handleToggleActiveFlag = async (checked, record) => {
        try {
            const response = await toggleActiveFlag(record.id);
            if (response.status === 200) {
                const { data, message } = response.data.data;
                setEntityData((prevData) =>
                    prevData.map((item) => (item.id === record.id ? { ...item, activeFlag: data } : item)),
                );
                messageApi.success(message);
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi cập nhật.';
            messageApi.error(errorMessage);
        }
    };

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const params = queryString.stringify(filters);
                const response = await getReaders(params);
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

    const columns = [
        {
            title: 'Số thẻ',
            dataIndex: 'cardNumber',
            key: 'cardNumber',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Họ tên',
            dataIndex: 'fullName',
            key: 'fullName',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Loại thẻ',
            dataIndex: 'cardType',
            key: 'cardType',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Ngày sinh',
            dataIndex: 'dateOfBirth',
            key: 'dateOfBirth',
            sorter: true,
            showSorterTooltip: false,
            render: (text) => (text ? new Date(text).toLocaleDateString() : 'N/A'),
        },
        {
            title: 'Số điện thoại',
            dataIndex: 'phoneNumber',
            key: 'phoneNumber',
            sorter: true,
            showSorterTooltip: false,
            render: (text) => text || 'N/A',
        },
        {
            title: 'Trạng thái',
            dataIndex: 'activeFlag',
            key: 'activeFlag',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => (
                <Space>
                    {text ? 'Đang theo dõi' : 'Ngừng theo dõi'}
                    <Switch checked={text} onChange={(checked) => handleToggleActiveFlag(checked, record)} />
                </Space>
            ),
        },
        {
            title: 'Số lượt mượn sách',
            dataIndex: 'currentBorrowedBooks',
            key: 'currentBorrowedBooks',
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

            {/* Modal thêm mới thẻ */}
            <Modal title="Thêm mới thẻ bạn đọc" open={isAddModalOpen} onOk={addForm.submit} onCancel={closeAddModal}>
                <Form form={addForm} layout="vertical" onFinish={handleCreateEntity}>
                    <Row gutter={16}>
                        {/* Loại thẻ */}
                        <Col span={12}>
                            <Form.Item
                                label="Loại thẻ"
                                name="cardType"
                                rules={[{ required: true, message: 'Vui lòng chọn loại thẻ' }]}
                            >
                                <Select placeholder="Chọn loại thẻ">
                                    <Select.Option value="STUDENT">Thẻ sinh viên</Select.Option>
                                    <Select.Option value="TEACHER">Thẻ giáo viên</Select.Option>
                                </Select>
                            </Form.Item>
                        </Col>

                        {/* Họ tên */}
                        <Col span={12}>
                            <Form.Item
                                label="Họ tên"
                                name="fullName"
                                rules={[
                                    { required: true, message: 'Vui lòng nhập họ tên' },
                                    { max: 100, message: 'Tên quá dài' },
                                ]}
                            >
                                <Input placeholder="Nhập họ tên" />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        {/* Ngày sinh */}
                        <Col span={12}>
                            <Form.Item
                                label="Ngày sinh"
                                name="dateOfBirth"
                                rules={[{ required: true, message: 'Vui lòng chọn ngày sinh' }]}
                            >
                                <DatePicker format="YYYY-MM-DD" placeholder="Chọn ngày sinh" className="w-100" />
                            </Form.Item>
                        </Col>

                        {/* Giới tính */}
                        <Col span={12}>
                            <Form.Item label="Giới tính" name="gender">
                                <Select placeholder="Chọn giới tính">
                                    <Select.Option value="MALE">Nam</Select.Option>
                                    <Select.Option value="FEMALE">Nữ</Select.Option>
                                </Select>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        {/* Địa chỉ */}
                        <Col span={12}>
                            <Form.Item
                                label="Địa chỉ"
                                name="address"
                                rules={[{ max: 255, message: 'Địa chỉ quá dài' }]}
                            >
                                <Input placeholder="Nhập địa chỉ" autoComplete="off" />
                            </Form.Item>
                        </Col>

                        {/* Số điện thoại */}
                        <Col span={12}>
                            <Form.Item
                                label="Số điện thoại"
                                name="phoneNumber"
                                rules={[
                                    { required: true, message: 'Vui lòng nhập số điện thoại' },
                                    { pattern: new RegExp(REGEXP_PHONE_NUMBER), message: 'Số điện thoại không hợp lệ' },
                                    { min: 10, max: 20, message: 'Số điện thoại phải từ 10 đến 20 ký tự' },
                                ]}
                            >
                                <Input placeholder="Nhập số điện thoại" />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        {/* Số thẻ */}
                        <Col span={12}>
                            <Form.Item
                                label="Số thẻ"
                                name="cardNumber"
                                rules={[
                                    { required: true, message: 'Vui lòng nhập số thẻ' },
                                    { max: 100, message: 'Số thẻ quá dài' },
                                ]}
                            >
                                <Input placeholder="Nhập số thẻ" />
                            </Form.Item>
                        </Col>

                        {/* Mật khẩu */}
                        <Col span={12}>
                            <Form.Item
                                label="Mật khẩu"
                                name="password"
                                rules={[
                                    { required: true, message: 'Vui lòng nhập mật khẩu' },
                                    { max: 100, message: 'Mật khẩu quá dài' },
                                ]}
                            >
                                <Input.Password placeholder="Nhập mật khẩu" />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        {/* Ngày hết hạn */}
                        <Col span={12}>
                            <Form.Item label="Ngày hết hạn" name="expiryDate">
                                <DatePicker format="YYYY-MM-DD" placeholder="Chọn ngày hết hạn" className="w-100" />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>

            {/* Modal chỉnh sửa */}
            <Modal title="Sửa nhóm loại sách" open={isEditModalOpen} onOk={editForm.submit} onCancel={closeEditModal}>
                <Form form={editForm} layout="vertical" onFinish={handleUpdateEntity}>
                    <Row gutter={16}>
                        {/* Loại thẻ */}
                        <Col span={12}>
                            <Form.Item
                                label="Loại thẻ"
                                name="cardType"
                                rules={[{ required: true, message: 'Vui lòng chọn loại thẻ' }]}
                            >
                                <Select placeholder="Chọn loại thẻ">
                                    <Select.Option value="STUDENT">Thẻ sinh viên</Select.Option>
                                    <Select.Option value="TEACHER">Thẻ giáo viên</Select.Option>
                                </Select>
                            </Form.Item>
                        </Col>

                        {/* Họ tên */}
                        <Col span={12}>
                            <Form.Item
                                label="Họ tên"
                                name="fullName"
                                rules={[
                                    { required: true, message: 'Vui lòng nhập họ tên' },
                                    { max: 100, message: 'Tên quá dài' },
                                ]}
                            >
                                <Input placeholder="Nhập họ tên" />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        {/* Ngày sinh */}
                        <Col span={12}>
                            <Form.Item
                                label="Ngày sinh"
                                name="dateOfBirth"
                                rules={[{ required: true, message: 'Vui lòng chọn ngày sinh' }]}
                            >
                                <DatePicker format="YYYY-MM-DD" placeholder="Chọn ngày sinh" className="w-100" />
                            </Form.Item>
                        </Col>

                        {/* Giới tính */}
                        <Col span={12}>
                            <Form.Item label="Giới tính" name="gender">
                                <Select placeholder="Chọn giới tính">
                                    <Select.Option value="MALE">Nam</Select.Option>
                                    <Select.Option value="FEMALE">Nữ</Select.Option>
                                </Select>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        {/* Địa chỉ */}
                        <Col span={12}>
                            <Form.Item
                                label="Địa chỉ"
                                name="address"
                                rules={[{ max: 255, message: 'Địa chỉ quá dài' }]}
                            >
                                <Input placeholder="Nhập địa chỉ" autoComplete="off" />
                            </Form.Item>
                        </Col>

                        {/* Số điện thoại */}
                        <Col span={12}>
                            <Form.Item
                                label="Số điện thoại"
                                name="phoneNumber"
                                rules={[
                                    { required: true, message: 'Vui lòng nhập số điện thoại' },
                                    { pattern: new RegExp(REGEXP_PHONE_NUMBER), message: 'Số điện thoại không hợp lệ' },
                                    { min: 10, max: 20, message: 'Số điện thoại phải từ 10 đến 20 ký tự' },
                                ]}
                            >
                                <Input placeholder="Nhập số điện thoại" />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        {/* Số thẻ */}
                        <Col span={12}>
                            <Form.Item
                                label="Số thẻ"
                                name="cardNumber"
                                rules={[
                                    { required: true, message: 'Vui lòng nhập số thẻ' },
                                    { max: 100, message: 'Số thẻ quá dài' },
                                ]}
                            >
                                <Input placeholder="Nhập số thẻ" />
                            </Form.Item>
                        </Col>

                        {/* Mật khẩu */}
                        <Col span={12}>
                            <Form.Item
                                label="Mật khẩu"
                                name="password"
                                rules={[{ max: 100, message: 'Mật khẩu quá dài' }]}
                            >
                                <Input.Password placeholder="Nhập mật khẩu" />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        {/* Ngày hết hạn */}
                        <Col span={12}>
                            <Form.Item label="Ngày hết hạn" name="expiryDate">
                                <DatePicker format="YYYY-MM-DD" placeholder="Chọn ngày hết hạn" className="w-100" />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>

            <Flex className="py-2" wrap justify="space-between" align="center">
                <h2>Thẻ bạn đọc</h2>

                <Button type="primary" onClick={showAddModal}>
                    Thêm mới
                </Button>
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

export default Reader;
