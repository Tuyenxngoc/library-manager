import { useEffect, useState } from 'react';
import {
    Button,
    Col,
    DatePicker,
    Dropdown,
    Flex,
    Form,
    Image,
    Input,
    message,
    Modal,
    Popconfirm,
    Row,
    Select,
    Space,
    Switch,
    Table,
    Tag,
    Upload,
} from 'antd';
import { MdOutlineModeEdit, MdOutlineFileUpload } from 'react-icons/md';
import { FaRegTrashAlt, FaPrint } from 'react-icons/fa';
import queryString from 'query-string';
import moment from 'moment';
import { INITIAL_FILTERS, INITIAL_META, REGEXP_PHONE_NUMBER } from '~/common/commonConstants';
import {
    createReader,
    deleteReader,
    getReaders,
    printCards,
    toggleActiveFlag,
    updateReader,
} from '~/services/readerService';
import images from '~/assets';

const cardTypeMapping = {
    TEACHER: { label: 'Giảng viên', color: 'blue' },
    STUDENT: { label: 'Học sinh', color: 'green' },
};

const getTagByCardType = (text) => {
    const { label, color } = cardTypeMapping[text] || { label: 'Khác', color: 'default' };
    return <Tag color={color}>{label}</Tag>;
};

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

    const [selectedRowKeys, setSelectedRowKeys] = useState([]);
    const [fileList, setFileList] = useState([]);
    const [previousImage, setPreviousImage] = useState(images.placeimg);

    const handleUploadChange = ({ file, fileList }) => {
        setFileList(fileList);

        const { originFileObj } = file;
        if (!originFileObj) {
            return;
        }

        // Tạo URL cho hình ảnh và cập nhật giá trị trong form
        const url = URL.createObjectURL(originFileObj);
        setPreviousImage(url);
        if (addForm) {
            addForm.setFieldValue('image', originFileObj);
        }
        if (editForm) {
            editForm.setFieldValue('image', originFileObj);
        }
    };

    const showAddModal = () => {
        setIsAddModalOpen(true);
    };

    const closeAddModal = () => {
        setIsAddModalOpen(false);
        addForm.resetFields();
        setPreviousImage(images.placeimg);
        setFileList([]);
    };

    const showEditModal = (record) => {
        const values = {
            ...record,
            dateOfBirth: record.dateOfBirth ? moment(record.dateOfBirth) : null,
            expiryDate: record.expiryDate ? moment(record.expiryDate) : null,
        };
        setEditingItem(values);
        editForm.setFieldsValue(values);
        setPreviousImage(record.avatar || images.placeimg);
        setIsEditModalOpen(true);
    };

    const closeEditModal = () => {
        setIsEditModalOpen(false);
        editForm.resetFields();
        setPreviousImage(images.placeimg);
        setFileList([]);
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

                setEntityData((prevData) => [data, ...prevData.slice(0, -1)]);
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

    const handleCreateCard = async () => {
        if (selectedRowKeys.length === 0) {
            messageApi.info('Vui lòng chọn ít nhất một thẻ.');
            return;
        }
        setIsLoading(true);
        try {
            const response = await printCards({
                principalName: 'Kiều Xuân Thực',
                managementUnit: 'Bộ Công Thương',
                schoolName: 'Trường Đại học Công nghiệp Hà Nội',
                readerIds: selectedRowKeys,
            });

            if (response.status === 200) {
                // Chuyển đổi mảng byte thành Blob
                const pdfBlob = new Blob([response.data], { type: 'application/pdf' });
                const url = URL.createObjectURL(pdfBlob);

                // Mở hoặc tải xuống file PDF
                const newTab = window.open(url, '_blank');
                newTab.focus(); // Đưa tab mới lên trước

                // Giải phóng URL sau khi sử dụng
                URL.revokeObjectURL(url);
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi xuất dữ liệu.';
            messageApi.error(errorMessage);
        } finally {
            setIsLoading(false);
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

    const rowSelection = {
        onChange: (selectedRowKeys) => {
            setSelectedRowKeys(selectedRowKeys);
        },
        getCheckboxProps: (record) => ({
            name: record.id,
        }),
    };

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
            render: getTagByCardType,
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

    const items = [
        {
            label: <span onClick={handleCreateCard}>In thẻ bạn đọc</span>,
            key: '0',
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
            <Modal
                title="Thêm mới thẻ bạn đọc"
                open={isAddModalOpen}
                onOk={addForm.submit}
                onCancel={closeAddModal}
                width={800}
            >
                <Form form={addForm} layout="vertical" onFinish={handleCreateEntity}>
                    <Row gutter={16}>
                        <Col span={8} className="text-center">
                            <Image width={200} src={previousImage} fallback={images.placeimg} />
                            <Form.Item className="mt-2" name="image">
                                <Upload
                                    accept="image/*"
                                    fileList={fileList}
                                    maxCount={1}
                                    showUploadList={false}
                                    beforeUpload={(file) => {
                                        const isImage = file.type.startsWith('image/');
                                        if (!isImage) {
                                            messageApi.error('Bạn chỉ có thể upload file hình ảnh!');
                                        }
                                        return isImage;
                                    }}
                                    onChange={handleUploadChange}
                                    customRequest={() => false}
                                >
                                    <Button icon={<MdOutlineFileUpload />}>Chọn ảnh thẻ</Button>
                                </Upload>
                            </Form.Item>
                        </Col>
                        <Col span={16}>
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

                                {/* Ngày sinh */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Ngày sinh"
                                        name="dateOfBirth"
                                        rules={[{ required: true, message: 'Vui lòng chọn ngày sinh' }]}
                                    >
                                        <DatePicker
                                            format="YYYY-MM-DD"
                                            placeholder="Chọn ngày sinh"
                                            className="w-100"
                                        />
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
                                            {
                                                pattern: new RegExp(REGEXP_PHONE_NUMBER),
                                                message: 'Số điện thoại không hợp lệ',
                                            },
                                            { min: 10, max: 20, message: 'Số điện thoại phải từ 10 đến 20 ký tự' },
                                        ]}
                                    >
                                        <Input placeholder="Nhập số điện thoại" />
                                    </Form.Item>
                                </Col>

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

                                {/* Ngày hết hạn */}
                                <Col span={12}>
                                    <Form.Item label="Ngày hết hạn" name="expiryDate">
                                        <DatePicker
                                            format="YYYY-MM-DD"
                                            placeholder="Chọn ngày hết hạn"
                                            className="w-100"
                                        />
                                    </Form.Item>
                                </Col>
                            </Row>
                        </Col>
                    </Row>
                </Form>
            </Modal>

            {/* Modal chỉnh sửa */}
            <Modal
                title="Chỉnh sửa thẻ bạn đọc"
                open={isEditModalOpen}
                onOk={editForm.submit}
                onCancel={closeEditModal}
                width={800}
            >
                <Form form={editForm} layout="vertical" onFinish={handleUpdateEntity}>
                    <Row gutter={16}>
                        <Col span={8} className="text-center">
                            <Image width={200} src={previousImage} fallback={images.placeimg} />
                            <Form.Item className="mt-2" name="image">
                                <Upload
                                    accept="image/*"
                                    fileList={fileList}
                                    maxCount={1}
                                    showUploadList={false}
                                    beforeUpload={(file) => {
                                        const isImage = file.type.startsWith('image/');
                                        if (!isImage) {
                                            messageApi.error('Bạn chỉ có thể upload file hình ảnh!');
                                        }
                                        return isImage;
                                    }}
                                    onChange={handleUploadChange}
                                    customRequest={() => false}
                                >
                                    <Button icon={<MdOutlineFileUpload />}>Chọn ảnh thẻ</Button>
                                </Upload>
                            </Form.Item>
                        </Col>
                        <Col span={16}>
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

                                {/* Ngày sinh */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Ngày sinh"
                                        name="dateOfBirth"
                                        rules={[{ required: true, message: 'Vui lòng chọn ngày sinh' }]}
                                    >
                                        <DatePicker
                                            format="YYYY-MM-DD"
                                            placeholder="Chọn ngày sinh"
                                            className="w-100"
                                        />
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
                                            {
                                                pattern: new RegExp(REGEXP_PHONE_NUMBER),
                                                message: 'Số điện thoại không hợp lệ',
                                            },
                                            { min: 10, max: 20, message: 'Số điện thoại phải từ 10 đến 20 ký tự' },
                                        ]}
                                    >
                                        <Input placeholder="Nhập số điện thoại" />
                                    </Form.Item>
                                </Col>

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
                                    <Form.Item label="Mật khẩu" name="password">
                                        <Input.Password placeholder="Nhập mật khẩu" />
                                    </Form.Item>
                                </Col>

                                {/* Ngày hết hạn */}
                                <Col span={12}>
                                    <Form.Item label="Ngày hết hạn" name="expiryDate">
                                        <DatePicker
                                            format="YYYY-MM-DD"
                                            placeholder="Chọn ngày hết hạn"
                                            className="w-100"
                                        />
                                    </Form.Item>
                                </Col>
                            </Row>
                        </Col>
                    </Row>
                </Form>
            </Modal>

            <Flex className="py-2" wrap justify="space-between" align="center">
                <h2>Thẻ bạn đọc</h2>

                <Space>
                    <Button type="primary" onClick={showAddModal}>
                        Thêm mới
                    </Button>
                    <Dropdown menu={{ items }} trigger={['click']}>
                        <Button icon={<FaPrint />}></Button>
                    </Dropdown>
                </Space>
            </Flex>

            <Table
                bordered
                rowKey="id"
                dataSource={entityData}
                columns={columns}
                loading={isLoading}
                onChange={handleSortChange}
                rowSelection={rowSelection}
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
