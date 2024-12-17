import { useEffect, useState } from 'react';
import { Image, Input, message, Modal, Popconfirm, Row, Select, Space } from 'antd';
import { Button, Col, DatePicker, Dropdown, Flex, Form, Table, Tag, Upload } from 'antd';
import { MdOutlineModeEdit, MdOutlineFileUpload } from 'react-icons/md';
import { FaRegTrashAlt, FaPrint } from 'react-icons/fa';
import queryString from 'query-string';
import dayjs from 'dayjs';
import images from '~/assets';
import { INITIAL_FILTERS, INITIAL_META } from '~/common/commonConstants';
import { REGEXP_FULL_NAME, REGEXP_PASSWORD, REGEXP_PHONE_NUMBER } from '~/common/commonConstants';
import { createReader, deleteReader, getReaders, printCards, updateReader } from '~/services/readerService';
import { cardGender, cardStatus, cardTypes } from '~/common/cardConstants';

const options = [
    { value: 'cardNumber', label: 'Số thẻ' },
    { value: 'fullName', label: 'Họ tên' },
    { value: 'status', label: 'Trạng thái' },
];

const cardTypeMapping = {
    TEACHER: { label: 'Giảng viên', color: 'blue' },
    STUDENT: { label: 'Học sinh', color: 'green' },
};

const cardStatusMapping = {
    ACTIVE: { label: 'Đã kích hoạt', color: 'green' },
    INACTIVE: { label: 'Chưa kích hoạt', color: 'default' },
    SUSPENDED: { label: 'Tạm dừng', color: 'warning' },
    REVOKED: { label: 'Thu hồi thẻ', color: 'red' },
};

const getTagByCardType = (text) => {
    const { label, color } = cardTypeMapping[text] || { label: 'Khác', color: 'default' };
    return <Tag color={color}>{label}</Tag>;
};

const getTagByCardStatus = (text) => {
    const { label, color } = cardStatusMapping[text] || { label: 'Khác', color: 'default' };
    return <Tag color={color}>{label}</Tag>;
};

function Reader() {
    const [meta, setMeta] = useState(INITIAL_META);
    const [filters, setFilters] = useState(INITIAL_FILTERS);

    const [entityData, setEntityData] = useState(null);

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
        if (isAddModalOpen) {
            addForm.setFieldValue('image', originFileObj);
        } else if (isEditModalOpen) {
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
            dateOfBirth: record.dateOfBirth ? dayjs(record.dateOfBirth) : null,
            expiryDate: record.expiryDate ? dayjs(record.expiryDate) : null,
        };

        setPreviousImage(record.avatar || images.placeimg);

        setEditingItem(values);
        editForm.setFieldsValue(values);
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
                dateOfBirth: values.dateOfBirth ? values.dateOfBirth.format('YYYY-MM-DD') : undefined,
                expiryDate: values.expiryDate ? values.expiryDate.format('YYYY-MM-DD') : undefined,
            };

            const response = await createReader(formattedValues);
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
        } finally {
            setIsLoading(false);
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
        getCheckboxProps: (record, index) => ({
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
            dataIndex: 'status',
            key: 'status',
            sorter: true,
            showSorterTooltip: false,
            render: getTagByCardStatus,
        },
        {
            title: 'Thống kê',
            children: [
                {
                    title: 'Sách mượn',
                    dataIndex: 'currentBorrowedBooks',
                    key: 'currentBorrowedBooks',
                },
                {
                    title: 'Vào thư viện',
                    dataIndex: 'libraryVisitCount',
                    key: 'libraryVisitCount',
                },
            ],
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
            key: '0',
            label: 'In thẻ bạn đọc',
            onClick: handleCreateCard,
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
                        cardType: cardTypes[0].value,
                        status: cardStatus[0].value,
                        gender: cardGender[0].value,
                        dateOfBirth: dayjs(),
                        expiryDate: dayjs().add(1, 'month'),
                    }}
                >
                    <Row gutter={16}>
                        <Col span={8} className="text-center">
                            <Image width={200} src={previousImage} fallback={images.placeimg} />
                            <Form.Item
                                className="mt-2"
                                name="image"
                                help="Vui lòng chọn hình ảnh dung lượng không quá 2MB"
                            >
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
                                <Col span={24}>
                                    <Form.Item
                                        label="Loại thẻ"
                                        name="cardType"
                                        rules={[{ required: true, message: 'Vui lòng chọn loại thẻ' }]}
                                    >
                                        <Select placeholder="Chọn loại thẻ">
                                            {cardTypes.map((card) => (
                                                <Select.Option key={card.value} value={card.value}>
                                                    {card.label}
                                                </Select.Option>
                                            ))}
                                        </Select>
                                    </Form.Item>
                                </Col>

                                {/* Họ tên */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Họ tên"
                                        name="fullName"
                                        hasFeedback
                                        rules={[
                                            { required: true, message: 'Vui lòng nhập họ tên' },
                                            {
                                                pattern: REGEXP_FULL_NAME,
                                                message: 'Họ tên không hợp lệ',
                                            },
                                            { min: 2, max: 100, message: 'Độ dài từ 2 - 100 kí tự' },
                                        ]}
                                    >
                                        <Input placeholder="Nhập họ tên" />
                                    </Form.Item>
                                </Col>

                                {/* Ngày sinh */}
                                <Col span={12}>
                                    <Form.Item label="Ngày sinh" name="dateOfBirth">
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
                                            {cardGender.map((card) => (
                                                <Select.Option key={card.value} value={card.value}>
                                                    {card.label}
                                                </Select.Option>
                                            ))}
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

                                {/* Email */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Email"
                                        name="email"
                                        rules={[
                                            {
                                                type: 'email',
                                                message: 'Địa chỉ email không hợp lệ',
                                            },
                                            {
                                                min: 5,
                                                message: 'Email phải có ít nhất 5 ký tự',
                                            },
                                            {
                                                max: 255,
                                                message: 'Email không được vượt quá 255 ký tự',
                                            },
                                            {
                                                required: true,
                                                message: 'Vui lòng nhập email',
                                            },
                                        ]}
                                    >
                                        <Input placeholder="Nhập email" autoComplete="off" />
                                    </Form.Item>
                                </Col>

                                {/* Số điện thoại */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Số điện thoại"
                                        name="phoneNumber"
                                        hasFeedback
                                        rules={[
                                            { required: true, message: 'Vui lòng nhập số điện thoại' },
                                            {
                                                pattern: REGEXP_PHONE_NUMBER,
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
                                        hasFeedback
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
                                        hasFeedback
                                        rules={[
                                            { required: true, message: 'Vui lòng nhập mật khẩu' },
                                            { pattern: REGEXP_PASSWORD, message: 'Mật khẩu không đúng định dạng' },
                                            { min: 6, max: 30, message: 'Mật khẩu từ 6 - 30 kí tự' },
                                        ]}
                                    >
                                        <Input.Password placeholder="Nhập mật khẩu" />
                                    </Form.Item>
                                </Col>

                                {/* Ngày hết hạn */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Ngày hết hạn"
                                        name="expiryDate"
                                        rules={[{ required: true, message: 'Vui lòng chọn ngày hết hạn' }]}
                                    >
                                        <DatePicker
                                            format="YYYY-MM-DD"
                                            placeholder="Chọn ngày hết hạn"
                                            className="w-100"
                                        />
                                    </Form.Item>
                                </Col>

                                {/* Trạng thái */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Trạng thái"
                                        name="status"
                                        rules={[{ required: true, message: 'Vui lòng chọn trạng thái thẻ' }]}
                                    >
                                        <Select placeholder="Chọn trạng thái thẻ">
                                            {cardStatus.map((card) => (
                                                <Select.Option key={card.value} value={card.value}>
                                                    {card.label}
                                                </Select.Option>
                                            ))}
                                        </Select>
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
                        <Col span={8} className="text-center">
                            <Image width={200} src={previousImage} fallback={images.placeimg} />
                            <Form.Item
                                className="mt-2"
                                name="image"
                                help="Vui lòng chọn hình ảnh dung lượng không quá 2MB"
                            >
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
                                <Col span={24}>
                                    <Form.Item
                                        label="Loại thẻ"
                                        name="cardType"
                                        rules={[{ required: true, message: 'Vui lòng chọn loại thẻ' }]}
                                    >
                                        <Select placeholder="Chọn loại thẻ">
                                            {cardTypes.map((card) => (
                                                <Select.Option key={card.value} value={card.value}>
                                                    {card.label}
                                                </Select.Option>
                                            ))}
                                        </Select>
                                    </Form.Item>
                                </Col>

                                {/* Họ tên */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Họ tên"
                                        name="fullName"
                                        hasFeedback
                                        rules={[
                                            { required: true, message: 'Vui lòng nhập họ tên' },
                                            {
                                                pattern: REGEXP_FULL_NAME,
                                                message: 'Họ tên không hợp lệ',
                                            },
                                            { min: 2, max: 100, message: 'Độ dài từ 2 - 100 kí tự' },
                                        ]}
                                    >
                                        <Input placeholder="Nhập họ tên" />
                                    </Form.Item>
                                </Col>

                                {/* Ngày sinh */}
                                <Col span={12}>
                                    <Form.Item label="Ngày sinh" name="dateOfBirth">
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
                                            {cardGender.map((card) => (
                                                <Select.Option key={card.value} value={card.value}>
                                                    {card.label}
                                                </Select.Option>
                                            ))}
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

                                {/* Email */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Email"
                                        name="email"
                                        rules={[
                                            {
                                                type: 'email',
                                                message: 'Địa chỉ email không hợp lệ',
                                            },
                                            {
                                                min: 5,
                                                message: 'Email phải có ít nhất 5 ký tự',
                                            },
                                            {
                                                max: 255,
                                                message: 'Email không được vượt quá 255 ký tự',
                                            },
                                            {
                                                required: true,
                                                message: 'Vui lòng nhập email',
                                            },
                                        ]}
                                    >
                                        <Input placeholder="Nhập email" autoComplete="off" />
                                    </Form.Item>
                                </Col>

                                {/* Số điện thoại */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Số điện thoại"
                                        name="phoneNumber"
                                        hasFeedback
                                        rules={[
                                            { required: true, message: 'Vui lòng nhập số điện thoại' },
                                            {
                                                pattern: REGEXP_PHONE_NUMBER,
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
                                        hasFeedback
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
                                    <Form.Item
                                        label="Ngày hết hạn"
                                        name="expiryDate"
                                        rules={[{ required: true, message: 'Vui lòng chọn ngày hết hạn' }]}
                                    >
                                        <DatePicker
                                            format="YYYY-MM-DD"
                                            placeholder="Chọn ngày hết hạn"
                                            className="w-100"
                                        />
                                    </Form.Item>
                                </Col>

                                {/* Trạng thái */}
                                <Col span={12}>
                                    <Form.Item
                                        label="Trạng thái"
                                        name="status"
                                        rules={[{ required: true, message: 'Vui lòng chọn trạng thái thẻ' }]}
                                    >
                                        <Select placeholder="Chọn trạng thái thẻ">
                                            {cardStatus.map((card) => (
                                                <Select.Option key={card.value} value={card.value}>
                                                    {card.label}
                                                </Select.Option>
                                            ))}
                                        </Select>
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
                    <Dropdown menu={{ items }} trigger={['click']}>
                        <Button icon={<FaPrint />}></Button>
                    </Dropdown>
                </Space>
            </Flex>

            <Table
                bordered
                rowKey="id"
                scroll={{ x: 'max-content' }}
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
